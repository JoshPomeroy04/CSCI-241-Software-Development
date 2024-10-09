/**
 * Contains the loop that creates and recreates the game instance if the player wants to play again.
 * Also displays the welcome message and has the method for selecting a game mode. Contains the 
 * scanner that is used throughout the game to retrieve user input
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn;

import java.util.Scanner;


public class Main {
	private static String gameMode;
	private static boolean selection;
	private static int maxColumnSize;
	private static int maxRowSize;
	private static Leaderboard leaderboard = new Leaderboard();
	public static Scanner userInput = new Scanner(System.in);
	
	
	public static void main(String[] args){
		boolean gameRunning = true;
		welcomeToGame();
		
		// Play again loop
		while (gameRunning) {
			selection = false;
			ConnectNGame game = new ConnectNGame();
			if (game.playerWin) {
				leaderboard.updateLeaderboard();
			}
			leaderboard.displayLeaderboard();
			
			// Loop to force the user to input "Y", "N", or "QUIT"
			while (!selection) {
				System.out.println("Would you like to play again? (Y/N)");
				String playAgain = userInput.nextLine();
				switch (playAgain) {
					case "N":
						System.out.println("Thanks for playing!");
						System.exit(0);
				
					case "Y":
						selection = true;
						break;
					case "QUIT":
						System.out.println("Thanks for playing!");
						System.exit(0);
					default:
						System.out.println("\nPlease select an option: Y/N");
				}
			}
			modeSelection();
		}
		userInput.close();	
	}
	
	
	
	/**
	 * Prompts user for game size and stores the resulting maximum amount of columns and rows and sets the mode for the leaderboard
	 */
	private static void modeSelection() {
		selection = false;
		
		System.out.println("Select a mode by typing 'Small' 'Medium' or 'Large'");
		while (!selection){
			gameMode = userInput.nextLine();
			switch (gameMode) {
				case "Small":
					maxColumnSize = 5;
					maxRowSize = 4;
					leaderboard.setMode = 1;
					selection = true;
					break;
				case "Medium":
					maxColumnSize = 7;
					maxRowSize = 6;
					leaderboard.setMode = 2;
					selection = true;
					break;
				case "Large":
					maxColumnSize = 9;
					maxRowSize = 8;
					leaderboard.setMode = 3;
					selection = true;
					break;
				case "QUIT":
					System.out.println("Aww, you too scared to play :(");
					System.exit(0);
				default:
					System.out.println("Select a mode by typing 'Small' 'Medium' or 'Large'");
			}
		}
	}
	
	/**
	 * Prints out the welcome message and calls modeSelection()
	 * Also displays leaderboard
	 */
	private static void welcomeToGame() {
		System.out.println("Welcome to ConnectN game!\n"
				+ "Rules:\n\n"
				+ "The goal of the game is to connect your pieces in a row,\n"
				+ "for the 'small' mode you must connect 3 in a row,\n"
				+ "for the 'medium' mode you must connect 4 in a row,\n"
				+ "and for the 'large' mode you must connect 5 in a row.\n\n"
				+ "You will alternate placing your pieces on the board with the computer,\n"
				+ "if the board fills without a winner then the game result is a tie.\n\n"
				+ "To input your move, type the number of the column you want to place in.\n"
				+ "Your piece will 'drop' to the lowest available row and your turn will be over.\n\n"
				+ "The leaderboard is based on how many moves it takes you to win, good luck!\n\n\n\n");
		leaderboard.displayLeaderboard();
		
		modeSelection();
	}
	
	/**
	 * @return - returns the size of the board as a string 
	 */
	public static String getGameMode() {
		return gameMode;
	}
	
	/**
	 * @return - returns the max number of columns as an int
	 */
	public static int getColumnSize() {
		return maxColumnSize;
	}
	
	/**
	 * @return - returns the max number of rows as an int
	 */
	public static int getRowSize() {
		return maxRowSize;
	}
}
