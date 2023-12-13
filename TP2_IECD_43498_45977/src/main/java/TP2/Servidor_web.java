package TP2;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import TP1.Game;

/**
 * Classe servidor vai ter a socket do servidor. Para jogar em dois hosts
 * diferentes, é necessário definir um ip-adress para o servidor com o bind.
 */

public class Servidor_web {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9001);
			System.out.println("Server started, waiting for players...");

			while (true) {
				Socket player1Socket = serverSocket.accept();
				System.out.println("Player 1 connected!");

				Socket player2Socket = serverSocket.accept();
				System.out.println("Player 2 connected!");

				Game game = new Game(player1Socket, player2Socket);
				game.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}

}
