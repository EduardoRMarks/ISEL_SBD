package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Game extends Thread {

    private Socket player1Socket;
    private Socket player2Socket;
    
    private String message;
    private String filePath;
    private UserXmlWriter xmlWriter;
    private Document document;
    
    
    /**
     * Motor de jogo da parte do servidor.
     * Construtor. Recebe os sockets dos players e tem conhecimento da path do ficheiro xml.
     * Só o servidor tem acesso ao xml
     * @param player1Socket
     * @param player2Socket
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public Game(Socket player1Socket, Socket player2Socket) throws ParserConfigurationException, SAXException, IOException {
        this.player1Socket = player1Socket;
        this.player2Socket = player2Socket;
        this.filePath = "./src/main/webapp/xml/Users.xml"; 			// PARA MAC
        //this.filePath = ".\\src\\main\\webapp\\xml\\Users.xml"; 	// PARA WINDOWS
        
    }

    @Override
    public void run() {
        try {
        	//Inicialização dos BufferedReaders e dos PrintWriters dos dois players.
            BufferedReader player1In = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            PrintWriter player1Out = new PrintWriter(player1Socket.getOutputStream(), true);

            BufferedReader player2In = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            PrintWriter player2Out = new PrintWriter(player2Socket.getOutputStream(), true);
            
            //Conversão do ficheiro xml para string
            String xmlString = convertXmlToString(filePath);
            xmlWriter = new UserXmlWriter(xmlString);
            
            //Inicialização das variaveis auxiliares dos nomes dos players
            String player1Name = "";
            String player2Name = "";
            
            //Comunicação do ficheiro xml em string aos players
            xmlWriter.sendFullXML(player1Out, xmlString);
            xmlWriter.sendFullXML(player2Out, xmlString);


            // Mensagem de boas vindas
            player1Out.println("Welcome to Four in a Row! You are player 1.");
            player2Out.println("Welcome to Four in a Row! You are player 2.");
            
            //variavel aux
            int turn = 1;
            
            //motor de jogo do servidor
            for(;;) {
            	
            	if(turn == 1) {
            		player1Out.println("Your turn.");
            	}
            	
            	//leitura da mensagem do player1
            	System.out.println();
            	System.out.println("ESTOU A ESPERA DO PLAYER 1");
            	String inputLine1 = player1In.readLine();
				if(inputLine1==null)
					break;
				
				/**
				 * Verificação se a mensagem do player 1 diz que o jogo acabou.
				 * Caso o jogo acaba, Inicializamos a atualização do xml final.
				 * Acaba-se o motor de jogo.
				 */
				if(inputLine1.equals("Game is Finished")) {
					System.out.println("Recebi do player1: " + inputLine1);
					updateFinalXML(player1In, player1Name, player2Name);
					break;
				}
				
				/**
				 * Verificação se a mesnagem do player 1 diz que há um novo registo.
				 * Caso haja um novo registo, é feita a leitura do xml que o player mandou.
				 * É atualizado o ficheiro xml da parte do servidor.
				 * É feito uma nova leitura da mensagem do player 1.
				 */
				if(inputLine1.equals("Novo registo")) {
					System.out.println("Recebi do player1: " + inputLine1);
					String updatedXML = xmlWriter.readFullXML(player1In);
					xmlWriter.updateXML(updatedXML, filePath);
					inputLine1 = player1In.readLine();
				}
				
				/**
				 * Verificação se a mensagem do player 1 diz que há uma mudança de imagem.
				 * Caso haja um novo registo, é feita a leitura do xml que o player mandou.
				 * É atualizado o ficheiro xml da parte do servidor.
				 * É feito uma nova leitura da mensagem do player 1.
				 */
				if(inputLine1.equals("Muda de picture")) {
					System.out.println("Recebi do player1: " + inputLine1);
					String updatedXML = xmlWriter.readFullXML(player1In);
					xmlWriter.updateXML(updatedXML, filePath);
					inputLine1 = player1In.readLine();
				}
				System.out.println();
				//Jogada do player 1
				System.out.println("Recebi do player1: " + inputLine1);
				System.out.println();
				System.out.println("REENCAMINHEI PARA O PLAYER 2");
				//Comunicação ao player 2 a jogada do player 1
				player2Out.println("O player 1 jogou na posição: " + inputLine1 + " Your turn");
				
				
            	System.out.println();
            	System.out.println("ESTOU A ESPERA DO PLAYER 2");
				//leitura da mensagem do player 2
				String inputLine2 = player2In.readLine();
				if(inputLine2==null)
					break;
				
				/**
				 * Verificação se a mensagem do player 2 diz que o jogo acabou.
				 * Caso o jogo acaba, Inicializamos a atualização do xml final.
				 * Acaba-se o motor de jogo.
				 */
				if(inputLine2.equals("Game is Finished")) {
					System.out.println("Recebi do player2: " + inputLine2);
					updateFinalXML(player2In, player1Name, player2Name);
					break; 
				}
				
				/**
				 * Verificação se a mesnagem do player 2 diz que há um novo registo.
				 * Caso haja um novo registo, é feita a leitura do xml que o player mandou.
				 * É atualizado o ficheiro xml da parte do servidor.
				 * É feito uma nova leitura da mensagem do player 1.
				 */
				if(inputLine2.equals("Novo registo")) {
					System.out.println("Recebi do player2: " + inputLine2);
					String updatedXML = xmlWriter.readFullXML(player2In);
					System.out.println(updatedXML);
					xmlWriter.updateXML(updatedXML, filePath);
					inputLine2 = player2In.readLine();
				}
				
				/**
				 * Verificação se a mensagem do player 2 diz que há uma mudança de imagem.
				 * Caso haja um novo registo, é feita a leitura do xml que o player mandou.
				 * É atualizado o ficheiro xml da parte do servidor.
				 * É feito uma nova leitura da mensagem do player 1.
				 */
				if(inputLine2.equals("Muda de picture")) {
					System.out.println("Recebi do player2: " + inputLine2);
					String updatedXML = xmlWriter.readFullXML(player2In);
					System.out.println(updatedXML);
					xmlWriter.updateXML(updatedXML, filePath);
					inputLine2 = player2In.readLine();
				}
				
				System.out.println();
				//leitura da jogada do player 2
				System.out.println("Recebi do player2: " + inputLine2);
					
				System.out.println();
				System.out.println("REENCAMINHEI PARA O PLAYER 1");
				//Comunicação ao player 1 a jogada do player 2
				player1Out.println("O player 2 jogou na posição: " + inputLine2 + " Your turn");
				
				//Caso seja a primeira ronda, é feita a leitura dos nomes dos jogadores.
				if(turn == 1) {
					player1Name = player1In.readLine();
					player2Name = player2In.readLine();
					turn +=1;
				}
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * Método que converte um fiheicro xml para uma string
     * @param filepath
     * @return xml em string
     */
    public String convertXmlToString(String filepath) {
        try {
        	
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
        	
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            
            document = builder.parse(new File( filepath ));
            document.getDocumentElement().normalize();

            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Método que, quando o jogo acaba, atualiza o ficheiro xml final.
     * @param in
     * @param player1Name
     * @param player2Name
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private void updateFinalXML(BufferedReader in, String player1Name, String player2Name) throws IOException, ParserConfigurationException, SAXException {
    	//leitura do outcome final. O outcome diz quem ganha e a duração do jogo
    	String outcome = in.readLine();
    	String copy = outcome;
    	
    	//extração da duração do jogo
    	String timeSpent = copy.replaceAll("[^0-9]", "");
    	
		System.out.println("OUTCOME FINAL: " + outcome);
		
		//Verificação quem ganha, quem perde e atualização do ficheiro xml
		if((outcome.contains(player1Name) && outcome.contains("won")) || (outcome.contains(player2Name) && outcome.contains("lost") )) {
			try {
				xmlWriter.updateInfo(player1Name, player2Name, filePath, timeSpent);
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if((outcome.contains(player2Name) && outcome.contains("won")) || (outcome.contains(player1Name) && outcome.contains("lost"))) {
			
		
			try {
				xmlWriter.updateInfo(player2Name, player1Name, filePath, timeSpent);

			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	} 
    }
    
    
}


