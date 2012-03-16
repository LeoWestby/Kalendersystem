package gruppe19.model;

public class User {
	
	private String username, firstname, lastname, tlfnr;
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getTlfnr() {
		return tlfnr;
	}
	public void setTlfnr(String tlfnr) {
		this.tlfnr = tlfnr;
	}
	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname=lastname;
	}
	public String getName(){
		return firstname + " " + lastname;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if(username==null){
			throw new NullPointerException();
		}
		this.username = username;
	}


	public void setfirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getfirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
}
