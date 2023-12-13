import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Eng. Porfírio Filipe
 *
 */

// class para ser usada no lado do cliente
public class CliStub extends Comando {
	
	public CliStub() {
		super();
	}

	public CliStub(Document D) {
		super(D);
	}
	
	// constroí o comando listar
	public Document listar() {
		Element listar = cmd.createElement("listar");
		Element request = cmd.createElement("request");
		listar.appendChild(request);
		Element reply = cmd.createElement("reply");
		listar.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(listar);
		return cmd;
	}

	// constroí o comando consultar
	public Document consultar(String tit) {
		Element consultar = cmd.createElement("consultar");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		Element titulo = cmd.createElement("título");
		// acrescentar o titulo
		titulo.appendChild(cmd.createTextNode(tit));
		request.appendChild(titulo);
		consultar.appendChild(request);
		consultar.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(consultar);
		return cmd;
	}

	// constroí o comando obter
	public Document obter(String[] palavras) {
		Element obter = cmd.createElement("obter");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		obter.appendChild(request);
		obter.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(obter);
		// criar lista de palavras
		for (int i = 0; i < palavras.length; i++) {
			Element palavra = cmd.createElement("palavra");
			palavra.appendChild(cmd.createTextNode(palavras[i]));
			request.appendChild(palavra);
		}
		return cmd;
	}

	// constroí o comando para submeter o poema ou a foto
	public Document submeter(Object pf) {
		Element submeter = cmd.createElement("submeter");
		Element request = cmd.createElement("request");
		Element reply = cmd.createElement("reply");
		submeter.appendChild(request);
		submeter.appendChild(reply);
		Element protocol = (Element) cmd.getElementsByTagName("protocol").item(0);
		protocol.appendChild(submeter);
		Element clone = null;
		if (pf instanceof Poema)
			clone = (Element) cmd.importNode(((Poema)pf).getElement(), true);
		else if (pf instanceof Foto)
			clone = (Element) cmd.importNode(((Foto)pf).getElement(), true);
		else
			return cmd;  // não deve acontecer
		request.appendChild(clone);
		return cmd;
	}
}

