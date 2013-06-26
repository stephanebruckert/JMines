package client;

import java.rmi.RemoteException;

import client.controller.Receiver;
import client.model.CurrentGame;
import client.view.ClientWindow;
import client.view.WelcomeWindow;

/**
 * Client Launcher
 * Initiates the MVC
 * 
 * @author stephane
 */
public class RunClient {
	private static WelcomeWindow welcomeView;
	private static CurrentGame model;
	private static Receiver controller;
	private static ClientWindow clientView;
	
	/** 
	 * Main class 
	 * Launches the welcome window
	 */
	public static void main(String args[]) throws RemoteException, InterruptedException{
		showConnectionWindow();
	}
	
	/**
	 * Shows the connection window
	 */
	public static void showConnectionWindow() {
		model = new CurrentGame();
		controller = new Receiver(model);
		welcomeView = new WelcomeWindow();
		welcomeView.setController(controller);
	}
	
	/**
	 * Shows the game window
	 * @throws RemoteException
	 */
	public static void showMainWindow() throws RemoteException {
		clientView = new ClientWindow(controller);
		model.addObserver(clientView);
		controller.getModel().updateConnectedUsers();
	}

}
