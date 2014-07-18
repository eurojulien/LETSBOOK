package element;


public class User {

	private String id;
	private String lastName;
	private String firstName;
	private String email;
	private String phone;
	private String password;
	private String googlePlusID;
	private String faceBookID;
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getGooglePlusID() {
		return googlePlusID;
	}
	public void setGooglePlusID(String googlePlusID) {
		this.googlePlusID = googlePlusID;
	}
	public String getFaceBookID() {
		return faceBookID;
	}
	public void setFaceBookID(String faceBookID) {
		this.faceBookID = faceBookID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
