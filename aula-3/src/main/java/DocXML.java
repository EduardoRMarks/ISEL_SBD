
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;

/* load & save */
import org.w3c.dom.ls.*;
import org.xml.sax.SAXException;

/* XML Transformation */
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.OutputKeys;

/**
 * Classe para manipulação de documentos XML
 */

/**
 * @author Porfírio Filipe
 *
 */

final public class DocXML {

	/**
	 * Devolve a pasta de contexto do projeto corrente
	 * 
	 * @return
	 */
	public static final String getContext() {
		String contexto = "WebContent";
		File f = new File(contexto);
		if (!(f.exists() && f.isDirectory())) {
			contexto = "src/main/webapp";
		}
		return contexto;
	}

	/**
	 * Devolve lista de nós gerada pela expressão xPath indicada
	 * 
	 * @param expression xpath
	 * @param doc        raiz do documento XML
	 * @return lista de nós
	 */

	public static final NodeList documentXPath(final Document doc, final String expression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes;
		try {
			nodes = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
			return nodes;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Executa uma expressão XPath numa arvore DOM e devolve um double
	 * 
	 * @param expression
	 * @param doc
	 * @return
	 */
	public static final int documentXPathN(final Document doc, final String expression) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			return ((Double) xpath.evaluate(expression, doc, XPathConstants.NUMBER)).intValue();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * Executa uma expressão XPath numa arvore DOM e devolve o 1º string (valor)
	 * 
	 * @param expression
	 * @param doc
	 * @return string que é o valor do nó encontrado
	 * 
	 */
	public static final String documentXPathV(final Document doc, final String expression) {
		NodeList aux = documentXPath(doc, expression);
		if (aux == null)
			return null;
		else if (aux.item(0) == null)
			return null;
		else
			return aux.item(0).getNodeValue();
	}

	// devolve um ArrayList com os nomes do ficheiros existentes a pasta indicada
	public static final ArrayList<String> getFiles(final String pasta) {
		ArrayList<String> result = new ArrayList<String>(); // Create an ArrayList of filenames
		File folder = new File(pasta);
		File[] listOfFiles = folder.listFiles();
		// && listOfFiles[i].getName().toLowerCase().endsWith(".xml")
		// não faz qualquer filtragem
		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				result.add(listOfFiles[i].getName());
		return result;
	}

	public static void main(String[] args) throws Exception {
		String cont = getContext();

		ArrayList<String> xml = DocXML.getFiles(cont+"/poemas");
		for (int i = 0; i < xml.size(); i++)
			System.out.println(xml.get(i));
		String file = cont+"/poemas/poeta.xml";
		Document doc = parseFile(file);
		
		System.out.println("prettyPrint ("+file+"):");
		documentPrettyPrint(doc);
		
		System.out.println("documentWrite ("+file+"\"):");
		documentWrite(doc, System.out);

	}

	/**
	 * Parses XML file and returns XML document.
	 * 
	 * @param fileName XML file to parse
	 * @return XML document or null if error occured
	 */
	public static final Document parseFile(final String fileName) {
		DocumentBuilder docBuilder;
		Document doc = null;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
			return null;
		}
		File sourceFile = new File(fileName);
		try {
			doc = docBuilder.parse(sourceFile);
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
		return doc;
	}

	public static final void documentPrettyPrint(final Document xml) throws Exception {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		Writer out = new StringWriter();
		tf.transform(new DOMSource(xml), new StreamResult(out));
		System.out.println(out.toString());
	}

	/**
	 * @param DOMtree        arvore DOM
	 * @param targetFileName ficheiro usado para escrita
	 */
	public static final void documenttSerialization(final Document DOMtree, final String targetFileName) {
		/*
		 * demonstração da funcionalidade save da arvore DOM alternativa ao uso da
		 * transformação vazia
		 */
		FileOutputStream FOS = null;
		DOMImplementationLS DOMiLS = null;

		// testing the support for DOM Load and Save
		if ((DOMtree.getFeature("Core", "3.0") != null) && (DOMtree.getFeature("LS", "3.0") != null)) {
			DOMiLS = (DOMImplementationLS) (DOMtree.getImplementation()).getFeature("LS", "3.0");
			System.out.println("[Using DOM Load and Save]");
		} else {
			System.err.println("[DOM Load and Save unsupported]");
			System.exit(0);
		}

		// get a LSOutput object
		LSOutput LSO = DOMiLS.createLSOutput();
		// LSO.setEncoding("UTF-16"); // codificação por omissão do windows
		LSO.setEncoding("ISO-8859-1"); // codificação para português

		// setting the location for storing the result of serialization

		try {
			FOS = new FileOutputStream(targetFileName);
			LSO.setByteStream((OutputStream) FOS);
			// LSO.setByteStream(System.out); // usa o output para testes
		} catch (java.io.FileNotFoundException e) {
			System.err.println(e.getMessage());
		}

		// get a LSSerializer object
		LSSerializer LSS = DOMiLS.createLSSerializer();

		// do the serialization
		boolean ser = LSS.write(DOMtree, LSO);

		// publish the result
		if (ser)
			System.out.println("\n[Serialization done!]");
		else
			System.out.println("[Serialization failed!]");

		try {
			FOS.close();
		} catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Transformação XML
	 * 
	 * @param xmlFileName    documento XML
	 * @param xsltFileName   documento XSL
	 * @param targetFileName documento gerado
	 */
	public static final void documentTransformation(Document xml, String xsltFileName, String targetFileName) {

		try {
			Source input = new DOMSource(xml);
			Source xsl = new StreamSource(xsltFileName);

			Result output = new StreamResult(targetFileName);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xsl);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(input, output);

		} catch (TransformerException te) {
			System.out.println("Transformer exception: " + te.getMessage());
		}
	}

	/**
	 * Transformação XML
	 * 
	 * @param xmlFileName  documento XML
	 * @param xsltFileName documento XSL
	 * @param targetStream output
	 */
	public static final void documentTransformation(Document xml, String xsltFileName, PrintStream targetStream) {

		try {
			Source input = new DOMSource(xml);
			Source xsl = new StreamSource(xsltFileName);

			Result output = new StreamResult(targetStream);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xsl);

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			transformer.transform(input, output);

		} catch (TransformerException te) {
			System.out.println("Transformer exception: " + te.getMessage());
		}
	}

	/**
	 * Transformação XML
	 * 
	 */

	public static Document documentTransformation(Document xml, String xsltFileName)
			throws TransformerException, ParserConfigurationException, FactoryConfigurationError {

		Source xmlSource = new DOMSource(xml);
		Source xsltSource = new StreamSource(xsltFileName);
		DOMResult result = new DOMResult();

		// the factory pattern supports different XSLT processors
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);

		trans.transform(xmlSource, result);

		Document resultDoc = (Document) result.getNode();

		return resultDoc;
	}

