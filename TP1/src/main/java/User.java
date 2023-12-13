public class User {
    private String nickname;
    private String password;
    private String nationality;
    private String age;
    private String profilePicturePath;

    public User(String nickname, String password, String nationality, String age, String profilePicturePath) {
        this.nickname = nickname;
        this.password = password;
        this.nationality = nationality;
        this.age = age;
        this.profilePicturePath = profilePicturePath;
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
    
    public String toString() {
		return "Nickname: " + nickname + " Password: "+ password + " Nationality: "+ nationality+ " Age: " + age + " Picture Path: " + profilePicturePath;
    	
    }
}
