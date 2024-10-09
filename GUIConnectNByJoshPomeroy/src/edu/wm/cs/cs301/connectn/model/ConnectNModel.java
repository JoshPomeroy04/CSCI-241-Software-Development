/**
 * Class containing the main model for ConnectN. Responsible for keeping track of the game board. 
 * Checks game board to see if a player has won or if the game is a draw.
 * Updated connected values of each tile on the game board. 
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.model;

import java.util.ArrayList;

public class ConnectNModel {
	
	private int columnLimit, rowLimit, turn, connectedTiles, connectedTilesRight, connectedTilesLeft, scoreToWin;
	
	private ArrayList<Integer> openColumns;
	
	private Location[][] boardGrid;
	
	private final ComputerPlayer computer;
	
	private final HumanPlayer human;
	
	private boolean gameOver;
	
	private final Leaderboard leaderboard;
	
	public ConnectNModel() {
		this.columnLimit = 7;
		this.rowLimit = 6; 
		this.scoreToWin = 4;
		this.turn = 1;
		this.computer = new ComputerPlayer(this);
		this.human = new HumanPlayer(this);
		this.openColumns = new ArrayList<Integer>();
		this.boardGrid = initializeBoardGrid();
		this.gameOver = false;
		this.leaderboard = new Leaderboard(this);
		
		setOpenColumns();
	}
	
	/**
	 * Sets all values to the default size of medium
	 */
	public void setDefaultSize() {
		columnLimit = 7;
		rowLimit = 6;
		scoreToWin = 4;
		this.boardGrid = initializeBoardGrid();
		setOpenColumns();
		gameOver = false;
	}
	
	/**
	 * Creates the board grid of Location objects
	 * @return - the board grid
	 */
	private Location[][] initializeBoardGrid() {
		Location[][] boardGrid = new Location[rowLimit][columnLimit];
		
		for (int row = 0; row < boardGrid.length; row++) {
			for (int column = 0; column < boardGrid[row].length; column++) {
				boardGrid[row][column] = new Location();
			}
		}
		return boardGrid;		
	}
	
	/**
	 * Populates the ArrayList openColumns for new games
	 */
	private void setOpenColumns() {
		openColumns.clear();
		
		for (int i = 1; i <= columnLimit; i++) {
			openColumns.add(i);
		}
	}
	
	/**
	 * Checks if the tile under row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile under row, col has the same character, false if not
	 */
	private boolean tileUnder(int row, int col) {
		if ((row != rowLimit-1)&&(boardGrid[row][col].equals(boardGrid[row+1][col]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile right of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile right of row, col has the same character, false if not
	 */
	private boolean tileRight(int row, int col) {
		if ((col != columnLimit-1)&&(boardGrid[row][col].equals(boardGrid[row][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile left of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile left of row, col has the same character, false if not
	 */
	private boolean tileLeft(int row, int col) {
		if ((col != 0)&&(boardGrid[row][col].equals(boardGrid[row][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile up and right of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile up and right of row, col has the same character, false if not
	 */
	private boolean tileUpRight(int row, int col) {
		if ((row != 0)&&(col != columnLimit-1)&&(boardGrid[row][col].equals(boardGrid[row-1][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile down and left of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile down and left of row, col has the same character, false if not
	 */
	private boolean tileDownLeft(int row, int col) {
		if ((row != rowLimit-1)&&(col != 0)&&(boardGrid[row][col].equals(boardGrid[row+1][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile up and left of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile up and left of row, col has the same character, false if not
	 */
	private boolean tileUpLeft(int row, int col) {
		if ((row != 0)&&(col != 0)&&(boardGrid[row][col].equals(boardGrid[row-1][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile down and right of row, col contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile down and right of row, col has the same character, false if not
	 */
	private boolean tileDownRight(int row, int col) {
		if ((row != rowLimit-1)&&(col != columnLimit-1)&&(boardGrid[row][col].equals(boardGrid[row+1][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks tiles under the inputed tile
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesUnder(int row, int col) { 
		connectedTiles = 0;
		// Checks if the tile under the most recent play is the same player
		if (tileUnder(row, col)) { 
			connectedTiles = 1;
			// Sees how many connected tiles there are
			for (int r = row+2; r < rowLimit; r++) { 
				if (boardGrid[r][col].equals(boardGrid[row][col])) {
					connectedTiles++;
				} else {
					break;
				}
			}
			
			// Updates the tiles connected variable for connected tiles 
			for (int r = 0; r <= connectedTiles; r++) {
				if (boardGrid[row+r][col].getConnected() < connectedTiles) {
					boardGrid[row+r][col].setConnected(connectedTiles);
				}
			}
		}
	}
	
	/**
	 * Checks tiles to the left and right of the inputed tile
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesHorizontal(int row, int col) {
		connectedTilesRight = 0;
		connectedTilesLeft = 0;
		
		// Checks if the tile to the right of the most recent play is the same player
		if (tileRight(row, col)) {
			connectedTilesRight = 1;
			// Sees how many connected tiles there are
			for (int c = col+2; c < columnLimit; c++) {
				if (boardGrid[row][c].equals(boardGrid[row][col])) {
					connectedTilesRight++;
				} else {
					break;
				}
			}
		}
					
		// Checks if the tile to the left of the most recent play is the same player
		if (tileLeft(row, col)) {
			connectedTilesLeft = 1;
			// Sees how many connected tiles there are 
			for (int c = col-2; c >= 0; c--) {
				if (boardGrid[row][c].equals(boardGrid[row][col])) {
					connectedTilesLeft++;
				} else {
					break;
				}
			}	
		}
		
		// Updates the connected variable for connected tiles to the right
		for (int c = 0; c <= connectedTilesRight; c++) {
			if (boardGrid[row][col+c].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row][col+c].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
				
		// Updates the tiles connected variable for connected tiles 
		for (int c = 0; c <= connectedTilesLeft; c++) {
			if (boardGrid[row][col-c].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row][col-c].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
	}
	
	/**
	 * Checks tiles that are up-right and down-left of the inputed tile
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesPositiveDiagonal(int row, int col) {
		connectedTilesRight = 0;
		connectedTilesLeft = 0;
		
		// Checks if the tile diagonally up and to the right of the inputed tile is the same player
		if (tileUpRight(row, col)) {
			connectedTilesRight = 1;
			// Sees how many connected tiles there are 
			for (int r = row-2, c = col+2; r >= 0 && c < columnLimit; r--, c++) {
				if (boardGrid[r][c].equals(boardGrid[row][col])) {
					connectedTilesRight++;
				} else {
					break;
				}
			}
		}
		
		// Checks if the tile diagonally down and to the left of the inputed tile is the same player
		if (tileDownLeft(row, col)) {
			connectedTilesLeft = 1;
			// Sees how many connected tiles there are 
			for (int r = row+2, c = col-2; r < rowLimit && c >= 0; r++, c--) {
				if (boardGrid[r][c].equals(boardGrid[row][col])) {
					connectedTilesLeft++;
				} else {
					break;
				}
			}	
		}
		
		for (int i = 0; i <= connectedTilesRight; i++) {
			if (boardGrid[row-i][col+i].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row-i][col+i].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
		
		for (int i = 0; i <= connectedTilesLeft; i++) {
			if (boardGrid[row+i][col-i].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row+i][col-i].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
	}
	
	/**
	 * Checks tiles that are up-left and down-right of the inputed tile
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesNegativeDiagonal(int row, int col) {
		connectedTilesRight = 0;
		connectedTilesLeft = 0;
		
		// Checks if the tile diagonally up and to the left of the inputed tile is the same player
		if (tileUpLeft(row, col)) {
			connectedTilesLeft = 1;
			// Sees how many connected tiles there are 
			for (int r = row-2, c = col-2; r >= 0 && c >= 0; r--, c--) {
				if (boardGrid[r][c].equals(boardGrid[row][col])) {
					connectedTilesLeft++;
				} else {
					break;
				}
			}
		}
		
		// Checks if the tile diagonally down and to the right of the inputed tile is the same player
		if (tileDownRight(row, col)) {
			connectedTilesRight = 1;
			// Sees how many connected tiles there are 
			for (int r = row+2, c = col+2; r < rowLimit && c < columnLimit; r++, c++) {
				if (boardGrid[r][c].equals(boardGrid[row][col])) {
					connectedTilesRight++;
				} else {
					break;
				}
			}	
		}

		for (int i = 0; i <= connectedTilesLeft; i++) {
			if (boardGrid[row-i][col-i].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row-i][col-i].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
		
		for (int i = 0; i <= connectedTilesRight; i++) {
			if (boardGrid[row+i][col+i].getConnected() < connectedTilesLeft+connectedTilesRight) {
				boardGrid[row+i][col+i].setConnected(connectedTilesLeft+connectedTilesRight);
			}
		}
	}
	
	/**
	 * Checks to see if the game has been won by player
	 * @param player - Computer or Human player
	 */
	private void checkWin(String player) {
		if (player.equals("Human")) {
			if (boardGrid[human.getPlayerMoveRow()][human.getPlayerMoveCol()].getConnected() >= scoreToWin-1) {
				openColumns.clear();
				gameOver = true;
			}
		} else {
			if (boardGrid[computer.getComputerMoveRow()][computer.getComputerMoveCol()].getConnected() >= scoreToWin-1) {
				openColumns.clear();
				gameOver = true;
			}
		}
	}
	
	/**
	 * Updates the tiles' connected value around the piece played by player.
	 * @param player - Human or Computer player
	 */
	public void updateGameState(String player) {
		int playerMoveRow = human.getPlayerMoveRow();
		int playerMoveCol = human.getPlayerMoveCol();
		int computerMoveRow = computer.getComputerMoveRow();
		int computerMoveCol = computer.getComputerMoveCol();
		
		if (player.equals("Human")) {
			checkTilesUnder(playerMoveRow, playerMoveCol);
			checkTilesHorizontal(playerMoveRow, playerMoveCol);
			checkTilesPositiveDiagonal(playerMoveRow, playerMoveCol);
			checkTilesNegativeDiagonal(playerMoveRow, playerMoveCol);
		} else {
			checkTilesUnder(computerMoveRow, computerMoveCol);
			checkTilesHorizontal(computerMoveRow, computerMoveCol);
			checkTilesPositiveDiagonal(computerMoveRow, computerMoveCol);
			checkTilesNegativeDiagonal(computerMoveRow, computerMoveCol);
		}
		checkWin(player);
	}
	
	/**
	 * Turns the number received from pressing a column button into the corresponding grid column value
	 * @param value - Should be a number ranging from 1 - columnLimit
	 */
	public void calculatePlayerMove(int value) {
		human.setPlayerMoveCol(value-1);
	}
	
	/**
	 * Sets columnLimit, rowLimit, and scoreToWin to the corresponding grid size
	 * @param dif - String representing grid size
	 */
	public void setDifficulty(String dif) {
		switch (dif) {
			case "Small":
				columnLimit = 5;
				rowLimit = 4;
				scoreToWin = 3;
				break;
			case "Medium":
				columnLimit = 7;
				rowLimit = 6;
				scoreToWin = 4;
				break;
			case "Large":
				columnLimit = 9;
				rowLimit = 8;
				scoreToWin = 5;
				break;
		}
		setOpenColumns();
		this.boardGrid = initializeBoardGrid();
	}
	
	/**
	 * Checks to see if the most recent move by player filled a column.
	 * If so, it removes that column from openColumns.
	 * @param player - Human or Computer player
	 * @return - the column being removed as an integer representing the corresponding column button
	 */
	public int recalculateOpenColumns(String player) {
		if ((human.getPlayerMoveRow() == 0)&&(player == "Human")) {
			openColumns.remove(Integer.valueOf(human.getPlayerMoveCol()+1));
			return human.getPlayerMoveCol()+1;
		}

		if ((computer.getComputerMoveRow() == 0)&&(player == "Computer")) {
			openColumns.remove(Integer.valueOf(computer.getComputerMoveCol()+1));
			return computer.getComputerMoveCol()+1;
		} else {
			return 0;
		}
	}
	
	public void resetTurn() {
		turn = 1;
	}
	
	public void updateTurn() {
		turn++;
	}
	
	public void takeComputerTurn() {
		computer.takeTurn();
	}
	
	public void computerOffense() {
		computer.findOffensiveMoves();
	}
	
	public void computerDefense() {
		computer.findDefensiveMoves();
	}
	
	public void updateComputerMoves() {
		computer.checkBestMoves();
	}
	
	public void resetComputer() {
		computer.reset();
	}
	
	public void resetHuman() {
		human.reset();
	}
	
	/**
	 * Sets the token value of the tile at row, col
	 * @param row - Row to set
	 * @param col - Column to set
	 * @param c - Character to set
	 */
	public void setBoardLocation(int row, int col, char c) {
		boardGrid[row][col].setToken(c);
	}
	
	/**
	 * Updates the leaderboard to the new player name
	 * @param name - Name to save
	 * @param size - Board size to save to
	 */
	public void setLeaderboard(String name, int size) {
		switch (size) {
			case 5:
				leaderboard.setSmallScore(name + ": " + (turn-1));
				break;
			case 7:
				leaderboard.setMediumScore(name + ": " + (turn-1));
				break;
			case 9:
				leaderboard.setLargeScore(name + ": " + (turn-1));
				break;
		}
	}
	
	public int getComputerMove() {
		return computer.getComputerMoveCol();
	}
	
	public int getRowLimit() {
		return rowLimit;
	}

	public int getColumnLimit() {
		return columnLimit;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public ArrayList<Integer> getOpenColumns() {
		return openColumns;
	}
	
	public int getPlayerMoveCol() {
		return human.getPlayerMoveCol();
	}
	
	public int getPlayerMoveRow() {
		return human.getPlayerMoveRow();
	}
	
	public Location[][] getBoardGrid(){
		return boardGrid;
	}
	
	public boolean getGameStatus() {
		return gameOver;
	}
	
	public int getScoreToWin() {
		return scoreToWin;
	}
	
	public Leaderboard getLeaderboard() {
		return leaderboard;
	}
}