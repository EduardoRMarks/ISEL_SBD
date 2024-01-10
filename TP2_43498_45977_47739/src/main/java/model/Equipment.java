package model;

public class Equipment {

	private int id;
	private int nifClube;
	private String nome;
	private String demonstracao;
	private int estado;
	private String imagem;

	public Equipment() {
	}

	public Equipment(int id, int nifClube, String nome, String demonstracao, int estado, String imagem) {
		this.id = id;
		this.nifClube = nifClube;
		this.nome = nome;
		this.demonstracao = demonstracao;
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

	public String getDemonstracao() {
		return demonstracao;
	}

	public void setDemonstracao(String demonstracao) {
		this.demonstracao = demonstracao;
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
		return "Equipment{" + "id=" + id + ", nifClube='" + nifClube + '\'' + ", nome='" + nome + '\''
				+ ", demonstracao='" + demonstracao + '\'' + ", estado=" + estado + ", imagem='" + imagem + '\'' + '}';
	}
}
