package gruppe19.server.db;

import gruppe19.gui.AppointmentDialogGUI;
import gruppe19.model.Appointment;
import gruppe19.model.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.fp.net.cl.ClException;

import junit.framework.TestCase;

public class TestDatabase extends TestCase{
	
	protected void setUp() {
		DatabaseAPI.open();
	}
	protected void tearDown(){
//		DatabaseAPI.close();
	}
	public void testExsistUser()throws SQLException{
		System.out.println("test exsist user");
		String username = "vegahar";
		assertFalse(DatabaseAPI.userNotExists(username));
		username = "dagrunki";
		assertFalse(DatabaseAPI.userNotExists(username));
		username = "lol";
		assertTrue(DatabaseAPI.userNotExists(username));
		
	}
	
	public void testGetUser() throws SQLException{
		System.out.println("test get users");
		String brukernavn = "dagrun";
		User bruker1 = new User(brukernavn);
		
		assertTrue(DatabaseAPI.getUser(brukernavn).getUsername().equals(brukernavn));
		brukernavn="lollololololollololololol";
		
		assertNull(DatabaseAPI.getUser(brukernavn));
		
	}
//	public void testgetid() throws SQLException{
//		System.out.println("test getid");
//		User a = new User("lolol", "vegard", "harper", 1023120, "passord");
//		DatabaseAPI.insertUser(a);
//	}
	
	public void testGetAppointments() throws SQLException{
		System.out.println("test get appointments");
		User a = new User("dagrun");
		ArrayList<Appointment> list = DatabaseAPI.findAppointments(a);
		for (Appointment appointment : list) {
			System.out.println(appointment.getTitle() + " " + appointment.getID() + " " 
					+appointment.getPlace() + " " + appointment.getDescription());
		}
		System.out.println();
	}
	
	public void testParticipant() throws SQLException{
		System.out.println("test participant");
		User a = new User("leon");
		ArrayList<Appointment> list = DatabaseAPI.findAppointmentsParticipant(a);
		for (Appointment appointment : list) {
			System.out.println(appointment.getTitle() + " " + appointment.getID() + " " 
					+appointment.getPlace() + " " + appointment.getDescription());
		}
		System.out.println();
		

	}
	
	public void testInsertApp() throws SQLException{
		Appointment a = new Appointment();
		AppointmentDialogGUI gui = new AppointmentDialogGUI();
		a.setOwner(new User("fredrik"));
		gui.setModel(a);
		gui.setVisible(true);
		DatabaseAPI.createAppointment(a);	
	}
	
	public void testUpdateAppointment() throws SQLException{
		User a = new User("dagrunki");
		ArrayList<Appointment> list = DatabaseAPI.findAppointments(a);
		list.get(0).getDateEnd().getHours();
		Appointment b = list.get(0);
		System.err.println(b.getTitle());
		b.setTitle("Reiser hjem");
		b.setPlace("toget");
		DatabaseAPI.updateAppointment(b);
		
	}
	
	public void testchangeStatus() throws SQLException{
		User a = DatabaseAPI.getUser("dagrun");
		ArrayList<Appointment> b  = DatabaseAPI.findAppointmentsParticipant(a);

		Appointment c = b.get(0);
		DatabaseAPI.changeParticipantStatus(a, c, 3);
	}

}
