package client.model;

import java.util.List;
import java.util.Observable;

import server.model.Cell;

/**
 * Represents the current game of the client
 * The class automatically notifies the view from its changes
 *
 * @author stephane
 */
public class CurrentGame extends Observable {

	/**
	 * The player current instance
	 */
	private Player me;

	/**
	 * The opponent current instance
	 */
	private Player opponent;

	/**
	 * Indicates whether it's my turn
	 */
	private boolean myTurn;

	/**
	 * The current grid
	 */
	private Cell[][] cells;

	/**
	 * The list of connected users
	 */
	private List<String> connectedUsers;

	/**
	 * Indicates whether the client is the host of the current game
	 */
	private boolean isHost;

	/**
	 * Indicates number of bomb which rest in game
	 */
	private int bombRest;


	/**
	 * Constructor
	 */
	public CurrentGame() {
		this.me = new Player("", true);
		this.opponent = new Player("", false);
		this.bombRest = 51;
	}

	/**
	 * @param canUseBomb
	 */
	public void setCanUseBomb(boolean canUseBomb) {
		if(!this.me.getMyBombIsUsed()){
			this.me.setCanUseBomb(canUseBomb);
		}else {
			this.me.setCanUseBomb(false);
		}
		this.doNotify(this.me);
	}

	/**
	 * @param bombIsUsed
	 */
	public void setBombIsUsed(boolean bombIsUsed) {
		this.me.setMyBombIsUsed(bombIsUsed);
		this.me.setCanUseBomb(false);
		this.doNotify(this.me);
	}

	/**
	 * @return
	 */
	public Player getMe() {
		return me;
	}

	/**
	 * @return
	 */
	public Player getOpponent() {
		return opponent;
	}

	/**
	 * @return
	 */
	public int getMyMines() {
		return me.getMinesFound();
	}

	/**
	 * @param myMines
	 */
	public void setMyMines(int myMines) {
		this.me.setMinesFound(myMines);
		this.doNotify(this.me);
	}

	/**
	 * @return
	 */
	public int getBombRest() {
		return this.bombRest;
	}

	/**
	 * @param myMines
	 */
	public void setBombRest(int mines) {
		this.bombRest = mines;
	}

	/**
	 * @return
	 */
	public int getOpponentMines() {
		return this.opponent.getMinesFound();
	}

	/**
	 * @param opponentMines
	 */
	public void setOpponentMines(int opponentMines) {
		this.opponent.setMinesFound(opponentMines);
		this.doNotify(this.opponent);
	}

	/**
	 * @return
	 */
	public boolean isMyTurn() {
		return myTurn;
	}

	/**
	 * @param myTurn
	 */
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
		this.doNotify(this.myTurn);
	}

	/**
	 * @return
	 */
	public boolean isHost() {
		return isHost;
	}

	/**
	 * @param isHost
	 */
	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

	/**
	 * @return
	 */
	public Cell[][] getCells() {
		return cells;
	}

	/**
	 * @param cells
	 */
	public void setCells(Cell[][] cells) {
		this.cells = cells;
		this.doNotify(this.cells);
	}

	/**
	 * @return
	 */
	public String getMyUsername() {
		return me.getUsername();
	}

	/**
	 * @param username
	 */
	public void setMyUsername(String username) {
		this.me.setUsername(username);
		this.doNotify(username);
	}

	/**
	 * @return
	 */
	public String getOpponentUsername() {
		return this.me.getUsername();
	}

	/**
	 * @param opponentUsername
	 */
	public void setOpponentUsername(String opponentUsername) {
		this.opponent.setUsername(opponentUsername);
		this.doNotify(opponentUsername);
	}

	/**
	 * @return
	 */
	public List<String> getConnectedUsers() {
		return connectedUsers;
	}

	/**
	 * @param connectedUsers
	 */
	public void setConnectedUsers(List<String> connectedUsers) {
		this.connectedUsers = connectedUsers;
		this.doNotify(this.connectedUsers);
	}

	/**
	 * Updates the connected users in the view
	 */
	public void updateConnectedUsers() {
		this.doNotify(this.connectedUsers);
	}

	/**
	 * Notifies the view from the changes
	 * @param obj the object that changed
	 */
	public void doNotify(Object obj) {
		setChanged();
		notifyObservers(obj);
	}
}
