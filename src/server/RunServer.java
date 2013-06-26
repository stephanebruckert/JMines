package server;

import java.rmi.RemoteException;

import server.controller.Dispatcher;

/**
 * Server launcher
 * @author stephane
 */
public class RunServer {
	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {
		@SuppressWarnings("unused")
		Dispatcher dispatcher = new Dispatcher();
	}
}
