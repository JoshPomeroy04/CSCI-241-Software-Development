/**
 * Class that controls the leaderboard's calculations and loading.
 * Creates leaderboard.log if it does not already exist, updates and reads from this log.
 * 
 * @author Josh Pomeroy
 */
package edu.wm.cs.cs301.connectn.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Leaderboard{
	
	private String smallScore, mediumScore, largeScore;
	
	private String path, log;
	
	private final ConnectNModel model;
	
	public Leaderboard(ConnectNModel model) {
		String fileSeparator = System.getProperty("file.separator");
		this.model = model;
		this.path = System.getProperty("user.home") + fileSeparator + "ConnectN";
		this.log = fileSeparator + "leaderboard.log";
		
		try {
			File file = new File(path);
			file.mkdir();
			file = new File(path + log);
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		readLeaderboard();
	}
	
	/**
	 * Reads the leaderboard file and sets each score value accordingly 
	 */
	private void readLeaderboard() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + log));
			try {
				this.smallScore = br.readLine().trim();
			} catch (NullPointerException e) {
				this.smallScore = "";
			}
			
			try {
				this.mediumScore = br.readLine().trim();
			} catch (NullPointerException e) {
				this.mediumScore = "";
			}
			
			try {
				this.largeScore = br.readLine().trim();
			} catch (NullPointerException e) {
				this.largeScore = "";
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes to the leaderboard to update each score value
	 */
	private void writeLeaderboard() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path + log));
			bw.write(smallScore);
			bw.write(System.lineSeparator());
			bw.write(mediumScore);
			bw.write(System.lineSeparator());
			bw.write(largeScore);
			bw.write(System.lineSeparator());
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks to see if the score that the current player ended with is better than the stored score.
	 * @param size - Board size to check for
	 * @return - true if the current player's score is better, false if not
	 */
	public boolean checkBetterScore(int size) {
		int startParseAt;
		int storedScore;
		String[] arrayScore;
		
		switch (size) {
			case 5:
				if (smallScore.length() == 0) {
					return true;
				}
				startParseAt = smallScore.length()-1;
				arrayScore = smallScore.split("");
				break;
			case 7:
				if (mediumScore.length() == 0) {
					return true;
				}
				startParseAt = mediumScore.length()-1;
				arrayScore = mediumScore.split("");
				break;
			case 9:
				if (largeScore.length() == 0) {
					return true;
				}
				startParseAt = largeScore.length()-1;
				arrayScore = largeScore.split("");
				break;
			default:
				return false;
		}
		
		if (arrayScore[startParseAt-1].equals(" ")) {
			storedScore = Integer.valueOf(arrayScore[startParseAt].trim());
		} else {
			storedScore = Integer.valueOf(arrayScore[startParseAt-1]+arrayScore[startParseAt].trim());
		}
		
		if (model.getTurn()-1 >= storedScore) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setSmallScore(String text) {
		smallScore = text;
		writeLeaderboard();
	}
	
	public void setMediumScore(String text) {
		mediumScore = text;
		writeLeaderboard();
	}
	
	public void setLargeScore(String text) {
		largeScore = text;
		writeLeaderboard();
	}
	
	public String getSmallScore() {
		return smallScore;
	}
	
	public String getMediumScore() {
		return mediumScore;
	}
	
	public String getLargeScore() {
		return largeScore;
	}
}