package client.view;

import java.awt.BorderLayout;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import server.model.Cell;
import client.controller.Receiver;
import client.model.Player;
import client.view.grid.GridPanel;
import client.view.lounge.PlayersPanel;
import client.view.score.ScorePanel;

/**
 * Class displaying of the main gaming view
 *
 * @author stephane
 */
public class ClientWindow extends JFrame implements Observer {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** The main client view instance */
	private static ClientWindow instance;

	/** The controller instance */
	private Receiver controller;

	/** The left division: displays the scores */
	private ScorePanel scorePanel;

	/** The middle division: displays the grid */
	private GridPanel gridPanel;

	/** The right division: displays the connected players */
	private PlayersPanel playersPanel;

	/**
	 * Constructor
	 * @param controller (MVC)
	 * @throws RemoteException
	 */
	public ClientWindow(Receiver controller) throws RemoteException{
		ClientWindow.setInstance(this);
		this.setController(controller);
		this.setTitle("JMines");
		this.setSize(760, 438);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());

		this.scorePanel = new ScorePanel();
		this.gridPanel = new GridPanel();
		this.playersPanel = new PlayersPanel();

		this.getContentPane().add(scorePanel, BorderLayout.WEST);
		this.getContentPane().add(gridPanel, BorderLayout.CENTER);
		this.getContentPane().add(playersPanel, BorderLayout.EAST);

		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * This manages all changes occuring on the client's model
	 * and displays them to the view
	 */
	@Override
	public void update(Observable arg0, Object message) {
		if (message instanceof List) {
			//change on connected users
			this.getPlayersPanel().update(arg0, message);
		} else if (message instanceof Cell[][]) {
			//change on grid
			this.gridPanel.update(arg0, message);
		} else if (message instanceof Player || message instanceof Boolean) {
			//change on score panel
			this.getScorePanel().update(arg0, message);
		}
	}

	/**
	 * @return
	 */
	public Receiver getController() {
		return this.controller;
	}

	/**
	 * @param controller
	 */
	public void setController(Receiver controller) {
		this.controller = controller;
	}

	/**
	 * @return
	 */
	public ScorePanel getScorePanel() {
		return scorePanel;
	}

	/**
	 * @param scorePanel
	 */
	public void setScorePanel(ScorePanel scorePanel) {
		this.scorePanel = scorePanel;
	}

	/**
	 * @return
	 */
	public GridPanel getGridPanel() {
		return gridPanel;
	}

	/**
	 * @param gridPanel
	 */
	public void setGridPanel(GridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}

	/**
	 * @return
	 */
	public PlayersPanel getPlayersPanel() {
		return playersPanel;
	}

	/**
	 * @param playersPanel
	 */
	public void setPlayersPanel(PlayersPanel playersPanel) {
		this.playersPanel = playersPanel;
	}

	/**
	 * @return
	 */
	public static ClientWindow getInstance() {
		return instance;
	}

	/**
	 * @param instance
	 */
	public static void setInstance(ClientWindow instance) {
		ClientWindow.instance = instance;
	}

}
