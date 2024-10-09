/**
 * Class that is responsible for the computer's actions.
 * Stores computer move information and selects a move for the computer to play.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.model;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer{
	
	private final ConnectNModel model;
	
	private int computerMoveCol, computerMoveRow, removeAt;
	
	private final Random rand;
	
	private ArrayList<Integer[]> offensiveMoves, defensiveMoves;
	
	public ComputerPlayer(ConnectNModel model) {
		this.model = model;
		this.rand = new Random();
		this.computerMoveCol = -1;
		this.computerMoveRow = -1;
		this.offensiveMoves = new ArrayList<Integer[]>();
		this.defensiveMoves = new ArrayList<Integer[]>();
	}
	
	/**
	 * Sets the row in which the computer's move lands in
	 */
	private void updateComputerMoveRow() {
		int row = 0;
		
		while ((row+1 <= model.getRowLimit()-1)&&(model.getBoardGrid()[row+1][computerMoveCol].getToken() == ' ')) {
			row++;
		}
		computerMoveRow = row;
	}
	
	/**
	 * Calculates the best move for the computer by checking the tiles around inputRow and inputCol to 
	 * calculate how many pieces in a row there are. If the piece in a row is 1 less than that required to win,
	 * it stores the location of where a piece can be played to create a row big enough to win the game.
	 * Also works to calculate player moves for defense to block the player from winning.
	 * @param inputRow - Row to check
	 * @param inputCol - Column to check
	 * @param moveArray - ArrayList to store the best move
	 */
	private void calculateMoves(int inputRow, int inputCol, ArrayList<Integer[]> moveArray) {
		Location[][] board = model.getBoardGrid();
		int scoreToWin = model.getScoreToWin(); // 2
		int connectedTiles = 0;
		int connectedTilesRight = 0;
		int connectedTilesLeft = 0;
		int disconnected = 0;
		int possibleRow = 0;
		int possibleCol = 0;
		int rowCheck = 0;
		int columnCheck = 0;
		
		// Check how many connected tiles are below inputRow, inputCol
		for (int r = inputRow+1; r <= model.getRowLimit()-1; r++) {   
			if (board[r][inputCol].equals(board[inputRow][inputCol])) {
				connectedTiles++;
				Integer[] candidateMove = {inputRow-1, inputCol};
				if ((inputRow-1 >= 0)&&(connectedTiles >= scoreToWin-2)&&!bestContains(candidateMove, moveArray)) {
					moveArray.add(candidateMove);
					break;
				}
			} else {
				break;
			}
		}
		
		/**
		 * Horizontal checking
		 * 
		 */
		// See if anything is to the right
		for (int r = inputCol+1; r <= model.getColumnLimit()-1; r++) {
			if (board[inputRow][r].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
			} else {
				break;
			}
		}
		
		Integer[] candidateMoveLeft = {inputRow, inputCol-1};
		// Check if the winning move is directly to the left of most recent placement 
		if ((inputCol-1 >= 0)&&(connectedTilesRight >= scoreToWin-2)&&board[inputRow][inputCol-1].isEmpty()&&!bestContains(candidateMoveLeft, moveArray)) {
			moveArray.add(candidateMoveLeft);
		}
		
		Integer[] candidateMoveRight = {inputRow, inputCol+connectedTilesRight+1};
		// Check if the winning move is to the right of most recent placement
		if ((connectedTilesRight >= scoreToWin-2)&&(inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveRight, moveArray)) {
			moveArray.add(candidateMoveRight);
		} else {
			possibleCol = 0;
			disconnected = 0;
			// Then check left side
			for (int r = inputCol-1; r >= 0; r--) {
				// Same tile
				if (board[inputRow][r].equals(board[inputRow][inputCol])) {
					connectedTilesLeft++;
					// A row 1 away from victory has been confirmed
					if ((connectedTilesLeft+connectedTilesRight >= scoreToWin-2)) {
						// It was found with 1 gap 
						Integer[] candidateMoveLeftGap = {inputRow, possibleCol};
						Integer[] candidateMoveFarLeft = {inputRow, inputCol-connectedTilesLeft-1};
						if ((possibleCol != 0)&&!bestContains(candidateMoveLeftGap, moveArray)) {
							moveArray.add(candidateMoveLeftGap);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputCol-connectedTilesLeft-1 >= 0)&&board[inputRow][inputCol-connectedTilesLeft-1].isEmpty()&&!bestContains(candidateMoveFarLeft, moveArray)) {
							moveArray.add(candidateMoveFarLeft);
						}
						// Check if the winning move is on the right side
						if ((inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveRight, moveArray)) {
							moveArray.add(candidateMoveRight);
						}
						break;
					}
				// Open tile
				} else if (board[inputRow][r].isEmpty()) {
					disconnected++;
					// Continue checking left if only 1 gap has been found
					if (disconnected == 1) {
						possibleCol = r;
					// Stop checking if more than 1 gap has been found
					} else {
						break;
					}
				// Other player's tile
				} else {
					break;
				}
			}
		}
		
		possibleCol = 0;
		disconnected = 0;
		columnCheck = connectedTilesRight;
		// Check right side starting from where previous check ended
		for (int r = inputCol+columnCheck+1; r <= model.getColumnLimit()-1; r++) {
			// Empty tile but next tile to the right is the same
			if (board[inputRow][r].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
			} else if (board[inputRow][r].isEmpty()) {
				disconnected++;
				if (disconnected == 1) {
					possibleCol = r;
				} else {
					break;
				}
			} else {
				break;
			}
			// A row 1 away from victory has been confirmed
			Integer[] candidateMoveRightGap = {inputRow, possibleCol};
			if ((connectedTilesRight+connectedTilesLeft >= scoreToWin-2)&&!bestContains(candidateMoveRightGap, moveArray)) {
				moveArray.add(candidateMoveRightGap);
				break;
			} 
		}
		
		/**
		 * Bottom left to up right diagonal checking
		 * 
		 */
		rowCheck = inputRow - 1;
		columnCheck = inputCol + 1;
		connectedTilesLeft = 0;
		connectedTilesRight = 0;
		
		// See if anything is to the upper right
		while ((rowCheck >= 0)&&(columnCheck <= model.getColumnLimit()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
				rowCheck--;
				columnCheck++;
			} else {
				break;
			}
		}
		
		rowCheck = inputRow + 1;
		columnCheck = inputCol - 1;
		// Check if the best move is directly to the lower left 
		Integer[] candidateMoveLowerLeft = {inputRow+1, inputCol-1};
		if ((rowCheck <= model.getRowLimit()-1)&&(columnCheck >= 0)&&(connectedTilesRight >= scoreToWin-2)&&board[inputRow+1][inputCol-1].isEmpty()&&!bestContains(candidateMoveLowerLeft, moveArray)) {
			moveArray.add(candidateMoveLowerLeft);
		}
		
		// Check if a piece can be played on the upper right side
		Integer[] candidateMoveUpperRight = {inputRow-connectedTilesRight-1, inputCol+connectedTilesRight+1};
		if ((connectedTilesRight >= scoreToWin-2)&&(inputRow-connectedTilesRight-1 >= 0)&&(inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow-connectedTilesRight-1][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveUpperRight, moveArray)) {
			moveArray.add(candidateMoveUpperRight);
		} else {
			possibleCol = 0;
			possibleRow = 0;
			disconnected = 0;
			// Then check lower left side
			while ((rowCheck <= model.getRowLimit()-1)&&(columnCheck >= 0)) {
				// Same tile
				if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
					connectedTilesLeft++;
					// A row 1 from victory has been confirmed
					if (connectedTilesLeft+connectedTilesRight >= scoreToWin-2) {
						// It was found with 1 gap
						Integer[] candidateMoveLowerLeftGap = {possibleRow, possibleCol};
						Integer[] candidateMoveLowerLeftFar = {inputRow+connectedTilesLeft+1, inputCol-connectedTilesLeft-1};
						if ((possibleCol != 0)&&!bestContains(candidateMoveLowerLeftGap, moveArray)) {
							moveArray.add(candidateMoveLowerLeftGap);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputRow+connectedTilesLeft+1 <= model.getRowLimit()-1)&&(inputCol-connectedTilesLeft-1 >= 0)&&board[inputRow+connectedTilesLeft+1][inputCol-connectedTilesLeft-1].isEmpty()&&!bestContains(candidateMoveLowerLeftFar, moveArray)) {
							moveArray.add(candidateMoveLowerLeftFar);
						}
						// Check if a piece can be played on the upper right side
						if ((inputRow-connectedTilesRight-1 >= 0)&&(inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow-connectedTilesRight-1][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveUpperRight, moveArray)) {
							moveArray.add(candidateMoveUpperRight);
							}
						break;
					}
					rowCheck++;
					columnCheck--;
				// Open tile
				} else if (board[rowCheck][columnCheck].isEmpty()) {
					disconnected++;
					// Continue checking lower left if only 1 gap has been found
					if (disconnected == 1) {
						possibleRow = rowCheck;
						possibleCol = columnCheck;
						rowCheck++;
						columnCheck--;
					// Stop checking if more than 1 gap has been found
					} else {
						break;
					}
				// Other player's tile
				} else {
					break;
				}
			}
		}
		
		rowCheck = inputRow - connectedTilesRight - 1;
		columnCheck = inputCol + connectedTilesRight + 1;
		possibleRow = rowCheck;
		possibleCol = columnCheck;
		disconnected = 0;
		// Check upper right side starting from where previous check ended
		while ((rowCheck >= 0)&&(columnCheck <= model.getColumnLimit()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
			} else if (board[rowCheck][columnCheck].isEmpty()){
				disconnected++;
				if (disconnected == 1) {
					possibleRow = rowCheck;
					possibleCol = columnCheck;
				} else {
					break;
				}
			} else {
				break;
			}
		
			// A row 1 away from victory has been confirmed
			Integer[] candidateMoveUpperRightGap = {possibleRow, possibleCol};
			if ((connectedTilesRight+connectedTilesLeft >= scoreToWin-2)&&!bestContains(candidateMoveUpperRightGap, moveArray)) {
				moveArray.add(candidateMoveUpperRightGap);
				break;
			} 
			rowCheck--;
			columnCheck++;
		}
		
		/**
		 * Top left to bottom right diagonal checking
		 * 
		 */
		rowCheck = inputRow + 1;
		columnCheck = inputCol + 1;
		connectedTilesLeft = 0;
		connectedTilesRight = 0;
		// See if anything is to the lower right
		while ((rowCheck <= model.getRowLimit()-1)&&(columnCheck <= model.getColumnLimit()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
				rowCheck++;
				columnCheck++;
			} else {
				break;
			}
		}
		
		rowCheck = inputRow - 1;
		columnCheck = inputCol - 1;
		// Check if the best move is directly to the upper left 
		Integer[] candidateMoveUpperLeft = {inputRow-1, inputCol-1};
		if ((rowCheck >= 0)&&(columnCheck >= 0)&&(connectedTilesRight >= scoreToWin-2)&&board[inputRow-1][inputCol-1].isEmpty()&&!bestContains(candidateMoveUpperLeft, moveArray)) {
			moveArray.add(candidateMoveUpperLeft);
		}
		
		// Check if a piece can be played on the lower right side 
		Integer[] candidateMoveLowerRight = {inputRow+connectedTilesRight+1, inputCol+connectedTilesRight+1};
		if ((connectedTilesRight >= scoreToWin-2)&&(inputRow+connectedTilesRight+1 <= model.getRowLimit()-1)&&(inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow+connectedTilesRight+1][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveLowerRight, moveArray)) {
			moveArray.add(candidateMoveLowerRight);
		} else {
			possibleCol = 0;
			possibleRow = 0;
			disconnected = 0;
			// Then check upper left side
			while ((rowCheck >= 0)&&(columnCheck >= 0)) {
				// Same tile
				if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
					connectedTilesLeft++;
					// A row 1 from victory has been confirmed
					if ((connectedTilesLeft+connectedTilesRight >= scoreToWin-2)) {
						// It was found with 1 gap
						Integer[] candidateMoveUpperLeftGap = {possibleRow, possibleCol};
						Integer[] candidateMoveUpperLeftFar = {inputRow-connectedTilesLeft-1, inputCol-connectedTilesLeft-1};
						if ((possibleCol != 0)&&!bestContains(candidateMoveUpperLeftGap, moveArray)) {
							moveArray.add(candidateMoveUpperLeftGap);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputRow-connectedTilesLeft-1 >= 0)&&(inputCol-connectedTilesLeft-1 >= 0)&&board[inputRow-connectedTilesLeft-1][inputCol-connectedTilesLeft-1].isEmpty()&&!bestContains(candidateMoveUpperLeftFar, moveArray)) {
							moveArray.add(candidateMoveUpperLeftFar);
						}
						// Check if a piece can be played on the lower right side
						if ((inputRow+connectedTilesRight+1 <= model.getRowLimit()-1)&&(inputCol+connectedTilesRight+1 <= model.getColumnLimit()-1)&&board[inputRow+connectedTilesRight+1][inputCol+connectedTilesRight+1].isEmpty()&&!bestContains(candidateMoveLowerRight, moveArray)) {
							moveArray.add(candidateMoveLowerRight);
						}
						break;
					}
					rowCheck--;
					columnCheck--;
				// Open tile
				} else if (board[rowCheck][columnCheck].isEmpty()) {
					disconnected++;
					// Continue checking lower left if only 1 gap has been found
					if (disconnected == 1) {
						possibleRow = rowCheck;
						possibleCol = columnCheck;
						rowCheck--;
						columnCheck--;
					// Stop checking if more than 1 gap has been found
					} else {
						break;
					}
				// Other player's tile
				} else {
					break;
				}
			}
		}
		
		rowCheck = inputRow + connectedTilesRight + 1;
		columnCheck = inputCol + connectedTilesRight + 1;
		possibleRow = rowCheck;
		possibleCol = columnCheck;
		disconnected = 0;
		// Check lower right side starting from where previous check ended
		while ((rowCheck <= model.getRowLimit()-1)&&(columnCheck <= model.getColumnLimit()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				connectedTilesRight++;
			} else if (board[rowCheck][columnCheck].isEmpty()){
				disconnected++;
				if (disconnected == 1) {
					possibleRow = rowCheck;
					possibleCol = columnCheck;
				} else {
					break;
				}
			} else {
				break;
			}
			// A row 1 away from victory has been confirmed
			Integer[] candidateLowerRightGap = {possibleRow, possibleCol};
			if ((connectedTilesRight+connectedTilesLeft >= scoreToWin-2)&&!bestContains(candidateLowerRightGap, moveArray)) {
				moveArray.add(candidateLowerRightGap);
				break;
			} 
			rowCheck++;
			columnCheck++;
		}
	}
	
	/**
	 * Checks to see if move is already in bestArray
	 * 
	 * @param move - move to check, given in form {row, column}
	 * @param bestArray - ArrayList to look in, must be ArrayList<Integer[]>
	 * @return - true or false
	 */
	private boolean bestContains(Integer[] move, ArrayList<Integer[]> bestArray) {
		if (bestArray.size() == 0) {
			return false;
		}
		
		for (int i = 0; i < bestArray.size(); i++) {
			if ((bestArray.get(i)[0] == move[0])&&(bestArray.get(i)[1] == move[1])){
				removeAt = i;
				return true;
			}
		}
		return false;	
	}
	
	/**
	 * Gets the best move for the computer to play. First checks offensive moves (moves that would win the game)
	 * then checks defensive moves (moves to stop the player from winning). If they are both empty or no move in
	 * either ArrayLists is valid, then it selects a random move.
	 * 
	 * @return - Integer representing the column in which the computer should play
	 */
	private int getBestMove() {
		int bestMove;
		if (offensiveMoves.size() != 0) {
			for (int i = 0; i < offensiveMoves.size(); i++) {
				bestMove = offensiveMoves.get(i)[1];
				// If the row correlating to the column is the bottom row
				if (offensiveMoves.get(i)[0] == model.getRowLimit()-1) {
					offensiveMoves.remove(i);
					
					return bestMove;
				} else if (!model.getBoardGrid()[offensiveMoves.get(i)[0]+1][offensiveMoves.get(i)[1]].isEmpty()) {
					offensiveMoves.remove(i);
					
					return bestMove;
				}
			}
		}
		
		if (defensiveMoves.size() != 0){
			for (int i = 0; i < defensiveMoves.size(); i++) {
				bestMove = defensiveMoves.get(i)[1];
				// If the row correlating to the column is the bottom row
				if (defensiveMoves.get(i)[0] == model.getRowLimit()-1) {
					defensiveMoves.remove(i);
		
					return bestMove;
				} else if (!model.getBoardGrid()[defensiveMoves.get(i)[0]+1][defensiveMoves.get(i)[1]].isEmpty()) {
					defensiveMoves.remove(i);
					
					return bestMove;
				}
			}
		}
		
		return model.getOpenColumns().get(rand.nextInt(model.getOpenColumns().size()))-1;
	}
		
	/**
	 * Provides public access to calculate offensive moves for the computer
	 */
	public void findOffensiveMoves() {
		calculateMoves(computerMoveRow, computerMoveCol, offensiveMoves);	
	}
	
	/**
	 * Provides public access to calculate defensive moves for the computer
	 */
	public void findDefensiveMoves() {
		calculateMoves(model.getPlayerMoveRow(), model.getPlayerMoveCol(), defensiveMoves);
	}
	
	/**
	 * Public method to make the computer make a move
	 */
	public void takeTurn() {
		if (model.getOpenColumns().size() != 0) {
			computerMoveCol = getBestMove();
			
			updateComputerMoveRow();
			model.setBoardLocation(computerMoveRow, computerMoveCol, 'O');
		}
	}
	
	/**
	 * Checks to see if the player blocked the computer from winning, removes the move from the ArrayList if so
	 */
	public void checkBestMoves() {
		Integer[] playerMove = {model.getPlayerMoveRow(), model.getPlayerMoveCol()};
		
		if (bestContains(playerMove, offensiveMoves)) {
			offensiveMoves.remove(removeAt);
		}
	}
	
	/**
	 * Resets the computer for new games
	 */
	public void reset() {
		computerMoveCol = -1;
		computerMoveRow = -1;
		offensiveMoves.clear();
		defensiveMoves.clear();
	}

	public int getComputerMoveCol() {
		return computerMoveCol;
	}
	
	public int getComputerMoveRow() {
		return computerMoveRow;
	}
}