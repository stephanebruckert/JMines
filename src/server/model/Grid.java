package server.model;

import java.io.Serializable;

public class Grid implements Serializable {
	private static final long serialVersionUID = 1L;

	// Attribute
	private Cell[][] cells; // Cells grid, contain the game
	private int nbBomb; // Number of mine in the game
	private int edgeSize; // Edge size of the grid

	// Methods

	/**
	 * Constructor
	 */
	public Grid(int edgeSize, int nbBomb) {
		this.nbBomb = nbBomb;
		this.edgeSize = edgeSize;
		this.cells = new Cell[edgeSize][edgeSize];
		// Initialization of all cells on the grid
		for(int x = 0; x < edgeSize ; x++) {
			for(int y = 0; y < edgeSize ; y++) {
				this.cells[x][y] = new Cell();
			}
		}

		// Create a playable grid with mines
		this.fillGrid();
		// Checks all adjcents mines for each cells to create number
		this.findAdjacentCell();
	}



	/**
	 * Place all mines that the game can contain with random
	 */
	private void fillGrid(){
		int nbBombToPut = 0;
		int x;
		int y;

		while ( nbBombToPut < this.nbBomb ){
			// Take a random coordinates
			x =  (int)(Math.random() * (this.edgeSize-1));
			y =  (int)(Math.random() * (this.edgeSize-1));

			if( !this.cells[x][y].isMine() ){
				this.cells[x][y].setMine(true);
				nbBombToPut++;
			}
		}
	}


	/**
	 * For each cell of the grid, set adajcents mines
	 */
	private void findAdjacentCell(){
		int nbCellsAdjacent = 0;

		for (int x = 0; x < this.edgeSize; x++) {
			for (int y = 0; y < this.edgeSize; y++) {
				if( x<(edgeSize-1) && this.cells[x+1][y].isMine()   ) nbCellsAdjacent++;
				if( x<(edgeSize-1) && y<(edgeSize-1) && this.cells[x+1][y+1].isMine() ) nbCellsAdjacent++;
				if( y<(edgeSize-1) && this.cells[x][y+1].isMine()   ) nbCellsAdjacent++;
				if( y<(edgeSize-1) && x>0 && this.cells[x-1][y+1].isMine() ) nbCellsAdjacent++;
				if( x>0 && this.cells[x-1][y].isMine()   ) nbCellsAdjacent++;
				if( x>0 && y>0 && this.cells[x-1][y-1].isMine() ) nbCellsAdjacent++;
				if( y>0 && this.cells[x][y-1].isMine()   ) nbCellsAdjacent++;
				if( x<(edgeSize-1) && y>0 && this.cells[x+1][y-1].isMine() ) nbCellsAdjacent++;

				this.cells[x][y].setNbCellsAdjacent(nbCellsAdjacent);
				nbCellsAdjacent = 0;
			}
		}
	}


	/**
	 * Discover all cells accordingly with the type of cell clicked
	 * @param x axis x
	 * @param y axis y
	 * @param discoverer Determine who has discovered a mine
	 * @return Mine has been discovered or not
	 */
	public boolean cellIsMine(int x, int y, boolean discoverer){
		// if cell contain a mine, neither other cell are discovered
		if( this.cells[x][y].isMine() ){
			this.cells[x][y].setDiscovered(discoverer);
			return true;
		}else{
			// if cell don't contain mine
			if( this.cells[x][y].getNbCellsAdjacent() == 0 ){
				// if this cell don't contain adjacents mine, we do
				// unfold all other adjacent cell which has no adjacents mines and after
				// cropping border of the area
				this.unfoldGaps(x, y, discoverer);
				this.unfoldBorder(discoverer);
			}else{
				// When it's a number, we only display this cell
				this.cells[x][y].setDiscovered(discoverer);
			}
			return false;
		}
	}

