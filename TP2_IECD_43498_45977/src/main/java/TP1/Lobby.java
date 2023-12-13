package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Classe Lobby. Esta classe dispõe a informação do player após ser efectuado o
 * login.
 */
public class Lobby {
	private User user;

	private int playerNumber;
	long timeStart;
	boolean changedPicture = false;
	private String path;
	UserXmlWriter xmlWriter;

	public Lobby(User user, FourInARowGUI game, int playerNumber, UserXmlWriter xmlWriter) {
		this.user = user;
		this.playerNumber = playerNumber;
		this.xmlWriter = xmlWriter;

		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		frame.setLayout(new BorderLayout(0, 0));

		//fields e info
		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.getContentPane().add(userInfoPanel, BorderLayout.NORTH);
		GridBagLayout gbl_userInfoPanel = new GridBagLayout();
		gbl_userInfoPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_userInfoPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_userInfoPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_userInfoPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		userInfoPanel.setLayout(gbl_userInfoPanel);

		JLabel lblNewLabel = new JLabel("Nickname:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		userInfoPanel.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblNickname = new JLabel(user.getNickname());
		GridBagConstraints gbc_lblNickname = new GridBagConstraints();
		gbc_lblNickname.anchor = GridBagConstraints.WEST;
		gbc_lblNickname.insets = new Insets(0, 0, 5, 0);
		gbc_lblNickname.gridx = 1;
		gbc_lblNickname.gridy = 0;
		userInfoPanel.add(lblNickname, gbc_lblNickname);

		JLabel lblNewLabel_1 = new JLabel("Wins:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		userInfoPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		JLabel lblWins = new JLabel(user.getWins());
		GridBagConstraints gbc_lblWins = new GridBagConstraints();
		gbc_lblWins.anchor = GridBagConstraints.WEST;
		gbc_lblWins.insets = new Insets(0, 0, 5, 0);
		gbc_lblWins.gridx = 1;
		gbc_lblWins.gridy = 1;
		userInfoPanel.add(lblWins, gbc_lblWins);

		JLabel lblNewLabel_2 = new JLabel("Losses:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		userInfoPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		JLabel lblLosses = new JLabel(user.getLosses());
		GridBagConstraints gbc_lblLosses = new GridBagConstraints();
		gbc_lblLosses.anchor = GridBagConstraints.WEST;
		gbc_lblLosses.insets = new Insets(0, 0, 5, 5);
		gbc_lblLosses.gridx = 1;
		gbc_lblLosses.gridy = 2;
		userInfoPanel.add(lblLosses, gbc_lblLosses);

		JLabel lblNewLabel_3 = new JLabel("Time:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		userInfoPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		JLabel lblTime = new JLabel(user.getTime());
		GridBagConstraints gbc_lblTime = new GridBagConstraints();
		gbc_lblTime.anchor = GridBagConstraints.WEST;
		gbc_lblTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblTime.gridx = 1;
		gbc_lblTime.gridy = 3;
		userInfoPanel.add(lblTime, gbc_lblTime);

		JLabel lblNewLabel_4 = new JLabel("Age:");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;
		userInfoPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		JLabel lblAge = new JLabel(user.getAge());
		GridBagConstraints gbc_lblAge = new GridBagConstraints();
		gbc_lblAge.anchor = GridBagConstraints.WEST;
		gbc_lblAge.insets = new Insets(0, 0, 5, 5);
		gbc_lblAge.gridx = 1;
		gbc_lblAge.gridy = 4;
		userInfoPanel.add(lblAge, gbc_lblAge);

		JLabel lblNewLabel_5 = new JLabel("Picture:");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 5;
		userInfoPanel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		JLabel lblPic = new JLabel(user.getProfilePicturePath());
		GridBagConstraints gbc_lblPic = new GridBagConstraints();
		gbc_lblPic.anchor = GridBagConstraints.WEST;
		gbc_lblPic.insets = new Insets(0, 0, 5, 5);
		gbc_lblPic.gridx = 1;
		gbc_lblPic.gridy = 5;
		userInfoPanel.add(lblPic, gbc_lblPic);

		JButton playButton = new JButton("Play");
		GridBagConstraints gbc_playButton = new GridBagConstraints();
		gbc_playButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_playButton.insets = new Insets(0, 0, 5, 5);
		gbc_playButton.gridx = 0;
		gbc_playButton.gridy = 6;
		userInfoPanel.add(playButton, gbc_playButton);

		JButton changePic = new JButton("Change Picture");
		GridBagConstraints gbc_changePic = new GridBagConstraints();
		gbc_changePic.fill = GridBagConstraints.HORIZONTAL;
		gbc_changePic.insets = new Insets(0, 0, 5, 5);
		gbc_changePic.gridx = 1;
		gbc_changePic.gridy = 6;
		userInfoPanel.add(changePic, gbc_changePic);

		frame.setVisible(true);
		
		//actionlistener for play
		playButton.addActionListener(e -> {
			game.setVisible(true);
			frame.dispose();
			timeStart = System.currentTimeMillis();
		});
		
		//action listener for change pic
		changePic.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGES", "jpeg", "gif", "png");
			fileChooser.addChoosableFileFilter(filter);
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				path = selectedFile.getAbsolutePath();
				try {
					xmlWriter.updatePicture(user, path);
				} catch (ParserConfigurationException | TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				changedPicture = true;
			} else if (result == JFileChooser.CANCEL_OPTION) {
				System.out.println("No File selected");
			}
		});

	}

	public User getUser() {
		return user;
	}

	public User getUserWinner(int winner) {
		if (winner == playerNumber) {
			return user;
		}
		return null;
	}

	public User getUserLoss(int winner) {
		if (winner != playerNumber) {
			return user;
		}
		return null;
	}

	public long getTimeStart() {
		return timeStart;
	}

	public boolean getChangedPicture() {
		return changedPicture;
	}

	public void setChangedPicture() {
		this.changedPicture = false;
	}

	public String getPath() {
		return path;
	}

}
