package gruppe19.model;

import java.beans.PropertyChangeSupport;

public class Room {

	private PropertyChangeSupport pcs;
	
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
	
	public void setValues(Room newRoom){
		setName(newRoom.getName());
	}
}
