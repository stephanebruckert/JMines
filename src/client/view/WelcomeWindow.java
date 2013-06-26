package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import client.RunClient;
import client.controller.Receiver;
import client.view.util.ImagePanel;

/**
 * Displays the welcome view window
 * @author stephane
 */
public class WelcomeWindow extends JFrame {
	
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	/** Controller MVC instance */
	private Receiver controller;

	/** Login button */
	private JButton blogin;
	
	/** Text field for username */
	private JTextField txuser;
	
	/** Text field for server IP */
	private JTextField serverIp;
	
	/** Main panel */
	private JPanel loginPanel;

	/** Background image */
	private Image background;

	/** Button labels */
	private JLabel username, serverIpLabel;

	/** Presentation picture */
	private ImagePanel picturePanel;
	
	/** Position of the window, abscissa */
	private int X = 0;
	
	/** Position of the window, ordinate */
	private int Y = 0;
	
	
	/**
	 * Constructor
	 */
	public WelcomeWindow(){
		super("Connection to the JMines network");
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setBackground(Color.BLACK);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		this.txuser = new JTextField(10);
		this.serverIp = new JTextField(10);
		this.username = new JLabel("Nickname: ");
		this.username.setForeground(Color.white);
		this.serverIpLabel = new JLabel("Server IP:");
		this.serverIpLabel.setForeground(Color.white);
		
		// Add background image
		this.background = new ImageIcon(getClass().getClassLoader().getResource("splash.jpg")).getImage();
		this.picturePanel = new ImagePanel(background);
		this.picturePanel.setBackground(Color.BLACK);
		addMoveWindowListeners(this.picturePanel);

		//Login button
		this.blogin = new JButton();
		this.blogin.setIcon(new ImageIcon(
				new ImageIcon(getClass().getClassLoader()
						.getResource("login.png")).getImage()
						.getScaledInstance(150, 40, java.awt.Image.SCALE_SMOOTH)));
		this.blogin.setBorderPainted(false);
		
		this.loginPanel = new JPanel();
		this.loginPanel.setBackground(Color.BLACK);
		this.loginPanel.add(username);
		this.loginPanel.add(txuser);
		this.loginPanel.add(serverIpLabel);
		this.loginPanel.add(serverIp);
		this.loginPanel.add(blogin);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                this.loginPanel, picturePanel);
		splitPane.setResizeWeight(0);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);
		this.add(splitPane, BorderLayout.CENTER);

		//JPanel properties
		this.setVisible(true);
		this.setSize(600, 345);
		
		actionlogin();
	}
	
	/**
	 * Allows the Welcome Window to be moved by dragging the picture
	 * @param picturePanel the picture
	 */
	public void addMoveWindowListeners(ImagePanel picturePanel) {
		/* Gets the position */
		picturePanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				X = e.getX();
				Y = e.getY();
			}
		});
		/* Moves the window */
		picturePanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(getLocation().x + (e.getX() - X),
						getLocation().y + (e.getY() - Y));
			}
		});
	}
	
	/**
	 * Called when the login button is clicked
	 */
	public void actionlogin(){
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String puname = txuser.getText();
				String puip = serverIp.getText();
				getController().setMyUsername(puname);
				int connectionAttempt = getController().connect(puip);
				if(connectionAttempt > 0) {
					try {
						RunClient.showMainWindow();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					dispose();
				} else if (connectionAttempt == 0){
					JOptionPane.showMessageDialog(null,"Username already taken");
					txuser.requestFocus();
				} else {
					JOptionPane.showMessageDialog(null,"Can't connect to server");
					serverIp.requestFocus();
				}
			}
		});
	}

	/**
	 * @return
	 */
	public Receiver getController() {
		return controller;
	}

	/**
	 * @param controller
	 */
	public void setController(Receiver controller) {
		this.controller = controller;
	}

}