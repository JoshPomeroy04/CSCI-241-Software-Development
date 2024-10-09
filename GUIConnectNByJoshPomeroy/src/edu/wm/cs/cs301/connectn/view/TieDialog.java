/**
 * Class that creates the window that pops-up when the game ends in a tie.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wm.cs.cs301.connectn.controller.PlayAgainButtonAction;
import edu.wm.cs.cs301.connectn.controller.QuitButtonAction;
import edu.wm.cs.cs301.connectn.model.ConnectNModel;

public class TieDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	private final QuitButtonAction quitAction;
	
	private final PlayAgainButtonAction againAction;
	
	public TieDialog(ConnectNFrame view, ConnectNModel model) {
		super(view.getFrame(), "Good Game", true);
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
		
		add(createMainPanel(), BorderLayout.NORTH);
		add(createButtonPanel(), BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(view.getFrame());
		setVisible(true);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel mainText = new JLabel("Tie Game!");
		
		mainText.setFont(AppFonts.getTitleFont());
		panel.add(mainText);
		
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		JButton againButton = new JButton("Play Again");
		againButton.addActionListener(againAction);
		panel.add(againButton);
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(quitAction);
		panel.add(quitButton);
		
		return panel;
	}
}