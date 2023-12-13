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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Esta classe serve como auxílio de leitura e escrita em xml. Esta classe não
 * tem conhecimento do ficheiro xml original.
 */
public class UserXmlWriter {

	private Element root = null;
	private Document document;
	private String xmlDOC;

	/**
	 * Construtor. Recebe um xml em string e cria o doc.
	 * 
	 * @param xmlDOC
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public UserXmlWriter(String xmlDOC) throws ParserConfigurationException, SAXException, IOException {
		this.xmlDOC = xmlDOC;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlDOC));
			Document doc = builder.parse(is);
			this.document = doc;
		} catch (SAXParseException e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Atualizção do this.document com um novo xml string
	 * 
	 * @param xmlDOC
	 */
	public void UpdateDoc(String xmlDOC) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlDOC));
			Document doc = builder.parse(is);
			this.document = doc;
		} catch (SAXParseException e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Escrita no xml string um novo user. Este método é chamado no registo
	 * 
	 * @param user novo registo
	 * @return string do xml string atualizado
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public String writeUserToFile(User user)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		this.document.getDocumentElement().normalize();

		root = document.getDocumentElement();

		Element user_element = document.createElement("user");
		root.appendChild(user_element);

		Element nickname = document.createElement("nickname");
		nickname.appendChild(document.createTextNode(user.getNickname()));
		Attr wins = document.createAttribute("wins");
		wins.setValue("0");
		nickname.setAttributeNode(wins);
		Attr losses = document.createAttribute("losses");
		losses.setValue("0");
		nickname.setAttributeNode(losses);
		Attr time_in_games = document.createAttribute("time");
		time_in_games.setValue("0");
		nickname.setAttributeNode(time_in_games);
		user_element.appendChild(nickname);

		Element password = document.createElement("password");
		password.appendChild(document.createTextNode(user.getPassword()));
		user_element.appendChild(password);

		Element nationality = document.createElement("nationality");
		nationality.appendChild(document.createTextNode(user.getNationality()));
		user_element.appendChild(nationality);

		Element age = document.createElement("age");
		age.appendChild(document.createTextNode(String.valueOf(user.getAge())));
		user_element.appendChild(age);

		Element picture = document.createElement("picture");
		picture.appendChild(document.createTextNode(user.getProfilePicturePath()));
		user_element.appendChild(picture);
		
		Element color = document.createElement("color");
		picture.appendChild(document.createTextNode(user.getProfilePicturePath()));
		user_element.appendChild(color);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("indent", "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
		DOMSource source = new DOMSource(document);
		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * Este método disponibiliza um array list de users. Este método é chamado para
	 * o ranking system.
	 * 
	 * @return array list de users.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public List<User> readUsersFromFile() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		document.getDocumentElement().normalize();

		// Get the list of user elements
		NodeList userList = document.getElementsByTagName("user");

		// Create a list to store User objects
		List<User> users = new ArrayList<>();

		// Iterate through the user elements and create User objects
		for (int i = 0; i < userList.getLength(); i++) {
			Node userNode = userList.item(i);
			if (userNode.getNodeType() == Node.ELEMENT_NODE) {
				Element userElement = (Element) userNode;

				// Get the user's nickname, password, nationality, age, and picture path
				String nickname = userElement.getElementsByTagName("nickname").item(0).getTextContent();
				String password = userElement.getElementsByTagName("password").item(0).getTextContent();
				String nationality = userElement.getElementsByTagName("nationality").item(0).getTextContent();
				String age = userElement.getElementsByTagName("age").item(0).getTextContent();
				String picturePath = userElement.getElementsByTagName("picture").item(0).getTextContent();
				String color = userElement.getElementsByTagName("color").item(0).getTextContent();
				NamedNodeMap info = userElement.getElementsByTagName("nickname").item(0).getAttributes();
				String wins = info.getNamedItem("wins").getTextContent();
				String losses = info.getNamedItem("losses").getTextContent();
				String time = info.getNamedItem("time").getTextContent();

				// Create a new User object and add it to the list
				User user = new User(nickname, password, nationality, age, picturePath, wins, losses, time, color);
				users.add(user);
			}
		}

		return users;
	}

	/**
	 * Método que verifica se o username e a password do login estão corretas.
	 * 
	 * @param nickname
	 * @param password
	 * @return user
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public User loginChecker(String nickname, String password)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		document.getDocumentElement().normalize();

		NodeList nList = document.getElementsByTagName("user");
		for (int i = 0; i < nList.getLength(); i++) {
			Element user = (Element) nList.item(i);
			String nicknameFromXML = user.getElementsByTagName("nickname").item(0).getTextContent();
			String passwordFromXML = user.getElementsByTagName("password").item(0).getTextContent();
			if (nickname.equals(nicknameFromXML) && password.equals(passwordFromXML)) {
				String nationality = user.getElementsByTagName("nationality").item(0).getTextContent();
				String age = user.getElementsByTagName("age").item(0).getTextContent();
				String picturePath = user.getElementsByTagName("picture").item(0).getTextContent();
				String color = user.getElementsByTagName("color").item(0).getTextContent();
				NamedNodeMap info = user.getElementsByTagName("nickname").item(0).getAttributes();
				String wins = info.getNamedItem("wins").getTextContent();
				String losses = info.getNamedItem("losses").getTextContent();
				String time = info.getNamedItem("time").getTextContent();
				return new User(nicknameFromXML, passwordFromXML, nationality, age, picturePath, wins, losses, time, color);
			}
		}

		return null;
	}

	/**
	 * Atualização dos atributos wins, losses e time do ficheiro xml. Este método é
	 * chamado pela classe Game.
	 * 
	 * @param userWinner
	 * @param userLoss
	 * @param filePath
	 * @param timeSpent
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void updateInfo(String userWinner, String userLoss, String filePath, String timeSpent)
			throws ParserConfigurationException, TransformerException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(new File(filePath));

		NodeList nList = document.getElementsByTagName("user");
		for (int i = 0; i < nList.getLength(); i++) {
			Element user = (Element) nList.item(i);
			String nicknameFromXML = user.getElementsByTagName("nickname").item(0).getTextContent();
			if (userWinner.equals(nicknameFromXML)) {
				Element nickname = (Element) user.getElementsByTagName("nickname").item(0);
				nickname.setAttribute("wins", String.valueOf(Integer.parseInt(nickname.getAttribute("wins")) + 1));
				nickname.setAttribute("time",
						String.valueOf(Integer.parseInt(nickname.getAttribute("time")) + Integer.parseInt(timeSpent)));

			}
			if (userLoss.equals(nicknameFromXML)) {
				Element nickname = (Element) user.getElementsByTagName("nickname").item(0);
				nickname.setAttribute("losses", String.valueOf(Integer.parseInt(nickname.getAttribute("losses")) + 1));
				nickname.setAttribute("time",
						String.valueOf(Integer.parseInt(nickname.getAttribute("time")) + Integer.parseInt(timeSpent)));
			}

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);

	}

	/**
	 * Atualização do ficheiro xml com um xml string
	 * 
	 * @param xmlDOC
	 * @param filePath
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public void updateXML(String xmlDOC, String filePath)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlDOC));
		Document doc = builder.parse(is);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
	}

	/**
	 * Update do xml string da picture do user.
	 * 
	 * @param user
	 * @param picturePath
	 * @return xml string
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public String updatePicture(User user, String picturePath)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		NodeList nList = document.getElementsByTagName("user");
		for (int i = 0; i < nList.getLength(); i++) {
			Element userElement = (Element) nList.item(i);
			String nicknameFromXML = userElement.getElementsByTagName("nickname").item(0).getTextContent();
			if (user.getNickname().equals(nicknameFromXML)) {
				userElement.getElementsByTagName("picture").item(0).setTextContent(picturePath);
			}
		}

		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
		return stringWriter.toString();
	}
	
	public String updateWebInfo(User user, User newUser)
			throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		NodeList nList = document.getElementsByTagName("user");
		for (int i = 0; i < nList.getLength(); i++) {
			
			  Node userNode = nList.item(i);

              if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                  Element userElement = (Element) userNode;
                  Element nicknameElement = (Element) userElement.getElementsByTagName("nickname").item(0);
                  String currentNickname = nicknameElement.getTextContent();

                  if (currentNickname.equals(user.getNickname())) {
                      // Update the information of the user with the information of the newUser
                      nicknameElement.setTextContent(newUser.getNickname());
                      userElement.getElementsByTagName("password").item(0).setTextContent(newUser.getPassword());
                      userElement.getElementsByTagName("nationality").item(0).setTextContent(newUser.getNationality());
                      userElement.getElementsByTagName("age").item(0).setTextContent(newUser.getAge());
                      userElement.getElementsByTagName("picture").item(0).setTextContent(newUser.getProfilePicturePath());
                      userElement.getElementsByTagName("color").item(0).setTextContent(newUser.getColor());

                      break; // Stop searching since the user is found and updated
                  }
              }
			/**Element userElement = (Element) nList.item(i);
			String nicknameFromXML = userElement.getElementsByTagName("nickname").item(0).getTextContent();
			if (user.getNickname().equals(nicknameFromXML)) {
				userElement.getElementsByTagName("nickname").item(0).setTextContent(newUser.getNickname());
				userElement.getElementsByTagName("password").item(0).setTextContent(newUser.getPassword());
				userElement.getElementsByTagName("nationality").item(0).setTextContent(newUser.getNationality());
				userElement.getElementsByTagName("age").item(0).setTextContent(newUser.getAge());
				userElement.getElementsByTagName("picture").item(0).setTextContent(newUser.getProfilePicturePath());
				userElement.getElementsByTagName("color").item(0).setTextContent(newUser.getColor());
				;
			}**/
		}

		StringWriter stringWriter = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * Método que envia o xml string na sua totalidade
	 * 
	 * @param out
	 * @param xmlString
	 */
	public void sendFullXML(PrintWriter out, String xmlString) {
		String output = "";
		for (int i = 0; i <= xmlString.length() - 1; i++) {
			output = output + xmlString.charAt(i);
			if (i == xmlString.length() - 1) {
				if (xmlString.charAt(i) == '>') {
					out.println(output);
					output = "";
					break;
				}
			}
			if (xmlString.charAt(i) == '>' && xmlString.charAt(i + 1) == '\n') {
				out.println(output);
				output = "";
			}

		}
	}

	/**
	 * Método que efectua a leitura do xml string na sua totalidade
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public String readFullXML(BufferedReader in) throws IOException {
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
