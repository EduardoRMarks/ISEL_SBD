package model;

public class Pt {

	private int ID;
	private String ptEmail;
	private UserRole role;

	public Pt(int ID, String clienteEmail, UserRole role) {
		this.ID = ID;
		this.ptEmail = clienteEmail;
		this.role = role;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getPtEmail() {
		return ptEmail;
	}

	public void setPtEmail(String ptEmail) {
		this.ptEmail = ptEmail;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String toString() {
		return role + " com o ID " + ID + " e com o email " + ptEmail;

	}

}
