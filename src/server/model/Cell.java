package server.model;

import java.io.Serializable;

/**
 * Permet de gérer les cellules
 */
public class Cell implements Serializable{

	private static final long serialVersionUID = 1L;

	/* Number adjacent mines (0-8) */
	private int nbCellsAdjacent;

	/* true : discovered by host | false : discovered by client | not discovered */
	private Boolean isDiscovered;

	/* false = not mine | true = mine */
	private boolean isMine;

	/* The last mine clicked in the game */
	private boolean isLast; 

	/**
	 * Default constructor
	 */
	public Cell() {
		this.nbCellsAdjacent = 0;
		this.isDiscovered = null;
		this.isMine = false;
	}

	/**
	 * Constructor for lightened cells
	 * @param nbCellsAdjacent Number of adjacent mines
	 * @param statut Boolean specify if is discovered by one player
	 * @param isMine Specify if cell contain mine
	 * @param isLast Specify id it's the last cliqued in game
	 */
	public Cell(int nbCellsAdjacent, boolean statut, boolean isMine, boolean isLast) {
		this.nbCellsAdjacent = nbCellsAdjacent;
		this.isDiscovered = statut;
		this.isMine = isMine;
		this.isLast = isLast;
	}

	// Getters | Setters
	public int getNbCellsAdjacent() {
		return nbCellsAdjacent;
	}

	public void setNbCellsAdjacent(int nbCellsAdjacent) {
		this.nbCellsAdjacent = nbCellsAdjacent;
	}

	public Boolean isDiscovered() {
		return isDiscovered;
	}

	public void setDiscovered(boolean discoveredByHost) {
		this.isDiscovered = discoveredByHost;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public String toString(boolean beDiscovered) {
		if (this.isDiscovered() != null || beDiscovered) {
			if (this.isMine()) {
				return "x";
			} else {
				return String.valueOf(this.getNbCellsAdjacent());
			}
		} else {
			return "o";
		}
	}
}
