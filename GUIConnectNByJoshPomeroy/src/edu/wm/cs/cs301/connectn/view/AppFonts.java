/**
 * Class that contains the fonts for fonts used in the GUI.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.view;

import java.awt.Font;

public class AppFonts {
	
	public static Font getMediumTextFont() {
		return new Font("Dialog", Font.BOLD, 20);
	}
	
	public static Font getTextFont() {
		return new Font("Dialog", Font.PLAIN, 16);
	}
	
	public static Font getTitleFont() {
		return new Font("Dialog", Font.BOLD, 36);
	}

}