package gruppe19.model;

import java.io.Serializable;

public class User implements Serializable {
	
	private String username, firstname, lastname, tlfnr, password;
	
	/**
	 * @deprecated Use the other constructor instead.
	 */
	public User(String brukernavn){
		this.username=brukernavn;
	}
	
	public User(String firstname, String lastname) {
		this.firstname = firstname;
		this.lastname=lastname;
	}
	
	
	
	public User(String username, String firstname, 
				String lastname, String tlfnr, String password) {
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.tlfnr = tlfnr;
		this.password = password;
	}



	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
