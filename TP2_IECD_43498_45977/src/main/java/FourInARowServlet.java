import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import TP1.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/FourInARowServlet")
public class FourInARowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int ROWS = 8;
	private static final int COLS = 8;
	private static final char EMPTY = '-';
	private static final char PLAYER1 = 'X';
	private static final char PLAYER2 = 'O';

	Socket socket = null;
	BufferedReader in = null;
	PrintWriter out = null;
	float timeStart;

	int turn = 1;

	@Override
	public void init() throws ServletException {

	}

	/**
	 * @Override public void init() throws ServletException { try { socket = new
	 *           Socket("localhost", 9001); // Replace with the server IP address in
	 *           = new BufferedReader(new
	 *           InputStreamReader(socket.getInputStream())); out = new
	 *           PrintWriter(socket.getOutputStream(), true); String input =
	 *           readFullXML(in); System.out.println(input);
	 * 
	 *           input = in.readLine(); System.out.println(input); } catch
	 *           (IOException e) { e.printStackTrace(); } }
	 * 
	 * @Override public void destroy() { try { in.close(); out.close();
	 *           socket.close(); } catch (IOException e) { e.printStackTrace(); } }
	 **/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DO GET");
		HttpSession session = request.getSession();
		Socket socket = (Socket) session.getAttribute("socket");
		BufferedReader in = (BufferedReader) session.getAttribute("in");
		PrintWriter out = (PrintWriter) session.getAttribute("out");
		//User user = (User) session.getAttribute("user");
		char[][] board = (char[][]) session.getAttribute("board");
		if (board == null) {
			board = createEmptyBoard();
			session.setAttribute("board", board);
			session.setAttribute("turn", 1); // Set initial turn value
		}
		if (turn != 1) {
			String input = in.readLine();
			System.out.println(input);

			int play = checkIfIsAPlay(input);
			if (play == -1) {
				//input = in.readLine();
				//System.out.println(input);
			} else {
				System.out.println("VOU FAZER DROP NA COLUNA: " + play);
				int row = dropPiece(board, play, session);
				boolean hasWon = checkWin(board, row, play, session);
				if (hasWon) {
					char currentPlayer = getPlayerTurn(session);
					session.setAttribute("winner", String.valueOf(currentPlayer));
					System.out.println("Player " + currentPlayer + " wins!");
					
					float timeEnd = System.currentTimeMillis();
					out.println("Game is Finished");
					out.print((int) TimeUnit.MILLISECONDS.toSeconds((long) (timeEnd - timeStart)));
					out.println(currentPlayer + " won");
				} else {
					switchPlayerTurn(session);
				}
			}
		}
		else {
			timeStart = System.currentTimeMillis();
		}
		request.setAttribute("board", board);
		request.getRequestDispatcher("/game.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("DO POST");
		HttpSession session = request.getSession();
		Socket socket = (Socket) session.getAttribute("socket");
		BufferedReader in = (BufferedReader) session.getAttribute("in");
		PrintWriter out = (PrintWriter) session.getAttribute("out");
		
		//System.out.println("1");
		System.out.println();
		char[][] board = (char[][]) session.getAttribute("board");
		
		//System.out.println("2");
		//System.out.println();


		//System.out.println("3");
		//System.out.println();
		if (turn == 1) {
			String input = in.readLine();
			System.out.println(input);
			if (input.equals("Your turn.")) {
				//System.out.println("3 1");
				//System.out.println();
			} else {
				//System.out.println("3 2");
				//System.out.println();
				int play = checkIfIsAPlay(input);
				if (play == -1) {
					input = in.readLine();
					System.out.println(input);
				} else {
					//System.out.println("3 3");
					//System.out.println();
					int row = dropPiece(board, play, session);
					// request.setAttribute("board", board);
					session.setAttribute("board", board);
					boolean hasWon = checkWin(board, row, play, session);
					if (hasWon) {
						char currentPlayer = getPlayerTurn(session);
						session.setAttribute("winner", String.valueOf(currentPlayer));
						System.out.println("Player " + currentPlayer + " wins!");
						
						float timeEnd = System.currentTimeMillis();
						out.println("Game is Finished");
						out.print((int) TimeUnit.MILLISECONDS.toSeconds((long) (timeEnd - timeStart)));
						out.println(currentPlayer + " won");
					} else {
						switchPlayerTurn(session);
					}
				}
			}
		}
		//System.out.println("4");
		//System.out.println();
		board = (char[][]) session.getAttribute("board");
		/**
		 * String input = in.readLine(); System.out.println(input);
		 * 
		 * int play = checkIfIsAPlay(input); if (play == -1) { input = in.readLine();
		 * System.out.println(input); } else { int row = dropPiece(board, play,
		 * session); boolean hasWon = checkWin(board, row, play, session); if (hasWon) {
		 * char currentPlayer = getPlayerTurn(session); session.setAttribute("winner",
		 * String.valueOf(currentPlayer)); System.out.println("Player " + currentPlayer
		 * + " wins!"); } else { switchPlayerTurn(session); } }
		 **/

		int column = Integer.parseInt(request.getParameter("column"));
		int row = dropPiece(board, column, session);
		//System.out.println("5");
		System.out.println();
		System.out.println();
		System.out.println("JOGUEI NA COLUNA: " + column);
		out.println(column);

		if (turn == 1) {
			User user = (User) session.getAttribute("user");
			out.println(user.getNickname());
			turn = 2;
		}
		//System.out.println("6");
		System.out.println();
		boolean hasWon = checkWin(board, row, column, session);
		if (hasWon) {
			char currentPlayer = getPlayerTurn(session);
			session.setAttribute("winner", String.valueOf(currentPlayer));
			System.out.println("Player " + currentPlayer + " wins!");
		} else {
			switchPlayerTurn(session);
		}

		// request.getRequestDispatcher("/game.jsp").forward(request, response);

		// response.sendRedirect("game.jsp");
		response.sendRedirect("FourInARowServlet");
	}

	private char[][] createEmptyBoard() {
		char[][] board = new char[ROWS][COLS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				board[i][j] = EMPTY;
			}
		}
		return board;
	}

	private int dropPiece(char[][] board, int col, HttpSession session) {
		int row = ROWS - 1;
		while (board[row][col] != EMPTY) {
			row--;
		}
		board[row][col] = getPlayerTurn(session);
		session.setAttribute("board", board);
		return row;
	}

	private boolean checkWin(char[][] board, int row, int column, HttpSession session) {
		char currentPlayer = getPlayerTurn(session);

		// Check horizontally
		int count = 1;
		for (int i = column - 1; i >= 0; i--) {
			if (board[row][i] == currentPlayer) {
				count++;
			} else {
				break;
			}
		}
		for (int i = column + 1; i < COLS; i++) {
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
		for (int i = row + 1, j = column + 1; i < ROWS && j < COLS; i++, j++) {
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
		for (int i = row - 1, j = column + 1; i >= 0 && j < COLS; i--, j++) {
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

	private char getPlayerTurn(HttpSession session) {
		Integer turn = (Integer) session.getAttribute("turn");
		return (turn != null && turn == 1) ? PLAYER1 : PLAYER2;
	}

	private void switchPlayerTurn(HttpSession session) {
		Integer turn = (Integer) session.getAttribute("turn");
		session.setAttribute("turn", (turn != null && turn == 1) ? 2 : 1);
	}

	private static String readFullXML(BufferedReader in) throws IOException {
		String StringXML = "";
		int turn = 0;

		while (true) {
			String input = in.readLine();
			if (input.isBlank()) {
				input = in.readLine();
			}
			if (input.contains("</Users>")) {
				StringXML = StringXML + input;
				return StringXML;
			}
			if (turn != 0) {

				StringXML = StringXML + input;

			}
			turn = 1;
		}
	}

	private static int checkIfIsAPlay(String s) {
		if (s.charAt(0) == 'O') {
			// System.out.println(s.charAt(29));
			// System.out.println("Foi jogado: " + Character.getNumericValue(s.charAt(29)));
			return Character.getNumericValue(s.charAt(29));
		} else {
			return -1;
		}
	}
}