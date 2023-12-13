import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Indice {
	Document I = null;
	String file=null;

	Indice(String file) {
		this.file=file;
		I = DocReadWrite.documentFromFile(file);
	}

	public void show() {
		System.out.println("\nDescritores do indice \""+file+"\": ");
		NodeList indice = I.getElementsByTagName("descritor");
		for (int i = 0; i < indice.getLength(); i++) {
			Node item = indice.item(i);
			NamedNodeMap attributes = item.getAttributes();
			for (int j = 0; j < attributes.getLength(); j++) {
				Attr attr = (Attr) attributes.item(j);
				String attrName = attr.getNodeName();
				String attrValue = attr.getNodeValue();
				System.out.print(attrName + " = \"" + attrValue+"\" ");
			}
			System.out.println("("+item.getTextContent()+")");
		}
	}
	// Falta validar com o indice.xsd
	public static void main(String args[]) {
		Poema.geraIndice(MyFile.getContext()+"/xml/indice-poemas.xml");
		Indice indP = new Indice(DocXML.getContext()+"/xml/indice-poemas.xml");
		indP.show();
		Foto.geraIndice(MyFile.getContext()+"/xml/indice-fotos.xml");
		Indice indF = new Indice(DocXML.getContext()+"/xml/indice-fotos.xml");
		indF.show();
	}
}
