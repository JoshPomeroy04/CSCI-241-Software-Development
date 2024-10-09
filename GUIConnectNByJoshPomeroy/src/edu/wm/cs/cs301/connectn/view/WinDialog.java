/**
 * Class that creates the window that pops-up when the player wins.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import edu.wm.cs.cs301.connectn.controller.PlayAgainButtonAction;
import edu.wm.cs.cs301.connectn.controller.QuitButtonAction;
import edu.wm.cs.cs301.connectn.model.ConnectNModel;

public class WinDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	private final QuitButtonAction quitAction;
	
	private final PlayAgainButtonAction againAction;
	
	private final ConnectNModel model;
	
	private String playerName;
	
	public WinDialog(ConnectNFrame view, ConnectNModel model){
		super(view.getFrame(), "Congratulations", true);
		this.model = model;
		this.quitAction = new QuitButtonAction(view);
		this.againAction = new PlayAgainButtonAction(view, model, this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				dispose();
				System.exit(0);
			}
		});
		
		add(createTitlePanel(), BorderLayout.NORTH);
		// Checks to see if the player's score was good enough to record, if so it displays the enter username panel
		if (model.getLeaderboard().checkBetterScore(model.getColumnLimit())) {
			add(createSubmissionPanel(), BorderLayout.CENTER);
		} else {
			add(createAlternatePanel(), BorderLayout.CENTER);
		}
		
		add(createButtonPanel(), BorderLayout.SOUTH);
		
		
		pack();
		setLocationRelativeTo(view.getFrame());
		setVisible(true);
	}
	
	private JPanel createTitlePanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		
		JLabel label = new JLabel("You Won!");
		JLabel turnCount = new JLabel("It took you " + (model.getTurn()-1) + " turns to win!");
		label.setFont(AppFonts.getTitleFont());
		turnCount.setFont(AppFonts.getMediumTextFont());
		
		panel.add(label);
		panel.add(turnCount);
		
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		JButton againButton = new JButton("Play Again");
		againButton.addActionListener(againAction);
		panel.add(againButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(quitAction);
		panel.add(quitButton);
		
		return panel;
	}
	
	private JPanel createSubmissionPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 30, 5));
		
		JLabel text = new JLabel();
		JTextField textBox = new JTextField();
		textBox.setDocument(new JTextFieldLimit(18));
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playerName = textBox.getText().trim();

				if (playerName.length() == 0) {
					playerName = "Guest";
	        	} 
	        	 
	        	model.setLeaderboard(playerName, model.getColumnLimit());
	        	panel.remove(textBox);
	        	panel.remove(submit);
	        }
	    });
		
		panel.add(text);
		panel.add(textBox);
		panel.add(submit);
		
		return panel;
	}
	
	private JPanel createAlternatePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		
		JLabel text = new JLabel("Your score isn't good enough! You need to win in fewer moves!");
		text.setFont(AppFonts.getTextFont());
		
		panel.add(text);
		
		return panel;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	/**
	 * Class that is used to set a character limit for the JTextField
	 */
	private class JTextFieldLimit extends PlainDocument {

		private static final long serialVersionUID = 1L;
		
		private int limit;

		JTextFieldLimit(int limit) {
			super();
		    this.limit = limit;
		}

		public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
			if (str == null) return;

		    if ((getLength() + str.length()) <= limit) {
		    	super.insertString(offset, str, attr);
		    }
		}
	}
}