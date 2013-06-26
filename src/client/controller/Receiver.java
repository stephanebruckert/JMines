package client.controller;

import java.awt.Point;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import server.controller.DispatcherInterface;
import server.model.Cell;
import client.model.CurrentGame;

/**
 * Main client class
 * Receives the game info from the server
 *
 * @author stephane
 *
 */
public class Receiver implements ReceiverInterface {

	/** Server instance */
	private DispatcherInterface proxy;

	/**
	 * Link to the model
	 * which manages current game instance
	 */
	private CurrentGame model;

	/**
	 * Constructor
	 * @param model the client model (MVC)
	 */
	public Receiver(CurrentGame model) {
		this.model = model;
	}

	/**
	 * @param serverIp the server IP
	 * @return > 0 if connection successful
	 * 			 0 if username already taken
	 * 			-1 if connection failed
	 */
	public int connect(String serverIp) {
		try {
			this.proxy = (DispatcherInterface) Naming.lookup("rmi://" + serverIp + "/JMines");
			int result = this.proxy.connection((ReceiverInterface)UnicastRemoteObject.exportObject(this,0), this.model.getMyUsername());
			if (result > 0) {
				System.out.println("Welcome! You're the " + result + "th player to connect!\n");
				return result;
			} else {
				System.out.println("Username already taken");
				UnicastRemoteObject.unexportObject(this, false);
				return 0;
			}
		} catch (Exception e) {
			System.err.println("Can't connect to server: " + e.getMessage());
			return -1;
		}
	}

	/**
	 * Checks if the user is indeed online
	 * @return true if ready, false else
	 * TODO check if it really works
	 */
	@Override
	public boolean isReady() {
		return true;
	}

	/**************************************************
	 * The following methods are called from the server
	 * to update the view
	 **************************************************/

	/* (non-Javadoc)
	 * @see client.controller.ReceiverInterface#sendConnectedUsers(java.util.List)
	 */
	@Override
	public void sendConnectedUsers(List<String> connectedUsers)
			throws RemoteException {
		this.model.setConnectedUsers(connectedUsers);
	}

	/* (non-Javadoc)
	 * TODO ˆ fusionner avec startGame
	 * @see client.controller.ReceiverInterface#sendNewGrid(server.model.Cell[][])
	 */
	@Override
	public void sendNewGrid(Cell[][] cells)
			throws RemoteException {
		this.model.setCells(cells);
	}

	/* (non-Javadoc)
	 * @see client.controller.ReceiverInterface#sendNewResults(int, int)
	 */
	@Override
	public void sendNewResults(int myMinesFound, int opponentMinesFound)
			throws RemoteException {
		this.model.setMyMines(myMinesFound);
		this.model.setOpponentMines(opponentMinesFound);

		this.model.setBombRest(51-myMinesFound-opponentMinesFound);

		if( myMinesFound < opponentMinesFound ){
			this.model.setCanUseBomb(true);
		}else{
			this.model.setCanUseBomb(false);
		}
	}

	/* (non-Javadoc)
	 * @see client.controller.ReceiverInterface#opponentCanPlay()
	 */
	@Override
	public void opponentCanPlay(boolean isMyTurn) throws RemoteException {
		this.getModel().setMyTurn(isMyTurn);
	}


	/************************************************
	 * The following methods are called from the view
	 ************************************************/
	/**
	 * @param p the coordinates clicked by the client
	 * @param bombUsed indicates whether the mega bomb has been used
	 * @throws RemoteException
	 */
	public void confirmClickedCoordinates(Point p, boolean bombUsed) {
		int res;
		try {
			res = this.proxy.confirmClickedCoordinates(this.model.getMyUsername(), p, bombUsed);
			
			/* 1=win, play again
			 * 0=lost, next player
			 * -1=wrong, play again
			 */
			if (res == 0) {
				this.getModel().setMyTurn(false);
			}
		} catch (RemoteException e) {
			System.out.println("Server is offline");
		}

	}

	/**
	 * TODO peut-tre fusionner isUserReady avec startGameWithOpponent histoire
	 * de supprimer une requte rŽseau
	 * CrŽation de l'instance de jeu pour celui qui lance la partie
	 *
	 * @param opponent
	 * @throws RemoteException
	 */
	public void startGameWithOpponent(String opponent) throws RemoteException {
		this.proxy.playWith(this.model.getMyUsername(), opponent);
		this.model.setOpponentUsername(opponent);
		this.model.setMyMines(0);
		this.model.setHost(true);
		this.model.setMyTurn(false);
	}

	/**
	 * Creates a game instance if the client is the guest
	 * @param opponent the player username
	 */
	@Override
	public void gameStarts(String opponent) throws RemoteException {
		this.model.setOpponentUsername(opponent);
		this.model.setMyMines(0);
		this.model.setHost(false);
		this.model.setMyTurn(true);
	}

	/**
	 * Checks if an opponent is still online
	 * @param potentialOpponent the username of the potential opponent
	 * @return true if online; false otherwise
	 * @throws RemoteException
	 */
	public boolean isUserReady(String potentialOpponent) throws RemoteException {
		return this.proxy.isUserReady(potentialOpponent);
	}

	/**
	 * This calls the server to retrieve the connected users
	 * @throws RemoteException
	 */
	public void updateConnectedUsers() throws RemoteException {
		this.proxy.updateConnectedUsers();
	}

	/******************************
	 * Get information from model
	 ******************************/
	/**
	 * Directly gets the username from the model
	 * @return
	 */
	public String getMyUsername() {
		return model.getMyUsername();
	}

	/**
	 * Sets the username of the client
	 * @param username the new username of the client
	 */
	public void setMyUsername(String username) {
		this.model.setMyUsername(username);
	}

	/**
	 * Easy way to get the model
	 * @return the model instance of the controller
	 */
	public CurrentGame getModel() {
		return model;
	}


	/**
	 * Sets the model to the controller
	 * Method used by the RunClient in order to initialize the MVC
	 * @param model the model instance
	 */
	public void setModel(CurrentGame model) {
		this.model = model;
	}

	@Override
	public void retrieveMegabombNotification() throws RemoteException {
		// TODO Auto-generated method stub
		this.model.getOpponent().setMyBombIsUsed(true);
	}

}