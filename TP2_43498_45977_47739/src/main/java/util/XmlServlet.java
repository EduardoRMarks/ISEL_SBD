package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;

@WebServlet("/XmlServlet")
@MultipartConfig
public class XmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getPart("xmlFile") != null) {
			importarPerfis(request.getPart("xmlFile").getInputStream());
			response.sendRedirect("administrador.jsp");
		} else if ("true".equals(request.getParameter("export"))) {
			String userEmail = request.getParameter("UserEmail").trim();
			exportarPerfis(response, userEmail);
		}
	}
	
	private void importarPerfis(InputStream xmlInputStream) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlInputStream);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("Utilizador");

			for (int temp = 0; temp < nodeList.getLength(); temp++) {
				Node node = nodeList.item(temp);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					String tipo = element.getElementsByTagName("Tipo").item(0).getTextContent();

					if (tipo.equals("Cliente")) {
						String nif = element.getElementsByTagName("Nif").item(0).getTextContent();
						String nomeUtilizador = element.getElementsByTagName("Nome").item(0).getTextContent();
						String dataNascimento = element.getElementsByTagName("DataDeNascimento").item(0).getTextContent();
						String telemovel = element.getElementsByTagName("Telemovel").item(0).getTextContent();
						String email = element.getElementsByTagName("Email").item(0).getTextContent();
						String password = element.getElementsByTagName("Password").item(0).getTextContent();
						System.out.println(nomeUtilizador);
						inserirUtilizador(tipo, nif, nomeUtilizador, dataNascimento, telemovel, email, password);
					} else {
						String id = element.getElementsByTagName("ID").item(0).getTextContent();
						String nomeUtilizador = element.getElementsByTagName("Nome").item(0).getTextContent();
						String telemovel = element.getElementsByTagName("Telemovel").item(0).getTextContent();
						String linkFotografia = element.getElementsByTagName("Fotografia").item(0).getTextContent();
						String email = element.getElementsByTagName("Email").item(0).getTextContent();
						String password = element.getElementsByTagName("Password").item(0).getTextContent();
						inserirUtilizador(tipo, id, nomeUtilizador, linkFotografia, telemovel, email, password);

					}
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private void inserirUtilizador(String tipo, String id, String nomeUtilizador, String varVariavel, String selfPhone,
			String email, String password) throws SQLException {

		String query = null;
		PreparedStatement statement = null;
		String hashedPassword = PasswordUtil.hashPassword(password);

		Connection connection = DBConnectionManager.getConnection();

		query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`utilizador` (Email, Password, Role)" + " VALUES ('" + email
				+ "', '" + hashedPassword + "', '" + tipo + "');";
		statement = connection.prepareStatement(query);
		statement.executeUpdate();

		if (tipo.equals("Cliente")) {

			query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`cliente` (`Nif`, `Nome`, `DataDeNascimento`, "
					+ "`Telemovel`, `Email`) VALUES ('" + id + "', '" + nomeUtilizador + "', '" + varVariavel + "', '"
					+ selfPhone + "', '" + email + "');";
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} else {
			query = "INSERT INTO `sbd_tp1_43498_45977_47739`.`pt` (`Id`, `Nome`, `Telemovel`, `Email`, `Fotografia`) "
					+ "VALUES ('" + id + "', '" + nomeUtilizador + "', '" + selfPhone + "', '" + email + "', '"
					+ varVariavel + "');";
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		}
	}

	private void exportarPerfis(HttpServletResponse response, String userEmail) throws IOException {
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition", "attachment;filename=perfis_exportados.xml");

		try (PrintWriter out = response.getWriter(); Connection connection = DBConnectionManager.getConnection()) {
			Document doc = criarDocumentoXML(connection, userEmail);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);

			transformer.transform(source, result);
		} catch (SQLException | TransformerException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	private Document criarDocumentoXML(Connection connection, String userEmail) {
		Document doc = null;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("Perfis");
			doc.appendChild(rootElement);
			
			String query = "SELECT Role FROM sbd_tp1_43498_45977_47739.utilizador WHERE Email = '" + userEmail + "';";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();
			
			String role = null;
			if (resultSet.next()) {
				role = resultSet.getString("Role");
			}
			
			if(role.equals("Cliente")) {
				query = "SELECT * FROM sbd_tp1_43498_45977_47739.cliente WHERE Email = '" + userEmail + "';";
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				
				if (resultSet.next()) {
					Element utilizadorElement = doc.createElement("Utilizador");
					rootElement.appendChild(utilizadorElement);

					// Adiciona elementos ao nó Utilizador
					adicionarElementoXML(doc, utilizadorElement, "Role", "Cliente");
					adicionarElementoXML(doc, utilizadorElement, "Nif", resultSet.getString("Nif"));
					adicionarElementoXML(doc, utilizadorElement, "Nome", resultSet.getString("Nome"));
					adicionarElementoXML(doc, utilizadorElement, "DataDeNascimento", resultSet.getString("DataDeNascimento"));
					adicionarElementoXML(doc, utilizadorElement, "Telemovel", resultSet.getString("Telemovel"));			
					adicionarElementoXML(doc, utilizadorElement, "Email", resultSet.getString("Email"));
				}
			}
			else {
				query = "SELECT * FROM sbd_tp1_43498_45977_47739.pt WHERE Email = '" + userEmail + "';";
				statement = connection.prepareStatement(query);
				resultSet = statement.executeQuery();
				
				if (resultSet.next()) {
					Element utilizadorElement = doc.createElement("Utilizador");
					rootElement.appendChild(utilizadorElement);

					// Adiciona elementos ao nó Utilizador
					adicionarElementoXML(doc, utilizadorElement, "Role", "Pt");
					adicionarElementoXML(doc, utilizadorElement, "Id", resultSet.getString("Id"));
					adicionarElementoXML(doc, utilizadorElement, "Nome", resultSet.getString("Nome"));
					adicionarElementoXML(doc, utilizadorElement, "Telemovel", resultSet.getString("Telemovel"));			
					adicionarElementoXML(doc, utilizadorElement, "Email", resultSet.getString("Email"));
					adicionarElementoXML(doc, utilizadorElement, "LinkFotografia", resultSet.getString("Fotografia"));
				}
			}
	
		} catch (ParserConfigurationException | SQLException e) {
			e.printStackTrace();
		}

		return doc;
	}

	private void adicionarElementoXML(Document doc, Element parentElement, String tagName, String textContent) {
		Element element = doc.createElement(tagName);
		element.appendChild(doc.createTextNode(textContent));
		parentElement.appendChild(element);
	}

}