package client.view.lounge;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import client.view.ClientWindow;

/**
 * Right division
 * Displays the connected users
 * 
 * @author stephane
 */
public class PlayersPanel extends JPanel implements Observer {
	
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	/** The representation of the list */
	private JList list;
	
	/** The list of connected players */
	private DefaultListModel listModel;

	/** The "play with" label */
	private static final String fireString = "Play with";
	
	/** The "play with" button */
	private JButton playWithButton;

	/**
	 * Constructor
	 */
	public PlayersPanel() {
		super(new BorderLayout());
		
		listModel = new DefaultListModel();
		listModel.addElement("");

		//Create the list and put it in a scroll pane.
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);

		playWithButton = new JButton(fireString);
		playWithButton.setActionCommand(fireString);
		playWithButton.addActionListener(new PlayWithListener());

		//Create a panel that uses BoxLayout.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,
				BoxLayout.LINE_AXIS));
		buttonPane.add(playWithButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,31,5,31));

		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END); 
	}

	/**
	 * Listener on the "play with" button
	 * 
	 * @author stephane
	 */
	class PlayWithListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String username = (String) list.getModel().getElementAt(list.getSelectedIndex());
			try {
				if (!ClientWindow.getInstance().getController().isUserReady(username)) {
					JOptionPane.showMessageDialog(null, username + " is playing.");
				} else {
					ClientWindow.getInstance().getController().startGameWithOpponent(username);
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Changes states of the "play with" button
	 * @param e
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() == -1) {
				//No selection, disable fire button.
				playWithButton.setEnabled(false);
			} else {
				//Selection, enable the fire button.
				playWithButton.setEnabled(true);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		@SuppressWarnings("unchecked")
		List<String> list = ((ArrayList<String>)arg);
		this.listModel.clear();
		String currentUsername = ClientWindow.getInstance().getController().getMyUsername();
		for (String username : list) {
			if (!username.equals(currentUsername)) {
				this.listModel.addElement(username);
			}
		}
	}

}