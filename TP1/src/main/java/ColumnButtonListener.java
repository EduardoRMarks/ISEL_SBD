import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ColumnButtonListener implements ActionListener {

	private static final int ROWS = 8;
	private static final int COLUMNS = 8;

	private int[][] board;
	private Circle[][] circles;
	private int currentPlayer;
	private JLabel currentPlayerLabel;
	private boolean gameFinished;

	public ColumnButtonListener(int[][] board, Circle[][] circles, int currentPlayer, JLabel currentPlayerLabel) {
		this.board = board;
		this.circles = circles;
		this.currentPlayer = currentPlayer;
		this.currentPlayerLabel = currentPlayerLabel;
		this.gameFinished = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		int column = Integer.parseInt(button.getText());
		//System.out.println("JOGADOR "+ currentPlayer + " jogou na coluna: " + column);

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
		currentPlayerLabel.setText("Player " + currentPlayer);
		
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

}

