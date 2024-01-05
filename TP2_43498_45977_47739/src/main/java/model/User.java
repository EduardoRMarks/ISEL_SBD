package model;


public class User {
    private int NIF;
    private String userEmail;
    private UserRole role;

    public User(int NIF, String userEmail, UserRole role) {
        this.NIF = NIF;
        this.userEmail = userEmail;
        this.role = role;
    }
    
    public int getNIF() {
        return NIF;
    }

    public void setNIF(int NIF) {
        this.NIF = NIF;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public String toString() {
		return role + " com o NIF " + NIF + " e com o email " + userEmail ;
		
	}
}
