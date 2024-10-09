/**
 * Class that creates the button panel that the player clicks to make a move.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wm.cs.cs301.connectn.controller.ColumnButtonAction;
import edu.wm.cs.cs301.connectn.model.ConnectNModel;

public class ColumnButtonPanel {

	private int buttonIndex, buttonCount;

	private JButton[] buttons;

	private JPanel panel;

	private final ConnectNModel model;
	
	private final ColumnButtonAction action;

	public ColumnButtonPanel(ConnectNFrame view, ConnectNModel model) {
		this.model = model;
		this.buttonIndex = 0;
		this.buttonCount = model.getColumnLimit();
		this.buttons = new JButton[buttonCount];
		this.action = new ColumnButtonAction(view, model);
		this.panel = createMainPanel();
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel();
		panel.add(createButtonPanel());

		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new GridLayout(0, model.getColumnLimit(), 6, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));
		Font textfont = AppFonts.getTextFont();

		String[] numbers = getNumbers();

		for (int index = 0; index < numbers.length; index++) {
			JButton button = new JButton(numbers[index]);
			button.setPreferredSize(new Dimension(64, 64));
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}
	
	/**
	 * Gets the numbers that are needed for the buttons depending on board size
	 * @return - String[] containing needed numbers
	 */
	private String[] getNumbers() {
		switch (model.getColumnLimit()) {
			case 5: 
				String[] numbersSmall = {"1", "2", "3", "4", "5"};
				return numbersSmall;
			case 9: 
				String[] numbersLarge = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
				return numbersLarge;
			default: 
				String[] numbersMedium = {"1", "2", "3", "4", "5", "6", "7"};
				return numbersMedium;
		}
	}
	
	/**
	 * Recreates the button panel for new games or board changes.
	 */
	public void resetButtons() {
		this.panel.remove(0);
		this.buttonIndex = 0;
		this.buttonCount = model.getColumnLimit();
		this.buttons = new JButton[buttonCount];
		this.panel.add(createMainPanel());
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	/**
	 * Allows the color of the buttons to be changed
	 * @param number - Which button to change
	 * @param color - Color to change to
	 */
	public void setColor(String number, Color color) {
		for (JButton button : buttons) {
			if (button.getActionCommand().equals(number)) {
				button.setBackground(color);
				break;
			}
		}
	}
}
