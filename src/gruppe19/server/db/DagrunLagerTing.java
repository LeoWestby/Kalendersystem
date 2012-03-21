package gruppe19.server.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;


public class DagrunLagerTing extends DatabaseAPI{

	
	public static ArrayList<Appointment> findAppointmentsParticipant(User user) throws SQLException{
		if(userNotExists(user.getName())){
			throw new SQLException();
		}
		else{
			ArrayList<Appointment> liste = new ArrayList<Appointment>();
			String st = "SELECT * FROM deltager,avtale WHERE brukernavn LIKE "+user.getName()+" AND avtale.avtaleID = deltager.avtaleID";
			ResultSet rs = conn.createStatement().executeQuery(st);

			while(rs.next()){
				ArrayList<User> userList = getUserList(rs.getInt("avtaleID"));
				Room rom = new Room(rs.getString("romNavn"));
				User leder = new User(rs.getString("lederBrukernavn"));
				liste.add(new Appointment(rs.getInt("avtaleID"), rs.getString("avtalenavn"), 
						rs.getDate("dato"), rs.getDate("dato"), rs.getString("sted"),
						leder, rom , userList, rs.getString("beskrivelse")));
			}
			return liste;		
		}
	}
	
	
	public static ArrayList<Appointment> findAppointments(User user) throws SQLException{
		
		if(userNotExists(user.getName())){
			throw new SQLException();
		}
		else{
			ArrayList<Appointment> liste = new ArrayList<Appointment>();
			String st = "SELECT * FROM avtale WHERE lederBrukernavn LIKE "+user.getName()+" ";
			ResultSet rs = conn.createStatement().executeQuery(st);

			while(rs.next()){
				ArrayList<User> userList = getUserList(rs.getInt("avtaleID"));
				Room rom = new Room(rs.getString("romNavn"));
				User leder = new User(rs.getString("lederBrukernavn"));
				liste.add(new Appointment(rs.getInt("avtaleID"), rs.getString("avtalenavn"), 
						rs.getDate("dato"), rs.getDate("dato"), rs.getString("sted"),
						leder, rom , userList, rs.getString("beskrivelse")));
			}
			return liste;
		}
			
	}
	

	
	
	
	
	public static ArrayList<User> getUserList(int appointmentID) throws SQLException{
		ArrayList<User> liste = new ArrayList<User>();
		
		String st = "SELECT * FROM deltager,bruker WHERE avtaleID LIKE "+appointmentID+" AND bruker.brukernavn = deltager.brukernavn";
		ResultSet rs = conn.createStatement().executeQuery(st);	
		while(rs.next()){
			liste.add(new User(rs.getString("brukernavn"), rs.getString("fornavn"), rs.getString("etternavn"), rs.getInt("tlf"), rs.getString("passord")));
			
		}
		
		return liste;
		
	}
	
	
}
