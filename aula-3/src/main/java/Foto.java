import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Engº Porfírio Filipe
 * 
 */
@SuppressWarnings("serial")
public class Foto implements Serializable {
	public final static String fotos = DocXML.getContext() + "/fotos/";
	MyFile descriptor = null; // descreve o ficheiro
	byte[] content = null; // conteudo em base64
	String titulo = null; // título, por omissão nome ficheiro
	String autor = genName(); // nome do fotografo ou do criador/desenhador

	/**
	 * construtor por omissao
	 */
	public Foto() {
		descriptor = new MyFile("isel.jpg");
		titulo = descriptor.getName();
		load();
	}

	/**
	 * construtor de cópia
	 */

	public Foto(Foto f) {
		descriptor = f.descriptor;
		content = f.content;
		titulo = descriptor.getName();
	}

	/**
	 * @param p nome do ficheiro
	 * 
	 */
	public Foto(final String p) {
		descriptor = new MyFile("fotos/"+p);
		titulo = descriptor.getName().toUpperCase();
		load();
	}
	/**
	 * 
	 * @param caminho relativo
	 * @param p nome do ficheiro
	 * 
	 */
	public Foto(String caminho, String p) {
		if(caminho==null)
			return;
		if(caminho.length()>0)
			caminho = caminho+"/";
		descriptor = new MyFile(caminho+p);
		titulo = descriptor.getName().toUpperCase();
		load();
	}
	// cria uma foto a partir de um elemento XML
	public Foto(Element ft) {
		descriptor = new MyFile(((Element)ft.getElementsByTagName("ficheiro").item(0)).getTextContent());
		titulo = ((Element)ft.getElementsByTagName("título").item(0)).getTextContent();
		autor =  ((Element)ft.getElementsByTagName("autor").item(0)).getTextContent();
		setStrContent(((Element)ft.getElementsByTagName("conteudo").item(0)).getTextContent());
	}
	// devolve um elemento que contem a foto em xml
	public Element getElement() {
		try {
			String fs = getXML();
			Document fd = DocReadWrite.documentFromXML(fs);
			return fd.getDocumentElement();
					//(Element) (fd.getElementsByTagName("fotografia").item(0));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Devolve o um nome aleatório
	 */
	public String genName() {
		final Random random = new Random();
		String[] nomes = { "VW5nb3duZWQgQ29oZXJlbmNpZXM=", "RHlzcGxhc3RpYyBSZXRpbm9zY29weQ==", "RnVzdGlhbiBXYWx0b24=",
				"QXV0b3RlbGljIE1hc2xpbnM=", "R2FsdmFuaWMgQ293ZmlzaA==", "UHJlY2lwaXRhdGl2ZSBDdWJpY2FsbmVzcw==",
				"Rm9saWFyIEJvZXJz", "R3VhcmRhYmxlIFN1ZmZlcmFuY2Vz", "VGVlbmllc3QgS2lyc2Nod2Fzc2Vy",
				"RWFydGhseSBMYXNzaWU=", "SG9tZXdhcmQgSG91c2Vob2xk", "SGVsbWludGhvbG9naWMgRWxhdGl2ZQ==",
				"U2VtcGl0ZXJuYWwgTmV3Y2FzdGxl", "Um9zeS1jaGVla2VkIERyYWZ0ZXI=", "Q29ja2FtYW1pZSBSZWZpbmVyeQ==",
				"SW5xdWlsaW5vdXMgS2VlbmluZw==", "QXF1YSBGbGVldA==", "UGVvcGxlZCBEaXNsaWtl",
				"TmV3LWZhc2hpb25lZCBXYWxrd2F5cw==", "SW50aW1pc3QgT2NjaWRlbnRhbGlzbQ==", "T2NocmVvdXMgVHJheW1vYmlsZXM=",
				"QWdvbml6ZWQgVmVudGFnZQ==", "VHJpYW5nbGVkIFR1c3NvcmVz", "QWx0ZXJuYXRpdmUgQWNyb2JhdA==",
				"UHJvZmFuYXRvcnkgVGVzc2l0dXJhcw==", "SW9uaWFuIFBlcmlkb3RpdGU=", "QXN0aXIgTGVpdG1vdGl2cw==",
				"QWVuZW9saXRoaWMgSGFpcidzLWJyZWFkdGg=", "Q29sdWJyaWQgQWxlc3NhbmRyaWE=", "QnVuY2hlZCBTaGVuYW5pZ2Fu",
				"U2VwYXJhdGUgUGFuaWNtb25nZXJz", "U3BvdXRlZCBJbnRlbGxlY3R1YWxpc3Q=",
				"U2ltYXJvdWJhY2VvdXMgUGFuc29waGlzdHM=", "QWNjZWxlcmF0ZWQgQnJ1dGFsaXRpZXM=",
				"SHVzaC1odXNoIEJhY2toYW5kZXI=", "QWxsZWdvcmljYWwgUmVwZXJ0b2lyZXM=", "U2VycmFuaWQgTmlnaHRz",
				"UHJvdmlzaW9uYXJ5IERlbGlucXVlbnRz", "Tm9uY29nbml6YWJsZSBFYmJz", "Q2lyY2FkaWFuIFRva2VuaXNt",
				"SW50cnVkaW5nIFBlbnRvZGVz", "U2l0dWF0ZSBDYXRlcnBpbGxhcnM=", "Q29uc3VtcHRpdmUgUnVjaGVz",
				"UGFyYWxsZWwgVGV1Y3JpYW4=", "UG9yb3NlIEltcGVyaW91c25lc3M=", "SGllcm9jcmF0aWMgSG90ZWxpZXI=",
				"SHlkcm9waHl0aWMgUmVmaW5pbmc=", "TXlydGFjZW91cyBDb3VyYW50cw==", "VXJhbGljIEV2ZW50cmF0aW9u",
				"RGVjaW1hbCBDb3BhcmNlbmVy", "UGV0cmlmaWVkIEdyZWF0LWdyYW5kZGF1Z2h0ZXI=", "RGl2ZXJzaWZpZWQgQnVuZGxpbmc=",
				"RGlzdHJhY3RlZCBTaWxpY2E=", "Q29tbWlzZXJhYmxlIERlbmRyb2JpdW1z", "TWFuaXB1bGFibGUgVm9sdW1lcw==",
				"RGlhbHl6YWJsZSBEaWRlcm90", "VHdpbi1zY3JldyBCdWxsZG9n", "TmFtYnktcGFtYnkgQ3JvY29kaWxpYW5z",
				"UHJvc2ltaWFuIEZyYW5jcw==", "Qm93ZXJ5IFBhZWxsYQ==", "RGVtb25pYyBCaXRtYXBz",
				"SHlwZXJzdGhlbmljIFN0aWZmZW5pbmc=", "UHV0cmVmaWFibGUgR3JhbmRmYXRoZXJz",
				"UGFsYWVhbnRocm9waWMgU25vLWNhdHM=", "Qmxvd2llciBDaG9yZG9waG9uZXM=",
				"UHJlcG9uZGVyYXRpbmcgRGlmZnJhY3RvbWV0ZXJz", "QmlsaW5ndWFsIFRzZXRzZXM=", "VW1iZWxsYXIgSHlncm9zY29wZXM=",
				"TGFwaWRpZmljIExpdHVyZ2lzdHM=", "RnVsbC1mcm9udGFsIEltcGVydGluZW5jeQ==", "VW5pbWFnaW5lZCBCdW5n",
				"VHVuaWNhdGUgSW50ZXJicmFpbg==", "UHVnLW5vc2VkIE1lcmNlcg==", "R3JhdmVzdCBWYWxvcml6YXRpb25z",
				"Q3JhYmJ5IEdhbGljaWE=", "VW5lbWJvZGllZCBEb3R0aW5lc3M=", "U2VnbWVudGF0ZSBDb250dW1lbHk=",
				"RXh0cmVtaXN0IE1hcmNlbGxl", "Q3JhbmUtZmx5IEJhZ2xleQ==", "QWx0ZXJuYW50IEFudG9mYWdhc3Rh",
				"SGVyaXRhYmxlIE5lY3Rhcg==", "Q3lwcmlvdCBBbnRpY3ljbG9uZXM=", "RGVuaWdyYXRpbmcgR3lwcw==",
				"T21uaWZhcmlvdXMgVGhlbmFycw==", "SXJyZXNvbHZhYmxlIEVyZWN0b3I=", "TGVudGlmb3JtIFN0dWRpZXJz",
				"Q3VscGFibGUgUHJvdG96b2E=", "UGFudG9ncmFwaGljYWwgQ2FycG9nb25pdW0=", "R2lsdC1lZGdlZCBOaWRkZXJpbmdz",
				"VHJpY2hyb21lIFNvbHZlbmN5", "VW5wcm92aWRlbnQgT3N0ZW9sb2dpc3Q=", "VW5jaXJjdW1jaXNlZCBDaGxvcm9wbGFzdHM=",
				"U3RvcmFibGUgTW9yaWFydHk=", "UHJlbWF0dXJlIEdvbGRleWU=", "UHJvcG9ydGlvbmxlc3MgQmxpdHo=",
				"RHluYW1vbWV0cmljIFBlY3VsaWFy", "R3JhbmRpbG9xdWVudCBPZWlsbGFkZXM=", "V2FyZGVkIE1pZGRheXM=",
				"SWZmeSBEaXRjaGVz", "TWFocmF0dGEgSW1wYWxhcw==" };
		byte[] auz = Base64.getDecoder().decode(nomes[random.nextInt(nomes.length)]);
		return new String(auz);
	}

	/**
	 * Devolve o título da foto
	 */
	public String getTitulo() {
		return DocXML.properStr(titulo);
	}

	/**
	 * Devolve o autor da foto
	 */
	public String getAutor() {
		return autor;
	}

	/**
	 * carrega a foto a partir do ficheiro
	 * 
	 * @return sucesso
	 */
	public boolean load() {
		String path = descriptor.getPath();
		content = null;
		if (path != null) {
			File fi = new File(path);
			if (fi.exists())
				try {
					content = Files.readAllBytes(fi.toPath());
					return true;
				} catch (final IOException e) {
					// e.printStackTrace();
					System.err.println("Falhou a abertura do ficheiro com o nome '" + path + "'!");
					return false;
				}
			else
				System.err.println("Não encontrou o ficheiro com o nome '" + path + "'!");
		}
		return false;
	}

	/**
	 * Guarda o ficheiro indicado em argumento 
	 * Fazendo backup/rename se o ficheiro já existir
	 * 
	 * @return o nome do ficheiro usado para salvar o conteudo
	 */

	public String save(String relativePath) {
		String dataFile = null;
		String bkFile = null;
		DateFormat Formater = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
		String instante = Formater.format(new Date().getTime()).replaceAll(":", "").replaceAll("/", "").replaceAll(", ",
				"");

		if (relativePath != null) {
			dataFile = MyFile.getContext() + File.separatorChar + relativePath + File.separatorChar + descriptor.getFName();
			bkFile = MyFile.getContext() + File.separatorChar + relativePath + File.separatorChar + descriptor.getName()
					+ instante + "." + descriptor.getExtension();
		} else {
			dataFile = MyFile.getContext() + File.separatorChar + descriptor.getFName();
			bkFile = MyFile.getContext() + File.separatorChar + descriptor.getName() + instante + "."
					+ descriptor.getExtension();
		}
		final File file = new File(dataFile);
		if (file.exists()) {
			if (!file.renameTo(new File(bkFile))) {
				System.err.println("Falhou a alteração do nome '" + dataFile + "' do ficheiro para '" + bkFile + "'!");
				return "";
			} // else {
				// System.out.println("Ficheiro de backup gerado '" + bkFile+
				// "'!");
				// }
		}
		try {
			File f = new File(dataFile);
			Path path = Paths.get(f.getAbsolutePath());
			Files.write(path, content);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return dataFile;
	}

	/**
	 * @return existe
	 */
	public boolean isOk() {
		return content != null && descriptor != null;
	}

	/**
	 * @return mime
	 */
	public String getMime() {
		return descriptor.getMimeType();
	}

	/**
	 * @return caminho
	 */
	public String getPath() {
		return descriptor.getPath();
	}

	/**
	 * @return nome
	 */
	public String getFileName() {
		return descriptor.getName();
	}

	/**
	 * @return conteudo
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * 
	 */
	public void setContent(byte[] c) {
		content = c;
	}

	/**
	 * conversão para string na base 64
	 * 
	 * @return conteudo
	 */

	public String getStrContent() {
		if (isOk())
			// return MyBase64.encode(content);
			return Base64.getEncoder().encodeToString(content);
		return null;
	}

	/**
	 * conversão de base 64 para string
	 * 
	 * @param content
	 */

	public void setStrContent(String content) {
		if (content != null)
			// setContent(myBase64.decode(content));
			setContent(Base64.getDecoder().decode(content));
	}

	/* devolve um String com a foto transformada em XML */
	public String getXML() {
		return "<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>"
				+ "<?xml-stylesheet type='text/xsl' href='imagem-to-html.xsl'?>" + "<fotografia>" + "<título>"
				+ getTitulo() + "</título>" + "<autor>" + getAutor() + "</autor>" + "<tipo>" + getMime()
				+ "</tipo>" + "<ficheiro>" + descriptor.getFName() + "</ficheiro>" + "<conteudo>" + getStrContent()
				+ "</conteudo>" + "</fotografia>";
	}

	// gera os documentos XML e HTML correspondentes
	public void gerar() throws IOException, ParserConfigurationException, SAXException {
		// construir um DOM a partir do xml
		Document D = DocReadWrite.documentFromXML(getXML());
		// validar o DOM com o XSD
		if (DocXML.documentValidationXSD(D, MyFile.getContext() + "/xml/" + "imagem.xsd")) {
			// Escrever o DOM no ficheiro XML
			DocReadWrite.documentToFile(D, MyFile.getContext() + "/fxml/" + descriptor.getName() + ".xml");
			// Aplicar transformação para gerar HTML
			DocXML.documentTransformation(D, MyFile.getContext() + "/xml/" + "imagem-to-html.xsl",
					MyFile.getContext() + "/fxml/" + descriptor.getName() + ".html");
		}
	}

	/**
	 * Mostra os dados da fotografia e visualiza numa frame
	 */
	public void show() {
		System.out.println("Dados da Imagem/Fotografia:");
		if (titulo != null)
			System.out.println("Título: '" + titulo+"'");
		if (autor != null)
			System.out.println("Autor: '" + autor + "'");
		else
			System.out.println("Autor: 'Desconhecido|'");
		if (descriptor != null)
			descriptor.show();
		if (content == null)
			System.out.println("Não existe conteudo em memória!");
		else {
			System.out.println("Dimensão do conteudo em memória: " + content.length + " bytes");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JFrame editorFrame = new JFrame("Imagem/Fotografia: "+titulo);
					//editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					editorFrame.setAlwaysOnTop(true);
					editorFrame.setExtendedState(editorFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
					BufferedImage image = null;
					try {
						image = ImageIO.read(new ByteArrayInputStream(content));
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
					editorFrame.add(new JLabel(new ImageIcon(image)));
					editorFrame.pack();
					editorFrame.setLocationRelativeTo(null);
					editorFrame.setVisible(true);
				}
			});
		}
	}
	
	/**
	 * seriação da fotografia para um socket
	 */
	public void seriar(Socket socket) throws IOException {
		try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());) {
			outputStream.writeObject(this);
		}
	}

