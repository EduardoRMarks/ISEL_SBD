import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Contador de tarefas que correspondem a instanciações ativas do servidor dedicado
 * @author Prof. P. Filipe
 *
 */
class count {
	private static long count = 0;

	public static synchronized long get(long step) {
		return count = count + step;
	}
}

/**
 * Tarefa que implementa o servidor dedicado a cada circuito virtual
 * 
 * @author Prof. P. Filipe
 * 
 */
class ServidorDedicado extends Thread {
	private DocReadWrite.Protocol protocol;
	private Socket connection;

	/**
	 * @param connection - socket que representa o circuito virtual
	 * @param protocol   - tipo de protocolo (objectos, XML, JSON)
	 */
	public ServidorDedicado(Socket connection, DocReadWrite.Protocol protocol) {
		this.connection = connection;
		this.protocol = protocol;
	}

	// processamento especifico do servidor
	public static Document processar(Document xmlRequest) { // usar só no servidor
		SrvStub cmd = new SrvStub(xmlRequest);
		if (cmd.validar()) // verifica se não existe nenhum erro na recepção do pedido
			System.out.println("\nValidação da mensagem do cliente realizada com sucesso!");
		else {
			System.err.println("\nAviso: falhou a validação da mensagem do cliente!");
			// return xmlRequest; // devolve o mesmo comando
		}

		Document com = cmd.executar();
		if (com == null) {
			System.out.println("\nA processar comando vazio!");
			return xmlRequest; // devolve o mesmo comando
		} else {
			Comando resp = new Comando(com);
			// verifica se não existe nenhum erro na construção da respostas
			if (resp.validar())
				System.out.println("\nValidação da mensagem do servidor realizada com sucesso!");
			else {
				System.err.println("\nAviso: falhou a validação da mensagem do servidor!");
				// return xmlRequest; // devolve o mesmo comando; podia incluir um erro!
			}
		}
		return com;
	}

	public void run() {// servidor dedicado
		try {
			// circuito virtual estabelecido: socket cliente na variavel newSock
			System.out.println("Quantidade de servidores dedicados ativos: "+ count.get(1));
			System.out.println("Estabeleceu a ligação (circuito virtual), Thread " + this.getId() + ": " + connection.getRemoteSocketAddress());

			// a tarefa só termina quando o circuito virtual for quebrado
			for (;;) { // forever: a interacção mantem-se enquando o cliente nao desligar

				/* ciclo: lê pedido processa e envia a resposta */

				Document xmlRequest = DocReadWrite.documentFromNetwork(connection, protocol); // recebe o pedido

				if (xmlRequest == null)
					break; // houve desistência sai do'for'

				Document xmlReply = processar(xmlRequest); // processa o pedido

				DocReadWrite.documentToNetwork(xmlReply, connection, protocol); // envia a resposta

			}
		} catch (Exception e) {
			// quando o client desligar vai ocorrer uma excepção
			// mas se for por ter desligado não é nada de grave
			// é um comportamento comum
			System.out.println("Excepção/Aviso: " + e.getMessage());
		} finally {
			try {// IMPORTANTE: garantir que o socket é fechado
				connection.close();
			} catch (Exception e) {
				System.err.println("Erro no fecho da ligaçao: " + e.getMessage());
			}
		}
		System.out.println("Terminou a ligaçao (circuito virtual), Thread " + this.getId() + ": "
				+ connection.getRemoteSocketAddress());
		System.out.println("Quantidade de servidores dedicados ativos: " + count.get(-1));
	} // end run

} // end HandleConnectionThread - servidor dedicado

/**
 * classe que implementa o servidor principal
 * @author Prof. P. Filipe
 *
 */
public class Servidor {

	/**
	 * Lança 3 servidores TCP um para cada protocolo
	 * >>>>lança 1 servidor UPD para responder ao comando listar
	 */
	public static void main(String[] args) {
		Poema.check();
		new Thread(() -> {
			ServerTCP(DocReadWrite.Protocol.Objectos);
		}).start();
		new Thread(() -> {
			ServerTCP(DocReadWrite.Protocol.XML);
		}).start();
		new Thread(() -> {
			ServerTCP(DocReadWrite.Protocol.JSON);
		}).start();
		new Thread(() -> {
			ServerUDP();
		}).start();
	}

