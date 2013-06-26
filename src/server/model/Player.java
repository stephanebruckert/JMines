package server.model;

import java.awt.Point;

import client.controller.ReceiverInterface;

public class Player {

	// Attribute
	private String username;
	private int minesFound;
	private boolean myTurn;
	private ReceiverInterface RMIUser;
	private Point lastPlayed; // The last cell played

	public Player(String username) {
		this.username = username;
		this.minesFound = 0;
	}

	public void incrementsScore(){
		this.minesFound++;
	}

	/**
	 * @return false if the game continues,
	 * true if the score is at 24
	 */
	public boolean didUserWin() {
		return (this.minesFound == 26) ? true : false;
	}

	// Setters | Getters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getMinesFound() {
		return minesFound;
	}

	public void setMinesFound(int minesFound) {
		this.minesFound = minesFound;
	}

	public void incrementMinesFound(int minesFound) {
		this.minesFound += minesFound;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public ReceiverInterface getRMIUser() {
		return RMIUser;
	}

	public void setRMIUser(ReceiverInterface rMIUser) {
		RMIUser = rMIUser;
	}

	public Point getLastPlayed() {
		return lastPlayed;
	}

	public void setLastPlayed(Point lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	@Override
	public String toString() {
		return "Player " + this.username
				+ (this.getRMIUser() != null ? "RMI ok "
							+ this.getRMIUser() : "RMI null");
	}

}
