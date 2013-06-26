package client.view.grid;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;

import client.view.ClientWindow;

/**
 * Listens for a move on the grid
 * @author stephane
 */
public class MoveListener extends Point implements MouseListener {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** Indicates whether the mega bomb is active */
	private static boolean bombIsActive;

	/** Static instance of the move listener */
	private static MoveListener instance;

	/**
	 * Constructor
	 * @param x abscissa
	 * @param y ordinates
	 */
	public MoveListener(int x, int y) {
		super(x, y);
		MoveListener.bombIsActive = false;
		MoveListener.setInstance(this);
	}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
	public void mouseClicked(MouseEvent e) {

    }

    /**
     * When a mine is clicked
     * @param eventDescription
     * @param e
     * @throws RemoteException
     */
    void mineClicked(String eventDescription, MouseEvent e)
    		throws RemoteException {
        if(bombIsActive){
        	bombIsActive = !bombIsActive;
			ClientWindow.getInstance().getGridPanel().bombDisplay(this.x, this.y);
        	ClientWindow.getInstance().getController().getModel().setBombIsUsed(true);
        	ClientWindow.getInstance().getController()
    								.confirmClickedCoordinates(new Point(x, y), true);
        }else{
            ClientWindow.getInstance().getController()
    								.confirmClickedCoordinates(new Point(x, y), false);
        }

    }

	/**
	 * @return
	 */
	public boolean getBombIsActive() {
		return bombIsActive;
	}

	/**
	 * @param bombIsActive
	 */
	public void setBombIsActive(boolean bombIsActive) {
		MoveListener.bombIsActive = bombIsActive;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		if( bombIsActive ){
			ClientWindow.getInstance().getGridPanel().bombDisplay(this.x, this.y);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		if( bombIsActive ){
			ClientWindow.getInstance().getGridPanel().bombDisplay(this.x, this.y);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
    	try {
			mineClicked("(" + super.x + ", " + super.y + ")", e);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return
	 */
	public static MoveListener getInstance() {
		return instance;
	}

	/**
	 * @param instance
	 */
	public static void setInstance(MoveListener instance) {
		MoveListener.instance = instance;
	}
}
