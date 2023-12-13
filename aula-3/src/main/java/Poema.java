import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Classe para manipulação de poemas
 * 
 * @author Prof. Porfírio Filipe
 * 
 */

/**
 * @author Porfírio
 *
 */
/**
 * @author Porfírio
 *
 */
public class Poema {
	public final static String contexto = DocXML.getContext() + "/xml/";
	public final static String poemas = DocXML.getContext() + "/poemas/";
	/**
	 * Verificação dos poemas existentes
	 * @return true se existe ealgum poema inválido
	 */
	public static boolean check() {
		System.out.println("Verificação dos poemas ... ");
		boolean erro = true;
		ArrayList<String> xml = DocXML.getFiles(Poema.poemas);
		for (int i = 0; i < xml.size(); i++)
			if (!DocXML.documentValidationXSD(Poema.poemas + xml.get(i), Poema.contexto + "poema.xsd")) {
				// System.out.println(xml.get(i));
				erro = false;
			}
		return erro;
	}
	// Devolve um poema com um determinado título.
	public static Document consultar(String titulo) {
		ArrayList<String> poemas = getPoemas();
		for (int i = 0; i < poemas.size(); i++) {
			Poema p = new Poema(poemas.get(i));
			if (p.temTitulo(titulo))
				return p.DOMDoc();
		}
		return null;
	}

	// Gerar o indice
	public static boolean geraIndice(String local) {
		return DocReadWrite.documentToFile(getIndice(), local);
	}

	// Devolve Poemas com o título indicado.
	public static ArrayList<String> getFicheiros(String título) {
		ArrayList<String> docPoemas = new ArrayList<String>();
		ArrayList<String> xmlPoemas = getPoemas();
		for (int i = 0; i < xmlPoemas.size(); i++) {
			Poema p = new Poema(xmlPoemas.get(i));
			if (título==null || p.getTitulo().compareToIgnoreCase(título) == 0)
				docPoemas.add(xmlPoemas.get(i));
		}
		return docPoemas;
	}

	// Devolve DOM com indice que inclui: tipo, ficheiro, titulo e autor, descriçao
	public static Document getIndice() {
		Element items = DocXML.documentNew("indice");
		Document newDoc = items.getOwnerDocument();
		// create a comment node given the specified string
		Comment comment = newDoc.createComment("Lista de Poemas");
		newDoc.insertBefore(comment, items);
		// vai buscar a lista de poemas
		ArrayList<String> poemas = getPoemas();
		for (int i = 0; i < poemas.size(); i++) {
			// System.out.println(i+"-"+poemas[i]);
			Poema p = new Poema(poemas.get(i));
			items.appendChild(p.getDescritor(newDoc));
		}
		return newDoc;
	}
	
	// devolve os nome dos ficheiros com poemas
	public static ArrayList<String> getPoemas() {
		return DocXML.getFiles(poemas);
	}
	// Devolve Poemas com o título indicado.
	public static ArrayList<Poema> getPoemas(String título) {
		ArrayList<Poema> docPoemas = new ArrayList<Poema>();
		ArrayList<String> xmlPoemas = getPoemas();
		for (int i = 0; i < xmlPoemas.size(); i++) {
			Poema p = new Poema(xmlPoemas.get(i));
			if (título==null || p.getTitulo().compareToIgnoreCase(título) == 0)
				docPoemas.add(p);
		}
		return docPoemas;
	}
	
	// Devolve DOM com lista de títulos dos poemas existentes.
	public static Document getTitulos() {
		Element titulos = DocXML.documentNew("títulos");
		Document newDoc = titulos.getOwnerDocument();
		// create a comment node given the specified string
		Comment comment = newDoc.createComment("Lista de títulos dos poemas");
		newDoc.insertBefore(comment, titulos);
		ArrayList<String> poemas = getPoemas();
		for (int i = 0; i < poemas.size(); i++) {
			// System.out.println(i+"-"+poemas[i]);
			Poema p = new Poema(poemas.get(i));
			String t = p.getTitulo();
			if (t != null) {
				Element tituloElement = newDoc.createElement("título");
				tituloElement.setTextContent(t);
				titulos.appendChild(tituloElement);
			}
		}
		return newDoc;
	}
	
