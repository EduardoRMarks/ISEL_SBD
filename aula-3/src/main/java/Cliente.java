
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Prof. P. Filipe
 *
 */
public class Cliente {
	DocReadWrite.Protocol protocol = DocReadWrite.Protocol.XML;
	// poder ser alterado se houver alteração do servidor por omissão

	int DEFAULT_PORT = DocReadWrite.Portos.get(protocol);
	// poder ser alterado se houver alteração do servidor por omissão

	final String DEFAULT_HOSTNAME = "localhost";  // servidor por omissão

	private Socket sock = ligar(DEFAULT_HOSTNAME,DEFAULT_PORT); // mantem-se ligado durante a existencia do cliente

	// lista de servidores a contactar na mesma rede
	List<String> Servers = Arrays.asList("95.93.120.44","192.168.1.2","192.168.1.253","localhost"); 
	// configuração do protocolo de descoberta
	final DocReadWrite.Protocol PROTO = DocReadWrite.Protocol.Objectos;
	final int PORT = DocReadWrite.Portos.get(PROTO);

	/**
	 * Substitui a ligação atual, que é fechada caso exista, por outra
	 * 
	 * @param host
	 * @return
	 */
	private boolean reLigar(String host, int porto) {
		Socket aux = ligar(host,porto);
		if (aux == null || !aux.isConnected())
			return false;
		if (sock != null && sock.isConnected()) 
			try {
			    	sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		sock = aux;
		return true;
	}

	/**
	 * Indica se o servidor está disponivel
	 * 
	 * @return
	 */
	public boolean avaiable(String host) {
		InetAddress server = null;
		try {
			server = InetAddress.getByName(host);
			return server.isReachable(150);
		} catch (IOException e) {
			System.out.println("O servidor " + host + " não está disponível");
			// e.printStackTrace();
		}
		return false;
	}

	/**
	 * Indica se o servidor está presente
	 * 
	 * @return
	 */
	public boolean ligado() {
		if (sock == null || !sock.isConnected() || sock.isClosed())
			return false;
		try {
			return sock.getInetAddress().isReachable(150);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Efectua ligação á máquina indicada em argumento usando o porto indicado
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	public Socket ligar(String host, int port) {
		if (!avaiable(host))  // se faltar a ligação demora menos tempo
			return null;
		Socket sckt = null;
		try {
			sckt = new Socket(host, port);

		} catch (Exception e) {
			System.err.println("Não conseguiu fazer a ligação: " + e.getMessage());
		}
		return sckt;
	}

	/**
	 * Efectua ligação á máquina indicada em argumento usando o porto por omissão
	 * 
	 * @param host
	 * @return
	 */
	public Socket ligar(String host) {
		return ligar(host, DEFAULT_PORT);
	}

	/**
	 * menu de desenvolvimento
	 */
	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			if (ligado())
				System.out.println(
						"*** Menu Cliente (" + sock.getInetAddress().getCanonicalHostName() + ">>" + sock + ") ***");
			else
				System.out.println("*** Menu Cliente (sem ligação) ***");

			System.out.println(
					"1 – Listar (Consultar) os recursos disponíveis (no servidor) apresentando o seu titulo (foto ou poema)");
			System.out.println(
					"2 – Consultar (Descarregar, Visualizar) recursos (1 ou mais, no servidor) identificados pelo seu título");
			System.out.println("3 – Submeter (Carregar) um recurso existente num ficheiro local");
			System.out.println("4 – Obter os poemas (no servidor) que incluam um conjunto de palavras");
			System.out.println("5 – Mudar de servidor");
			System.out.println("6 – Consultar recursos (1 ou mais, nos servidores) identificados pelo seu título");
			System.out.println("0 - Terminar!");
			if (ligado()) {
				String str = sc.nextLine();
				if (!ligado()) // pode ter caido o servidor
					op = '5';
				else {
					if (str != null && str.length() > 0)
						op = str.charAt(0);
					else
						op = ' ';
				}
			} else
				op = '5'; // outros servidores!
			switch (op) {
			case '1':
				try {
					ArrayList<String> titulos = listar();
					for (int i = 0; i < titulos.size(); i++)
						System.out.println("\t" + (i + 1) + " - " + titulos.get(i));
				} catch (ClassNotFoundException | IOException e2) {
					e2.printStackTrace();
				}
				break;
			case '2':
				String titulo = null;
				try {
					titulo = perguntar(listar(), sc);
				} catch (ClassNotFoundException | IOException e) {
					System.out.println("\nFalhou acesso ao servidor: " + e.getLocalizedMessage());
					break;
				}
				if (titulo == null)
					break;
				System.out.println("Título indicado: " + titulo);
				// obtem a lista de fotos e poemas
				try {
					show(consultar(titulo));
				} catch (ClassNotFoundException | IOException e1) {
					e1.printStackTrace();
				}
				break;
			case '3':
				// indica simplesmente os nomes do ficheiros com recursos
				System.out.println("Submissão de um recurso.\n");
				System.out.println("Lista de ficheiros em \"" + Poema.poemas + "\":");
				ArrayList<String> todos = new ArrayList<String>();
				int conta = 0;
				for (String s : DocReadWrite.getFiles(Poema.poemas)) {
					System.out.println("\t" + (++conta) + " - " + s);
					todos.add(s);
				}
				System.out.println("Lista de ficheiros em \"" + Foto.fotos + "\":");
				for (String s : DocReadWrite.getFiles(Foto.fotos)) {
					System.out.println("\t" + (++conta) + " - " + s);
					todos.add(s);
				}
				int pos = 0;
				do {
					System.out.println("\nIndique o número [1.." + todos.size() + "] associado ao ficheiro:");
					pos = sc.nextInt();
					sc.nextLine();
				} while (pos < 1 || pos > todos.size());
				String recFileName = todos.get(pos - 1);
				if (submeter(recFileName))
					System.out.println("\nSubmissão realizada com sucesso!");
				else
					System.out.println("\nFalhou a submissão!");
				break;
			case '4':
				System.out.println("Obter os poemas que incluam um conjunto de palavras.");
				System.out.println("Indique as palavras: ");
				ArrayList<String> palList = new ArrayList<String>();
				String pv = "";
				do {
					pv = sc.nextLine();
					if (pv.compareTo("") == 0)
						break;
					palList.add(pv);
					System.out.println("Indique outra palavra ou <enter> para terminar:");
				} while (true);
				String[] palArray = palList.toArray(new String[palList.size()]);
				NodeList P;
				try {
					P = obter(palArray);
					if (P.getLength() == 0)
						System.out.println("\nNão existe nenhum poema com todas as palavras indicadas!");
					else {
						System.out.println("\nPoemas:\n");
						for (int i = 0; i < P.getLength(); i++) {
							System.out.println("**********************************");
							new Poema((Element) P.item(i)).apresenta();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case '5':
				Hashtable<String, String> lista = listAll();
				Set<String> values = new HashSet<String>();
				if (lista.size() == 0) {
					System.out.println("Não existem servidores na rede local!\n");
					op = '0'; // sai da aplicação
					break;
				}
				System.out.println("Lista de endereços IP dos servidores na rede local:\n");
				values.addAll(lista.values()); // remove os duplicados
				int count = 0;
				for (String value : values) {
					Set<String> keys = lista.keySet();
					Iterator<String> itr = keys.iterator();
					while (itr.hasNext()) {
						String ip = lista.get(itr.next());
						if (ip.compareToIgnoreCase(value) == 0)
							count++;
					}
					if (avaiable(value)) // podem entretanto ter-se desligado algum servidor
						System.out.println("Endereço IP \"" + value + "\" com " + count + " recursos diferentes!");
					else
						System.out.println("O servidor com endereço IP \"" +value+"\" deixou de responder!");
					count = 0;
				}
				System.out.println("\nIndique o endereço IP para ligar ou <enter> para sair:\n");
				String endereco = sc.nextLine();
				if (endereco.length() == 0) {
					if (!ligado())
						op = '0'; // saí do while
					break;
				}
				if (values.contains(endereco) && reLigar(endereco,PORT)) { 
					// muda para o porto aberto e para o protocolo está disponivel
					protocol=PROTO;
					DEFAULT_PORT=PORT;
					System.out.println("\nSucesso na ligação ao servidor com endereço IP \"" + endereco + "\"!\n");
				}
				else
					System.out.println("\nEndereço IP \"" + endereco + "\" inválido!\n");
				break;
			case '6':
				System.out.println("\nListar ... ");
				Hashtable<String, String> recursos = listAll();
				if (recursos.size() == 0) {
					System.out.println("Não existem recursos na redel!\n");
					op = '0'; // sai da aplicação
					break;
				}
				System.out.println("\nLista com " + recursos.size() + " títulos disponiveis:\n");
				Set<String> keys = recursos.keySet(); // filtra os duplicados
				List<String> myList = new ArrayList<String>();
				myList.addAll(keys);
				Collections.sort(myList); // ordena
				for (int i = 0; i < myList.size(); i++)
					System.out.println("\t" + (i + 1) + " - " + myList.get(i));
				int num;
				do {
					System.out.print("Indique o número associado ao título: ");
					num = sc.nextInt();
					sc.nextLine();
				} while (num < 1 || num > recursos.size());
				String title = myList.get(num - 1);
				System.out.println("Título indicado: " + title);
				Set<String> valores = new HashSet<String>();
				valores.addAll(recursos.values());
				for (String ip : valores) {  // pede a todos os servidores
					System.out.println("\n >>>> Endereço IP \"" + ip + "\" do servidor!");
					Socket skt = ligar(ip, PORT);
					// obtem a lista de fotos e poemas
					try {
						show(consultar(title, skt, PROTO));
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					if (skt.isConnected()) {
						try {
							skt.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			case '0':
				break;
			default:
				System.out.println("Opção (" + op + ") inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		try {
			if (sock != null && sock.isConnected())
				sock.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Terminou a execução!");
		System.exit(0);
	}

	// utilizador para enviar o pedido para o servidor e obter a respsta

	private Document executar(Document request, Socket sock) throws IOException, ClassNotFoundException {
		return executar(request, sock, protocol);
	}
	// utilizador para enviar o pedido para o servidor e obter a respsta

	private Document executar(Document request, Socket sock, DocReadWrite.Protocol prot)
			throws IOException, ClassNotFoundException {
		DocReadWrite.documentToNetwork(request, sock, prot); // envia o pedido
		Document x = DocReadWrite.documentFromNetwork(sock, prot); // obtém resposta
		if (x == null)
			throw new IOException("\n\tAviso: Sem resposta!");
		return x;
	}

	/**
	 * Visualiza os recursos indicados
	 * 
	 * @param recursos
	 */
	private void show(ArrayList<Object> recursos) {
		if (recursos == null || recursos.size() == 0)
			System.out.println("\nNão existem recursos (foto ou poema) com o título indicado!");
		else {
			System.out.println("\n(" + recursos.size() + ") Recurso(s) com o título indicado:\n");
			for (int i = 0; i < recursos.size(); i++) {
				Object recurso = recursos.get(i);
				if (recurso instanceof Poema) {
					System.out.println("************** POEMA **************");
					((Poema) recurso).apresenta();
				}
				if (recurso instanceof Foto) {
					System.out.println("************** FOTO **************");
					((Foto) recurso).show();
				}
				// aqui podem existir nós de texto ou de outros tipos que são ignorados
			}
		}
	}

	/**
	 * Pergunta pelo título ao utilizador
	 * 
	 * @param titulos
	 * @param sc
	 * @return
	 */
	private String perguntar(ArrayList<String> titulos, Scanner sc) {
		if (titulos == null || titulos.size() == 0)
			return null;
		System.out.println("\nLista dos títulos de recursos disponiveis no servidor:");
		for (int i = 0; i < titulos.size(); i++)
			System.out.println("\t" + (i + 1) + " - " + titulos.get(i));
		int num;
		do {
			System.out.print("Indique o número associado ao título: ");
			num = sc.nextInt();
			sc.nextLine();
		} while (num < 1 || num > titulos.size());
		return titulos.get(num - 1);
	}

	/**
	 * Transforma a lista de descritores num array ordenado de titulos sem
	 * duplicados
	 * 
	 * @param list
	 * @return
	 */
	private ArrayList<String> getTitulos(NodeList descritores) {
		if (descritores.getLength() == 0) {
			System.out.println("\nNão existe nenhum recurso (foto ou poema)!");
			return null;
		}
		ArrayList<String> lista = new ArrayList<String>();
		for (int i = 0; i < descritores.getLength(); i++) {
			Element desc = (Element) descritores.item(i);
			String titulo = desc.getAttribute("título");
			if (titulo.length() == 0) {// quando vem do JSON só devolve elementos
				titulo = desc.getElementsByTagName("título").item(0).getTextContent();
			}
			lista.add(titulo);
		}
		Collections.sort(lista); // ordena
		String antes = "";
		ArrayList<String> titulos = new ArrayList<String>();
		for (int i = 0; i < lista.size(); i++) { // remove duplicados
			if (antes.compareTo(lista.get(i)) != 0)
				titulos.add(lista.get(i));
			antes = lista.get(i);
		}
		return titulos;
	}

	/**
	 * devolve a lista dos descritores dos recursos existentes num servidor
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private ArrayList<String> listar(Socket sck, DocReadWrite.Protocol prot)
			throws ClassNotFoundException, IOException {
		CliStub cmd = new CliStub();
		Document request = cmd.listar();
		Document reply = executar(request, sck, prot); // obtém a lista o servidor
		return getTitulos(reply.getElementsByTagName("descritor"));
	}

	private ArrayList<String> listar() throws ClassNotFoundException, IOException {
		return listar(sock, protocol); // usa a ligação existente
	}

	/**
	 * Acrescenta recursos, ainda não incluidos, que existam em servidores
	 * especificos
	 * 
	 * @param fontes
	 * @return
	 */
	private Hashtable<String, String> joinServers(Hashtable<String, String> fontes, List<String> servers) {
		for (String server : servers) {
			try {
				System.out.println("Vai ligar ao servidor com endereço IP \"" + server+"\" via porto "+PORT+"...");
				Socket skt = ligar(server, PORT); // em conformidade com o protocolo de objectos seriados
				if (skt != null && skt.isConnected()) {
					try {
						System.out.println("Ligou ao servidor: " + server);
						ArrayList<String> titulos = listar(skt, PROTO);
						for (String titulo : titulos) {
							fontes.putIfAbsent(titulo, server);
							// não acrescenta repetidos
							// se o servidor tiver exatamente os mesmos recursos não aparece
						}
						System.out.println("\tObteve " + titulos.size() + " total " + fontes.size() + " títulos...");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					if (skt != null)
						skt.close();
				} else
					System.out.println("O servidor \"" + server + "\" não está disponível!");
			} catch (IOException e) {
				// e.printStackTrace();
				// não se passa nada avança na lista
			}
		}
		return fontes;
	}

	/**
	 * Implementa o protocolo de descoberta fazendo broadcast na rede
	 * 
	 * @return - titulos (key) dos recursos e respetivos endereços IP (value)
	 */
	private Hashtable<String, String> listAll() {
		final int DIM_BUFFER = 64 * 1024;

		final String DEFAULT_HOST = "255.255.255.255"; // endereço de broadcast

		final int DEFAULT_PORT = DocReadWrite.Portos.get(DocReadWrite.Protocol.Broadcast);

		final int SoTimeout = 1 * 1000; // milisegundos
		final int numeroTentativas = 2;

		Hashtable<String, String> fontes = new Hashtable<String, String>();

		try (DatagramSocket socket = new DatagramSocket();
		// Cria socket - UDP com um porto atribuído dinamicamente pelo sistema
		// (anonymous port)
		)

		{
			// vai acrescentar os dados dos servidores fixos contatados via IP
			fontes = joinServers(fontes, Servers);

			socket.setBroadcast(true); // IMPORTANTE: socket de broadcast
			
			// constroi mensagem
			String userInput = "<?xml version='1.0' encoding='ISO-8859-1'?><protocol><listar><request/><reply/></listar></protocol>";

			// Cria um datagrama para envio
			DatagramPacket outputPacket = new DatagramPacket(userInput.getBytes(), userInput.length(),
					InetAddress.getByName(DEFAULT_HOST), DEFAULT_PORT);
			// Criar datagrama para recepção
			byte[] buf = new byte[DIM_BUFFER];
			DatagramPacket inputPacket = new DatagramPacket(buf, buf.length);
			int tentativas = 0;
			do { // tentativas
					// --- Envia pedido ---
					// System.out.println(" --- Envia pedido ---");
					
					socket.send(outputPacket);

				// --- Recebe resposta ---

				socket.setSoTimeout(SoTimeout); // set the timeout in millisecounds.

				try {
					while (true) { // repete leitura das respostas
									// que podem ser repetidas
						// espera pela resposta
						socket.receive(inputPacket); // recieve data until timeout

						// Mostra Resposta
						String received = new String(inputPacket.getData(), 0, inputPacket.getLength());
						/*
						 * System.out.println("Dados recebidos (" + inputPacket.getAddress() + ", " +
						 * inputPacket.getPort() + "):\n ");
						 */
						try {
							String IP = inputPacket.getAddress().toString().substring(1);
							Document reply = DocReadWrite.documentFromXML(received);
							ArrayList<String> titulos = getTitulos(reply.getElementsByTagName("descritor"));
							for (String titulo : titulos) {
								// System.out.println("\t"+titulo);
								fontes.putIfAbsent(titulo, IP); // não mete repetidos
								// se o servidor tiver exatamente os mesmos recursos não aparece
							}
						} catch (ParserConfigurationException | SAXException e) {
							e.printStackTrace();
						}
					}
				} catch (SocketTimeoutException e) {
					tentativas++;
					// timeout exception.
					// System.out.println("("+tentativas+")Timeout reached!!! ");
				}
			} while (tentativas < numeroTentativas);
			// System.out.println("Fim da lista!");
		} // end try
		catch (UnknownHostException e) {
			System.err.println("Servidor " + DEFAULT_HOST + " não foi encontrado");
		} catch (SocketException e) {
			System.err.println("Erro na criação do socket: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Erro nas comunicações: " + e);
		}
		return fontes;
	}

	/**
	 * Devolve a lista dos recursos que têm o título indicado
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private ArrayList<Object> consultar(String titulo, Socket sck, DocReadWrite.Protocol prot)
			throws ClassNotFoundException, IOException {
		CliStub cmd = new CliStub();
		Document request = cmd.consultar(titulo);
		Document reply = executar(request, sck, prot);
		NodeList recursos = reply.getElementsByTagName("reply").item(0).getChildNodes();
		ArrayList<Object> objectos = new ArrayList<Object>();
		for (int i = 0; i < recursos.getLength(); i++) {
			Node recurso = recursos.item(i);
			if (recurso.getNodeName().compareToIgnoreCase("poema") == 0) {
				objectos.add(new Poema((Element) recurso));
			} else if (recurso.getNodeName().compareToIgnoreCase("fotografia") == 0) {
				objectos.add(new Foto((Element) recurso));
			}
			// aqui podem existir nós de texto ou de outros tipos que são ignorados
		}
		return objectos;
	}

	private ArrayList<Object> consultar(String titulo) throws ClassNotFoundException, IOException {
		return consultar(titulo, sock, protocol);
	}

	/**
	 * Devolve a lista dos poemas que têm as palavras indicadas
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private NodeList obter(String[] palavras) throws ClassNotFoundException, IOException {
		CliStub cmd = new CliStub();
		Document request = cmd.obter(palavras);
		Document reply = executar(request, sock);
		return reply.getElementsByTagName("poema");
	}

	/**
	 * Submete o recurso indicdo em parametro
	 * 
	 * @param recusos
	 * @return
	 */
	private boolean submeter(Object recurso) {
		Document request = null;
		CliStub cmd = new CliStub();
		if (recurso instanceof Poema) {
			Poema p = (Poema) recurso;
			if (p.validar()) {
				System.out.println("Validação do poema realizada com sucesso!\n");
				p.addComment("\nSubmitted by (" + sock.getLocalAddress() + "/" + sock + ")\n" + "\nFrom file ("
						+ p.ficheiro + ") at " + LocalDateTime.now());
				p.apresenta();
			} else {
				System.out.println("Falhou a validação!");
				return false;
			}
			request = cmd.submeter(p);
		}
		request = cmd.submeter(recurso);
		Document reply;
		try {
			reply = executar(request, sock);
		} catch (ClassNotFoundException | IOException e) {
			// e.printStackTrace();
			return false;
		}
		NodeList S = reply.getElementsByTagName("sucesso");
		return S.getLength() == 1;
	}

	/**
	 * Devolve sucesso ou erro em resultado da submissão
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private boolean submeter(String recFileName) {
		System.out.println("Vai submeter: " + recFileName);
		if (recFileName.endsWith(".xml")) // averigua se é poema
			return submeter(new Poema(recFileName));
		else
			return submeter(new Foto(recFileName));
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Cliente().menu();
	} // end main

} // end ClienteTCP
