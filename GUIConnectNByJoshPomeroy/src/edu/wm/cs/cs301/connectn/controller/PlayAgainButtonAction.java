/**
 * Action class for the play again buttons in the pop-up dialog windows on win, loss, or tie.
 * Resets the frame and model for a new game to be played.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;

import edu.wm.cs.cs301.connectn.model.ConnectNModel;
import edu.wm.cs.cs301.connectn.view.ConnectNFrame;

public class PlayAgainButtonAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	private final ConnectNFrame view;
	
	private final ConnectNModel model;
	
	private final JDialog window;
	
	public PlayAgainButtonAction(ConnectNFrame view, ConnectNModel model, JDialog window) {
		this.view = view;
		this.model = model;
		this.window = window;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		window.dispose();
		model.setDefaultSize();
		view.resetBoard();
		view.updateTurnPanel();
	}
}