	/**
	 * Ponto de entrada
	 * 
	 * @param args - sem argumentos
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ArrayList<String> xml = DocXML.getFiles(poemas);
		for (int i = 0; i < xml.size(); i++)
			if (DocXML.documentValidationXSD(poemas + xml.get(i), contexto + "poema.xsd")) {
				// System.out.println(xml.get(i));
			}
		System.out.println("Lista de poemas (em \" + poemas + \"): ");
		ArrayList<String> ficheiros = Poema.getPoemas();
		for(int i=0; i<ficheiros.size(); i++) {
			System.out.println("\t"+(i+1)+" - "+ficheiros.get(i));
		}
		int num;
		do {
			System.out.print("Indique o número associado ao ficheiro: ");
			num = sc.nextInt();
			sc.nextLine();
		} while (num < 1 || num > ficheiros.size());
		String poemaFileName = ficheiros.get(num - 1);
		Poema pm = new Poema(poemaFileName);
		if(pm.validar()) 
			pm.menu();
		sc.close();
	}
	
	/**
	 * Devolve os poemas que incluam um conjunto de palavras.
	 * @param palavras
	 * @return lista de poemas
	 */
	public static ArrayList<Poema> obter(String[] palavras) {
		ArrayList<Poema> docPoemas = new ArrayList<Poema>();
		ArrayList<String> xmlPoemas = getPoemas();
		for (int i = 0; i < xmlPoemas.size(); i++) {
			Poema p = new Poema(xmlPoemas.get(i));
			if (p.contem(palavras))
				docPoemas.add(p);
		}
		return docPoemas;
	}
	 
	Document D = null; // representa a arvore DOM com o poema
	String ficheiro = null;

	/**
	 * Construtor
	 * Cria um poema a partir do elemento XML indicado em parametro
	 * @param pm
	 */
	public Poema(Element pm) {
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			D = builder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Element clone = (Element) D.importNode(pm, true);
		D.appendChild(clone);
	}
 