	/**
	 * deseriar a partir de um socket
	 * 
	 */

	public void deseriar(Socket socket) throws ClassNotFoundException, IOException {
		try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());) {
			Foto aux = (Foto) inputStream.readObject();
			descriptor = aux.descriptor;
			content = aux.content;
		}
	}

	public void envia(String host, int port) throws UnknownHostException, IOException {
		try (Socket socket = new Socket(host, port);) {
			seriar(socket);
		}
	}

	public void espera(int port) {
		Socket socket = null;
		try (ServerSocket serverSocket = new ServerSocket(port);) {
			socket = serverSocket.accept();
			deseriar(socket);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// devolve os nome dos ficheiros com fotos
	public static ArrayList<String> getFotos() {
		return DocXML.getFiles(MyFile.getContext() + "/fotos");
	}

	// Gerar o indice
	public static boolean geraIndice(String local) {
		return DocReadWrite.documentToFile(getIndice(), local);
	}
	
	public Element getDescritor(Document doc) {
		Element itemElement = doc.createElement("descritor");
		itemElement.setAttribute("tipo", "foto");
		itemElement.setAttribute("ficheiro", descriptor.getFName());
		itemElement.setAttribute("título", getTitulo());
		itemElement.setAttribute("autor", getAutor());
		itemElement.setTextContent("** Descrição/Resumo: "+getTitulo()+" **");
		return itemElement;
	}
	// Devolve DOM com indice que inclui: tipo, ficheiro, titulo e autor, descriçao
	public static Document getIndice() {
		Element items = DocXML.documentNew("indice");
		Document newDoc = items.getOwnerDocument();
		// create a comment node given the specified string
		Comment comment = newDoc.createComment("Lista de Imagens");
		newDoc.insertBefore(comment, items);
		ArrayList<String> fotos = getFotos();
		for (int i = 0; i < fotos.size(); i++) {
			Foto f = new Foto(fotos.get(i));
			items.appendChild(f.getDescritor(newDoc));
		}
		return newDoc;
	}
	
	// Devolve as fotos que tenham um determinado título que pode ser repetido.
	public static ArrayList<Document> consultar(String titulo) {
		ArrayList<Document> docs = new ArrayList<Document>();
		ArrayList<String> fotos = getFotos();
		for (int i = 0; i < fotos.size(); i++) {
			Foto f = new Foto(fotos.get(i));
			if (f.getTitulo().compareToIgnoreCase(titulo)==0) 
				try {
					String fs = f.getXML();
					Document fd = DocReadWrite.documentFromXML(fs);
					docs.add(fd);
				} catch (ParserConfigurationException | SAXException | IOException e) {
					e.printStackTrace();
				}	
		}
		return docs;
	}
	
	private static void exemplo1(Scanner sc) {
		System.out.println("    Lista de fotografias:");
		ArrayList<String> ficheiros = MyFile.listarFicheiros("fotos");
		for (int i = 0; i < ficheiros.size(); i++)
			if (ficheiros.get(i).toLowerCase().endsWith(".jpg") || ficheiros.get(i).toLowerCase().endsWith(".png"))
				System.out.println("    " + (i + 1) + "-" + ficheiros.get(i));
		// visualizar a imagem
		System.out.println("Indique o número da fotografia:\n");
		System.out.println("\n>> ");
		int pos = sc.nextInt();
		sc.nextLine();
		Foto f10 = new Foto(ficheiros.get(pos - 1));
		f10.show();
	}

	/* Teste do servidor seriação */
	/* guarda a fotografia recebida na pasta 'down' */
	private static void exemplo2(int port) {
		System.out.println("Aguarda por fotografia no porto:" + port);
		new Thread(() -> {
			Foto f10 = new Foto();
			f10.espera(port);
			System.out.println("Guarda fotografia em:" + f10.save("down"));
			f10.show();
		}).start();
	}

	/* teste cliente seriação */
	private static void exemplo3(Scanner sc, String host, int port) {
		System.out.println("Indique o nome do ficheiro (em " + MyFile.getContext() + "/fotos ) com a foto:\n");
		System.out.println("\n>> ");
		String FileName = sc.nextLine();
		System.out.println("Indique o endereço IP ou nome (" + host + ") do servidor:\n");
		System.out.println("\n>> ");
		String HostName = sc.nextLine();
		if (HostName.length() == 0)
			HostName = host;
		Foto f10 = new Foto(FileName);
		try {
			f10.envia(HostName, port);
			System.out.println("Enviou fotografia para o servidor em " + host + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void exemplo4(Scanner sc) {
		System.out.println("    Lista de fotografias:");
		ArrayList<String> ficheiros = MyFile.listarFicheiros("fotos");
		for (int i = 0; i < ficheiros.size(); i++)
			if (ficheiros.get(i).toLowerCase().endsWith(".jpg") || ficheiros.get(i).toLowerCase().endsWith(".png"))
				System.out.println("    " + (i + 1) + "-" + ficheiros.get(i));
		// visualizar a imagem
		System.out.println("Indique o número da fotografia:\n");
		System.out.println("\n>> ");
		int pos = sc.nextInt();
		sc.nextLine();
		Foto f10 = new Foto(ficheiros.get(pos - 1));
		f10.show();
		try {
			f10.gerar();
			System.out.println("XML&HTML da fotografia \""+ficheiros.get(pos - 1)+"\" gerados para 'fxml' com sucesso!\n");
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	public static void menu(String host, int port) {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Menu da Fotografia ***");
			System.out.println("1 – Visualizar fotografia");
			System.out.println("2 – Espera fotografia");
			System.out.println("3 – Envia fotografia");
			System.out.println("A – Gera XML&HTML da fotografia");
			System.out.println("B – Descarregar fotografia de URL");
			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.toUpperCase().charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				exemplo1(sc);
				break;
			case '2':
				exemplo2(port);
				break;
			case '3':
				exemplo3(sc, host, port);
				break;
			case 'A':
				exemplo4(sc);
				break;
			case 'B':
				try {
		    		// http://www.patrimoniocultural.gov.pt/static//img/monumeu_dgpc.jpg
		    		// Descarrega em http e https não tem problema!
		    		String fich=DocReadWrite.download("https://isel.pt/media/assets/default/images/logo-isel.png","down");
					//download("http://www.cm-lisboa.pt/typo3temp/pics/6d92da7172.jpg",contexto+"/down");
		    		Foto f2 = new Foto("down",fich);
		    		f2.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case '0':
				break;
			default:
				System.out.println("Opção inválida, esolha uma opção do menu.");
			}
		} while (op != '0');
		sc.close();
		System.out.println("Terminou a execução.");
		System.exit(0);
	}

	@SuppressWarnings("unused")
	private static void tstBase64() {

		Foto f10 = new Foto("isel.jpg");

		String txt = f10.getStrContent(); // obtem o conteudo em base64;
		System.out.println("Texto em base 64: " + txt.length());
		Foto f3 = new Foto("down/isel2.jpg");
		f3.setStrContent(txt); // define a nova foto a partir de string em base64;
		f3.save("down"); // guarda no ficheiro
		Foto f4 = new Foto("down/isel2.jpg");
		f4.show();
		f4.getXML();
	}

	public static void main(final String[] args) {
		// tstBase64();
		String host = "localhost"; // endereço ou nome do servidor
		int port = 5025; // porto
		menu(host, port);
	}
}
