package gruppe19.model;

public class User {
	
	private String username, name, lastname;
	
	public User(String name, String lastname) {
		this.name = name;
		this.lastname=lastname;
	}
	public String getName(){
		return name + " " + lastname;
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


	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	
}
