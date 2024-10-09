/**
 * Contains a single game instance. Runs the game loop and switches between human
 * and computer player turns. Checks the state of the game after each turn to see if 
 * the game is over.
 * 
 * @author Josh Pomeroy 
 */
package edu.wm.cs.cs301.connectn;
import java.util.ArrayList;

public class ConnectNGame {
	private GameBoard board;
	private HumanPlayer human;
	private ComputerPlayer computer;
	private boolean playerTurn = true;
	public static boolean gameLoop;
	public static int turn;
	public static ArrayList<Integer> openColumns = new ArrayList<Integer>();
	public boolean playerWin;
	
	public ConnectNGame() {
		gameLoop = true;
		board = new GameBoard();
		human = new HumanPlayer();
		computer = new ComputerPlayer();
		openColumns.clear();
		turn = 1;
		
		board.displayBoard();
		
		// Fills the openColumns variable with the number of possible play columns
		for (int i = 1; i <= Main.getColumnSize(); i++) {
			openColumns.add(i);
		}
		
		while (gameLoop) {
			if (playerTurn) {
				human.takeTurn();
				board.updateBoard("Human");
				checkGameState("Human");
				playerWin = board.playerWin;
				playerTurn = false;
			} else if (!playerTurn) {
				computer.move = board.getBestMove();
				computer.takeTurn();
				board.updateBoard("Computer");
				board.displayBoard();
				System.out.println("Computer played in column: "+ComputerPlayer.getComputerColumn());
				checkGameState("Computer");
				playerWin = board.playerWin;
				playerTurn = true;
			}
			
		}
	}
	
	/**
	 * Checks if the game is a tie or if the game has been won
	 * @param player - Human or Computer player
	 */
	private void checkGameState(String player){
		if (openColumns.isEmpty()) {
			System.out.println("The result of the game is a tie!\n\n");
			gameLoop = false;
		}
		board.checkConnection(player);     
	}	
}