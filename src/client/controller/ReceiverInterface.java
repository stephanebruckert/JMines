package client.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.model.Cell;


/**
 * Client interface between the client controller and the server
 *
 * @author stephane
 */
public interface ReceiverInterface extends Remote {
	/**
	 * Checks if the user is connected
	 * @return true if user ready, false otherwise
	 * @throws RemoteException
	 */
	public boolean isReady() throws RemoteException;

	/**
	 * Sends the new grid to a player
	 * @param cells the new grid
	 * @throws RemoteException
	 */
	public void sendNewGrid(Cell[][] cells) throws RemoteException;

	/**
	 * Sends the new results to a player
	 * @param myMinesFound the amount of mines the client has found
	 * @param opponentMinesFound the amount of mines the client's opponent has found
	 * @throws RemoteException
	 */
	public void sendNewResults(int myMinesFound, int opponentMinesFound) throws RemoteException;

	/**
	 * Updates the player from new users
	 * @param connectedPlayers the list of the connected users' usernames
	 * @throws RemoteException
	 */
	public void sendConnectedUsers(List<String> connectedPlayers) throws RemoteException;

	/**
	 * Method that the server calls when the player has been asked a game
	 * @param opponent the opponent username
	 * @throws RemoteException
	 */
	public void gameStarts(String opponent) throws RemoteException;

	/**
	 * Method that the server calls when it's a new player's turn
	 * @throws RemoteException
	 */
	public void opponentCanPlay(boolean isMyTurn) throws RemoteException;

	/**
	 * Method that alerts the client that his opponent used the bomb
	 * @throws RemoteException
	 */
	public void retrieveMegabombNotification() throws RemoteException;

}
