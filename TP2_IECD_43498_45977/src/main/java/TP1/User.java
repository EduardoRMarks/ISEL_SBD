package TP1;
/**
 * IECD 22/23 SV
 * Docente: Porfírio Filipe
 * 
 * Feito por:
 * Roman Ishchuk 43498
 * Eduardo Marques 45977
 */
/**
 * Classe auxiliar. Contem informação do user.
 */
public class User {
	private String nickname;
	private String password;
	private String nationality;
	private String age;
	private String profilePicturePath;
	private String wins;
	private String losses;
	private String time;
	private String color;

	public User(String nickname, String password, String nationality, String age, String profilePicturePath,
			String wins, String losses, String time, String color) {
		this.nickname = nickname;
		this.password = password;
		this.nationality = nationality;
		this.age = age;
		this.profilePicturePath = profilePicturePath;
		this.wins = wins;
		this.losses = losses;
		this.time = time;
		this.color = color;
	}

	public String getNickname() {
		return nickname;
	}

	public String getPassword() {
		return password;
	}

	public String getNationality() {
		return nationality;
	}

	public String getAge() {
		return age;
	}

	public String getProfilePicturePath() {
		return profilePicturePath;
	}

	public void setProfilePicturePath(String profilePicturePath) {
		this.profilePicturePath = profilePicturePath;
	}

	public String getWins() {
		return wins;
	}

	public String getLosses() {
		return losses;
	}

	public String getTime() {
		return time;
	}
	
	public String getColor() {
		return color;
	}

	public String toString() {
		return "Nickname: " + nickname + " Password: " + password + " Nationality: " + nationality + " Age: " + age
				+ " Picture Path: " + profilePicturePath;

	}
}
