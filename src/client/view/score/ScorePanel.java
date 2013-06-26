package client.view.score;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.Border;

import client.model.Player;
import client.view.util.ImagePanel;

/**
 * Left panel
 * Displays the current game scores
 *
 * @author donovanrugby
 */
public class ScorePanel extends JPanel implements Observer {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** Two divisions, one for current player, one for opponent */
	private ImagePanel p1, p2, nbBombRest;

	/** Two background images, one for current player, one for opponent */
	private Image imgP1, imgP2, imgNbBombRest, imgP1Arrow, imgP2Arrow;

	/** Two labels for usernames, one for current player, one for opponent */
	private JLabel jlabelHisName, jlabelMyName;

	/** Number of bomb remaining in the game */
	private JLabel jlabelNbBomb;

	/** Two labels for scores, one for current player, one for opponent */
	private JLabel jlabelHisScore, jlabelMyScore;

	/** Two labels for megabomb buttons, one for current player, one for opponent */
	private JBombButton myBomb, hisBomb;

	/**
	 * Constructor
	 */
	public ScorePanel() {

		this.setLayout(new GridBagLayout());
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setBackground(Color.GRAY);

		setLayout(new BorderLayout());

		this.imgP1 = new ImageIcon(getClass().getClassLoader().getResource("me.png")).getImage();
		this.imgP2 = new ImageIcon(getClass().getClassLoader().getResource("him.png")).getImage();
		this.imgP1Arrow = new ImageIcon(getClass().getClassLoader().getResource("meArrow.png")).getImage();
		this.imgP2Arrow = new ImageIcon(getClass().getClassLoader().getResource("himArrow.png")).getImage();
		this.imgNbBombRest = new ImageIcon(getClass().getClassLoader().getResource("bombRest.png")).getImage();


		this.p1=new ImagePanel(imgP1);
		this.p2=new ImagePanel(imgP2);
		this.nbBombRest = new ImagePanel(imgNbBombRest);

		this.p1.setBackground(Color.BLACK);
		this.p2.setBackground(Color.BLACK);
		this.nbBombRest.setBackground(Color.BLACK);

		Dimension minSize = new Dimension(200, 0);
		Dimension prefSize = new Dimension(200, 0);
		Dimension maxSize = new Dimension(200, 0);
		this.p1.add(new Box.Filler(minSize, prefSize, maxSize));
		this.p2.add(new Box.Filler(minSize, prefSize, maxSize));
		this.nbBombRest.add(new Box.Filler(minSize, prefSize, maxSize));


		this.p1.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.p2.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.nbBombRest.setLayout(new FlowLayout(FlowLayout.CENTER));

		this.jlabelNbBomb = new JLabel("51", JLabel.CENTER);
		this.jlabelNbBomb.setFont(new Font("Verdana",1,17));
		this.jlabelNbBomb.setPreferredSize(new Dimension(50, 20));
		this.jlabelNbBomb.setForeground(Color.white);
		Border paddingBorder7 = BorderFactory.createEmptyBorder(-4,0,0,0);
		this.jlabelNbBomb.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder7));
		this.nbBombRest.add(jlabelNbBomb);

		// ButtonBomb
		this.hisBomb = new JBombButton();
		//Border used as padding
		Border paddingBorderBomb = BorderFactory.createEmptyBorder(8,0,0,0);
		this.hisBomb.setBorder(BorderFactory.createCompoundBorder(null,paddingBorderBomb));

		//Jlabel
		this.jlabelHisName = new JLabel("Opponent", JLabel.CENTER);
		this.jlabelHisName.setFont(new Font("Verdana",1,10));
		this.jlabelHisName.setForeground(Color.BLACK);
		this.jlabelHisName.setPreferredSize(new Dimension(200, 100));
		Border paddingBorder = BorderFactory.createEmptyBorder(57,0,0,0);
		this.jlabelHisName.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder));

		this.jlabelHisScore = new JLabel("0", JLabel.CENTER);
		this.jlabelHisScore.setFont(new Font("Verdana",1,17));
		this.jlabelHisScore.setForeground(Color.BLACK);
		this.jlabelHisScore.setPreferredSize(new Dimension(50, 100));
		Border paddingBorder2 = BorderFactory.createEmptyBorder(4,17,0,0);
		this.jlabelHisScore.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder2));

		JPanel scoreBombContent = new JPanel(new BorderLayout());
		scoreBombContent.add(jlabelHisScore, BorderLayout.WEST);
		scoreBombContent.add(hisBomb, BorderLayout.EAST);
		scoreBombContent.setOpaque(false);

		this.p1.add(jlabelHisName);
		this.p1.add(scoreBombContent);

		// ButtonBomb
		this.myBomb = new JBombButton();
		//Border used as padding
		Border paddingBorderBomb2 = BorderFactory.createEmptyBorder(8,0,0,0);
		this.myBomb.setBorder(BorderFactory.createCompoundBorder(null,paddingBorderBomb2));

		//Jlabel
		this.jlabelMyName = new JLabel("Opponent", JLabel.CENTER);
		this.jlabelMyName.setFont(new Font("Verdana",1,10));
		this.jlabelMyName.setForeground(Color.BLACK);
		this.jlabelMyName.setPreferredSize(new Dimension(200, 100));
		Border paddingBorder3 = BorderFactory.createEmptyBorder(57,0,0,0);
		this.jlabelMyName.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder3));


		this.jlabelMyScore = new JLabel("0", JLabel.CENTER);
		this.jlabelMyScore.setFont(new Font("Verdana",1,17));
		this.jlabelMyScore.setForeground(Color.BLACK);
		this.jlabelMyScore.setPreferredSize(new Dimension(50, 100));
		Border paddingBorder4 = BorderFactory.createEmptyBorder(4,17,0,0);
		this.jlabelMyScore.setBorder(BorderFactory.createCompoundBorder(null,paddingBorder4));

		JPanel scoreBombContent2 = new JPanel(new BorderLayout());
		scoreBombContent2.add(jlabelMyScore, BorderLayout.WEST);
		scoreBombContent2.add(myBomb, BorderLayout.EAST);
		scoreBombContent2.setOpaque(false);

		this.p2.add(jlabelMyName);
		this.p2.add(scoreBombContent2);


		JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT, p1, nbBombRest);
		splitPaneLeft.setResizeWeight(0);
		splitPaneLeft.setEnabled(false);
		splitPaneLeft.setDividerSize(0);
		splitPaneLeft.setBounds(0, 0, 200, 100);

		JSplitPane splitPaneRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneLeft, p2);
		splitPaneRight.setResizeWeight(0);
		splitPaneRight.setEnabled(false);
		splitPaneRight.setDividerSize(0);

		splitPaneRight.setBounds(0, 223, 113, 200);



		splitPaneLeft.setBackground(Color.BLACK);
		splitPaneRight.setBackground(Color.BLACK);

		add(splitPaneRight, BorderLayout.CENTER);
		add(splitPaneLeft, BorderLayout.CENTER);


	}

	/**
	 * @param newScore
	 */
	public void setHisScore(int newScore){
		this.jlabelHisScore.setText(String.valueOf(newScore));
	}

	/**
	 * @param newScore
	 */
	public void setMyScore(int newScore){
		this.jlabelMyScore.setText(String.valueOf(newScore));
	}

	/**
	 * @param newScore
	 */
	public void setNbBombRest(int nbBombRest){
		this.jlabelNbBomb.setText(String.valueOf(nbBombRest));
	}

	/**
	 * @param name
	 */
	public void setHisName(String name){
		this.jlabelHisName.setText(name);
	}

	/**
	 * @param name
	 */
	public void setMyName(String name){
		this.jlabelMyName.setText(name);
	}

	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object message) {
		if (message instanceof Boolean) {
			if((Boolean) message){
				this.p2.setImg(this.imgP2Arrow);
				this.p1.setImg(this.imgP1);
			}else{
				this.p1.setImg(this.imgP1Arrow);
				this.p2.setImg(this.imgP2);
			}
			return;
		}

		Player player = (Player)message;

		Integer nbBomb = client.view.ClientWindow.getInstance().getController().getModel().getBombRest();
		this.jlabelNbBomb.setText(nbBomb.toString());

		if (player.isItMe()) {
			this.setMyScore(player.getMinesFound());
			this.setMyName(player.getUsername());
			if (player.getMinesFound() >= 26) {
				final String[] TEXT = {
						"Game over",
				"You won!"};
				showMyPane(TEXT);
			}
			if(player.getMyBombIsUsed() == true){ // used
				this.myBomb.setState(0);
			}else if( !player.getMyBombIsUsed() && player.getCanUseBomb()){ // unused
				this.myBomb.setState(1);
			}else if( !player.getMyBombIsUsed() && !player.getCanUseBomb()){ // disabled
				this.myBomb.setState(2);
			}
		} else {
			this.setHisScore(player.getMinesFound());
			this.setHisName(player.getUsername());
			if (player.getMinesFound() >= 26) {
				final String[] TEXT = {
						"Game over",
				"You lost!"};
				showMyPane(TEXT);
			}
			if(player.getMyBombIsUsed() == true){ // used
				this.hisBomb.setState(0);
			}else if(player.getMyBombIsUsed() == false && player.getCanUseBomb()){ // unused
				this.hisBomb.setState(1);
			}else if(player.getMyBombIsUsed() == false && !player.getCanUseBomb()){ // disabled
				this.hisBomb.setState(2);
			}
		}
	}

	/**
	 * Displays a message dialog
	 * @param TEXT
	 */
	public static void showMyPane(final String[] TEXT) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, TEXT[1], TEXT[0],
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}