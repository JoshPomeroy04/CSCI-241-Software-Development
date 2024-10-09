/**
 * Creates and updates the game board. Contains the code responsible for checking 
 * how many pieces are touching each other. Also calculates the computer's best moves.
 * 
 * If you get a file error, change the variable FOLDERPATH to a string containing the correct folder path
 * of the resources folder for your machine, for example this is my path: C:/Users/Joshu/git/ConnectNByJoshPomeroy/resources/
 * 
 * @author Josh Pomeroy 
 */
package edu.wm.cs.cs301.connectn;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class GameBoard {
	private Location[][] board;			//do not change!
	private String RUNNINGPATH = getClass().getClassLoader().getResource(".").getPath();    // Attempting to get the correct path without hard coding
	private String FOLDERPATH = RUNNINGPATH.substring(0, RUNNINGPATH.length()-4)+"resources/";
	private File CURRENTBOARD = new File(FOLDERPATH+"CurrentGameBoard.txt"); 
	private File SMALLBOARD = new File(FOLDERPATH+"SmallGameBoard.txt");
	private File MEDIUMBOARD = new File(FOLDERPATH+"MediumGameBoard.txt");
	private File LARGEBOARD = new File(FOLDERPATH+"LargeGameBoard.txt");
	private ArrayList<String> boardArray = new ArrayList<String>();
	private FileWriter fWriter = null;
	private Scanner boardScanner = null;
	private int scoreToWin;
	private int rowLiteral;
	private int playerMoveCol;
	private int playerMoveRow;
	private int computerMoveCol;
	private int computerMoveRow;
	private ArrayList<Integer> computerBestOffCol = new ArrayList<Integer>();
	private ArrayList<Integer> computerBestOffRow = new ArrayList<Integer>();
	private ArrayList<Integer> computerBestDefCol = new ArrayList<Integer>();
	private ArrayList<Integer> computerBestDefRow = new ArrayList<Integer>();
	private int connected;
	private int connectedLeft;
	private int connectedRight;
	private int connectedDiagUpRight;
	private int connectedDiagDownLeft;
	private int connectedDiagUpLeft;
	private int connectedDiagDownRight;
	private int removeAt;
	
	private int computerTilesLeft;
	private int computerTilesRight;
	private int computerTilesBelow;
	private int disconnected;
	private int  possibleCol;
	private int possibleRow;
	private int rowCheck;
	private int columnCheck;
	public boolean playerWin;
	
	public GameBoard() {
		// Sets how many pieces in a row are required to win and prepares initial board setup
		try {
			switch(Main.getGameMode()) {
				case("Small"):
					scoreToWin = 3;
					boardScanner = new Scanner(SMALLBOARD);
					break;
				case("Medium"):
					scoreToWin = 4;
					boardScanner = new Scanner(MEDIUMBOARD);
					break;
				case("Large"):
					scoreToWin = 5;
					boardScanner = new Scanner(LARGEBOARD);
					break;
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
				
		// Copies blank board to the file that will be changed
		openWriter();
		while (boardScanner.hasNextLine()) {
			writeToBoard(boardScanner.nextLine());
		}
				
		closeWriter();
		boardScanner.close();
		// Creates location objects for every game square		
		populateBoard(Main.getGameMode());
	}
	
	/**
	 * Opens the file writer
	 */
	private void openWriter() {
		try {
			fWriter = new FileWriter(CURRENTBOARD, false);
		} catch(IOException e) {
	        System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Closes the file writer
	 */
	private void closeWriter() {
		try {
			fWriter.flush();
			fWriter.close();
		} catch(IOException e) {
	        System.out.println(e.getMessage());
		}
	}
	
	/**
	 * fWriter must be opened prior to calling
	 * 
	 * Writes to the file storing the current game
	 * @param contents - What should be written to the file
	 */
	private void writeToBoard(String contents) {
		try {
			fWriter.append(contents+"\n");
			fWriter.flush();
		} catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}
	
	/**
	 * Reads in the current board and stores it in an ArrayList by line
	 */
	private void readFromBoard() {
		try {
			boardScanner = new Scanner(CURRENTBOARD);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		while (boardScanner.hasNextLine()) {
			boardArray.add(boardScanner.nextLine());
		}
		boardScanner.close();
	}
	
	/**
	 * Populates the 2D Array board with Location objects based on the
	 * board size selected by the player.
	 * @param size - Board size, "Small" "Medium" or "Large"
	 */
	private void populateBoard(String size) {
		switch (size) {
			case "Small":
				board = new Location[4][5];
				for (int r = 1; r <= 4; r++) {
					for (int c = 1; c <= 5; c++) {
						board[r-1][c-1] = new Location();
					}
				}
			case "Medium":
				board = new Location[6][7];
				for (int r = 1; r <= 6; r++) {
					for (int c = 1; c <= 7; c++) {
						board[r-1][c-1] = new Location();
					}
				}
			case "Large":
				board = new Location[8][9];	
				for (int r = 1; r <= 8; r++) {
					for (int c = 1; c <= 9; c++) {
						board[r-1][c-1] = new Location();
					}
				}
		}
	}
	
	/**
	 * Displays the current game board
	 */
	public void displayBoard() {
		try {
			boardScanner = new Scanner(CURRENTBOARD);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("\n\nTurn: "+ConnectNGame.turn);
		for(int i = 0; i < Main.getColumnSize()*2; i++) {
			System.out.println(boardScanner.nextLine());
		}
		boardScanner.close();
	}
	
	/**
	 * Updates the board to display moves
	 * @param player - Human or Computer player
	 */
	public void updateBoard(String player) {
		rowLiteral = -1;
		
		// Places X and lets it "fall" to the bottom
		if (player.equals("Human")) {
			for (int r = Main.getRowSize(); r > 0; r--) {
				if (board[r-1][HumanPlayer.getPlayerColumn()-1].isEmpty()) {
					board[r-1][HumanPlayer.getPlayerColumn()-1].setToken('X');
					rowLiteral = r;
					break;
				}
			}
		}
		// Places O and lets it "fall" to the bottom
		if (player.equals("Computer")) {
			for (int r = Main.getRowSize(); r > 0; r--) {
				if (board[r-1][ComputerPlayer.getComputerColumn()-1].isEmpty()) {
					board[r-1][ComputerPlayer.getComputerColumn()-1].setToken('O');
					rowLiteral = r;
					break;
				}
			}
		}
		
		// Removes the ability to place pieces in columns that are full
		if (rowLiteral == 1) {
			switch (player) {
				case "Human":
					ConnectNGame.openColumns.remove(Integer.valueOf(HumanPlayer.getPlayerColumn()));
					break;
				case "Computer":
					ConnectNGame.openColumns.remove(Integer.valueOf(ComputerPlayer.getComputerColumn()));
					break;
			}
		}
		
		// Reads board and updates the line where the new piece has "fallen"
		readFromBoard();
		openWriter();
		String[] edittingSplit = boardArray.get(rowLiteral*2).split("");
		ArrayList<String> edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
		
		// Updates the actual board to display an X or O
		switch (player) {
			case "Human":
				edittingList.set((3+4*(HumanPlayer.getPlayerColumn()-1)), "X");
				break;
			case "Computer":
				edittingList.set((3+4*(ComputerPlayer.getComputerColumn()-1)), "O");
				break;
		}
		// Update the file storing the board
		boardArray.set(rowLiteral*2, String.join("", edittingList));    
		for (int i = 0; i < boardArray.size(); i++) {
			writeToBoard(boardArray.get(i));
		}
		closeWriter();
		boardArray.clear();	
	}
	
	/**
	 * Checks if the tile under the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile under the given coords has the same character, false if not
	 */
	private boolean tileUnder(int row, int col) {
		if ((row != Main.getRowSize()-1)&&(board[row][col].equals(board[row+1][col]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile right of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile right of the given coords has the same character, false if not
	 */
	private boolean tileRight(int row, int col) {
		if ((col != Main.getColumnSize()-1)&&(board[row][col].equals(board[row][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile left of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile left of the given coords has the same character, false if not
	 */
	private boolean tileLeft(int row, int col) {
		if ((col != 0)&&(board[row][col].equals(board[row][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile up and right of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile up and right of the given coords has the same character, false if not
	 */
	private boolean tileUpRight(int row, int col) {
		if ((row != 0)&&(col != Main.getColumnSize()-1)&&(board[row][col].equals(board[row-1][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile down and left of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile down and left of the given coords has the same character, false if not
	 */
	private boolean tileDownLeft(int row, int col) {
		if ((row != Main.getRowSize()-1)&&(col != 0)&&(board[row][col].equals(board[row+1][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile up and left of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile up and left of the given coords has the same character, false if not
	 */
	private boolean tileUpLeft(int row, int col) {
		if ((row != 0)&&(col != 0)&&(board[row][col].equals(board[row-1][col-1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks if the tile down and right of the given coords contains the same character
	 * @param row - Row to check
	 * @param col - Column to check
	 * @return - true if the tile down and right of the given coords has the same character, false if not
	 */
	private boolean tileDownRight(int row, int col) {
		if ((row != Main.getRowSize()-1)&&(col != Main.getColumnSize()-1)&&(board[row][col].equals(board[row+1][col+1]))) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks tiles under where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesUnder(int row, int col) { 
		// Checks if the tile under the most recent play is the same player
		if (tileUnder(row, col)) { 
			connected = 1;
			// Sees how many connected tiles there are
			for (int r = row+2; r <= Main.getRowSize()-1; r++) { 
				if (board[r][col].equals(board[row][col])) {
					connected++;
				} else {
					break;
				}
			}
			
			// Updates the tiles connected variable for connected tiles 
			for (int r = 0; r <= connected; r++) {
				if (board[row+r][col].getConnected() < connected) {
					board[row+r][col].setConnected(connected);
				}
			}
		}
	}
	
	/**
	 * Checks tiles to the right of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesRight(int row, int col) {
		// Checks if the tile to the right of the most recent play is the same player
		if (tileRight(row, col)) {
			connectedRight = 1;
			// Sees how many connected tiles there are
			for (int r = col+2; r <= Main.getColumnSize()-1; r++) {
				if (board[row][r].equals(board[row][col])) {
					connectedRight++;
				} else {
					break;
				}
			}
			
			// Updates the tiles connected variable for connected tiles 
			for (int r = 0; r <= connectedRight; r++) {
				if (board[row][col+r].getConnected() < connectedRight) {
					board[row][col+r].setConnected(connectedRight);
				}
			}
		}
	}

	/**
	 * Checks tiles to the left of where the most recent piece was played 
	 * and updates tiles to the right of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesLeft(int row, int col) {
		// Checks if the tile to the left of the most recent play is the same player
		if (tileLeft(row, col)) {
			connectedLeft = 1;
			// Sees how many connected tiles there are 
			for (int r = col-2; r >= 0; r--) {
				if (board[row][r].equals(board[row][col])) {
					connectedLeft++;
				} else {
					break;
				}
			}
			
			// Updates right side if piece is between two other pieces
			if ((col != Main.getColumnSize()-1)&&(board[row][col].equals(board[row][col+1]))) {
				for (int r = 1; r <= connectedRight; r++) {
					if (board[row][col+r].getConnected() <= connectedLeft+connectedRight) {
						board[row][col+r].setConnected(connectedLeft+connectedRight);
					}
				}
			}
			
			// Updates the tiles connected variable for connected tiles 
			for (int r = 0; r <= connectedLeft; r++) {
				if (board[row][col-r].getConnected() < connectedLeft+connectedRight) {
					board[row][col-r].setConnected(connectedLeft+connectedRight);
				}
			}
		}
	}
	
	/**
	 * Checks tiles that are diagonally up and to the right of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesDiagUpRight(int row, int col) {
		int rowCheck = row - 2;
		int columnCheck = col + 2;		
		
		// Checks if the tile diagonally up and to the right of the most recent play is the same player
		if (tileUpRight(row, col)) {
			connectedDiagUpRight = 1;
			// Sees how many connected tiles there are 
			while ((rowCheck >= 0)&&(columnCheck <= Main.getColumnSize()-1)) {
				if (board[rowCheck][columnCheck].equals(board[row][col])) {
					connectedDiagUpRight++;
					rowCheck--;
					columnCheck++;
				} else {
					break;
				}
			}
			
			// Updates the tiles connected variable for connected tiles 
			for (int r = 0; r <= connectedDiagUpRight; r++) {
				if (board[row-r][col+r].getConnected() < connectedDiagUpRight) {
					board[row-r][col+r].setConnected(connectedDiagUpRight);
				}
			}
		}
	}
	
	/**
	 * Checks tiles that are diagonally down and to the left of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesDiagDownLeft(int row, int col) {
		int rowCheck = row + 2;
		int columnCheck = col - 2;		
		
		// Checks if the tile diagonally down and to the left of the most recent play is the same player
		if (tileDownLeft(row, col)) {
			connectedDiagDownLeft = 1;
			// Sees how many connected tiles there are 
			while ((rowCheck <= Main.getRowSize()-1)&&(columnCheck >= 0)) {
				if (board[rowCheck][columnCheck].equals(board[row][col])) {
					connectedDiagDownLeft++;
					rowCheck++;
					columnCheck--;
				} else {
					break;
				}
			}
		}
		
		// Updates diagonally up and to the right if piece is between two other pieces
		if ((row != 0)&&(col != Main.getColumnSize()-1)&&(board[row][col].equals(board[row-1][col+1]))) {
			for (int r = 1; r <= connectedDiagUpRight; r++) {
				if (board[row-r][col+r].getConnected() <= connectedDiagDownLeft+connectedDiagUpRight) {
					board[row-r][col+r].setConnected(connectedDiagDownLeft+connectedDiagUpRight);
				}
			}
		}
		
		// Updates the tiles connected variable for connected tiles 
		for (int r = 0; r <= connectedDiagDownLeft; r++) {
			if (board[row+r][col-r].getConnected() < connectedDiagDownLeft+connectedDiagUpRight) {
				board[row+r][col-r].setConnected(connectedDiagDownLeft+connectedDiagUpRight);
			}
		}
	}
	
	/**
	 * Checks tiles that are diagonally up and to the left of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesDiagUpLeft(int row, int col) {
		int rowCheck = row - 2;
		int columnCheck = col - 2;
		
		// Checks if the tile diagonally up and to the left of the most recent play is the same player
		if (tileUpLeft(row, col)) {
			connectedDiagUpLeft = 1;
			// Sees how many connected tiles there are 
			while ((rowCheck >= 0)&&(columnCheck >= 0)) {
				if (board[rowCheck][columnCheck].equals(board[row][col])) {
					connectedDiagUpLeft++;
					rowCheck--;
					columnCheck--;
				} else {
					break;
				}
			}
		}
		
		// Updates the tiles connected variable for connected tiles
		for (int r = 0; r <= connectedDiagUpLeft; r++) {
			if (board[row-r][col-r].getConnected() < connectedDiagUpLeft) {
				board[row-r][col-r].setConnected(connectedDiagUpLeft);
			}
		}
	}
	
	/**
	 * Checks tiles that are diagonally down and to the right of where the most recent piece was played
	 * @param row - Row to check
	 * @param col - Column to check
	 */
	private void checkTilesDiagDownRight(int row, int col) {
		int rowCheck = row + 2;
		int columnCheck = col + 2;
		
		// Checks if the tile diagonally up and to the left of the most recent play is the same player
		if (tileDownRight(row, col)) {
			connectedDiagDownRight = 1;
			// Sees how many connected tiles there are 
			while ((rowCheck <= Main.getRowSize()-1)&&(columnCheck <= Main.getColumnSize()-1)) {
				if (board[rowCheck][columnCheck].equals(board[row][col])) {
					connectedDiagDownRight++;
					rowCheck++;
					columnCheck++;
				} else {
					break;
				}
			}	
		}
		
		// Updates diagonally up and to the left if piece is between two other pieces
		if ((row != 0)&&(col != 0)&&(board[row][col].equals(board[row-1][col-1]))) {
			for (int r = 1; r <= connectedDiagUpLeft; r++) {
				if (board[row-r][col-r].getConnected() <= connectedDiagUpLeft+connectedDiagDownRight) {
					board[row-r][col-r].setConnected(connectedDiagUpLeft+connectedDiagDownRight);
				}
			}
		}
		
		// Updates the tiles connected variable for connected tiles
		for (int r = 0; r <= connectedDiagDownRight; r++) {
			if (board[row+r][col+r].getConnected() < connectedDiagUpLeft+connectedDiagDownRight) {
				board[row+r][col+r].setConnected(connectedDiagUpLeft+connectedDiagDownRight);
			}
		}
	}

	/**
	 * Checks to see if the game has been won
	 * @param player - Computer or Human player
	 */
	private void checkWin(String player) {
		if (player.equals("Human")) {
			if (board[playerMoveRow][playerMoveCol].getConnected() >= scoreToWin-1) {
				displayBoard();
				System.out.println("\n\nYou Won!");
				System.out.println("It took you: "+(ConnectNGame.turn-1)+" moves to win.");
				playerWin = true;
				ConnectNGame.gameLoop = false;
			}
		} else {
			if (board[computerMoveRow][computerMoveCol].getConnected() >= scoreToWin-1) {
				System.out.println("\n\nYou Lost!");
				System.out.println("Better luck next time!\n");
				playerWin = false;
				ConnectNGame.gameLoop = false;
			}
		}
	}
	
	/**
	 * Performs a check on all elements in the computer's best moves to ensure none of them are out of bounds
	 */
	private void cleanBest() {
		for (int i = 0; i < computerBestOffCol.size(); i++) {
			if((computerBestOffRow.get(i) < 0)||(computerBestOffRow.get(i) >= Main.getRowSize())||(computerBestOffCol.get(i) < 0)||(computerBestOffCol.get(i) >= Main.getColumnSize())) {
				computerBestOffRow.remove(i);
				computerBestOffCol.remove(i);
				break;
			}
		}
		
		for (int i = 0; i < computerBestDefCol.size(); i++) {
			if((computerBestDefRow.get(i) < 0)||(computerBestDefRow.get(i) >= Main.getRowSize())||(computerBestDefCol.get(i) < 0)||(computerBestDefCol.get(i) >= Main.getColumnSize())) {
				computerBestDefRow.remove(i);
				computerBestDefCol.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Checks to see if a move is already in the computer's moves
	 * @param row - Row to check
	 * @param col - Column to check
	 * @param kind - Offense or Defense move
	 * @return - True if the move is already store and false if it isn't
	 */
	private boolean bestContains(int row, int col, String kind) {
		if (kind.equals("Offense")) {
			for (int i = 0; i < computerBestOffCol.size(); i++) {
				if((computerBestOffRow.get(i) == row)&&(computerBestOffCol.get(i) == col)) {
					removeAt = i;
					return true;
				}
			}
			return false;
		}
		
		if (kind.equals("Defense")){
			for (int i = 0; i < computerBestDefCol.size(); i++) {
				if((computerBestDefRow.get(i) == row)&&(computerBestDefCol.get(i) == col)) {
					removeAt = i;
					return true;
				}
			}
			return false;
		}
		return false;
		
	}
	
	/**
	 * Returns the first available column in the computer's best moves
	 * @return - Column number
	 */
	public int getBestMove() {
		// Offense first
		for (int i = 0; i < computerBestOffRow.size(); i++) {
			// If the row correlating to the column is the bottom row
			if (computerBestOffRow.get(i) == Main.getRowSize()-1) {
				computerBestOffRow.remove(i);
				return computerBestOffCol.remove(i);
			} else if (!board[computerBestOffRow.get(i)+1][computerBestOffCol.get(i)].isEmpty()) {
				computerBestOffRow.remove(i);
				return computerBestOffCol.remove(i);
			}
		}
		
		// Defense second
		for (int i = 0; i < computerBestDefRow.size(); i++) {
			// If the row correlating to the column is the bottom row
			if (computerBestDefRow.get(i) == Main.getRowSize()-1) {
				computerBestDefRow.remove(i);
				return computerBestDefCol.remove(i);
			} else if (!board[computerBestDefRow.get(i)+1][computerBestDefCol.get(i)].isEmpty()) {
				computerBestDefRow.remove(i);
				return computerBestDefCol.remove(i);
			}
		}
		// Best move not playable
		return -1;
	}
	
	/**
	 * Calculates the best move for the computer by checking the tiles around inputRow and inputCol to 
	 * calculate how many pieces in a row there are. If the piece in a row is 1 less than that required to win,
	 * it stores the location of where a piece can be played to create a row big enough to win the game.
	 * Also works to calculate player moves for defense to block the player from winning.
	 * @param inputRow - Row to check
	 * @param inputCol - Column to check
	 * @param storeRow - ArrayList to store the row of the best move
	 * @param storeCol - ArrayList to store the column of the best move
	 * @param kind - Offense or Defense 
	 */
	private void calculateMoves(int inputRow, int inputCol, ArrayList<Integer> storeRow, ArrayList<Integer> storeCol, String kind) {
		computerTilesLeft = 0;
		computerTilesRight = 0;
		computerTilesBelow = 0;
		
		// Check below
		for (int r = inputRow+1; r <= Main.getRowSize()-1; r++) {   
			if (board[r][inputCol].equals(board[inputRow][inputCol])) {
				computerTilesBelow++;
				if ((inputRow-1 >= 0)&&(computerTilesBelow >= scoreToWin-2)&&!bestContains(inputRow-1, inputCol, kind)) {
					storeRow.add(inputRow-1);
					storeCol.add(inputCol);
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
		for (int r = inputCol+1; r <= Main.getColumnSize()-1; r++) {
			if (board[inputRow][r].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
			} else {
				break;
			}
		}
		
		// Check if the winning move is directly to the left of most recent placement 
		if ((inputCol-1 >= 0)&&(computerTilesRight >= scoreToWin-2)&&board[inputRow][inputCol-1].isEmpty()&&!bestContains(inputRow, inputCol-1, kind)) {
			storeRow.add(inputRow);
			storeCol.add(inputCol-1);
		}
		
		// Check if the winning move is to the right of most recent placement
		if ((computerTilesRight >= scoreToWin-2)&&(inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow, inputCol+computerTilesRight+1, kind)) {
			storeRow.add(inputRow);
			storeCol.add(inputCol+computerTilesRight+1);
		} else {
			possibleCol = 0;
			disconnected = 0;
			// Then check left side
			for (int r = inputCol-1; r >= 0; r--) {
				// Same tile
				if (board[inputRow][r].equals(board[inputRow][inputCol])) {
					computerTilesLeft++;
					// A row 1 away from victory has been confirmed
					if ((computerTilesLeft+computerTilesRight >= scoreToWin-2)) {
						// It was found with 1 gap 
						if ((possibleCol != 0)&&!bestContains(inputRow, possibleCol, kind)) {
							storeRow.add(inputRow);
							storeCol.add(possibleCol);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputCol-computerTilesLeft-1 >= 0)&&board[inputRow][inputCol-computerTilesLeft-1].isEmpty()&&!bestContains(inputRow, inputCol-computerTilesLeft-1, kind)) {
							storeRow.add(inputRow);
							storeCol.add(inputCol-computerTilesLeft-1);	
						}
						// Check if the winning move is on the right side
						if ((inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow, inputCol+computerTilesRight+1, kind)) {
							storeRow.add(inputRow);
							storeCol.add(inputCol+computerTilesRight+1);
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
		columnCheck = computerTilesRight;
		// Check right side starting from where previous check ended
		for (int r = inputCol+columnCheck+1; r <= Main.getColumnSize()-1; r++) {
			// Empty tile but next tile to the right is the same
			if (board[inputRow][r].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
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
			if ((computerTilesRight+computerTilesLeft >= scoreToWin-2)&&!bestContains(inputRow, possibleCol, kind)) {
				storeRow.add(inputRow);
				storeCol.add(possibleCol);
				break;
			} 
		}
		
		/**
		 * Bottom left to up right diagonal checking
		 * 
		 */
		rowCheck = inputRow - 1;
		columnCheck = inputCol + 1;
		computerTilesLeft = 0;
		computerTilesRight = 0;
		
		// See if anything is to the upper right
		while ((rowCheck >= 0)&&(columnCheck <= Main.getColumnSize()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
				rowCheck--;
				columnCheck++;
			} else {
				break;
			}
		}
		
		rowCheck = inputRow + 1;
		columnCheck = inputCol - 1;
		// Check if the best move is directly to the lower left 
		if ((rowCheck <= Main.getRowSize()-1)&&(columnCheck >= 0)&&(computerTilesRight >= scoreToWin-2)&&board[inputRow+1][inputCol-1].isEmpty()&&!bestContains(inputRow+1, inputCol-1, kind)) {
			storeRow.add(inputRow+1);
			storeCol.add(inputCol-1);
		}
		
		// Check if a piece can be played on the upper right side
		if ((computerTilesRight >= scoreToWin-2)&&(inputRow-computerTilesRight-1 >= 0)&&(inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow-computerTilesRight-1][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow-computerTilesRight-1, inputCol+computerTilesRight+1, kind)) {
			storeRow.add(inputRow-computerTilesRight-1);
			storeCol.add(inputCol+computerTilesRight+1);
		} else {
			possibleCol = 0;
			possibleRow = 0;
			disconnected = 0;
			// Then check lower left side
			while ((rowCheck <= Main.getRowSize()-1)&&(columnCheck >= 0)) {
				// Same tile
				if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
					computerTilesLeft++;
					// A row 1 from victory has been confirmed
					if (computerTilesLeft+computerTilesRight >= scoreToWin-2) {
						// It was found with 1 gap
						if ((possibleCol != 0)&&!bestContains(possibleRow, possibleCol, kind)) {
							storeRow.add(possibleRow);
							storeCol.add(possibleCol);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputRow+computerTilesLeft+1 <= Main.getRowSize()-1)&&(inputCol-computerTilesLeft-1 >= 0)&&board[inputRow+computerTilesLeft+1][inputCol-computerTilesLeft-1].isEmpty()&&!bestContains(inputRow+computerTilesLeft+1, inputCol-computerTilesLeft-1, kind)) {
							storeRow.add(inputRow+computerTilesLeft+1);
							storeCol.add(inputCol-computerTilesLeft-1);
						}
						// Check if a piece can be played on the upper right side
						if ((inputRow-computerTilesRight-1 >= 0)&&(inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow-computerTilesRight-1][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow-computerTilesRight-1, inputCol+computerTilesRight+1, kind)) {
							storeRow.add(inputRow-computerTilesRight-1);
							storeCol.add(inputCol+computerTilesRight+1);
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
		
		rowCheck = inputRow - computerTilesRight - 1;
		columnCheck = inputCol + computerTilesRight + 1;
		possibleRow = rowCheck;
		disconnected = 0;
		// Check upper right side starting from where previous check ended
		while ((rowCheck >= 0)&&(columnCheck <= Main.getColumnSize()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
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
			if ((computerTilesRight+computerTilesLeft >= scoreToWin-2)&&!bestContains(possibleRow, possibleCol, kind)) {
				storeRow.add(possibleRow);
				storeCol.add(possibleCol);
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
		computerTilesLeft = 0;
		computerTilesRight = 0;
		// See if anything is to the lower right
		while ((rowCheck <= Main.getRowSize()-1)&&(columnCheck <= Main.getColumnSize()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
				rowCheck++;
				columnCheck++;
			} else {
				break;
			}
		}
		
		rowCheck = inputRow - 1;
		columnCheck = inputCol - 1;
		// Check if the best move is directly to the upper left 
		if ((rowCheck >= 0)&&(columnCheck >= 0)&&(computerTilesRight >= scoreToWin-2)&&board[inputRow-1][inputCol-1].isEmpty()&&!bestContains(inputRow-1, inputCol-1, kind)) {
			storeRow.add(inputRow-1);
			storeCol.add(inputCol-1);
		}
		
		// Check if a piece can be played on the lower right side 
		if ((computerTilesRight >= scoreToWin-2)&&(inputRow+computerTilesRight+1 <= Main.getRowSize()-1)&&(inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow+computerTilesRight+1][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow+computerTilesRight+1, inputRow+computerTilesRight+1, kind)) {
			storeRow.add(inputRow+computerTilesRight+1);
			storeCol.add(inputRow+computerTilesRight+1);
		} else {
			possibleCol = 0;
			possibleRow = 0;
			disconnected = 0;
			// Then check upper left side
			while ((rowCheck >= 0)&&(columnCheck >= 0)) {
				// Same tile
				if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
					computerTilesLeft++;
					// A row 1 from victory has been confirmed
					if ((computerTilesLeft+computerTilesRight >= scoreToWin-2)) {
						// It was found with 1 gap
						if ((possibleCol != 0)&&!bestContains(possibleRow, possibleCol, kind)) {
							storeRow.add(possibleRow);
							storeCol.add(possibleCol);
							break;
						// There is no gaps, and the space on the other side of the row is open
						} else if ((inputRow-computerTilesLeft-1 >= 0)&&(inputCol-computerTilesLeft-1 >= 0)&&board[inputRow-computerTilesLeft-1][inputCol-computerTilesLeft-1].isEmpty()&&!bestContains(inputRow-computerTilesLeft-1, inputCol-computerTilesLeft-1, kind)) {
							storeRow.add(inputRow-computerTilesLeft-1);
							storeCol.add(inputCol-computerTilesLeft-1);
						}
						// Check if a piece can be played on the lower right side
						if ((inputRow+computerTilesRight+1 <= Main.getRowSize()-1)&&(inputCol+computerTilesRight+1 <= Main.getColumnSize()-1)&&board[inputRow+computerTilesRight+1][inputCol+computerTilesRight+1].isEmpty()&&!bestContains(inputRow+computerTilesRight+1, inputCol+computerTilesRight+1, kind)) {
							storeRow.add(inputRow+computerTilesRight+1);
							storeCol.add(inputCol+computerTilesRight+1);
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
		
		rowCheck = inputRow + computerTilesRight + 1;
		columnCheck = inputCol + computerTilesRight + 1;
		possibleRow = rowCheck;
		disconnected = 0;
		// Check lower right side starting from where previous check ended
		while ((rowCheck <= Main.getRowSize()-1)&&(columnCheck <= Main.getColumnSize()-1)) {
			if (board[rowCheck][columnCheck].equals(board[inputRow][inputCol])) {
				computerTilesRight++;
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
			if ((computerTilesRight+computerTilesLeft >= scoreToWin-2)&&!bestContains(possibleRow, possibleCol, kind)) {
				storeRow.add(possibleRow);
				storeCol.add(possibleCol);
				break;
			} 
			rowCheck++;
			columnCheck++;
		}
	}
	
	/**
	 * Checks all connection sides on the piece just played
	 * @param player - Human or Computer player
	 */
	public void checkConnection(String player) {
		playerMoveRow = rowLiteral - 1;
		playerMoveCol = HumanPlayer.getPlayerColumn() - 1;
		computerMoveRow = rowLiteral - 1;
		computerMoveCol = ComputerPlayer.getComputerColumn() - 1;
		connected = 0;
		connectedLeft = 0;
		connectedRight = 0;
		connectedDiagUpRight = 0;
		connectedDiagDownLeft = 0;
		connectedDiagUpLeft = 0;
		connectedDiagDownRight = 0;
		
		if (player.equals("Human")) {
			checkTilesUnder(playerMoveRow, playerMoveCol);
			checkTilesRight(playerMoveRow, playerMoveCol);
			checkTilesLeft(playerMoveRow, playerMoveCol);
			checkTilesDiagUpRight(playerMoveRow, playerMoveCol);
			checkTilesDiagDownLeft(playerMoveRow, playerMoveCol);
			checkTilesDiagUpLeft(playerMoveRow, playerMoveCol);
			checkTilesDiagDownRight(playerMoveRow, playerMoveCol);
			
			if (!bestContains(playerMoveRow, playerMoveCol, "Defense")) {
				calculateMoves(playerMoveRow, playerMoveCol, computerBestDefRow, computerBestDefCol, "Defense");
			}
			
			if (bestContains(playerMoveRow, playerMoveCol, "Offense")) {
				computerBestOffRow.remove(removeAt);
				computerBestOffCol.remove(removeAt);
			}
		} else {
			checkTilesUnder(computerMoveRow, computerMoveCol);
			checkTilesRight(computerMoveRow, computerMoveCol);
			checkTilesLeft(computerMoveRow, computerMoveCol);
			checkTilesDiagUpRight(computerMoveRow, computerMoveCol);
			checkTilesDiagDownLeft(computerMoveRow, computerMoveCol);
			checkTilesDiagUpLeft(computerMoveRow, computerMoveCol);
			checkTilesDiagDownRight(computerMoveRow, computerMoveCol);	
			
			if (!bestContains(computerMoveRow, computerMoveCol, "Offense")) {
				calculateMoves(computerMoveRow, computerMoveCol, computerBestOffRow, computerBestOffCol, "Offense");
			}
			
			if (bestContains(computerMoveRow, computerMoveCol, "Defense")) {
				computerBestDefRow.remove(removeAt);
				computerBestDefCol.remove(removeAt);
			}
		}
		cleanBest();
		checkWin(player);	
	}
}