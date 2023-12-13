
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author PFilipe
 *
 */
public class ClientePoema {

	private Socket sock = ligar(); // mantem-se ligado durante a existencia do cliente

	public Socket ligar() {

		final String DEFAULT_HOSTNAME = "localhost"; // "95.94.149.133";
		final int DEFAULT_PORT = 5026; // 80;
		// protocolo baseado em XML
		
		Socket socket = null;
		try {
			socket = new Socket(DEFAULT_HOSTNAME, DEFAULT_PORT);

		} catch (Exception e) {
			System.err.println("Erro desconhecido: " + e.getMessage());
		}
		return socket;
	}

	/* pede o titulo ao utilizador */
	private String getTitulo(Scanner sc) throws ClassNotFoundException, IOException {
		System.out.println("\nLista de títulos:");
		NodeList titulos = null;
		titulos = listar();  // vai perguntar ao servidor
		// obtem a lista de títulos
		if (titulos.getLength() == 0) {
			System.out.println("\nNão existe nenhum poema!");
			return null;
		}
		for (int i = 0; i < titulos.getLength(); i++)
			System.out.println((i + 1) + " (" + titulos.item(i).getTextContent()+")");
		int num;
		do {
			System.out.print("Indique o número associado ao título: ");
			num = sc.nextInt();
			sc.nextLine();
		} while (num < 1 || num > titulos.getLength());
		return titulos.item(num - 1).getTextContent();
	}
	/**
	 * 
	 */
	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println(
					"*** Menu Cliente (" + sock.getInetAddress().getCanonicalHostName() + ">>" + sock + ") ***");
			System.out.println("1 – Consultar poemas dado o seu título.");
			System.out.println("2 – Obter os poemas que incluam um conjunto de palavras.");
			System.out.println("3 – Submeter um poema.");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				System.out.println("Consultar um poema dado o seu título.");
				NodeList L = null;
				try {
					String titulo = getTitulo(sc);
					if(titulo!=null)
						L = consultar(titulo);
					if (L==null || L.getLength() == 0)
						System.out.println("\nNão existe nenhum poema!");
					else {
						System.out.println("\n"+L.getLength()+" Poema(s) com o título '"+titulo+"':\n");
						for (int i = 0; i < L.getLength(); i++) // pode existir mais do que um poema?
							new Poema((Element) L.item(i)).apresenta();
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case '2':
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
			case '3':
				/*
				// indica simplesmente o nome do ficheiro que inclui o poema
				System.out.println("Submissão de um poema.\n");
				System.out.println("Lista de ficheiros em \"" + Poema.poemas+"\":");
				Arrays.stream(XMLReadWrite.getFiles(Poema.poemas)).forEach(System.out::println);
				System.out.println("\nIndique o nome do ficheiro (ex: poema.xml):");
				String poemaFileName = sc.nextLine();
				if(submeter(poemaFileName))
					System.out.println("\nSubmissão realizada com sucesso!");
				else
					System.out.println("\nFalhou a submissão!");
				break;*/
				System.out.println("Submissão de um poema.\n");
				try {
					String titulo = getTitulo(sc);
					if (titulo != null) {
						ArrayList<String> poes = Poema.getFicheiros(titulo);
						for (int i = 0; i < poes.size(); i++)
							if (submeter(poes.get(i)))
								System.out.println("\nSubmissão realizada com sucesso!");
							else
								System.out.println("\nErro na submissão!");
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				break;
			case '0':
				break;
			default:
				System.out.println("Opção (" + (int) op + ")inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}

	// envia o pedido para o servidor e recebe a resposta
	private Document executar(Document request, Socket sock) throws IOException, ClassNotFoundException {
		// envia o pedido
		DocReadWrite.documentToNetwork(request, sock);
		// obtém resposta
		return DocReadWrite.documentFromNetwork(sock);
	}

	/**
	 * Devolve a lista dos títulos dos poemas
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private NodeList listar() throws ClassNotFoundException, IOException {
		CliStub cmd = new CliStub();
		Document request = cmd.listar();
		Document reply = executar(request, sock);
		return reply.getElementsByTagName("título");
	}

	/**
	 * Devolve a lista dos poemas que têm o título indicado
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private NodeList consultar(String titulo) throws ClassNotFoundException, IOException {
		CliStub cmd = new CliStub();
		Document request = cmd.consultar(titulo);
		Document reply = executar(request, sock);
		return reply.getElementsByTagName("poema");
	}

	/**
	 * Devolve a lista dos poemas que têm as palavras indicadas
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
	 * Devolve sucesso ou erro em resultado da submissão
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	private boolean submeter(String poemaFileName) throws ClassNotFoundException, IOException {
		Poema p = new Poema(poemaFileName);
		Element pm = (Element) p.DOMDoc().getElementsByTagName("poema").item(0);
		if (pm.hasAttribute("xmlns:xsi"))
			pm.removeAttribute("xmlns:xsi");
		if (pm.hasAttribute("xsi:noNamespaceSchemaLocation"))
			pm.removeAttribute("xsi:noNamespaceSchemaLocation");

		if (p.validar()) {
			System.out.println("Validação do poema realizada com sucesso!\n");
			p.addComment("\nSubmitted by (" + sock.getLocalAddress() + "/" + sock + ")\n" + "\nFrom file ("
					+ poemaFileName + ") at " + LocalDateTime.now());
			p.apresenta();
		} else {
			System.out.println("Falhou a validação!");
			return false;
		}

		CliStub cmd = new CliStub();
		Document request = cmd.submeter(p);

		Document reply = executar(request, sock);
		
		NodeList S = reply.getElementsByTagName("sucesso");
		return S.getLength() == 1;
	}

	public static void main(String[] args) {

		new ClientePoema().menu();

	} // end main

} // end ClienteTCP
