import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Classe para suporte aos protocolos de comunicação com documentos XML ou JSON
 * @author Prof. P. Filipe
 *
 */
public final class DocReadWrite {
	
	private static Protocol protocol = Protocol.JSON;
	// usado por omissão nos metodos (to/from)Network e (to/from)Socket
	public enum Protocol {Objectos, 	// objectos seriados via TCP
							XML, 		// documentos XML via TCP
							JSON, 		// ficheiros JSON via UDP
							Broadcast	// porto de broadcast para descoberta via UDP
						};
	final public static Hashtable<Protocol,Integer> Portos = new Hashtable<Protocol,Integer>()
	{
		private static final long serialVersionUID = 1L;
			{
		    	put(Protocol.Objectos, 80);  // Convem que esteja aberto na firewall
		    	put(Protocol.XML, 5025);
		    	put(Protocol.JSON, 5026);
		    	put(Protocol.Broadcast, 5025);
		    }
	};


 
	/**
	 * Copia a cadeia de carateres do input para o output
	 * 
	 * @param input  - cadeira de carateres de origem
	 * @param output - cadeira de carateres de destino
	 * @throws IOException
	 */
	public static void copy(InputStream input, OutputStream output) throws IOException {
		int DEFAULT_BUFFER_SIZE = 1024 * 4;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer)))
			output.write(buffer, 0, n);
	}

	/**
	 * Devolve o documento gerado a partir do ficheiro xml indicado
	 * 
	 * @param inputFile - nome do ficheiro a ler
	 * @return - documento DOM
	 */
	public static final Document documentFromFile(String inputFile) {
		if(inputFile==null || inputFile.length()==0)
			return null;
		FileInputStream fil = null;
		try {
			fil = new FileInputStream(inputFile);
			return documentFromStream(fil);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fil.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// transform JSON em XML
	public static final Document documentFromJSON(String strJSON)
			throws JSONException, ParserConfigurationException, SAXException, IOException {
		if (strJSON == null || strJSON.length() == 0)
			return null;
		JSONObject jsonObject = new JSONObject(strJSON);
		String strXML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"yes\"?>" + XML.toString(jsonObject);
		return documentFromXML(strXML);
	}
	
	/**
	 * usa o protocolo por omissão
	 * @param socket
	 * @return DOM lido
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Document documentFromNetwork(Socket socket) throws IOException, ClassNotFoundException {
		if(socket==null || !socket.isConnected() || socket.isClosed())
			return null;
		return documentFromNetwork(socket,protocol);
	}

	/**
	 * Lê o conteudo do documento a partir do mecanismo seleccionado
	 * Usado na comunicação reconfiguravel pelo cliente e pelo servidor
	 * 
	 * @param socket - socket de origem
	 * @return - document DOM
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static final Document documentFromNetwork(Socket socket, Protocol atual) throws IOException, ClassNotFoundException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		try {
			switch (atual) {
				case Objectos:
					return documentFromObject(socket);
				case XML:
					return documentFromSocketXML(socket);
				case JSON:
					return documentFromSocketJSON(socket);
				default:
					break;
			}
		} catch (IOException | ParserConfigurationException | SAXException e) {
			if(atual!=Protocol.Objectos)
				System.err.println("\n\tAviso: "+e.getMessage());
			else
				System.out.println("\n\tAviso: Saiu/Terminou o protocolo de Objectos/Descoberta");
			//e.printStackTrace();
		}
		return null;
	}
	/**
	 * Igual como protocolo por omissão
	 * @param socket
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static final Document documentFromSocket(Socket socket)
			throws IOException, ParserConfigurationException, SAXException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		return documentFromSocket(socket,protocol);
	}
	/**
	 * Lê o conteudo do documento a partir do socket indicado fazendo a sicronização
	 * com mudança de linha
	 * 
	 * @param socket - socket de origem
	 * @return - document DOM
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static final Document documentFromSocket(Socket socket, Protocol atual)
			throws IOException, ParserConfigurationException, SAXException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		BufferedReader is = null;
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String xml = is.readLine(); // termina quando encontra mudança de linha
		if (xml == null)
			return null; // pode ter havido desistência
		if (atual == Protocol.XML)
			return documentFromXML(xml);
		else if (atual == Protocol.JSON) 
			try {
				return documentFromJSON(xml);
			} catch (JSONException | ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		return null;
	}
		
	public static final Document documentFromSocketJSON(Socket socket)
			throws IOException, ParserConfigurationException, SAXException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		if(!socket.isConnected())
			return null;
		BufferedReader is = null;
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String xml = is.readLine(); // termina quando encontra mudança de linha
		if (xml == null)
			return null; // pode ter havido desistência
		try {
			return documentFromJSON(xml);
		} catch (JSONException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static final Document documentFromSocketXML(Socket socket)
			throws IOException, ParserConfigurationException, SAXException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		BufferedReader is = null;
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String xml = is.readLine(); // termina quando encontra mudança de linha
		if (xml == null)
			return null; // pode ter havido desistência
		return documentFromXML(xml);
	}

	/**
	 * Lê o conteudo do documento a partir do socket indicado Fazendo a sicronização
	 * baseada em seriação
	 * 
	 * @param socket - socket de origem
	 * @return - document DOM
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Document documentFromObject(Socket socket) throws IOException, ClassNotFoundException {
		if(socket==null || !socket.isConnected() || socket.isClosed() )
			return null;
		ObjectInputStream iis = new ObjectInputStream(socket.getInputStream());
		return (Document) iis.readObject();
	}

	/**
	 * Devolve o conteudo do documento obtido a partir da cadeia de carateres
	 * indicada
	 * 
	 * @param input - cadeia de carateres de origem
	 * @return - documento DOM
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static final Document documentFromStream(InputStream input)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// enable XInclude processing
		factory.setNamespaceAware(true);
		factory.setXIncludeAware(true);

		DocumentBuilder parser = factory.newDocumentBuilder();
		return parser.parse(input);
	}
	/**
	 * Devolve o conteudo do documento obtido a partir do string indicado
	 * 
	 * @param strXML - string que representa o documento
	 * @return - documento DOM
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static final Document documentFromXML(String strXML)
			throws ParserConfigurationException, SAXException, IOException {
		if (strXML == null || strXML.length() == 0)
			return null;
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(strXML)));
	}

	/**
	 * Escreve o conteudo do documento no ficheiro indicado
	 * 
	 * @param inputDOM   - documento DOM
	 * @param outputFile - nome do ficheiro a escrever
	 * @return
	 */
	public static final boolean documentToFile(Document inputDOM, String outputFile) {
		if (outputFile == null || outputFile.length() == 0 || inputDOM==null)
			return false;
		FileOutputStream fil = null;
		try {
			fil = new FileOutputStream(outputFile);
			documentToStream(inputDOM, fil);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			try {
				fil.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// transforma XML em JSON
	public static final String documentToJSON(Document xmlDoc) throws TransformerFactoryConfigurationError, TransformerException, JSONException
	{
		if (xmlDoc==null)
			return null;
		String strXML = documentToXML(xmlDoc);
		JSONObject json;
		json = XML.toJSONObject(strXML);
		return json.toString(); // pode ter o nivel de identação
	}
	
	/**
	 * Igual com protocolo por omissão
	 * @param xmlDoc
	 * @param socket
	 * @throws IOException
	 */
	public static void documentToNetwork(Document xmlDoc, Socket socket) throws IOException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		documentToNetwork(xmlDoc,socket,protocol);
	}
	/**
	 * Escreve o conteudo do documento usando o mecanismo seleccionado
	 * Usado no protocolo reconfiguravel pelo cliente e pelo servidor
	 * 
	 * @param xmlDoc - DOM que vai ser enviado
	 * @param socket
	 * @throws IOException
	 */
	public static void documentToNetwork(Document xmlDoc, Socket socket, Protocol atual) throws IOException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		try {
			switch (atual) {
				case Objectos:
					documentToObject(xmlDoc, socket);
					break;
				case XML:
					documentToSocketXML(xmlDoc, socket);
					break;
				case JSON:
					documentToSocketJSON(xmlDoc, socket);
					break;
				default:
					break;
			}
		} catch (IOException | TransformerFactoryConfigurationError | TransformerException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Igual com protocolo por omissão
	 * @param xmlDoc
	 * @param socket
	 * @throws IOException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static void documentToSocket(Document xmlDoc, Socket socket)
			throws IOException, TransformerFactoryConfigurationError, TransformerException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		documentToSocket(xmlDoc, socket, protocol);
	}
	/**
	 * Escreve o conteudo do documento no socket indicado Fazendo a sicronização com
	 * mudança de linha
	 * 
	 * @param xmlDoc
	 * @param socket
	 * @return
	 * @throws IOException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 */
	public static void documentToSocket(Document xmlDoc, Socket socket, Protocol atual)
			throws IOException, TransformerFactoryConfigurationError, TransformerException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
		String xml = null;
		if (atual == Protocol.XML)
			xml = documentToXML(xmlDoc);
		else if (atual == Protocol.JSON) 
			try {
				xml = documentToJSON(xmlDoc);
			} catch (TransformerFactoryConfigurationError | TransformerException | JSONException e) {
				e.printStackTrace();
			}
		os.println(xml.replaceAll("\n|\r", "")); // descarta eventuais linhas
	}
	
	public static void documentToSocketJSON(Document xmlDoc, Socket socket)
			throws IOException, TransformerFactoryConfigurationError, TransformerException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
		String xml = null;
		try {
			xml = documentToJSON(xmlDoc);
		} catch (TransformerFactoryConfigurationError | TransformerException | JSONException e) {
			e.printStackTrace();
		}
		os.println(xml.replaceAll("\n|\r", "")); // descarta eventuais linhas
	}

	public static void documentToSocketXML(Document xmlDoc, Socket socket)
			throws IOException, TransformerFactoryConfigurationError, TransformerException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		PrintWriter os = new PrintWriter(socket.getOutputStream(), true);
		String xml = documentToXML(xmlDoc);
		os.println(xml.replaceAll("\n|\r", "")); // descarta eventuais linhas
	}

	/**
	 * Escreve o conteudo do documento no socket indicado fazendo a sicronização
	 * baseada em seriação
	 * 
	 * @param xmlDoc
	 * @param socket
	 * @throws IOException
	 */
	public static void documentToObject(Document xmlDoc, Socket socket) throws IOException {
		if(socket==null ||xmlDoc==null || !socket.isConnected() || socket.isClosed() )
			return;
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		oos.writeObject(xmlDoc);
	}
	/**
	 * Escreve o conteudo do documento na cadeia de carateres indicada
	 * 
	 * @param input  - documento DOM
	 * @param output - cadeia de carates de destino
	 * @return - erro
	 * @throws TransformerException
	 */
	public static final void documentToStream(Document input, OutputStream output) throws TransformerException {
		if(input==null)
			return;
		DOMSource domSource = new DOMSource(input);
		StreamResult resultStream = new StreamResult(output);
		TransformerFactory transformFactory = TransformerFactory.newInstance();
		Transformer transformer = transformFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		transformer.transform(domSource, resultStream);
	}

	/**
	 * Devolve o string obtido a partir do documento DOM indicado
	 * 
	 * @param xmlDoc - documento DOM
	 * @return - string que representa o documento
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static final String documentToXML(Document xmlDoc)
			throws TransformerFactoryConfigurationError, TransformerException {
		if (xmlDoc == null)
			return null;
		Writer out = new StringWriter();
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); // "UTF-8"
		//tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
		return out.toString();
	}

	/**
	 * Faz download do URL para a diretoria indicada
	 * retorna o nome do ficheiro
	 */
	
	public static String download(String sourceUrl, String targetDirectory)
	        throws MalformedURLException, IOException, FileNotFoundException
	{
	    URL imageUrl = new URL(sourceUrl);
	    
		String strPath=imageUrl.getFile();
	
		if(strPath.lastIndexOf('?')==-1)
			strPath=strPath.substring(strPath.lastIndexOf('/'));
		else
			strPath=strPath.substring(strPath.lastIndexOf('/'),strPath.lastIndexOf('?'));
	    
		try (
				InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
	            OutputStream imageWriter = new BufferedOutputStream(
	                    new FileOutputStream(DocXML.getContext()+"/"+targetDirectory + File.separator + strPath));
			)
	    {
	        int readByte;
	
	        while ((readByte = imageReader.read()) != -1)
	        {
	            imageWriter.write(readByte);
	        }
	        return strPath.substring(1);
	    }
	}

	// devolve array de strings com lista de ficheiros numa pasta
	// não usado: exemplo de que define o Array á media do conteudo
	public static String[] getFiles(final String pasta) {
		int j = 0;
		File folder = new File(pasta);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				j = j + 1;
		String[] files = new String[j];
		j = 0;
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				files[j++] = listOfFiles[i].getName();
		return files;
	}

	public static void main(String args[]) throws TransformerFactoryConfigurationError, Exception  {
		String Exemplo1="<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + 
				"<pangrama>\nGazeta publica hoje no jornal uma breve nota de faxina na quermesse</pangrama>";

		String Exemplo2="<?xml version='1.0' encoding='ISO-8859-1' standalone='yes'?>" + 
				"<pangrama>\nThe quick brown fox jumps over the lazy dog</pangrama>";
		
		Document doc = DocReadWrite.documentFromXML(Exemplo1);

		DocXML.documentPrettyPrint(doc);
		
		System.out.println("1º Exemplo XML:\n"+DocReadWrite.documentToXML(doc));
		
		String strJSON = DocReadWrite.documentToJSON(doc);
		System.out.println("\n1º Exemplo JSON:\n"+strJSON);
		Document docJSON = DocReadWrite.documentFromJSON(strJSON);
		System.out.println("\n1º Exemplo XML:\n");
		DocXML.documentPrettyPrint(docJSON);

		System.out.println();
		// exemplo lado servidor
		// cria o socket principal
		
		ServerSocket serverSocket = new ServerSocket(5000);
		
		new Thread(() -> {
		// Espera connect do cliente
		try {
			// cria o socket que representa o circuito virtual
			Socket newSock = serverSocket.accept();
			//ler DOM do socket, espera envio da msg
			Document docs = documentFromNetwork(newSock);
			// escreve o DOM enviado pelo cliente no ecrã
			System.out.println("2º Exemplo:");
			DocReadWrite.documentToStream(docs, System.out);
			// fecha o socket
			newSock.close();
			serverSocket.close();
		} catch (IOException | ClassNotFoundException | TransformerException e) {
			e.printStackTrace();
		}
		}).start();
		// exemplo lado do cliente
		Socket socket = new Socket("localhost", 5000);
		// envia o DOM para o servidor
		DocReadWrite.documentToNetwork(DocReadWrite.documentFromXML(Exemplo2), socket);
		socket.close();
	}
}