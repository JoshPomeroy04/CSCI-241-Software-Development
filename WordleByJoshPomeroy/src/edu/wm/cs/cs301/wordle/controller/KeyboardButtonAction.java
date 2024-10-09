package edu.wm.cs.cs301.wordle.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import edu.wm.cs.cs301.wordle.model.AppColors;
import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.model.WordleResponse;
import edu.wm.cs.cs301.wordle.view.HintButton;
import edu.wm.cs.cs301.wordle.view.StatisticsDialog;
import edu.wm.cs.cs301.wordle.view.WordleFrame;

public class KeyboardButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	private final WordleFrame view;
	
	private final WordleModel model;
	
	private final HintButton hintButton;

	public KeyboardButtonAction(WordleFrame view, WordleModel model, HintButton hintButton) {
		this.view = view;
		this.model = model;
		this.hintButton = hintButton;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		String text = button.getActionCommand();
		switch (text) {
		case "Enter":
			// If the current row is full
			if (model.getCurrentColumn() >= (model.getColumnCount() - 1)) {
				// Gets boolean value of if there are more rows that the player can guess in
				boolean moreRows = model.setCurrentRow();
				// Gets the word in the current row
				WordleResponse[] currentRow = model.getCurrentRow();
				int greenCount = 0;
				int greyCount = 0;
				// Checks the player inputed word for any correct letters 
				for (WordleResponse wordleResponse : currentRow) {
					view.setColor(Character.toString(wordleResponse.getChar()),
							wordleResponse.getBackgroundColor(), 
							wordleResponse.getForegroundColor());
					if (wordleResponse.getBackgroundColor().equals(AppColors.GREEN)) {
						greenCount++;
					} 
					
					if (wordleResponse.getBackgroundColor().equals(AppColors.GRAY)) {
						greyCount++;
					}
				}
				// If the player got all of the letters correct
				if (greenCount >= model.getColumnCount()) {
					view.repaintWordleGridPanel();
					model.getStatistics().incrementTotalGamesPlayed();
					int currentRowNumber = model.getCurrentRowNumber();
					model.getStatistics().addWordsGuessed(currentRowNumber);
					int currentStreak = model.getStatistics().getCurrentStreak();
					model.getStatistics().setCurrentStreak(++currentStreak);
					new StatisticsDialog(view, model);
				// If the player has no more guesses	
				} else if (!moreRows) {
					view.repaintWordleGridPanel();
					model.getStatistics().incrementTotalGamesPlayed();
					model.getStatistics().setCurrentStreak(0);
					new StatisticsDialog(view, model);
				} else {
					//System.out.println(greyCount);
					if (greyCount > 0) {
						hintButton.enableHint();
					} else {
						hintButton.disableHint();
					}
					view.repaintWordleGridPanel();
				}
			}
			break;
		case "Backspace":
			model.backspace();
			view.repaintWordleGridPanel();
			break;
		default:
			model.setCurrentColumn(text.charAt(0));
			view.repaintWordleGridPanel();
			break;
		}
		
	}

}
