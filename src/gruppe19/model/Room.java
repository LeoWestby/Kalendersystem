package gruppe19.model;

import java.io.Serializable;


/**
 * Model for holding a room data 
 */
public class Room implements Serializable {
	
	private String name;

	public Room(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setRoom(Room a){
		setName(a.getName());
	}
	
	
}
