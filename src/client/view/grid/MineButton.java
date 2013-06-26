package client.view.grid;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import client.view.ClientWindow;

import server.model.Cell;


/**
 * Class that represents a mine into the grid
 * @author stephane
 */
public class MineButton extends JButton {
	
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	/** Indicates whether the mine is hovered */
	private boolean isHovered;

	/**
	 * Constructor
	 * @param x abscissa of the mine
	 * @param y ordinate of the mine
	 */
	public MineButton(int x, int y) {
		super();
		this.setBorderPainted(false);
		this.setStyleDefault();

		this.isHovered = false;

		Dimension minSize = new Dimension(200, 500);
		Dimension prefSize = new Dimension(200, 500);
		Dimension maxSize = new Dimension(200, 500);
		this.add(new Box.Filler(minSize, prefSize, maxSize));
		this.addMouseListener(new MoveListener(x, y));
	}

	/**
	 * Sets the default style of mine (meant undiscovered)
	 */
	public void setStyleDefault() {
		String imagePath = "notDiscovered.png";
		this.setIcon((new ImageIcon(((new ImageIcon(getClass().getClassLoader().getResource(imagePath))).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH))));
		((ImageIcon) this.getIcon()).setDescription(imagePath);
	}

	/**
	 * Changes the mine appearance when hovering with the mega bomb
	 */
	public void toggleMouseEnter() {
		if(((ImageIcon) this.getIcon()).getDescription() == "notDiscovered.png" ||
		   ((ImageIcon) this.getIcon()).getDescription() == "bombHover.png"){
			this.isHovered = !this.isHovered;
			if(this.isHovered){
				String imagePath = "bombHover.png";
				this.setIcon(
						new ImageIcon(
								new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage()
								.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
				((ImageIcon) this.getIcon()).setDescription(imagePath);
			}else {
				this.setStyleDefault();
			}
		}
	}

	/**
	 * Apply the new style to the picture
	 * @param cell
	 */
	public void applyStyle(Cell cell) {
		String imagePath;
		
		if (cell.isDiscovered() != null) {
			boolean isHost = ClientWindow.getInstance().getController().getModel().isHost();
			
			//We check whether the mine is the last play
			String color = "";
			if (cell.isLast()) {	
				if (cell.isDiscovered()) {
					color = (isHost ? "Red" : "Blue");
				} else {
					color = (!isHost ? "Red" : "Blue");
				}
			}
			
			if (cell.isMine()) {
				if (cell.isDiscovered()) {
					if (isHost) {
						imagePath = "myMine" + color + ".png";
					} else {
						imagePath = "hisMine" + color + ".png";
					}
				} else {
					if (isHost) {
						imagePath = "hisMine" + color + ".png";
					} else {
						imagePath = "myMine" + color + ".png";
					}
				}
			} else {
				imagePath = cell.getNbCellsAdjacent() + color + ".png";
			}
		} else if (cell.isMine()) {
			imagePath = "mineNotDiscovered.png";
		} else {
			imagePath = "notDiscovered.png";
		}

		/* 
		 * Only change the current cell style 
		 * if the style of the resulted cell has changed 
		 */
		if (((ImageIcon) this.getIcon()).getDescription() != imagePath) {
			this.setIcon(
					new ImageIcon(
							new ImageIcon(getClass().getClassLoader().getResource(imagePath)).getImage()
							.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
			((ImageIcon) this.getIcon()).setDescription(imagePath);
		}
	}
}