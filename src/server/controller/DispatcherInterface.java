package server.controller;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.controller.ReceiverInterface;

/**
 * Server interface between the server controller and the clients
 *
 * @author stephane
 */
public interface DispatcherInterface extends Remote {
	/**
	 * Log the user in the server
	 * @param client the network instance of the client
	 * @param username the username the player has been given
	 * @return 0 if username already used, > 0 if login successful
	 * @throws RemoteException
	 */
	public int connection(ReceiverInterface client, String username) throws RemoteException;

	/**
	 * Creates a new game between two players
	 * @param clientName the username of the client
	 * @param opponentName the username of the opponent
	 * @throws RemoteException
	 */
	public void playWith(String clientName, String opponentName) throws RemoteException;

	/**
	 * Checks if a user is ready
	 * @param potentialOpponent the username of the player we want to play with
	 * @return true if the user is ready, false else
	 * @throws RemoteException
	 */
	public boolean isUserReady(String potentialOpponent) throws RemoteException;

	/**
	 * Make a move
	 * @param the username of the player who just played
	 * @param p the coordinates of the new move
	 * @param bombUsed indicates whether the player used the big bomb
	 * @return 	1 if player found a mine and play again
	 *			0 if player didn't find a mine and then next player
	 *		   -1 if not his turn / played an already discovered mine
	 * @throws RemoteException
	 */
	public int confirmClickedCoordinates(String username, Point p, boolean bombUsed) throws RemoteException;

	/**
	 * Method being called by the client when he asks for the connected users
	 * @throws RemoteException
	 */
	public void updateConnectedUsers() throws RemoteException;

}
