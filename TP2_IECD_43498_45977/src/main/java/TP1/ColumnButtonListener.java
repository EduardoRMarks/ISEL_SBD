package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Classe que implementa o motor do jogo da parte do jogador.
 */
public class ColumnButtonListener implements ActionListener {

	private static final int ROWS = 8;
	private static final int COLUMNS = 8;

	private int[][] board;
	private Circle[][] circles;
	private int currentPlayer;
	private JLabel currentPlayerLabel;
	private boolean gameFinished;
	private int column;
	private Boolean[] number;
	private int playerWhenGameFinished;
	private int playerNumber;

	public ColumnButtonListener(int[][] board, Circle[][] circles, int currentPlayer, JLabel currentPlayerLabel, int playerNumber) {
		this.board = board;
		this.circles = circles;
		this.currentPlayer = currentPlayer;
		this.currentPlayerLabel = currentPlayerLabel;
		this.gameFinished = false;
		this.playerNumber = playerNumber;
		this.number = new Boolean[8];
		Arrays.fill(this.number, Boolean.FALSE);
	}
	
	/**
	 * Quando o jogador clica num botão para jogar é chamado este método.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		setFalse();
		JButton button = (JButton) e.getSource();
		column = Integer.parseInt(button.getText());
		this.number[column] = true;

		// Check if the game is finished
		if (gameFinished) {
			JOptionPane.showMessageDialog(null, "The game is already finished.");
			return;
		}

		// Check if the column is full
		if (board[0][column] != 0) {
			JOptionPane.showMessageDialog(null, "The column is full. Please select another column.");
			return;
		}

		// Drop the circle to the bottom
		int row = ROWS - 1;
		while (board[row][column] != 0) {
			row--;
		}

		// Update the board and the circles
		board[row][column] = currentPlayer;
		circles[row][column].setColor(currentPlayer == 1 ? Color.RED : Color.YELLOW);

		// Check if the game is finished
		if (isGameFinished(row, column)) {
			JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
			playerWhenGameFinished = currentPlayer;
			gameFinished = true;
			return;
		}

		// Check if the game is a tie
		if (isGameTie()) {
			JOptionPane.showMessageDialog(null, "The game is a tie!");

			gameFinished = true;
			return;
		}

		// Switch to the other player
		currentPlayer = currentPlayer == 1 ? 2 : 1;
		currentPlayerLabel.setText("Turn: Player " + currentPlayer + " You are the player number " + playerNumber);

		System.out.println("");
	}

	private boolean isGameFinished(int row, int column) {
		// Check horizontally
		int count = 1;
		for (int i = column - 1; i >= 0; i--) {
			if (board[row][i] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		for (int i = column + 1; i < COLUMNS; i++) {
			if (board[row][i] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 4) {
			return true;
		}

		// Check vertically
		count = 1;
		for (int i = row - 1; i >= 0; i--) {
			if (board[i][column] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		for (int i = row + 1; i < ROWS; i++) {
			if (board[i][column] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 4) {
			return true;
		}

		// Check diagonally (top-left to bottom-right)
		count = 1;
		for (int i = row - 1, j = column - 1; i >= 0 && j >= 0; i--, j--) {
			if (board[i][j] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		for (int i = row + 1, j = column + 1; i < ROWS && j < COLUMNS; i++, j++) {
			if (board[i][j] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 4) {
			return true;
		}

		// Check diagonally (top-right to bottom-left)
		count = 1;
		for (int i = row - 1, j = column + 1; i >= 0 && j < COLUMNS; i--, j++) {
			if (board[i][j] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		for (int i = row + 1, j = column - 1; i < ROWS && j >= 0; i++, j--) {
			if (board[i][j] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		if (count >= 4) {
			return true;
		}

		return false;

	}

	private boolean isGameTie() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (board[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

	public Boolean[] getColumn() {

		return this.number;
	}
	
	/**
	 * Método que atualiza a board com o play do outro jogador.
	 * @param column
	 */
	public void updateBoard(int column) {

		if (gameFinished) {
			JOptionPane.showMessageDialog(null, "The game is already finished.");
			return;
		}

		// Check if the column is full
		if (board[0][column] != 0) {
			JOptionPane.showMessageDialog(null, "The column is full. Please select another column.");
			return;
		}

		// Drop the circle to the bottom
		int row = ROWS - 1;
		while (board[row][column] != 0) {
			row--;
		}

		// Update the board and the circles
		board[row][column] = currentPlayer;
		circles[row][column].setColor(currentPlayer == 1 ? Color.RED : Color.YELLOW);

		// Check if the game is finished
		if (isGameFinished(row, column)) {
			JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " wins!");
			playerWhenGameFinished = currentPlayer;
			gameFinished = true;

			return;
		}

		// Check if the game is a tie
		if (isGameTie()) {
			JOptionPane.showMessageDialog(null, "The game is a tie!");
			gameFinished = true;
			return;
		}

		currentPlayer = currentPlayer == 1 ? 2 : 1;
		currentPlayerLabel.setText("Player " + currentPlayer + " You are the player number " + playerNumber);
	}

	public void setFalse() {
		Arrays.fill(this.number, Boolean.FALSE);
	}

	public int getWinner() {
		return playerWhenGameFinished;
	}

}
