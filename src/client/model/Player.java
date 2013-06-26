package client.model;

public class Player  {
	
	/**
	 * The player username
	 */
	private String username;
	
	/**
	 * Amount of mine the client found in his current game
	 */
	private int minesFound;
	
	/**
	 * Indicates whether it's client turn or not
	 */
	private boolean isItMe;
	
	/**
	 * Indicates whether the client used his mega bomb
	 */
	private boolean myBombIsUsed;
	
	/**
	 * Indicates whether the client can use his mega bomb
	 */
	private boolean canUseBomb;

	/**
	 * Contructor for a player
	 * @param username his username
	 * @param isItMe true if the player is a guest,
	 * 				 false if the player started the game
	 */
	public Player(String username, boolean isItMe) {
		this.username = username;
		this.isItMe = isItMe;
		this.minesFound = 0;
		this.myBombIsUsed = false;
		this.canUseBomb = false;
	}

	/**
	 * Sets if the mega bomb has been used
	 * @param myBombIsUsed true if the mega bomb has been used
	 * 					   false else
	 */
	public void setMyBombIsUsed(boolean myBombIsUsed) {
		this.myBombIsUsed = myBombIsUsed;
	}

	/**
	 * Gets whether the mega bomb has been used
	 * @return the mega bomb status
	 */
	public boolean getMyBombIsUsed() {
		return this.myBombIsUsed;
	}

	/**
	 * Gets whether the client can use his mega bomb
	 * @return true if client can use his mega bomb
	 * 		   false otherwise
	 */
	public boolean getCanUseBomb() {
		return canUseBomb;
	}

	/**
	 * Sets whether the client can use his bomb
	 * @param canUseBomb
	 */
	public void setCanUseBomb(boolean canUseBomb) {
		this.canUseBomb = canUseBomb;
	}

	/**
	 * Gets the client username
	 * @return the client username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the client username
	 * @param username the client username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the amount of mines the client found
	 * @return the amount of mines the client found
	 */
	public int getMinesFound() {
		return minesFound;
	}

	/**
	 * Sets the new amount of mines that the user found
	 * @param minesFound the amount of mines found
	 * @return true if game is over, false otherwise
	 */
	public boolean setMinesFound(int minesFound) {
		this.minesFound = minesFound;
		return this.minesFound >= 26;
	}

	/**
	 * Indicates if it's the client's turn
	 * @return true if it's the client's turn, false otherwise
	 */
	public boolean isItMe() {
		return isItMe;
	}

	/**
	 * Sets whether it's the client's turn
	 * @param isItMe true if it's the client's turn, false otherwise
	 */
	public void setItMe(boolean isItMe) {
		this.isItMe = isItMe;
	}

}
