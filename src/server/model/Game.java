package server.model;

import java.awt.Point;
import java.io.Serializable;

import server.controller.Dispatcher;


public class Game implements Serializable{

	private static final long serialVersionUID = 1L;
	// Atributes
	private Player playerHost;
	private Player playerClient;
	private Grid grid;
	private boolean isPlayerHostTurn;
	private boolean isGameFinished;
	private Dispatcher dispatcher;

	// Methods
	/**
	 * Contructor
	 */
	public Game(Player playerHost, Player playerClient, Dispatcher dispatcher) {
		//TODO rendre dispatcher static
		this.setDispatcher(dispatcher);
		this.playerHost = playerHost;
		this.playerClient = playerClient;
		this.grid = new Grid(16, 51);
		this.isGameFinished = false;

		/* Defines whose turn it is next */
		this.isPlayerHostTurn = false;

		// Modify player statuses
//		this.playerHost.setStatusPlayer(true);
//		this.playerClient.setStatusPlayer(true);
		this.playerHost.setMyTurn(false);
		this.playerClient.setMyTurn(true);

		System.out.println("Game has started");
	}

	/**
	 * TODO Can be merge with checkcoordinates
	 * TODO Prevent player to send query to server if it's not his turn(class view)
	 *
	 * Indicate if player has cliqued on a mine or not
	 *
	 * @return states :
	 * 		1 if player found a mine
	 * 		0 if player didn't find a mine
	 * 		-1 if played an already discovered mine
	 */
    public int move(Player player, Point p, boolean bombUsed){
		Player playerWhoShouldPlay = (isPlayerHostTurn() ? this.getPlayerHost() : this.getPlayerClient());
		if (player.getUsername().equals(playerWhoShouldPlay.getUsername())) {
			// Check coordinate to not overstep in the grid
			if( bombUsed ){
				if( p.x>13 ){
					p.x = 13;
				}else if( p.x<2 ) {
					p.x = 2;
				}
				if( p.y>13 ){
					p.y = 13;
				}else if( p.y<2 ){
					p.y = 2;
				}

				// pass over each cells which are overflown by bomb
				// There are 25 cells when bomb is used
				for (int i = p.x-2; i < p.x+3; i++) {
					for (int j = p.y-2; j < p.y+3; j++) {
						// Check state of cell
						this.checkCoordinates(i, j, isPlayerHost(player), bombUsed);
					}
				}
				/*
				 * We return 0 in this case,
				 * because the player can't play again after playing a bomb!
				 */
				return 0;
			}else{
				int foundSomething = this.checkCoordinates(p.x, p.y, isPlayerHost(player), bombUsed);
				if (foundSomething == 1) {
					/* Player won: found a mine */
					return 1;
				} else {
					/* Player lost: didn't find a mine or played an already discovered mine*/
					return foundSomething;
				}
			}
		}
		return -1; /* Wrong player played */
	}

    /**
     *
     * Check if coordinates send match mine
     *
     * @param x Coordinate in x axis
     * @param y Coordinate in x axis
     * @param discoverer player who play
     * @param bombUsed the bomb is used
     * @return mine is found or not
     */
	public int checkCoordinates(int x, int y, boolean discoverer, boolean bombUsed){
		//Player found something
		if (this.grid.getCells()[x][y].isDiscovered() == null) {
			// The player don't use bomb
			if (!bombUsed) {
				// Set the last mine only if bomb is not used because when bomb burst,
				// there are 25 mines played
				if (discoverer) { //player host last played
					this.playerHost.setLastPlayed(new Point(x, y));
				} else { //player client last played
					this.playerClient.setLastPlayed(new Point(x, y));
				}
			}
			// player found a mine
			if( this.grid.cellIsMine(x, y, discoverer) ) {
				this.getCurrentPlayer().incrementsScore();
				if (this.getCurrentPlayer().didUserWin()) {
					/* If true, the game is finished */
					this.setGameFinished(true);
				}
				return 1; //will be returned if player found a bomb
			} else {
				return 0; //will be returned if player found a number (0-8)
			}
		}
		return -1; //user clicked on an already discovered mine, he will play again
	}


    /**
     * Display the winner
     */
    public void finishGame() {
		System.out.println("Game finished, good game! "
				+ this.getWinner() + " won the game!");
    }


	/**
	 * Change the current player in the game. Set the player who should be playing
	 */
	public Player turnPlayer() {
		this.isPlayerHostTurn = !this.isPlayerHostTurn;

		//Save state of players two une game
		this.playerHost.setMyTurn(!this.playerHost.isMyTurn());
		this.playerClient.setMyTurn(!this.playerClient.isMyTurn());

		System.out.println("That's " + this.getCurrentPlayer().getUsername() + "'s turn!");
		return this.getCurrentPlayer();
	}


	/**
	 * Get cells which had played whihtout solution to avoid cheating
	 * Players can't see the solution, only server has it
	 * @return lite grid
	 */
	public Cell[][] getLightenedCells() {
		return applyLastCoordinates(this.grid.getLightenedCells());
	}

	/**
	 * Get cells of the game
	 * @return complete grid
	 */
	public Cell[][] getCells() {
		return applyLastCoordinates(this.grid.getCells());
	}


	/**
	 * Apply which cell has been played the last
	 * @param grid cells of game
	 * @return the grid with changes applied about the last cell cliqued
	 */
	public Cell[][] applyLastCoordinates(Cell[][] grid) {
	    //Get last played coordinates
		Point lastPlayedClient = this.playerClient.getLastPlayed();
		Point lastPlayedHost = this.playerHost.getLastPlayed();

		//Apply last played coordinates to the result grid
		if (lastPlayedClient != null) {
			grid[lastPlayedClient.x][lastPlayedClient.y].setLast(true);
		}

		if (lastPlayedHost != null) {
			grid[lastPlayedHost.x][lastPlayedHost.y].setLast(true);
		}

		return grid;
	}

	public boolean isPlayerHost(Player player) {
		return player == this.playerHost;
	}

	// Setters | Getters
	public Player getCurrentPlayer() {
		return this.isPlayerHostTurn ? playerHost : playerClient;
	}

	public Player getOpponentPlayer() {
		return this.isPlayerHostTurn ? playerClient : playerHost;
	}

	public Player getPlayerHost() {
		return playerHost;
	}

	public void setPlayerHost(Player playerHost) {
		this.playerHost = playerHost;
	}

	public Player getPlayerClient() {
		return playerClient;
	}

	public void setPlayerClient(Player playerClient) {
		this.playerClient = playerClient;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public boolean isPlayerHostTurn() {
		return isPlayerHostTurn;
	}

	public void setPlayerHostTurn(boolean isPlayerHostTurn) {
		this.isPlayerHostTurn = isPlayerHostTurn;
	}

	public boolean isGameFinished() {
		return isGameFinished;
	}

	public void setGameFinished(boolean isGameFinished) {
		this.isGameFinished = isGameFinished;
	}

	public Player getWinner() {
		return this.getPlayerClient().didUserWin() ?
				this.getPlayerClient() : this.getPlayerHost();
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

}