	/**
	 * When player click on a cell which has not adjacent mine, we search recursivly all other
	 * adjacent cell which has not adjacents mines (see result in game, it's when cell picture has no number)
	 * @param x axis x
	 * @param y axis y
	 * @param discoverer Specify the player who has cliqued
	 */
	public void unfoldGaps(int x, int y, boolean discoverer){
		this.cells[x][y].setDiscovered(discoverer);

		if( x<(edgeSize-1) && !this.cells[x+1][y].isMine() && this.cells[x+1][y].getNbCellsAdjacent() == 0 && this.cells[x+1][y].isDiscovered() == null ) {
			unfoldGaps(x+1, y, discoverer);
		}
		if( y<(edgeSize-1) && !this.cells[x][y+1].isMine() && this.cells[x][y+1].getNbCellsAdjacent() == 0 && this.cells[x][y+1].isDiscovered() == null) {
			unfoldGaps(x, y+1, discoverer);
		}
		if( x>0 && !this.cells[x-1][y].isMine() && this.cells[x-1][y].getNbCellsAdjacent() == 0 && this.cells[x-1][y].isDiscovered() == null ) {
			unfoldGaps(x-1, y, discoverer);
		}
		if( y>0 && !this.cells[x][y-1].isMine() && this.cells[x][y-1].getNbCellsAdjacent() == 0 && this.cells[x][y-1].isDiscovered() == null ) {
			unfoldGaps(x, y-1, discoverer);
		}
		if( x<(edgeSize-1) && y<(edgeSize-1) && !this.cells[x+1][y+1].isMine() && this.cells[x+1][y+1].getNbCellsAdjacent() == 0 && this.cells[x+1][y+1].isDiscovered() == null ) {
			unfoldGaps(x+1, y+1, discoverer);
		}
		if( x>0 && y<(edgeSize-1) && !this.cells[x-1][y+1].isMine() && this.cells[x-1][y+1].getNbCellsAdjacent() == 0 && this.cells[x-1][y+1].isDiscovered() == null ) {
			unfoldGaps(x-1, y+1, discoverer);
		}
		if( x>0 && y>0 && !this.cells[x-1][y-1].isMine() && this.cells[x-1][y-1].getNbCellsAdjacent() == 0 && this.cells[x-1][y-1].isDiscovered() == null ) {
			unfoldGaps(x-1, y-1, discoverer);
		}
		if( x<(edgeSize-1) && y>0 && !this.cells[x+1][y-1].isMine() && this.cells[x+1][y-1].getNbCellsAdjacent() == 0 && this.cells[x+1][y-1].isDiscovered() == null ) {
			unfoldGaps(x+1, y-1, discoverer);
		}
		return;
	}

	/**
	 * When we click in a cell which has no adjacents mines, we discorver
	 * the cells border, so the eight adjacents cells. These cells contains
	 * perforce number.
	 * @param discoverer Specify player who has clicked
	 */
	public void unfoldBorder(boolean discoverer){
		for (int x = 0; x < this.edgeSize ; x++) {
			for (int y = 0; y < this.edgeSize; y++) {
				if( this.cells[x][y].getNbCellsAdjacent() == 0
						&& this.cells[x][y].isDiscovered() != null
						&& !this.cells[x][y].isMine() ){
					if( x<(edgeSize-1) && this.cells[x+1][y].isDiscovered() == null ) {
						this.cells[x+1][y].setDiscovered(discoverer);
					}
					if( x<(edgeSize-1) && y<(edgeSize-1)  && this.cells[x+1][y+1].isDiscovered() == null ) {
						this.cells[x+1][y+1].setDiscovered(discoverer);
					}
					if( y<(edgeSize-1) && this.cells[x][y+1].isDiscovered() == null ) {
						this.cells[x][y+1].setDiscovered(discoverer);
					}
					if( y<(edgeSize-1) && x>0 && this.cells[x-1][y+1].isDiscovered() == null ) {
						this.cells[x-1][y+1].setDiscovered(discoverer);
					}
					if( x>0 && this.cells[x-1][y].isDiscovered() == null ) {
						this.cells[x-1][y].setDiscovered(discoverer);
					}
					if( x>0 && y>0 && this.cells[x-1][y-1].isDiscovered() == null ) {
						this.cells[x-1][y-1].setDiscovered(discoverer);
					}
					if( y>0 && this.cells[x][y-1].isDiscovered() == null ) {
						this.cells[x][y-1].setDiscovered(discoverer);
					}
					if( x<(edgeSize-1) && y>0 && this.cells[x+1][y-1].isDiscovered() == null ) {
						this.cells[x+1][y-1].setDiscovered(discoverer);
					}
				}
			}
		}
	}

	/**
	 * Create a grid only with discovered cells to avoid cheating
	 * @return grid with cells which is be going to be send to players.
	 */
	public Cell[][] getLightenedCells() {
		int size = this.getEdgeSize();
		Cell[][] lightenedCells = new Cell[size][size];
		Cell[][] cells = this.getCells();

		Cell cell;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				cell = cells[x][y];

				if (cell.isDiscovered() != null) {
					lightenedCells[x][y] = new Cell(cell.getNbCellsAdjacent(),
													cell.isDiscovered(),
													cell.isMine(),
													cell.isLast());
				} else {
					lightenedCells[x][y] = new Cell();
				}
			}
		}
		return lightenedCells;
	}

	// Getters | Setters
	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public int getNbBomb() {
		return nbBomb;
	}

	public void setNbBomb(int nbBomb) {
		this.nbBomb = nbBomb;
	}

	public int getEdgeSize() {
		return edgeSize;
	}

	public void setEdgeSize(int edgeSize) {
		this.edgeSize = edgeSize;
	}

	public String displayGrid() {
		String str = "";
		for (int x = 0; x < this.edgeSize ; x++) {
			for (int y = 0; y < this.edgeSize ; y++) {
				str += this.cells[x][y].toString() + " ";
			}
			str += "\n";
		}
		return str;
	}
}
