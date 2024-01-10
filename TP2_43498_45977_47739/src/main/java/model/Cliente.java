package model;

public class Cliente {

	private int NIF;
	private String clienteEmail;
	private UserRole role;
	private String age;

	public Cliente(int NIF, String clienteEmail, UserRole role) {
		this.NIF = NIF;
		this.clienteEmail = clienteEmail;
		this.role = role;
	}

	public int getNIF() {
		return NIF;
	}

	public void setNIF(int NIF) {
		this.NIF = NIF;
	}

	public String getUserEmail() {
		return clienteEmail;
	}

	public void setUserEmail(String userEmail) {
		this.clienteEmail = userEmail;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String toString() {
		return role + " com o NIF " + NIF + " e com o email " + clienteEmail;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAge() {
		return age;
	}

}