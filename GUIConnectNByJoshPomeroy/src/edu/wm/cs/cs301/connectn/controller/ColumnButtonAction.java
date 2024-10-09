/**
 * Action class for the column selection buttons. Updates the model with the column that 
 * the player selected to play in. Essentially contains the "game loop" as the computer is set to
 * respond after a button click.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import edu.wm.cs.cs301.connectn.model.ConnectNModel;
import edu.wm.cs.cs301.connectn.view.ConnectNFrame;
import edu.wm.cs.cs301.connectn.view.LoseDialog;
import edu.wm.cs.cs301.connectn.view.TieDialog;
import edu.wm.cs.cs301.connectn.view.WinDialog;

public class ColumnButtonAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	
	private final ConnectNFrame view;
	
	private final ConnectNModel model;
	
	public ColumnButtonAction(ConnectNFrame view, ConnectNModel model) {
		this.view = view;
		this.model = model;
	}
	
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		int columnPressed = Integer.valueOf(button.getActionCommand());
		if (model.getOpenColumns().contains(columnPressed)) {
			model.updateTurn();
			model.calculatePlayerMove(columnPressed);
			view.checkFullColumns("Human");
			view.repaintConnectNBoard();
			model.updateGameState("Human");
			model.computerDefense();
			model.updateComputerMoves();
			if (model.getGameStatus()) {
				new WinDialog(view, model);
			} else {
				model.takeComputerTurn();
				model.updateComputerMoves();
				view.checkFullColumns("Computer");
				view.repaintConnectNBoard();
				model.updateGameState("Computer");
				model.computerOffense();
				view.updateTurnPanel();
				if (model.getGameStatus()) {
					new LoseDialog(view, model);
				}
			}
			
			if (model.getOpenColumns().size() == 0) {
				new TieDialog(view, model);
			}
			
		}
	}
}