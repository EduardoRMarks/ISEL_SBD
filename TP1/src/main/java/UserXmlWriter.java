import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.Format;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserXmlWriter {
	
	Element root = null;
	Document document = null;

    public void writeUserToFile(User user, String filePath) throws ParserConfigurationException, SAXException, IOException, TransformerException {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);

        document = builder.parse(new File( filePath ));
        document.getDocumentElement().normalize();
         
        root = document.getDocumentElement();
        
        System.out.println("root element: " + root);
        
        Element user_element = document.createElement("user");
        root.appendChild(user_element);
        
        System.out.println(root.getFirstChild());

        Element nickname = document.createElement("nickname");
        nickname.appendChild(document.createTextNode(user.getNickname()));
        user_element.appendChild(nickname);
        System.out.println("Nickname: "+ nickname + " user nickname "+ user.getNickname());

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
        
     // Write the XML to a file
        File file = new File(filePath);
        OutputStream outputStream = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(outputStream, "ISO-8859-1");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("indent", "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        
 

    }
}
