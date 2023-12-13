import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

/**
 * @author Eng. Porfírio Filipe
 *
 *         Classe para manipular os serviços/comandos associados ao
 *         protocolo Define aspetos comuns ao cliente e ao servidor
 * 
 */

public class Comando {
	Document cmd = DocXML.documentNew("protocol").getOwnerDocument();

	public Comando() {
		
	}
	public Comando(Document doc) {
		cmd = doc;
	}
	
	public void show() {
		System.out.println();
		try {
			DocReadWrite.documentToStream(cmd, System.out);
			System.out.println();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	// valida se o comando respeita o protocolo
	protected boolean validar() {
		return DocXML.documentValidationXSD(cmd, Poema.contexto + "protocol.xsd");
	}
}
