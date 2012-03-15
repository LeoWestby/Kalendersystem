package gruppe19.model;



public class Room {
	public final static String ROOM_PROPERTY = "RoomProperty";
	
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
	
	
}
