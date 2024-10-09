/**
 * Class that is responsible for Human player information
 *
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.model;

public class HumanPlayer{
	
	private final ConnectNModel model;
	
	private int playerMoveCol, playerMoveRow;
	
	public HumanPlayer(ConnectNModel model) {
		this.model = model;
		this.playerMoveCol = -1;
		this.playerMoveRow = -1;
	}
	
	/**
	 * Sets playerMoveRow to the row in which the player's move landed in
	 */
	private void updatePlayerMoveRow() {
		int row = 0;
		
		while ((row+1 <= model.getRowLimit()-1)&&(model.getBoardGrid()[row+1][playerMoveCol].getToken() == ' ')) {
			row++;
		}
		playerMoveRow = row;
	}
	
	public void setPlayerMoveCol(int value) {
		playerMoveCol = value;
		updatePlayerMoveRow();
		model.setBoardLocation(playerMoveRow, playerMoveCol, 'X');
	}
	
	public int getPlayerMoveCol() {
		return playerMoveCol;
	}
	
	public int getPlayerMoveRow() {
		return playerMoveRow;
	}
	
	public void reset() {
		playerMoveCol = -1;
		playerMoveRow = -1;
	}
}