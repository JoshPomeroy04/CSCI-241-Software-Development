/**
 * Responsible for controlling the leaderboard. 
 * Allows the main class to control when the leaderboard is updated.
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

public class Leaderboard{
	private String RUNNINGPATH = getClass().getClassLoader().getResource(".").getPath();    // Attempting to get the correct path without hard coding
	private String FOLDERPATH = RUNNINGPATH.substring(0, RUNNINGPATH.length()-4)+"resources/";
	private File LEADERBOARD = new File(FOLDERPATH+"leaderboard.txt");
	private Scanner nameInput = new Scanner(System.in);
	private String playerName;
	private static Scanner leaderboardScanner = null;
	private static ArrayList<String> leaderboardArray = new ArrayList<String>();
	private FileWriter lWriter;
	private int start;
	private ArrayList<String> edittingList;
	public int setMode;
	
	public Leaderboard() {
		loadLeaderboard();
		setMode = 0;
	}
	
	/**
	 * Gets the current player's name to write to the leaderboard
	 */
	public void getPlayerName() {
		boolean notNamed = true;
		
		while(notNamed) {
			System.out.println("Enter a username to save your score. Hit enter to remain a guest: ");
			playerName = nameInput.nextLine();
			if (playerName.equals("")) {
				playerName = "Guest";
				notNamed = false;
			} else if (playerName.length() > 14){
				System.out.println("Your username is too long, it can be a max of 14 characters");	
			} else {
				notNamed = false;
			}
		}
	}
	
	/**
	 * Opens the leaderboard file writer
	 */
	private void openWriter() {
		try {
			lWriter = new FileWriter(LEADERBOARD, false);
		} catch(IOException e) {
	        System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Closes the leaderboard file writer
	 */
	private void closeWriter() {
		try {
			lWriter.flush();
			lWriter.close();
		} catch(IOException e) {
	        System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Writes a string to the file leaderboard
	 * @param contents - the string to be written to leaderboard
	 */
	public void writeToLeaderboard(String contents) {
		try {
			lWriter.append(contents+"\n");
			lWriter.flush();
		} catch (IOException e) {
            System.out.println(e.getMessage());
        }
	}
	
	/**
	 * Updates the leaderboard if needed with the current player's name and score
	 */
	public void updateLeaderboard() {
		getPlayerName();
		
		String[] edittingSplit = null;
		edittingList = null;
		start = 0;
		// Sets the value of where to start reading a line from depending on game size
		switch (setMode) {
			case 1:
				start = 2;
				break;
			case 2:
				start = 22;
				break;
			case 3:
				start = 42;
		}
		
		String build = "";
		int bestRow = 0;
		String inRow4 = "";
		String inRow3 = "";
		
		// See if score is good enough to be recorded. Loop starts by comparing with the lowest saved score
		for (int i = 5; i > 2; i--) {
			// Set the row of the leaderboard to compare with
			build = "";
			edittingSplit = leaderboardArray.get(i).split("");
			edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
			// Set the game size to compare with
			for (int x = start+16; x < start+18; x++) {
				if (!edittingList.get(x).equals(" ")) {
					// Get the score from the current row and correct game size
					build += edittingList.get(x);
				} else {
					// There is no score saved
					build = "";
					break;
				}
			}
			// If there is no score saved then set the row to write to as the current row i
			if (build.equals("")) {
				bestRow = i;
			} else if (Integer.parseInt(build) > ConnectNGame.turn-1) {
				// If the saved score is greater than the current player score
				switch (i) {
					// If the row is 4
					case 4:
						// Record the current row 4
						for (int x = start; x < start+18; x++) {
							inRow4 += edittingList.get(x);
						}
						break;
					// if the row is 3
					case 3:
						// Record the current row 3
						for (int x = start; x < start+18; x++) {
							inRow3 += edittingList.get(x);
						}
						break;			
				}
				// Set the row to write to as the current row i
				bestRow = i;
			}
		}
		
		// Check to see what the best row to write to is
		switch (bestRow) {
			// If it's row 5
			case 5:
				// Select row 5
				edittingSplit = leaderboardArray.get(5).split("");
				edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
				// Update row 5 with the current player's name and score
				updateLine(bestRow);
				break;
			// If it's row 4
			case 4:
				// Select row 4
				edittingSplit = leaderboardArray.get(4).split("");
				edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
				// Update row 4 with the current player's name and score
				updateLine(bestRow);
				// If there used to be another user and score in row 4
				if (inRow4 != "") {
					playerName = "";
					// Select row 5
					edittingSplit = leaderboardArray.get(5).split("");
					edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
					// Get the name of the user previously in row 4
					for (int i = 0; i < 18; i++) {
						char charAtI = inRow4.charAt(i);
						if ((charAtI != ':')) {
							playerName += Character.toString(charAtI);
						} else {
							break;
						}
					}
					// Get the score of the user previously in row 4
					build = "";
					for (int i = 16; i < 18; i++) {
						char charAtI = inRow4.charAt(i);
						build += Character.toString(charAtI);
					}
					ConnectNGame.turn = Integer.parseInt(build)+1;
					// Move the user and score from row 4 to row 5
					updateLine(5);
				}
				break;
			// If it's row 3
			case 3:
				// Select row 3
				edittingSplit = leaderboardArray.get(3).split("");
				edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
				// Update row 3 with the current player's name and score
				updateLine(bestRow);
				// If there used to be another user and score in row 4
				if (inRow4 != "") {
					playerName = "";
					edittingSplit = leaderboardArray.get(5).split("");
					edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
					for (int i = 0; i < 18; i++) {
						char charAtI = inRow4.charAt(i);
						if ((charAtI != ':')) {
							playerName += Character.toString(charAtI);
						} else {
							break;
						}
					}
					build = "";
					for (int i = 16; i < 18; i++) {
						char charAtI = inRow4.charAt(i);
						build += Character.toString(charAtI);
					}
								
					ConnectNGame.turn = Integer.parseInt(build)+1;
					updateLine(5);
				}
				// If there used to be another user and score in row 3
				if (inRow3 != "") {
					playerName = "";
					edittingSplit = leaderboardArray.get(4).split("");
					edittingList = new ArrayList<String>(Arrays.asList(edittingSplit));
					for (int i = 0; i < 18; i++) {
						char charAtI = inRow3.charAt(i);
						if ((charAtI != ':')) {
							playerName += Character.toString(charAtI);
						} else {
							break;
						}
					}
					build = "";
					for (int i = 16; i < 18; i++) {
						char charAtI = inRow3.charAt(i);
						build += Character.toString(charAtI);
					}						
					ConnectNGame.turn = Integer.parseInt(build)+1;
					updateLine(4);
				}
				break;
			// If the current player's score is not good enough
			default:
				System.out.println("Sorry, your score is not a top 3 score :(");	
		}
	}
	
	/**
	 * Updates a single box of the leaderboard
	 * Row is specified and the column is implied based on the game size that the player played on
	 * @param line - Row of leaderboard to be updated
	 */
	private void updateLine(int line) {
		// Records the player's name to the right location
		for (int i = 0; i < playerName.length();i++) {
			char charAtI = playerName.charAt(i);
			edittingList.set(i+start, Character.toString(charAtI));
		}
				
		// Clear any thing left over from previous usernames 
		edittingList.set(playerName.length()+start, ":");
		for (int i = playerName.length()+start+1; i < start+16; i++) {
			edittingList.set(i, " ");
		}

		
		String turnString;
		// If the player beat the computer in under 10 moves
		if (ConnectNGame.turn-1 < 10) {
			// Add a 0 to the front of their score
			turnString = "0"+String.valueOf(ConnectNGame.turn-1);
		} else {
			turnString = String.valueOf(ConnectNGame.turn-1);
		}
		// Records the player's score to the right location
		for (int i = start+16; i < start+18; i++) {
			char charAtI = turnString.charAt(i-start-16);
			edittingList.set(i, Character.toString(charAtI));
		}
				
		// Writes the updated row to the leaderboard file
		openWriter();
		leaderboardArray.set(line, String.join("", edittingList)); 
		for (int i = 0; i < leaderboardArray.size(); i++) {
			writeToLeaderboard(leaderboardArray.get(i));
		}
		closeWriter();
	}
	
	/**
	 * Prints out the leaderboard
	 */
	public void displayLeaderboard() {	
		for(int i = 0; i < leaderboardArray.size(); i++) {
			System.out.println(leaderboardArray.get(i));
		}
	}
	
	/**
	 * Loads the contents of the leaderboard file into an ArrayList
	 */
	public void loadLeaderboard() {
		try {
			leaderboardScanner = new Scanner(LEADERBOARD);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		while (leaderboardScanner.hasNextLine()) {
			leaderboardArray.add(leaderboardScanner.nextLine());
		}
		leaderboardScanner.close();
	}
}