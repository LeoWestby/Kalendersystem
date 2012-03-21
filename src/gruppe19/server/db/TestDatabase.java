package gruppe19.server.db;

import gruppe19.gui.AppointmentDialogGUI;
import gruppe19.model.Appointment;
import gruppe19.model.User;

import java.sql.SQLException;
import java.util.ArrayList;

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
		assertFalse(DatabaseAPI.userNotExists(username));
		
	}
	
	public void testGetUser() throws SQLException{
		System.out.println("test get users");
		String brukernavn = "vegahar";
		User bruker1 = new User(brukernavn);
		User bruker2;
		
		assertTrue(DatabaseAPI.getUser(brukernavn).getUsername().equals(brukernavn));
		brukernavn="lollololololollololololol";
		
		bruker2 = DatabaseAPI.getUser(brukernavn);
		System.out.println(bruker2.getUsername());
		assertFalse(bruker2.getUsername().equals(brukernavn));
		
	}
	public void testgetid() throws SQLException{
		System.out.println("test getid");
		User a = new User("lolol", "vegard", "harper", 1023120, "passord");
		DatabaseAPI.insertUser(a);
		DatabaseAPI.tester();
	}
	
	public void testGetAppointments() throws SQLException{
		System.out.println("test get appointments");
		User a = new User("vegahar");
		ArrayList<Appointment> list = DatabaseAPI.findAppointments(a);
		for (Appointment appointment : list) {
			System.out.println(appointment.getTitle() + " " + appointment.getDateStart());
		}
		System.out.println();
	}
	
	public void testParticipant() throws SQLException{
		System.out.println("test participant");
		User a = new User("vegahar");
		ArrayList<Appointment> list = DatabaseAPI.findAppointmentsParticipant(a);
		for (Appointment appointment : list) {
			System.out.println(appointment.getTitle() + " " + appointment.getID());
		}
		System.out.println();
	}

}
