import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Eng. Porfírio Filipe
 *
 */
// class para ser usada no lado do servidor
public class SrvStub extends Comando {

	public SrvStub(Document D) {
		super(D);
	}

	// constroí a resposta ao comando listar
	@SuppressWarnings("unused")
	private Document listar() {
		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		Document titulos = Poema.getTitulos(); // obtem os titulos dos poemas
		NodeList T = titulos.getElementsByTagName("título");
		for (int i = 0; i < T.getLength(); i++) {
			Element clone = (Element) cmd.importNode(T.item(i), true);
			reply.appendChild(clone);
		}
		// show();
		Document I = Foto.getIndice();
		NodeList indice = I.getElementsByTagName("descritor");
		for (int i = 0; i < indice.getLength(); i++) {
			Node item = indice.item(i);
			Element clone = (Element) cmd.importNode(item, true);
			reply.appendChild(clone);
		}
		//show();
		I = Poema.getIndice();
		indice = I.getElementsByTagName("descritor");
		for (int i = 0; i < indice.getLength(); i++) {
			Node item = indice.item(i);
			Element clone = (Element) cmd.importNode(item, true);
			reply.appendChild(clone);
		}
		return cmd;
	}
	// constroí a resposta ao comando consultar
	@SuppressWarnings("unused")
	private Document consultar() {
		NodeList T = cmd.getElementsByTagName("título");
		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		Document poema = Poema.consultar(T.item(0).getTextContent());
		if (poema != null) {
			System.out.println("Existe 1 poema!");
			Element p = (Element) poema.getElementsByTagName("poema").item(0);
			Element clone = (Element) cmd.importNode(p, true);
			if (clone.hasAttribute("xmlns:xsi"))
				clone.removeAttribute("xmlns:xsi");
			if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
				clone.removeAttribute("xsi:noNamespaceSchemaLocation");
			reply.appendChild(clone);
		}
		ArrayList<Document> fotos = Foto.consultar(T.item(0).getTextContent());
		System.out.println("Existe(m) "+fotos.size()+" foto(s)!");
		
		for(int i=0; i<fotos.size(); i++) {
			Element f = (Element) (fotos.get(i).getElementsByTagName("fotografia").item(0));
			Element clone = (Element) cmd.importNode(f, true);
			if (clone.hasAttribute("xmlns:xsi"))
				clone.removeAttribute("xmlns:xsi");
			if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
				clone.removeAttribute("xsi:noNamespaceSchemaLocation");
			reply.appendChild(clone);
		}
		//show();
		return cmd;
	}

	// constroí a resposta ao comando obter
	@SuppressWarnings("unused")
	private Document obter() {
		NodeList pal = cmd.getElementsByTagName("palavra");

		ArrayList<String> palList = new ArrayList<String>();
		for (int i = 0; i < pal.getLength(); i++) {
			palList.add(pal.item(i).getTextContent());
		}
		String[] palavras = palList.toArray(new String[palList.size()]);

		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		ArrayList<Poema> poemas = Poema.obter(palavras);
		for (int i = 0; i < poemas.size(); i++) {
			Element p = (Element) poemas.get(i).DOMDoc().getElementsByTagName("poema").item(0);
			Element clone = (Element) cmd.importNode(p, true);
			if (clone.hasAttribute("xmlns:xsi"))
				clone.removeAttribute("xmlns:xsi");
			if (clone.hasAttribute("xsi:noNamespaceSchemaLocation"))
				clone.removeAttribute("xsi:noNamespaceSchemaLocation");
			reply.appendChild(clone);
		}
		return cmd;
	}

	// constroí a resposta ao comando submeter
	@SuppressWarnings("unused")
	private Document submeter() {
		String target = "down";
		NodeList P = cmd.getElementsByTagName("poema");
		if(P.getLength()>0) {
			Poema pm = new Poema((Element) P.item(0));
			if(estado(pm.save(target+"/"+pm.getTitulo() + ".xml")))
				System.out.println("Ficheiro \""+pm.getTitulo()+"\" gravado em: "+target);
		}
		NodeList F = cmd.getElementsByTagName("fotografia");
		if(F.getLength()>0) {
			Foto ft = new Foto((Element) F.item(0));
			if(estado(ft.save(target).length()>0))
				System.out.println("Ficheiro \""+ft.getTitulo()+"\" gravado em: "+target);
		}
		return cmd;
	}

	// Acrescenta o indicador erro
	public void erro() {
		estado("erro");
	}

	// Acrescenta o indicador sucesso
	public void sucesso() {
		estado("sucesso");
	}
	
	// Acrescenta o indicador de estado da resposta
	private void estado(String estado) {
		Element ret = cmd.createElement(estado);
		Element reply = (Element) cmd.getElementsByTagName("reply").item(0);
		reply.appendChild(ret);
	}
	
	// Acrescenta o indicador dependendo do estado
	private boolean estado(boolean estado) {
		if(estado)
			sucesso();
		else 
			erro();	
		return estado;
	}

	// chama os metodos referidos no protocolo
	public Document executar() {
		Node protocolo = cmd.getElementsByTagName("protocol").item(0);
		NodeList filhos = protocolo.getChildNodes(); 
		for (int i = 0; i < filhos.getLength(); i++) {
			Node filho = filhos.item(i);
			if (filho.getNodeType() == Node.ELEMENT_NODE) { // podem haver nós de texto
				try {
					String mt = filho.getNodeName();
					mt = mt.substring(0, 1).toUpperCase() + mt.substring(1, mt.length());
					System.out.println("Chama o metodo: "+mt);
					
					// alternativa: chama dinamicamente (usando reflexão) os metodos referidos no protocolo
					
					Method methodTocall;
					try {
						methodTocall = getClass().getDeclaredMethod(mt.toLowerCase());
						return (Document) methodTocall.invoke(this);
					} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
						System.err.println("Exepção na chamada de metodo  \""+mt+"\": " + e.getMessage());
						e.printStackTrace();
					} 

					// alternativa: chama os metodos referidos no protocolo indicando o seu nome
					/*
					if(mt.compareToIgnoreCase("listar")==0) return listar();
					if(mt.compareToIgnoreCase("consultar")==0) return consultar(); 
					if(mt.compareToIgnoreCase("obter")==0) return obter(); 
					if(mt.compareToIgnoreCase("submeter")==0) return submeter(); 
					*/

				} catch (SecurityException | IllegalArgumentException e) {
					System.err.println("Exepção na chamada de metodo  (mt): " + e.getMessage());
					e.printStackTrace();
				}
				break; // sai do for
			}
		}
		return null;
	}
}
