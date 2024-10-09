/**
 * Class that creates Location objects. Stores needed information.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.model;

public class Location{
	private Character symbol = ' ';
	private int connected = 0;

	public Location() {
		
	}
	
	/**
	 * @return - returns the value representing how many similar tiles this instance is connected to
	 */
	public int getConnected() {
		return connected;
	}
	
	/**
	 * Sets the value of connected
	 * @param val - Integer value
	 */
	public void setConnected(int val) {
		connected = val;
	}
	
	/**
	 * @return - true if the location is empty and false if it isn't
	 */
	public boolean isEmpty() {
		if (symbol == ' ') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return - Returns the token within this instance
	 */
	public Character getToken() {
		return symbol;
	}
	
	/**
	 * Sets the value of the symbol
	 * @param set - Character to set symbol to
	 */
	public void setToken(char set) {
		symbol = set;
	}
	
	/**
	 * Overrides .equals() to compare the symbols within location objects
	 */
	@Override 
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		
		if (!(other instanceof Location)) {
			return false;
		}
		
		Location check = (Location) other;
		if (symbol == check.getToken()) {
			return true;
		} else {
			return false;
		}
	}
}