	/**
	 * Transformação XML
	 * 
	 */

	public static Document documentTransformation(Document xml, Document xslt)
			throws TransformerException, ParserConfigurationException, FactoryConfigurationError {

		Source xmlSource = new DOMSource(xml);
		Source xsltSource = new DOMSource(xslt);
		DOMResult result = new DOMResult();

		// the factory pattern supports different XSLT processors
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);

		trans.transform(xmlSource, result);

		Document resultDoc = (Document) result.getNode();

		return resultDoc;
	}

	/**
	 * Validação de documento na árvore DOM, com XSD ou DTD conforme o indicado no
	 * parametro type
	 * 
	 * @param document
	 * @param xsdFileName
	 * @param type        XMLConstants.W3C_XML_SCHEMA_NS_URI ou
	 *                    XMLConstants.XML_DTD_NS_URI
	 * @return sucesso/insucesso
	 * @throws SAXException
	 */
	public static final boolean documentValidation(Document document, String xsdFileName, String type) {

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(type);

		// load a WXS schema, represented by a Schema instance
		Source schemaFile = new StreamSource(new File(xsdFileName));
		try {
			Schema schema = factory.newSchema(schemaFile);

			// create a Validator instance, which can be used to validate an instance
			// document
			Validator validator = schema.newValidator();

			// validate the DOM tree

			validator.validate(new DOMSource(document));
			return true;
		} catch (IOException | SAXException e) {
			// instance document is invalid!
			System.err.println("\nReason: " + e.getLocalizedMessage());
		}
		return false;
	}

	/**
	 * Validação de documento em ficheiro com XSD ou DTD conforme o indicado no
	 * parametro type
	 * 
	 * @param xmlFileName
	 * @param vFileName
	 * @param type        XMLConstants.W3C_XML_SCHEMA_NS_URI ou
	 *                    XMLConstants.XML_DTD_NS_URI
	 * @return
	 */
	private static final boolean documentValidation(String xmlFileName, String vFileName, String type) {
		// System.out.println("Processo xml ("+xmlFileName+") e xsd ("+vFileName+")");
		Source xmlFile = new StreamSource(new File(xmlFileName));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(type);
		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(new File(vFileName));
		} catch (SAXException e1) {
			e1.printStackTrace();
			System.out.println("Erro no acesso ao ficheiro" + vFileName);
			return false;
		}
		Validator validator = schema.newValidator();
		try {
			try {
				validator.validate(xmlFile);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			System.out.println(URLDecoder.decode(xmlFile.getSystemId(), "UTF-8") + " is valid");
			return true;
		} catch (SAXException e) {
			try {
				System.err.println(URLDecoder.decode(xmlFile.getSystemId(), "UTF-8") + " is NOT valid");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			System.err.println("\nReason: " + e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static final boolean documentValidationDTD(String xmlFileName, String vFileName) {
		return documentValidation(xmlFileName, vFileName, XMLConstants.XML_DTD_NS_URI);
	}

	public static final boolean documentValidationXSD(String xmlFileName, String vFileName) {
		return documentValidation(xmlFileName, vFileName, XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}

	public static final boolean documentValidationXSD(Document xmlDoc, String vFileName) {
		return documentValidation(xmlDoc, vFileName, XMLConstants.W3C_XML_SCHEMA_NS_URI);
	}

	/**
	 * @param input  arvore DOM
	 * @param output stream usado para escrita
	 */
	public static final void documentWrite(final Document input, final OutputStream output) {
		/* implementação da escrita da arvore num ficheiro recorrendo ao XSLT */
		try {
			DOMSource domSource = new DOMSource(input);
			StreamResult resultStream = new StreamResult(output);
			TransformerFactory transformFactory = TransformerFactory.newInstance();

			// transformação vazia

			Transformer transformer = transformFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
			if (input.getXmlEncoding() != null)
				transformer.setOutputProperty(OutputKeys.ENCODING, input.getXmlEncoding());
			else
				transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			try {
				transformer.transform(domSource, resultStream);
			} catch (javax.xml.transform.TransformerException e) {

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Escreve arvore DOM num ficheiro
	 * 
	 * @param input  arvore DOM
	 * @param output ficheiro usado para escrita
	 */

	public static final boolean documentWrite(final Document input, final String output) {
		FileOutputStream fi = null;
		try {
			fi = new FileOutputStream(output);
			documentWrite(input, fi);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * Cria um DOM vazio com o elemento raiz indicado
	 * 
	 * @param root  nome do elemento raiz
	
	 */

	public static final Element documentNew(final String root) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document doc = builder.newDocument();
		doc.setXmlStandalone(true);
		// create the root element node
		return (Element) doc.appendChild(doc.createElement(root));
	}

	// Lista os nomes dos nós da árvore
	public static void traverse(Node node) {
		System.out.println("node: " + node.getNodeName());
		if (node.hasChildNodes()) {
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				traverse(child);
			}
		}
	}

	// capitalização de um String
	public static final String properStr(final String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}
	
		StringBuilder converted = new StringBuilder();
	
		boolean convertNext = true;
		for (char ch : text.toCharArray()) {
			if (Character.isSpaceChar(ch)) {
				convertNext = true;
			} else if (convertNext) {
				ch = Character.toTitleCase(ch);
				convertNext = false;
			} else {
				ch = Character.toLowerCase(ch);
			}
			converted.append(ch);
		}
	
		return converted.toString();
	}
}