	/**
	 * Inicia o servidor UDP que dá suporte á descoberta
	 * Só aceite protocolo texto (XML ou JSON)
	 * Detecta o protocolo pelo primeiro carater do pedido '{' ou '<'
	 */
	public static void ServerUDP() {// 
		final int DIM_BUFFER  = 64*1024;
		final int DEFAULT_PORT = DocReadWrite.Portos.get(DocReadWrite.Protocol.Broadcast);
		DocReadWrite.Protocol protocol = null;  // detectado pelo '{' ou '<'
        try (
        		// Cria socket - UDP no porto indicado (well-known port)
                // Este construtor pode gerar uma excepção SocketException o que significa que ou
                // existe outro programa a utilizar o porto pretendido ou o socket está a ser 
                // associado a um porto entre 1 e 1023 sem privilégios de administrador 
                // (como, por exemplo, no sistema UNIX)
        		DatagramSocket sockfd = new DatagramSocket(DEFAULT_PORT);
        		)
        { 
            // Cria um datagramaPacket para recepção
            byte inputBuffer[]  = new byte[DIM_BUFFER];
            DatagramPacket inputPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

            for ( ; ; ) {
                try {
                    System.out.println("Servidor UDP aguarda recepção no porto " + DEFAULT_PORT+"...");

                    // Recepção de um datagrama
                    inputPacket.setLength(DIM_BUFFER);
                    sockfd.receive(inputPacket);

                    String strRequest = new String(inputPacket.getData(), 0, inputPacket.getLength());
                    if(strRequest.charAt(0)=='{')
                    	protocol = DocReadWrite.Protocol.JSON;
                    else if(strRequest.charAt(0)=='<')
                    	protocol = DocReadWrite.Protocol.XML;
                    	else
                    		continue;
                    
                    System.out.println("Número de bytes recebidos: " + inputPacket.getLength());
                    System.out.println("Endereço do cliente: " + inputPacket.getAddress()
                            + " Porto: " + inputPacket.getPort());
                    
                    Document request=null;
					try {
						if(protocol == DocReadWrite.Protocol.XML)
							request = DocReadWrite.documentFromXML(strRequest);
						else if(protocol == DocReadWrite.Protocol.JSON)
							try {
								request = DocReadWrite.documentFromJSON(strRequest);
							} catch (JSONException e) {
								System.err.println("Aviso: "+e.getMessage());
								// e.printStackTrace();
							}
							else
								continue;
	                    Document reply = ServidorDedicado.processar(request);
	                 // Criar um datagrama para enviar a resposta
	                    String strReply=null;
	                    if(protocol == DocReadWrite.Protocol.XML)
	                    	strReply = DocReadWrite.documentToXML(reply);
	                    else  if(protocol == DocReadWrite.Protocol.JSON)
							try {
								strReply = DocReadWrite.documentToJSON(reply);
							} catch (JSONException e) {
								System.err.println("Aviso: "+e.getMessage());
								e.printStackTrace();
							}
	                    else
	                    	continue;
		                byte[] bytesReply = strReply.getBytes();
		                DatagramPacket outputPacket = new DatagramPacket(bytesReply, bytesReply.length, 
		                inputPacket.getAddress(), inputPacket.getPort());

		                // Enviar datagrama de resposta 
		                sockfd.send(outputPacket);
		                
					} catch (TransformerFactoryConfigurationError | TransformerException | ParserConfigurationException | SAXException e) {
						e.printStackTrace();
					}
                } 
                catch (IOException e) {
                    System.err.println("Erro nas comunicações: " + e.getMessage());
                }
            } // end for
        } 
        catch (SocketException e) {
            System.err.println("Erro na criação do socket: " + e.getMessage());
        }
    } // servidor UDP
	/**
	 * Inicia o servidor principal com o protocolo indicado
	 * 
	 * @param port
	 * @param protocolDocReadWrite.Protocol protocol
	 */
	public static void ServerTCP(DocReadWrite.Protocol protocol) {
		ServerSocket serverSocket = null;
		int port = DocReadWrite.Portos.get(protocol);

		try {
			serverSocket = new ServerSocket(port);

			Socket newSock = null;

			for (;;) {
				System.out.println("Servidor TCP aguarda ligacao no porto " + port + "...");

				// Espera estabelecimento do circuito virtual com o cliente
				newSock = serverSocket.accept();

				Thread th = new ServidorDedicado(newSock, protocol);
				th.start();
			}
		} catch (Exception e) {
			System.err.println("Excepção no servidor: " + e);
		}
	}
} // end Servidor principal
