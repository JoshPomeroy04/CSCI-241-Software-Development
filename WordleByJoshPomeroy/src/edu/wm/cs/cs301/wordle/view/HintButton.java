/***
 * Adds a hint button that the player can use in the game if they are stuck.
 * Upon clicking the hint button, it will replace one of the letters in the previous 
 * guess with the correct letter for that spot. The hint button can be used as many 
 * times as the player wishes. It is disabled on the first guess and is disabled when
 * there are no gray tiles in the player's last guess. 
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.wordle.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;

import edu.wm.cs.cs301.wordle.model.AppColors;
import edu.wm.cs.cs301.wordle.model.WordleModel;
import edu.wm.cs.cs301.wordle.model.WordleResponse;

public class HintButton {
	
	private final JButton hint = new JButton("Hint");
	
	private final WordleFrame view;
	
	private final WordleModel model;
	
	public HintButton(WordleFrame view, WordleModel model) {
		this.view = view;
		this.model = model;
		createHintButton();
	}
	
	private JButton createHintButton() {
		disableHint();
		hint.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 Random rand = new Random();
	        	 
	        	 if (!hint.getBackground().equals(AppColors.GRAY)) {
	        		 int hintPosition = rand.nextInt(model.getColumnCount());
	        		 char hintChar = model.getCurrentWord().charAt(hintPosition);
	        		 
	        		 model.setHintLocation(hintPosition, hintChar);
	        		 WordleResponse[] currentRow = model.getCurrentRow();
	        		 int greyCount = 0;
	        		 for (WordleResponse wordleResponse : currentRow) {
	        			 if (wordleResponse.getBackgroundColor().equals(AppColors.GRAY)) {
	        				 greyCount++;
	        			 }
	        		 }		
	        		 if (greyCount == 0) {
	        			 disableHint();
	        		 }
	        		 view.repaintWordleGridPanel();
	        		 System.out.println("DEBUG: Hint letter: "+hintChar);
	        		 System.out.println("DEBUG: Hint position: "+hintPosition);
	        	 }
	          }
	       });
		
		return hint;
	}
	
	public JButton getHintButton() {
		return hint;
	}
	
	public void enableHint() {
		hint.setBackground(AppColors.OUTLINE);
	}
	
	public void disableHint() {
		hint.setBackground(AppColors.GRAY);
	}
}