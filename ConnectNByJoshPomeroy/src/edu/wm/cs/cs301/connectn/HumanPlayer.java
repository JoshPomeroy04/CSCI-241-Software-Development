/**
 * Contains information regarding the human player. Prompts the player to make a move and 
 * stores the move so that it can be used in board processing
 * 
 *@author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn;


public class HumanPlayer implements Player {
	private static int playerColumn;
	private boolean playerTurn;
	
	public HumanPlayer() {

	}

	@Override
	public void takeTurn() {
		playerTurn = true;
		while(playerTurn) {
			System.out.println("\nEnter the number of the column you want to place in: ");
			String playerInput = Main.userInput.nextLine();
			
			// Quits game if user entered "QUIT"
			if (playerInput.equals("QUIT")) {
				System.out.println("Aww, giving up?");
				System.exit(0);
			}
			
			// Ensures the user entered a valid column number
			try {
				playerColumn = Integer.parseInt(playerInput);
				if (ConnectNGame.openColumns.contains(playerColumn)) {
					ConnectNGame.turn++;
					playerTurn = false;
				} else {
					System.out.println("Please input a valid column number\n\n");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please input a valid column number\n\n");
			}
		}
	}
	
	/**
	 * @return - returns the column that the player inputed 
	 */
	public static int getPlayerColumn() {
		return playerColumn;
	}
}