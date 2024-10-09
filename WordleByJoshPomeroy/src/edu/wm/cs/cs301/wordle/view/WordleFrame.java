package edu.wm.cs.cs301.wordle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.wordle.model.WordleModel;

public class WordleFrame {
	
	private final JFrame frame;
	
	private final KeyboardPanel keyboardPanel;
	
	private final HintButton hintButton;
	
	private final WordleModel model;
	
	private final WordleGridPanel wordleGridPanel;

	public WordleFrame(WordleModel model) {
		this.model = model;
		this.hintButton = new HintButton(this, model);
		this.keyboardPanel = new KeyboardPanel(this, model, this.hintButton);
		int width = keyboardPanel.getPanel().getPreferredSize().width;
		this.wordleGridPanel = new WordleGridPanel(this, model, width);
		this.frame = createAndShowGUI();
	}
	
	private JFrame createAndShowGUI() {
		JFrame frame = new JFrame("Wordle");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				shutdown();
			}
		});
		
		frame.add(createTitlePanel(), BorderLayout.NORTH);
		frame.add(wordleGridPanel, BorderLayout.CENTER);
		frame.add(keyboardPanel.getPanel(), BorderLayout.SOUTH);
		
		
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		System.out.println("Frame size: " + frame.getSize());
		
		return frame;
	}
	
	public void resetPanel() {
		this.wordleGridPanel.resetGrid();
		keyboardPanel.regenPanel();
		frame.add(keyboardPanel.getPanel(), BorderLayout.SOUTH);
		frame.pack();
		hintButton.disableHint();
		resetDefaultColors();
		repaintWordleGridPanel();
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu difficultyMenu = new JMenu("Difficulty");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(difficultyMenu);
		menuBar.add(helpMenu);
		
		JMenuItem kidsItem = new JMenuItem("Kids");
		kidsItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setGrid(3, 4);
	        	 resetPanel();
	         }
	    });
		difficultyMenu.add(kidsItem);
		
		JMenuItem normalItem = new JMenuItem("Normal");
		normalItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setGrid(5, 6);
	        	 resetPanel();
	         }
	    });
		difficultyMenu.add(normalItem);
		
		JMenuItem hardItem = new JMenuItem("Hard");
		hardItem.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 model.setGrid(7, 8);
	        	 resetPanel();
	         }
	    });
		difficultyMenu.add(hardItem);
		
		JMenuItem instructionsItem = new JMenuItem("Instructions...");
		instructionsItem.addActionListener(event -> new InstructionsDialog(this));
		helpMenu.add(instructionsItem);
		
		JMenuItem aboutItem = new JMenuItem("About...");
		aboutItem.addActionListener(event -> new AboutDialog(this));
		helpMenu.add(aboutItem);
		
		return menuBar;
	}
	
	private JPanel createTitlePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancelAction");
		ActionMap actionMap = panel.getActionMap();
		actionMap.put("cancelAction", new CancelAction());
		
		JLabel label = new JLabel("Wordle");
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);
		
		return panel;
	}
	
	public void shutdown() {
		model.getStatistics().writeStatistics();
		frame.dispose();
		System.exit(0);
	}
	
	public void resetDefaultColors() {
		keyboardPanel.resetDefaultColors();
	}
	
	public void setColor(String letter, Color backgroundColor, Color foregroundColor) {
		keyboardPanel.setColor(letter, backgroundColor, foregroundColor);
	}
	
	public void repaintWordleGridPanel() {
		wordleGridPanel.repaint();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	private class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			shutdown();
		}
		
	}

}
