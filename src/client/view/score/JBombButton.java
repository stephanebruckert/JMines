package client.view.score;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import client.view.ClientWindow;
import client.view.grid.MoveListener;

/**
 * Class that represents the mine state in the score panel
 * 
 * @author stephane
 */
public class JBombButton extends JButton {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	/** The index of the current image to display */
	private int stateNumber;
	
	/** The array of images */
	private String[] bombState = {"megaBombUsed.png", "megaBombUnused.png", "megaBombDisabled.png"};

	
	/**
	 * Constructor
	 */
	public JBombButton(){
		super();
		stateNumber = 2;

		this.setIcon(new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource(this.bombState[this.stateNumber]))
					.getImage().getScaledInstance(45, 30, java.awt.Image.SCALE_SMOOTH)));
		this.setPreferredSize(new Dimension(50, 20));
		this.setBorderPainted(false);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if (ClientWindow.getInstance().getController().getModel().isMyTurn()){
					if(stateNumber == 0 || stateNumber == 2){
						MoveListener.getInstance().setBombIsActive(false);
					}else{
						MoveListener.getInstance().setBombIsActive(true);
					}
				}
			}
		});
	}

	/**
	 * Sets the bomb button state by changing its picture
	 * @param state
	 */
	public void setState(int state){
		this.stateNumber = state;
		this.setIcon(new ImageIcon(
				new ImageIcon(getClass()
						.getClassLoader()
						.getResource(this.bombState[this.stateNumber]))
				.getImage().getScaledInstance(45, 30, java.awt.Image.SCALE_SMOOTH)));
	}
}
