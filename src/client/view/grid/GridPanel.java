package client.view.grid;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;


import server.model.Cell;


/**
 * Middle division
 * Represents the grid
 * 
 * @author stephane
 */
public class GridPanel extends JPanel implements Observer {
	
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** Size of the grid */
	private int size;

	/** Array of mines representing the grid */
	private MineButton[][] grid;

	
	/**
	 * Constructor
	 */
	public GridPanel() {
		super();
		this.size = 16;
		this.grid = new MineButton[this.size][this.size];
		this.makeNewMineGrid();
        this.setBounds(0, 0, 480, 420);
		this.setLayout(new GridLayout(size, size));
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
	}

	/**
	 * Initiates a grid of undiscovered mines
	 */
	public void makeNewMineGrid() {
		MineButton cell;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cell = new MineButton(i, j);
				this.add(cell);
				this.grid[i][j] = cell;
			}
		}
	}

	/**
	 * Displays the resulting grid
	 * @param cells
	 */
	public void setGridFromTab(Cell[][] cells) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.grid[i][j].applyStyle(cells[i][j]);
			}
		}
	}

	/**
	 * Displays the mega bomb
	 * @param x abscissa of the mouse position
	 * @param y ordinate of the mouse position
	 */
	public void bombDisplay(int x, int y) {
		if( x>13 ){
			x = 13;
		}else if( x<2 ) {
			x = 2;
		}
		if( y>13 ){
			y = 13;
		}else if( y<2 ){
			y = 2;
		}

		for (int i = x-2; i < x+3; i++) {
			for (int j = y-2; j < y+3; j++) {
				if(i < 16 && i >= 0 && j < 16 && j >= 0 ){
					this.getMineButton(i, j).toggleMouseEnter();
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		Cell[][] cells = (Cell[][]) arg1;
		this.setGridFromTab(cells);
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public MineButton getMineButton(int x, int y) {
		return this.grid[x][y];
	}

}