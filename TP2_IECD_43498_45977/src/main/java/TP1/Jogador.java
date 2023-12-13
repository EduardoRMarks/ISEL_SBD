package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.awt.Menu;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class Jogador {

	public static void main(String[] args)
			throws SAXException, ParserConfigurationException, InterruptedException, IOException, TransformerException {

		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;

		try {
			// Criação do socket no localhost. Para aceder a um servidor que não é local
			// baste colocar o ip-adress.
			socket = new Socket("localhost", 9001);

			// Mostrar os parametros da ligação
			System.out.println("Ligação: " + socket);

			// Stream para escrita no socket
			out = new PrintWriter(socket.getOutputStream(), true);

			// Stream para leitura do socket
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// variáveis auxiliares
			int columnClicked = 0;
			int columnUpdate = -1;
			boolean played;

			String inputLine1;
			String outcome;
			String playerName;

			int turn = 1;
			long timeStart;
			long timeEnd;

			// Leitura do xml que vem do servidor
			//O player não tem acesso ao ficheiro xml diretamente
			
			String xmlDOC = readFullXML(in);
			System.out.println("XML que li do servidor");
			System.out.println(xmlDOC);
			System.out.println("");

			// Welcome message
			inputLine1 = in.readLine();
			System.out.println(inputLine1);
			System.out.println("");

			// player number
			int playerNumber = Integer.parseInt(String.valueOf(inputLine1.charAt(inputLine1.length() - 2)));
			System.out.println("Sou o player number " + playerNumber);
			System.out.println("");

			// menu
			StartingMenu menu = new StartingMenu(xmlDOC, playerNumber);
			FourInARowGUI game = menu.getGame();

			// Motor do jogo da parte do player

			for (;;) {
				
				//leitura da mensagem do servidor
				inputLine1 = null;
				inputLine1 = in.readLine();

				if (inputLine1 == null) {
					played = true;
				} else {
					//verificar se o recepido é um play do outro jogador
					columnUpdate = checkIfIsAPlay(inputLine1);
					played = false;
					if (columnUpdate != -1) {
						//caso seja uma play, atualizamos a board
						game.buttonGroup.updateBoard(columnUpdate);
					}
				}

				System.out.println(inputLine1);
				System.out.println("");
				
				while (!played) {
					
					/**
					 * Verificação se o jogo já terminou. Calcula-se o tempo da duração do jogo.
					 * Passa-se ao servidor que o jogo terminou, passa-se o tempo do jogo e o vencedor.
					 */
					if (game.buttonGroup.isGameFinished()) {
						timeStart = menu.lobby.getTimeStart();
						timeEnd = System.currentTimeMillis();
						out.println("Game is Finished");
						outcome = menu.updateInfo(game.buttonGroup.getWinner());
						out.print((int) TimeUnit.MILLISECONDS.toSeconds((timeEnd - timeStart)));
						out.println(outcome);
						break;
					}
					
					/**
					 * Verificação se há um novo registo. Casa haja, comunicamos ao servidor que há um novo
					 * registo. Atualizamos o xml com o novo registo em formato de string. Passamos o xml para
					 * o servidor.
					 */
					if (menu.reg != null) {
						if (menu.reg.novoRegisto()) {
							out.println("Novo registo");
							String novoXML = menu.reg.getUpdatedXML();
							menu.xmlWriter.UpdateDoc(novoXML);
							menu.xmlWriter.sendFullXML(out, novoXML);
							menu.reg.setNovoRegisto();
						}
					}
					
					/**
					 * Verificação se há uma mudança de imagem. Casa haja, comunicamos ao servidor que há uma
					 * nova imagem para um utilizador. Vai se buscar a imagem, busca-se o utilizador para qual
					 * a imagem foi alterada, atualizamos o xml em formato de string e passamos o xml para o servidor.
					 */
					
					if (menu.lobby != null) {
						if (menu.lobby.getChangedPicture()) {
							out.println("Muda de picture");
							String novaPath = menu.lobby.getPath();
							User user = menu.lobby.getUser();
							String novoXML = menu.xmlWriter.updatePicture(user, novaPath);
							menu.xmlWriter.sendFullXML(out, novoXML);
							menu.lobby.setChangedPicture();
						}
					}

					columnClicked = 0;
					
					/**
					 * Verificação dos botões de jogo. Casa o player joga, vai se buscar a coluna onde o jogador
					 * jogou, e passamos a informação para o servidor.
					 */
					for (boolean b : game.getButtonClicked()) {
						if (b) {
							System.out.println();
							System.out.println("JOGUEI NA COLUNA: " + columnClicked);
							out.println(columnClicked);
							columnClicked = 0;
							played = true;
							game.setButtonClicked();
							if (turn == 1) {
								turn = 2;
							}
							break;
						}
						columnClicked += 1;
					}
					/**
					 * Passamos ao servidor o nome do jogador que está a jogar.
					 */
					if (turn == 2) {
						playerName = menu.lobby.getUser().getNickname();
						out.println(playerName);
						turn = 3;
					}
				}
			}
		}

		catch (IOException e) {
			System.err.println("Erro na ligação " + e.getMessage());
		} finally {
			// No fim de tudo, fechar os streams e o socket
			if (out != null)
				out.close();
			if (in != null)
				in.close();
			if (socket != null)
				socket.close();
		} // end finally

	}
	/**
	 * Método que verifica se a informação recebida é uma jogada do outro jogador
	 * @param s mensagem do servidor
	 * @return coluna 
	 */
	private static int checkIfIsAPlay(String s) {
		if (s.charAt(0) == 'O') {
			System.out.println(s.charAt(29));
			System.out.println("jogado: " + Character.getNumericValue(s.charAt(29)));
			return Character.getNumericValue(s.charAt(29));
		} else {
			return -1;
		}
	}
	
	/**
	 * Método que lê o xml proveniente do servidor na sua toalidade
	 * @param in BufferedReader
	 * @return xml em string
	 * @throws IOException
	 */
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
}
