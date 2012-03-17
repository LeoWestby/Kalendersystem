package gruppe19.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Appointment implements Serializable {
	
	private String title;
	private Date dateStart;
	private Date dateEnd;
	private String place;
	private User owner;
	private Room room;
	private ArrayList<User> userList;
	private String description;
	
	
	public Appointment(){
		this.title="";
		this.dateStart = new Date();
		this.dateEnd = new Date();
		this.room = new Room("");
		this.userList = new ArrayList<User>();
			
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Date getDateStart() {
		return dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}


	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
		
		//WTF is this?
//		Date a = new Date(dateStart.getTime());
//		a.setHours(dateStart.getHours()+1);
//		this.dateEnd  =a;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public User getOwner() {
		return owner;
	}


	public void setOwner(User owner) {
		this.owner = owner;
	}


	public Room getRoom() {
		return room;
	}


	public void setRoom(Room room) {
		this.room = room;
	}


	public ArrayList<User> getUserList() {
		return userList;
	}


	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	


	
}
