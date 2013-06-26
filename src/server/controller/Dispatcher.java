package server.controller;

import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import server.model.Cell;
import server.model.Game;
import server.model.Player;
import client.controller.ReceiverInterface;

/**
 * Main server class
 * Dispatch the game info to each player
 *
 * @author stephane
 *
 */
public class Dispatcher extends UnicastRemoteObject implements DispatcherInterface {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** The connected players */
	private List<Player> waitingList;

	/** The current games being played */
	private Map<Player, Game> gameList;

	/**
	 * Constructor that initiates the server
	 * @throws RemoteException
	 */
	public Dispatcher() throws RemoteException {
		super();
		this.waitingList = new ArrayList<Player>();
		this.gameList = new HashMap<Player, Game>();
		try {
			LocateRegistry.createRegistry(1099);
			Naming.rebind("JMines", this);
			System.out.println("Serveur pret");
		} catch (RemoteException e) {
			System.err.println("Remote Exception " + e.getMessage());
		} catch (MalformedURLException e) {
			System.err.println("Malformed " + e.getMessage());
		}
	}

	/************************************************
	 * Game management
	 ************************************************/
	/* (non-Javadoc)
	 * @see server.controller.DispatcherInterface#playWith(java.lang.String, java.lang.String)
	 */
	@Override
	public void playWith(String clientName, String opponentName) throws RemoteException {
		Player playerClient = getPlayerFromName(clientName);
		Player playerOpponent = getPlayerFromName(opponentName);

		Game game = new Game(playerClient, playerOpponent, this);
		playerOpponent.getRMIUser().gameStarts(clientName);
		sendResultToBothPlayers(game);

		//Adds users to gamelist
		this.gameList.put(playerClient, game);
		this.gameList.put(playerOpponent, game);
	}

	/* (non-Javadoc)
	 * @see server.controller.DispatcherInterface#confirmClickedCoordinates(java.lang.String, java.awt.Point, boolean)
	 */
	@Override
	public int confirmClickedCoordinates(String username, Point p, boolean bombUsed) throws RemoteException {
		Player player = this.getPlayerFromName(username);
		Game game = this.gameList.get(player);

		//TODO game est ˆ null dans le cas o game le mauvais joueur commence
		if (game != null) {

			int result = game.move(player, p, bombUsed);
			/*
			 * Result states :
			 * 1 if player found a mine
			 * 0 if player didn't find a mine
			 * -1 if played an already discovered mine
			 */
			if (result >= 0) {
				/* Notify the player's opponent that he used the megabomb */
				if (bombUsed) {
					ReceiverInterface opponent = game.getOpponentPlayer().getRMIUser();
					opponent.retrieveMegabombNotification();
				}

				/*
				 * Results equals 0 or 1, so the player found something.
				 * An update is needed.
				 */
				sendResultToBothPlayers(game);
				if (result == 0) {
					/*
					 * 3 cases for which we turn player :
					 * Player lost, didn't find a mine or played a bomb
					 * We can now alert the "new current" player
					 */
					game.turnPlayer().getRMIUser().opponentCanPlay(true);
					game.getOpponentPlayer().getRMIUser().opponentCanPlay(false);
				}
			}
			return result;
		} else {
			return -1; //Game not found
		}
	}

	/************************************************
	 * Notification methods (sends info to the view)
	 ************************************************/
	/**
	 * Sends results of a move to both players of a game
	 * @param game
	 * @throws RemoteException
	 */
	public void sendResultToBothPlayers(Game game) throws RemoteException {
		int myMinesFound = game.getCurrentPlayer().getMinesFound();
		int opponentMinesFound = game.getOpponentPlayer().getMinesFound();

		/* Checks if game is finished */
		Cell[][] newGrid;
		if (myMinesFound >= 26 || opponentMinesFound >= 26) {
			game.setGameFinished(true);
			this.gameList.remove(game.getPlayerHost());
			this.gameList.remove(game.getPlayerClient());
			newGrid = game.getCells();
		} else {
			newGrid = game.getLightenedCells();
		}

		/* Update grid and scores of players */
		game.getCurrentPlayer().getRMIUser().sendNewGrid(newGrid);
		game.getCurrentPlayer().getRMIUser().sendNewResults(myMinesFound, opponentMinesFound);

		game.getOpponentPlayer().getRMIUser().sendNewGrid(newGrid);
		game.getOpponentPlayer().getRMIUser().sendNewResults(opponentMinesFound, myMinesFound);
	}

	/**
	 * Sends the connected players list to all the players
	 * @throws RemoteException
	 */
	public void sendConnectedPlayersToAllPlayers() throws RemoteException {
		List<ReceiverInterface> connectedPlayersRMI = new ArrayList<ReceiverInterface>();
		List<String> connectedPlayers = new ArrayList<String>();

		for (Player player : this.waitingList) {
			if (player.getRMIUser().isReady()) {
				//Saves all RMI player instances
				connectedPlayersRMI.add(player.getRMIUser());
				connectedPlayers.add(player.getUsername());
			}
		}
		//Sends player list to each RMI instance
		for (ReceiverInterface client : connectedPlayersRMI) {
			client.sendConnectedUsers(connectedPlayers);
		}
	}

	/************************************************
	 * Player management
	 ************************************************/

	/* (non-Javadoc)
	 * @see server.controller.DispatcherInterface#connection(client.controller.ReceiverInterface, java.lang.String)
	 */
	@Override
	public int connection(ReceiverInterface client, String username) throws RemoteException {
		if (getPlayerFromName(username) == null) {
			System.out.println(username + " joined.");
			Player playerClient = new Player(username);
			playerClient.setRMIUser(client);
			this.waitingList.add(playerClient);
			sendConnectedPlayersToAllPlayers();
			return waitingList.size();
		} else {
			System.out.println(username + " tried to join but the username is already taken");
			return 0;
		}
	}

	/**
	 * Simple way for the server to get the player instance from his username
	 * @param username the username of the player
	 * @return the player server instance
	 */
	public Player getPlayerFromName(String username) {
		for (Player player : this.waitingList) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		System.out.println("Ne trouve pas le player !");
		return null;
	}

	/* (non-Javadoc)
	 * @see server.controller.DispatcherInterface#updateConnectedUsers()
	 */
	@Override
	public void updateConnectedUsers() throws RemoteException {
		this.sendConnectedPlayersToAllPlayers();
	}

	/* (non-Javadoc)
	 * @see server.controller.DispatcherInterface#isUserReady(java.lang.String)
	 */
	@Override
	public boolean isUserReady(String potentialOpponent) throws RemoteException {
		for (Entry<Player, Game> entry : this.gameList.entrySet()) {
			if (entry.getKey().getUsername().equals(potentialOpponent)) {
				return false;
			}
		}
		return true;
	}

}
