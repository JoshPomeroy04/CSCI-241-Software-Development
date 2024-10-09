/**
 * Main class that creates an instance of the frame and model, handles GUI for cross platform
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.wm.cs.cs301.connectn.model.ConnectNModel;
import edu.wm.cs.cs301.connectn.view.ConnectNFrame;

public class ConnectN implements Runnable {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new ConnectN());
		// Credit to Gilbert G. Le Blanc from Wordle for handling different operating systems
		if (!System.getProperty("os.name").contains("Windows")) {
			try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		new ConnectNFrame(new ConnectNModel());
	}

}