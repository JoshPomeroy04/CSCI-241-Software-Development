/**
 * Class that creates the main GUI window that contains the game.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import edu.wm.cs.cs301.connectn.model.AppColors;
import edu.wm.cs.cs301.connectn.model.ConnectNModel;

public class ConnectNFrame {
	
	private final JFrame frame;
	
	private final ConnectNModel model;
	
	private final ColumnButtonPanel columnButtonPanel;
	
	private final ConnectNBoardPanel connectNBoardPanel;
	
	private int width;
	
	private final JLabel turnDisplay, computerMove;
	
	public ConnectNFrame(ConnectNModel model) {
		this.model = model;
		this.columnButtonPanel = new ColumnButtonPanel(this, model);
		this.width = 64 * model.getColumnLimit() + 128;
		this.connectNBoardPanel = new ConnectNBoardPanel(this, model);
		this.turnDisplay = new JLabel();
		this.computerMove = new JLabel();
		this.frame = createAndShowGUI();
		new LeaderboardDialog(this, model.getLeaderboard());
	}
	
	private JFrame createAndShowGUI() {
		JFrame frame = new JFrame("ConnectN");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				shutdown();
			}
		});
		
		frame.add(createTurnPanel(), BorderLayout.NORTH);
		frame.add(connectNBoardPanel, BorderLayout.CENTER);
		frame.add(columnButtonPanel.getPanel(), BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		return frame;
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu mainMenu = new JMenu("Menu");
		menuBar.add(mainMenu);
		
		menuBar.add(Box.createHorizontalGlue());
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(event -> shutdown());
		menuBar.add(exitButton);
		
		JMenu difficultyMenu = new JMenu("Difficulty");
		mainMenu.add(difficultyMenu);
		
		JMenuItem smallItem = new JMenuItem("Small");
		smallItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setDifficulty("Small");

	        	 resetBoard();
	         }
	    });
		difficultyMenu.add(smallItem);
		
		JMenuItem mediumItem = new JMenuItem("Medium");
		mediumItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setDifficulty("Medium");

	        	 resetBoard();
	         }
	    });
		difficultyMenu.add(mediumItem);
		
		JMenuItem largeItem = new JMenuItem("Large");
		largeItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setDifficulty("Large");
	        	 
	        	 resetBoard();
	         }
	    });
		difficultyMenu.add(largeItem);
		
		JMenuItem instructionsItem = new JMenuItem("Instructions");
		instructionsItem.addActionListener(event -> new InstructionsDialog(this));
		mainMenu.add(instructionsItem);
		
		JMenuItem leaderboardItem = new JMenuItem("Leaderboard");
		leaderboardItem.addActionListener(event -> new LeaderboardDialog(this, model.getLeaderboard()));
		mainMenu.add(leaderboardItem);
		
		return menuBar;
	}
	
	/**
	 * Creates the top panel where the turn and computer move display are located
	 * @return - the panel
	 */
	private JPanel createTurnPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		turnDisplay.setText("Turn: " + model.getTurn());
		turnDisplay.setFont(AppFonts.getMediumTextFont());
		panel.add(turnDisplay, BorderLayout.WEST);
		
		computerMove.setText("Computer played in column: " + (model.getComputerMove()+1));
		computerMove.setFont(AppFonts.getMediumTextFont());
		panel.add(computerMove, BorderLayout.EAST);
		
		panel.setVisible(true);
		return panel;
	}
	
	/**
	 * Changes the text of the turn panel to represent the current turn and the last move of the computer
	 */
	public void updateTurnPanel() {
		turnDisplay.setText("Turn: " + model.getTurn());
		computerMove.setText("Computer played in column: " + (model.getComputerMove()+1));
	}
	
	public void resetBoard() {
		width = 64 * model.getColumnLimit() + 128;
		model.resetTurn();
		model.resetComputer();
		model.resetHuman();
		updateTurnPanel();
		this.connectNBoardPanel.resetBoard();
		columnButtonPanel.resetButtons();
		frame.add(columnButtonPanel.getPanel(), BorderLayout.SOUTH);
		frame.pack();
		repaintConnectNBoard();
		new LeaderboardDialog(this, model.getLeaderboard());
	}
	
	public void shutdown() {
		frame.dispose();
		System.exit(0);
	}
	
	/**
	 * Changes the color of buttons if the column the button is assigned to is full
	 * @param player - Human or Computer player
	 */
	public void checkFullColumns(String player) {
		int columnRemoved = model.recalculateOpenColumns(player);
		if (columnRemoved != 0) {
			setColor(String.valueOf(columnRemoved), AppColors.GRAY);
		}
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void repaintConnectNBoard() {
		connectNBoardPanel.repaint();
	}
	
	/**
	 * Sets the color of buttons
	 * @param number - Button to set
	 * @param color - Color to set
	 */
	public void setColor(String number, Color color) {
		columnButtonPanel.setColor(number, color);
	}
	
	public int getWidth() {
		return width;
	}
}