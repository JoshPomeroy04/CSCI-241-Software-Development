/**
 * Action class for the quit buttons in win, loss, and tie dialogs.
 * Terminates the program.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.wm.cs.cs301.connectn.view.ConnectNFrame;

public class QuitButtonAction extends AbstractAction{
	
	private static final long serialVersionUID = 1L;
	
	private final ConnectNFrame view;
	
	public QuitButtonAction(ConnectNFrame view) {
		this.view = view;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		view.shutdown();
		System.exit(0);
	}
}