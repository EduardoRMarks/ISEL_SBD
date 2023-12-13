package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.catalina.util.XMLWriter;
import org.xml.sax.SAXException;

/**
 * Classe StartingMenu é o menu inicial quando um player conecta-se. Tem a
 * possibilidade de fazer login, registar e ver o ranking dos players.
 *
 */
public class StartingMenu {

	private JFrame frame;
	private JTextField nicknameTextField;
	private JPasswordField passwordField;
	UserXmlWriter xmlWriter;
	private boolean logged = false;
	private FourInARowGUI game;
	private int playerNumber;
	Lobby lobby;
	RegistrationWindow reg;

	public StartingMenu(String xmlDOC, int playerNumber)
			throws SAXException, IOException, ParserConfigurationException {
		this.playerNumber = playerNumber;
		xmlWriter = new UserXmlWriter(xmlDOC);
		this.game = new FourInARowGUI(playerNumber);
		initialize();
	}

	private void initialize() throws SAXException, IOException, ParserConfigurationException {
		// criação dos labels, das fields e dos botões
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		JLabel nicknameLabel = new JLabel("Nickname:");
		GridBagConstraints gbc_nicknameLabel = new GridBagConstraints();
		gbc_nicknameLabel.anchor = GridBagConstraints.EAST;
		gbc_nicknameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nicknameLabel.gridx = 1;
		gbc_nicknameLabel.gridy = 0;
		frame.getContentPane().add(nicknameLabel, gbc_nicknameLabel);

		nicknameTextField = new JTextField();
		GridBagConstraints gbc_nicknameTextField = new GridBagConstraints();
		gbc_nicknameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_nicknameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nicknameTextField.gridx = 2;
		gbc_nicknameTextField.gridy = 0;
		frame.getContentPane().add(nicknameTextField, gbc_nicknameTextField);
		nicknameTextField.setColumns(10);

		JLabel passwordLabel = new JLabel("Password:");
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 1;
		frame.getContentPane().add(passwordLabel, gbc_passwordLabel);

		passwordField = new JPasswordField();
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordField.gridx = 2;
		gbc_passwordField.gridy = 1;
		frame.getContentPane().add(passwordField, gbc_passwordField);

		JButton loginButton = new JButton("Login");
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_loginButton.insets = new Insets(0, 0, 5, 5);
		gbc_loginButton.gridx = 1;
		gbc_loginButton.gridy = 2;
		frame.getContentPane().add(loginButton, gbc_loginButton);

		JButton registerButton = new JButton("Register");
		GridBagConstraints gbc_registerButton = new GridBagConstraints();
		gbc_registerButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_registerButton.insets = new Insets(0, 0, 5, 5);
		gbc_registerButton.gridx = 2;
		gbc_registerButton.gridy = 2;
		frame.getContentPane().add(registerButton, gbc_registerButton);

		JLabel username = new JLabel("test");
		GridBagConstraints gbc_username = new GridBagConstraints();
		gbc_username.anchor = GridBagConstraints.EAST;
		gbc_username.insets = new Insets(0, 0, 5, 5);
		gbc_username.gridx = 1;
		gbc_username.gridy = 0;

		// read users from XML file and create a list of users
		List<User> userList = xmlWriter.readUsersFromFile();

		// sort the list by the number of wins in descending order
		Collections.sort(userList, new Comparator<User>() {
			@Override
			public int compare(User u1, User u2) {
				return Integer.parseInt(u2.getWins()) - Integer.parseInt(u1.getWins());
			}
		});

		// create a table model with the user data
		String[] columnNames = { "Rank", "Nickname", "Wins" };
		Object[][] rowData = new Object[userList.size()][3];
		for (int i = 0; i < userList.size(); i++) {
			rowData[i][0] = i + 1;
			rowData[i][1] = userList.get(i).getNickname();
			rowData[i][2] = userList.get(i).getWins();
		}
		DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);

		// create the ranking table and add it to the ranking panel
		JTable rankingTable = new JTable(tableModel);
		rankingTable.getColumnModel().getColumn(0).setPreferredWidth(40);
		rankingTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		rankingTable.getColumnModel().getColumn(2).setPreferredWidth(50);
		rankingTable.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane(rankingTable);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		// create ranking panel
		JPanel rankingPanel = new JPanel();
		scrollPane.setColumnHeaderView(rankingPanel);
		rankingPanel.setBorder(new TitledBorder(null, "Ranking", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rankingPanel.setBounds(25, 125, 350, 175);
		rankingPanel.setLayout(new BorderLayout());

		frame.setVisible(true);
		
		//action listener for register
		registerButton.addActionListener(e -> {
			reg = new RegistrationWindow(xmlWriter);
		});
		
		//action listener for login
		loginButton.addActionListener(e -> {
			User access = null;
			try {
				access = xmlWriter.loginChecker(nicknameTextField.getText(), passwordField.getText());

			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (access != null) {
				System.out.println(access.toString());
				lobby = new Lobby(access, game, playerNumber, xmlWriter);
				logged = true;

				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(frame, "Invalid nickname or password.");
			}
		});
	}

	public boolean isLogged() {
		return this.logged;
	}

	public FourInARowGUI getGame() {
		return this.game;
	}
	
	/**
	 * Método que vai ver se o jogador é o vencedor ou perdedor.
	 * @param winner numero do player
	 * @return se ganhou ou perdeu
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public String updateInfo(int winner) throws ParserConfigurationException, TransformerException {
		User userWinner = lobby.getUserWinner(winner);
		User userLoss = lobby.getUserLoss(winner);
		String xmlDOC;

		if (userWinner != null) {
			return lobby.getUser().getNickname() + " won";
		}
		if (userLoss != null) {
			return lobby.getUser().getNickname() + " lost";
		}

		return null;
	}

}
