package model;

public class Room {

	private int id;
	private int nifClube;
	private String nome;
	private int ocupacaoMaxima;
	private int estado;
	private String imagem;

	// Constructors, getters, and setters

	public Room() {
	}

	public Room(int id, int nifClube, String nome, int ocupacaoMaxima, int estado, String imagem) {
		this.id = id;
		this.nifClube = nifClube;
		this.nome = nome;
		this.ocupacaoMaxima = ocupacaoMaxima;
		this.estado = estado;
		this.imagem = imagem;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNifClube() {
		return nifClube;
	}

	public void setNifClube(int nifClube) {
		this.nifClube = nifClube;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getOcupacaoMaxima() {
		return ocupacaoMaxima;
	}

	public void setOcupacaoMaxima(int ocupacaoMaxima) {
		this.ocupacaoMaxima = ocupacaoMaxima;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	@Override
	public String toString() {
		return "Room{" + "id=" + id + ", nifClube='" + nifClube + '\'' + ", nome='" + nome + '\'' + ", ocupacaoMaxima="
				+ ocupacaoMaxima + ", estado=" + estado + ", imagem='" + imagem + '\'' + '}';
	}
}
