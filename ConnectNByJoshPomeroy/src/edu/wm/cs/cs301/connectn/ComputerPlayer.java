/**
 * Contains information regarding the computer player. Gets the computer's
 * move.
 * 
 *@author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn;
import java.util.Random;


public class ComputerPlayer implements Player {
	private static int computerColumn;
	private Random rand = new Random();
	public int move;
	
	public ComputerPlayer() {
		move = -1;
	}

	@Override
	public void takeTurn() {
		if (ConnectNGame.openColumns.size() > 1) {
			if (move == -1) {
				computerColumn = ConnectNGame.openColumns.get(rand.nextInt(ConnectNGame.openColumns.size()));
			} else {
				computerColumn = move+1;
			}
		} else {
			computerColumn = ConnectNGame.openColumns.get(0);
		}
		
		while (!ConnectNGame.openColumns.contains(computerColumn)) {
			computerColumn = ConnectNGame.openColumns.get(rand.nextInt(ConnectNGame.openColumns.size()));
		}
	}
	
	/**
	 * @return - The column the computer plays in
	 */
	public static int getComputerColumn() {
		return computerColumn;
	}
}