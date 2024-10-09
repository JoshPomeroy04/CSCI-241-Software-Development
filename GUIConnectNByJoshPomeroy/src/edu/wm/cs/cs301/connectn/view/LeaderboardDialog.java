/**
 * Class that creates the Leaderboard window.
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wm.cs.cs301.connectn.model.Leaderboard;

public class LeaderboardDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final JLabel smallText, mediumText, largeText;
	
	public LeaderboardDialog(ConnectNFrame view, Leaderboard leaderboard) {
		super(view.getFrame(), "Leaderboard", true);
		this.smallText = new JLabel("1. " + leaderboard.getSmallScore());
		this.mediumText = new JLabel("1. " + leaderboard.getMediumScore());
		this.largeText = new JLabel("1. " + leaderboard.getLargeScore());
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				dispose();
			}
		});
		
		add(createMainPanel());
		
		pack();
		setLocationRelativeTo(view.getFrame());
		setVisible(true);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createTitlePanel(), BorderLayout.NORTH);
		panel.add(createLeaderboardPanel(), BorderLayout.CENTER);
		panel.add(closeLeaderboard(), BorderLayout.SOUTH);
		
		return panel;
	}
	
	private JPanel createTitlePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		JLabel label = new JLabel("Leaderboard");
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);
		
		return panel;
	}
	
	private JPanel createLeaderboardPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 3));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 30, 5));
		
		panel.add(createLeaderboardBox("Small"));
		panel.add(createLeaderboardBox("Medium"));
		panel.add(createLeaderboardBox("Large"));
		
		smallText.setAlignmentX(CENTER_ALIGNMENT);
		mediumText.setAlignmentX(CENTER_ALIGNMENT);
		largeText.setAlignmentX(CENTER_ALIGNMENT);
		// 18 max name length
		panel.add(smallText);
		panel.add(mediumText);
		panel.add(largeText);
		
		return panel;
	}
	
	/**
	 * Creates the titles for each score type (Small, Medium, Large)
	 * @param text - Text to set
	 * @return - panel
	 */
	private JPanel createLeaderboardBox(String text) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 50, 5, 50));
		
		JLabel valueLabel = new JLabel(text);
		valueLabel.setFont(AppFonts.getMediumTextFont());
		valueLabel.setAlignmentX(CENTER_ALIGNMENT);
		panel.add(valueLabel);
		
		return panel;
	}
	
	private JButton closeLeaderboard() {
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 dispose();
	         }
	    });
		return close;
	}
}