	/**
	 * Construtor
	 * 
	 * @param XMLdoc - ficheiro que contém o documento XML
	 */
	public Poema(String XMLdoc) {
		ficheiro = XMLdoc;
		XMLdoc = poemas + XMLdoc;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringElementContentWhitespace(true);
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			File sourceFile = new File(XMLdoc);
			D = docBuilder.parse(sourceFile);
			Element raiz = D.getDocumentElement();
			if (raiz.hasAttribute("xmlns:xsi"))
				raiz.removeAttribute("xmlns:xsi");
			if (raiz.hasAttribute("xsi:noNamespaceSchemaLocation"))
				raiz.removeAttribute("xsi:noNamespaceSchemaLocation");
		} catch (ParserConfigurationException e) {
			System.out.println("Wrong parser configuration: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Could not read source file: " + e.getMessage());
		}
	}
 
	/**
	 * Acrescenta o verso indicado á estrofe referida pelo seu numero
	 * 
	 * @param numEstrofe - numero da estrofe [1..12]
	 * @param verso      - novo verso
	 * @return - indicação de erro
	 */
	public boolean acrescenta(short numEstrofe, String verso) {
		System.out.println("Acrescenta o verso \"" + verso + "\" à estrofe " + numEstrofe + ".");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			Element vers = D.createElement("verso");
			vers.setTextContent(verso);
			estrofe.appendChild(vers);
			return true;
		}
		return false;
	}
	// acrescenta um comentário sobre a origem do poema
	public void addComment(String com) {
		if (D == null)
			return;
		Node element = D.getElementsByTagName("poema").item(0);
		Comment comment = D.createComment(com);
		element.appendChild(comment);
	}

	/**
	 * Apresenta o poema na sua forma textual clássica
	 */
	public void apresenta() {
		Element root = D.getDocumentElement();
		Element titulo = (Element) root.getElementsByTagName("título").item(0);
		System.out.println("Título: " + titulo.getTextContent());
		System.out.println();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			// podia simplesmente ir buscar os filhos da estrofe
			// assim só vai buscar exatamente os que são versos
			NodeList versos = estrofe.getElementsByTagName("verso");
			for (int i = 0; i < versos.getLength(); i++)
				System.out.println(versos.item(i).getTextContent());
			System.out.println();
		}
		Element autor = (Element) root.getElementsByTagName("autor").item(0);
		System.out.println("Autor: " + autor.getTextContent());
	}

	/**
	 * Classifica as estrofes quanto ao número de versos.
	 */
	public void classifica() {
		System.out.println("Classificação das estrofes quanto à quantidade de versos:");
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		System.out.println("O poema tem " + estrofes.getLength() + " estrofes.");
		for (int e = 0; e < estrofes.getLength(); e++) {
			Element estrofe = (Element) estrofes.item(e);
			NodeList versos = estrofe.getElementsByTagName("verso");
			System.out.println(e + 1 + "ª estrofe: " + escreveExtenso((short) versos.getLength()));
		}
	}

	/**
	 * O mesmo que a anterior usando XPATH
	 */
	public void classificaXPATH() {
		System.out.println("Classificação das estrofes quanto à quantidade de versos:");
		// só está disponivel XPATH 1.0 :
		// https://docs.oracle.com/en/java/javase/13/docs/api/java.xml/javax/xml/xpath/package-summary.html
		// com XPATH 2.0 seria só usar : "/poema/estrofe/count(verso)"

		// NodeList estrofes = XMLDoc.getXPath("/poema/estrofe", D);

		int qtEstrofes = DocXML.documentXPathN(D, "count(/poema/estrofe)");
		System.out.println("O poema tem " + qtEstrofes + " estrofes.");
		for (int e = 0; e < qtEstrofes; e++) {
			// contar os versos
			int nVersos = DocXML.documentXPathN(D, "count(/poema/estrofe[position()=" + (e + 1) + "]/verso)");
			System.out.println(e + 1 + "ª estrofe: " + escreveExtenso((short) nVersos));
		}
	}

	// permite testar se o poema tem a palavra assinalada em parametro
	public boolean contem(String palavra) {
		Element root = D.getDocumentElement();
		NodeList versos = root.getElementsByTagName("verso");
		for (int i = 0; i < versos.getLength(); i++) {
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				return true;
		}
		return false;
	}

	// permite testar se o poema tem as palavras assinaladas em parametro
	public boolean contem(String[] palavras) {
		for (int i = 0; i < palavras.length; i++)
			if (!contem(palavras[i]))
				return false;
		return true;
	}

	/**
	 * @return documento DOM que representa o poema
	 */
	public Document DOMDoc() {
		return D;
	}

	/**
	 * Retorna a designação referente ao número de versos
	 * 
	 * @param numero
	 * @return
	 */
	private String escreveExtenso(short numeroVersos) {
		switch (numeroVersos) {
		case 1:
			return "Monástico";
		case 2:
			return "Dístico ou parelha";
		case 3:
			return "Terceto";
		case 4:
			return "Quadra";
		case 5:
			return "Quintilha";
		case 6:
			return "Sextilha";
		case 7:
			return "Sétima";
		case 8:
			return "Oitava";
		case 9:
			return "Nona";
		case 10:
			return "Décima";
		default:
			return "Irregular (" + numeroVersos + ")";
		}
	}

	/**
	 * Verifica se a palavra ocorre no verso
	 * 
	 * @param palavra
	 * @param verso
	 * @return
	 */
	private boolean estaPresente(String palavra, String verso) {
		StringTokenizer st = new StringTokenizer(verso);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.compareToIgnoreCase(palavra) == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ",") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ".") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + ":") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + "...") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + "!") == 0)
				return true;
			if (token.compareToIgnoreCase(palavra + "?") == 0)
				return true;
			if (token.compareToIgnoreCase("-" + palavra) == 0)
				return true;
			if (token.compareToIgnoreCase("(" + palavra + ")") == 0 || token.compareToIgnoreCase("(" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + ")") == 0)
				return true;
			if (token.compareToIgnoreCase("'" + palavra + "'") == 0 || token.compareToIgnoreCase("'" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + "'") == 0)
				return true;
			if (token.compareToIgnoreCase("\"" + palavra + "\"") == 0 || token.compareToIgnoreCase("\"" + palavra) == 0
					|| token.compareToIgnoreCase(palavra + "\"") == 0)
				return true;
		}
		return false;
	}

	/**
	 * Devolve o autor do poema
	 */
	public String getAutor() {
		Element root = D.getDocumentElement();
		Element autor = (Element) root.getElementsByTagName("autor").item(0);
		if (autor == null)
			return null;
		return autor.getTextContent();
	}

	public Element getDescritor(Document doc) {
		Element itemElement = doc.createElement("descritor");
		itemElement.setAttribute("tipo", "poema");
		itemElement.setAttribute("ficheiro", ficheiro);
		itemElement.setAttribute("título", getTitulo());
		itemElement.setAttribute("autor", getAutor());
		itemElement.setTextContent("** Descrição/Resumo: "+getTitulo()+" **");
		return itemElement;
	}

	/**
	 * Devolve um elemento com o poema
	 * @return
	 */
	public Element getElement() {
		return D.getDocumentElement();
		//(Element) (fd.getElementsByTagName("poema").item(0));
	}
	/**
	 * Devolve o título do poema
	 */
	public String getTitulo() {
		Element root = D.getDocumentElement();
		Element titulo = (Element) root.getElementsByTagName("título").item(0);
		if (titulo == null)
			return null;
		return DocXML.properStr(titulo.getTextContent());
	}

	/**
	 * Lista os versos que incluem a palavra indicada
	 * 
	 * @param palavra - palavra usada para selecionar os versos
	 */
	public void indica(String palavra) {
		System.out.println("Indica os versos com a  palavra \"" + palavra + "\":\n");
		Element root = D.getDocumentElement();
		NodeList versos = root.getElementsByTagName("verso");
		for (int i = 0; i < versos.getLength(); i++)
			if (estaPresente(palavra, versos.item(i).getTextContent()))
				System.out.println(versos.item(i).getTextContent());
	}

	/**
	 * Faz 'aproximadamente' o mesmo que o anterior usando XPATH
	 * 
	 * @param palavra - palavra usada para selecionar os versos
	 */
	public void indicaXPATH(String palavra) {
		System.out.println("Indica os versos com a  palavra \"" + palavra + "\":\n");
		NodeList versos = DocXML.documentXPath(D,"/poema/estrofe/verso[contains(normalize-space(.), '" + palavra + "')]");
		for (int i = 0; i < versos.getLength(); i++)
			// if (estaPresente(palavra, versos.item(i).getTextContent())) // reforço
			System.out.println(versos.item(i).getTextContent());
	}

	/**
	 * Menu com as opções disponiveis na aplicação
	 */
	public void menu() {
		char op;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println();
			System.out.println();
			System.out.println("*** Menu Poema ***");
			System.out.println("1 - Apresenta o poema na sua forma escrita clássica");
			System.out.println("2 – Classifica as estrofes quanto ao número de versos");
			System.out.println("3 – Acrescenta um verso no fim da estrofe indicada");
			System.out.println("4 – Remove uma determinada estrofe");
			System.out.println("5 – Indica os versos que contêm determinada palavra");

			System.out.println("A – Classificar (XPATH) as estrofes quanto ao número de versos");
			System.out.println("B – Indicar (XPATH) os versos que contêm determinada palavra");
			System.out.println("C – Gravar (XSLT-XML) eventuais alterações realizadas no poema");
			System.out.println("D – Gravar (XSLT-HTML) o poema na sua forma escrita clássica");
			System.out.println("E – Mostrar (XSLT-TXT) o poema contando as estrofes, versos e sílabas");
			System.out.println("F – Validar (XSD) genericamente um poema");
			System.out.println("G – Reconhecer (XSD) soneto");
			System.out.println("H – Reconhecer (XSD) haicai");

			System.out.println("0 - Terminar!");
			String str = sc.nextLine();
			if (str != null && str.length() > 0)
				op = str.toUpperCase().charAt(0);
			else
				op = ' ';
			switch (op) {
			case '1':
				apresenta();
				break;
			case '2':
				classifica();
				break;
			case '3':
				System.out.println("Indique o numero da estrofe:");
				short i = sc.nextShort();
				sc.nextLine();
				System.out.println("Escreva o verso:");
				String verso = sc.nextLine();
				if (acrescenta(i, verso))
					apresenta();
				else
					System.out.println("Não acrescentou!");
				break;
			case '4':
				System.out.println("Indique o numero da estrofe a remover:");
				short r = sc.nextShort();
				sc.nextLine();
				if (remove(r))
					apresenta();
				else
					System.out.println("Não removeu!");
				break;
			case '5':
				System.out.println("Escreva a palavra:");
				String palavra = sc.nextLine();
				indica(palavra);
				break;
			case 'A': // Classificar (XPATH) as estrofes quanto ao número de versos.
				classificaXPATH();
				break;
			case 'B': // Indicar (XPATH) os versos que contêm determinada palavra
				System.out.println("Escreva a palavra:");
				String pal = sc.nextLine();
				indicaXPATH(pal);
				break;
			case 'C': // Gravar (XML) eventuais alterações realizadas no poema.
				System.out.println("Indique o nome do ficheiro (em " + Poema.contexto
						+ ") para guardar em 'down' o poema (ex: novopoema.xml):");
				String poemaFileName = sc.nextLine();
				save("down/"+poemaFileName);
				break;
			case 'D': // Gravar (HTML) o poema na sua forma escrita clássica
				System.out.println("Grava o poema na sua forma escrita (em html) clássica 'poema.html'");
				DocXML.documentTransformation(D, contexto + "poema_xml_to_html.xsl", contexto + "poema.html");
				break;
			case 'E': // Mostrar (XSLT-TXT) o poema contando as estrofes, versos e sílabas
				System.out.println("Mostrar o poema contando as estrofes, versos e sílabas");
				DocXML.documentTransformation(D, contexto + "poema_xml_to_txt.xsl", System.out);
				break;
			case 'F': // Validar/Reconhecer genericamente um poema
				System.out.println("Indique o nome do ficheiro (em " + Poema.contexto
						+ ") que representa o esquema XML (ex: poema.xsd, poemax.xsd):");
				String xsdFileName = sc.nextLine();
				if (xsdFileName.length() == 0) {
					xsdFileName = "poema.xsd";
					System.out.println("Foi assumido o esquema XML representado em: " + xsdFileName);
				}
				if (DocXML.documentValidationXSD(D, contexto + xsdFileName))
					System.out.println("Validação realizada com sucesso!");
				else
					System.out.println("Falhou a validação (" + xsdFileName + ")!");
				break;
			case 'G': // Reconhecer um soneto
				System.out.println(
						"Aplica ao DOM a transformação (poema_xml_to_xml.xsl) e aplica ao resultado (soneto.xml) o esquema XML (soneto.xsd).");
				// gera o ficheiro soneto.xml para manter um registo temporário
				DocXML.documentTransformation(D, contexto + "poema_xml_to_xml.xsl", contexto + "soneto_tmp.xml");
				if (DocXML.documentValidationXSD(contexto + "soneto_tmp.xml", contexto + "soneto.xsd"))
					System.out.println("Recohecimento do soneto realizada com sucesso!");
				else
					System.out.println("Falhou o recohecimento do soneto (soneto.xsd)!");
				break;
			case 'H': // Reconhecer um haicai
				System.out.println(
						"Aplica ao DOM a transformação (poema_xml_to_xml.xsl) e aplica ao DOM resultado o esquema XML (haicai.xsd).");
				// gera o DOM temporário
				Document tempDoc = null;
				try {
					tempDoc = DocXML.documentTransformation(D, contexto + "poema_xml_to_xml.xsl");
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				} catch (FactoryConfigurationError e) {
					e.printStackTrace();
				}
				if (tempDoc != null && DocXML.documentValidationXSD(tempDoc, contexto + "haicai.xsd"))
					System.out.println("Reconhecimento do haicai realizada com sucesso!");
				else
					System.out.println("Falhou o reconhecimento do haicai (haicai.xsd)!");
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

	/**
	 * @param numEstrofe - número da estrofe [1..n]
	 * @return true se a estrofe foi removida
	 */
	public boolean remove(short numEstrofe) {
		Element root = D.getDocumentElement();
		NodeList estrofes = root.getElementsByTagName("estrofe");
		if (numEstrofe <= estrofes.getLength()) {
			Element estrofe = (Element) estrofes.item(numEstrofe - 1);
			estrofe.getParentNode().removeChild(estrofe);
			System.out.println("Removeu a " + numEstrofe + "ª estrofe.");
			return true;
		}
		return false;
	}

	/**
	 * Escreve arvore DOM no ficheiro indicado
	 * 
	 * @param output - caminho relativo usado para escrever
	 * @return - indicação de erro
	 */
	public boolean save(final String output) {// output é o caminho relativo
		return DocXML.documentWrite(D,DocXML.getContext()+"/"+output);
	}

	// permite testar se o poema tem o título indicado
	public boolean temTitulo(String titulo) {
		return titulo.compareTo(getTitulo()) == 0;
	}

	// valida o documento para confirmar que se trata de um poema
	public boolean validar() {
		if (D == null)
			return false;
		return DocXML.documentValidationXSD(D, contexto + "poema.xsd");
	}
}
