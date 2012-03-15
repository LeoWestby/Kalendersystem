package gruppe19.model;

import java.util.Date;


public class Appointment {
	
	private String title;
	private Date dateStart;
	private Date dateEnd;
	private String place;
	private User owner;
	
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
	public Appointment(){
		this.title="";
		this.dateStart = new Date();
		this.dateEnd = new Date();
	
		
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
		
	}

	
}
