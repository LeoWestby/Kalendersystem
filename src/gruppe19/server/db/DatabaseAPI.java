package gruppe19.server.db;

import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.io.ObjectInputStream.GetField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.fp.model.Person;


/**
 * A static class used for communication between with the server and the SQL server.
 */
public class DatabaseAPI {
	private static Connection conn = null;

	/**
	 * Opens a connection to a MySQL database.
	 * 
	 * @param host The URL where the database is hosted.
	 * @param port The server port hosting the database.
	 * @param name The name of the database.
	 * @param user The username used to gain access to the database.
	 * @param password The password used to gain access to the database.
	 */
	public static void open(String host, int port, String name, String user, String password) {
		if (conn != null) {
			System.out.println("[Error] A database connection is already established.");
			return;
		}

		try {
			System.out.println("[Debug] Loading MySQL driver...");
			Class.forName("com.mysql.jdbc.Driver");

			String url = String.format("jdbc:mysql://%s:%d/%s", host, port, name);
			System.out.println("[Debug] Opening database " + url + "...");
			conn = DriverManager.getConnection(url, user, password);
		}
		catch (Exception e) {
			System.err.println("[Error] Failed to open database connection: "
					+ e.getMessage());
			System.exit(1);
		}
	}


	/**
	 * Opens the database with default values.
	 */
	public static void open() {
		open("mysql.stud.ntnu.no", //Hostname
				3306,  //Port
				"leomarti_kalendersystem", //Database 
				"leomarti_group19", //Username
				"group19"); //Password
	}
	public static void close(){
		try {
			conn.close();
			conn=null;
		} catch (SQLException e) {
			System.out.println("Klarte ikke å lukke forbindelse");
		}
	}
	public static void  createExampleData() throws SQLException{
		Statement s = conn.createStatement();


		s.executeUpdate("INSERT INTO `avtale` VALUES " +
				"(1,'Frisør','klippe meg for å bli pen','frisøren','2012-03-15','15:00:00','16:00:00','dagrunki','101')" +
				"(2,'lunsj',NULL,'parken','2012-03-12','12:00:00','13:00:00','fredrik','412')");

		s.executeUpdate("INSERT INTO `bruker` VALUES " +
				"('dagrunki','passord','dagrun','haugland',NULL)," +
				"('fraol','passord','Frank','olsen',NULL)," +
				"('annh','passord','anne','hansen',NULL)," +
				"('annha','passord','anne','haun',NULL)," +
				"('leoen','passord','Leo','Etternavn',78896756)," +
				"('fredrik','passord','fredrik','fredriksen',78895690)");

		s.executeUpdate("INSERT INTO `deltager` VALUES " +
				"('dagrun',1,1)," +
				"('dagrunki',2,2)," +
				"('dagrun',2,1);");

		s.executeUpdate("INSERT INTO `rom` VALUES " +
				"('101')," +
				"('106')," +
				"('123')," +
				"('215')," +
				"('406')," +
				"('412')," +
				"('hovedbygg1')," +
				"('hovedbygg2');");
	} 


	/**
	 * Clear all tables and optionally insert example data.
	 * 
	 * @param insertExampleData Whether or not example data should be inserted.
	 */
	public static void clearDatabase(boolean insertExampleData) throws SQLException {
		Statement s = conn.createStatement();

		//Clear all tables
		s.executeUpdate("DELETE FROM avtale");
		s.executeUpdate("DELETE FROM bruker");
		s.executeUpdate("DELETE FROM deltager");
		s.executeUpdate("DELETE FROM rom");

		if (insertExampleData) {
			createExampleData();
		}
	}

	public static boolean userNotExists(String brukernavn) throws SQLException{
		String st="SELECT brukernavn FROM bruker WHERE brukernavn LIKE'"+brukernavn+"';";
		ResultSet rs= conn.createStatement().executeQuery(st);
		boolean a = rs.first();
		if (a) {
			boolean b =rs.getString("brukernavn").equals("");
			if (b) {				
				return true;
			}
		}

		return false;
	}

	public static void insertUser(User user){

		try {
			if(userNotExists(user.getName())){
				String st="";

				if(user.getTlfnr()!=0){
					st="INSERT INTO bruker VALUES('"+
							user.getUsername()+"','"+user.getPassword()+"','"+user.getFirstname()+"','"+user.getLastname()+"',"+user.getTlfnr()+")";

				}
				else{
					st="INSERT INTO bruker VALUES('"+
							user.getUsername()+"','"+user.getPassword()+"','"+user.getFirstname()+"','"+user.getLastname()+"')";
				}
				conn.createStatement().executeQuery(st);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}


	}

	public static boolean roomNotExists(String navn)throws SQLException{
		String st="SELECT navn FROM rom WHERE navn LIKE='"+navn+"'";
		ResultSet rs= conn.createStatement().executeQuery(st);
		if(rs.wasNull())
			return false;
		return true;
	}

	public static void removeUser(User user) throws SQLException{
		String userToBeRemoved= user.getUsername();

		if(userNotExists(userToBeRemoved)){
			throw new SQLException();
		}

		else{
			Statement st= conn.createStatement();
			ResultSet rs= st.executeQuery("DELETE FROM bruker WHERE brukernavn='"+ userToBeRemoved+"'");

			rs.close();
		}

	}

	//status i deltaker 
	//2=avslått
	//1=godtatt
	//0=ubesvart

	public static void createParticipant(User user, Appointment appointment) throws SQLException{
		if(!userNotExists(user.getUsername())&& !appointmentNotExists(appointment)){
			Statement st= conn.createStatement();
			ResultSet rs = st.executeQuery("INSERT INTO deltaker VALUES('"+user.getUsername()+"',"+appointment.getID()+","+0);
		}
	}

	public static void createAppointment(Appointment appointment)throws SQLException{
		if(appointment.getRoom()!=null){
			if(roomNotExists(appointment.getRoom().getName())) throw new SQLException();
		}
		else if(appointment.getOwner()!=null){
			if(userNotExists(appointment.getOwner().getName()))throw new SQLException();
		}
		else{
			Statement st= conn.createStatement();
			ResultSet rs;

			if(appointment.getDescription()==null){
				if(appointment.getRoom()!=null && appointment.getPlace()==null){
					rs=st.executeQuery("INSERT INTO avtale (avtalenavn,romNavn,dato,start,slutt,lederBrukernavn) VALUES ('"+appointment.getTitle()+"','"+appointment.getRoom().getName()+"',"+appointment.getDateStart()+","+appointment.getDateStart().getTime()+","+appointment.getDateEnd().getTime()+",'"+appointment.getOwner().getName()+"'");
				}
				else if(appointment.getPlace()!=null && appointment.getRoom()==null){
					rs=st.executeQuery("INSERT INTO avtale (avtalenavn,sted,dato,start,slutt,lederBrukernavn) VALUES ('"+appointment.getTitle()+"','"+appointment.getPlace()+"',"+appointment.getDateStart()+","+appointment.getDateStart().getTime()+","+appointment.getDateEnd().getTime()+",'"+appointment.getOwner().getName()+"'");
				}
				else throw new SQLException();
			}
			else{
				if(appointment.getRoom()!=null && appointment.getPlace()==null){
					rs=st.executeQuery("INSERT INTO avtale (beskrivelse,avtalenavn,romNavn,dato,start,slutt,lederBrukernavn) VALUES ('"+appointment.getDescription()+"','"+appointment.getTitle()+"','"+appointment.getRoom().getName()+"',"+appointment.getDateStart()+","+appointment.getDateStart().getTime()+","+appointment.getDateEnd().getTime()+",'"+appointment.getOwner().getName()+"'");
				}
				else if(appointment.getPlace()!=null && appointment.getRoom()==null){
					rs=st.executeQuery("INSERT INTO avtale (beskrivelse,avtalenavn,sted,dato,start,slutt,lederBrukernavn) VALUES ('"+appointment.getDescription()+"','"+appointment.getTitle()+"','"+appointment.getPlace()+"',"+appointment.getDateStart()+","+appointment.getDateStart().getTime()+","+appointment.getDateEnd().getTime()+",'"+appointment.getOwner().getName()+"'");
				}
			}
			if(!appointment.getUserList().isEmpty()){
				ArrayList list = appointment.getUserList();
				for (int i=0;i<list.size();i++) {
					User user =(User) list.get(i);
					createParticipant(user, appointment);
				}
			}
		}
	}

	public static User getUser(String brukernavn)throws SQLException{
		User newUser = new User("");

		Statement st=conn.createStatement();
		if(!userNotExists(brukernavn)){

			ResultSet rs = st.executeQuery("SELECT * FROM bruker WHERE brukernavn LIKE '"+brukernavn+"';");

			while(rs.next()){
				newUser.setUsername(rs.getString("brukernavn"));
				newUser.setFirstname(rs.getString("fornavn"));
				newUser.setPassword(rs.getString("passord"));
				newUser.setLastname(rs.getString("etternavn"));
				newUser.setTlfnr(rs.getInt("tlf"));
			}
		}
		return newUser;

	}
	
	public static ArrayList<User> getUsers() throws SQLException{

		ArrayList<User> userList=new ArrayList<User>();
		Statement st=conn.createStatement();

		ResultSet rs = st.executeQuery("SELECT * FROM bruker ORDER BY brukernavn;");

		User newUser = null;

		while(rs.next()){
			newUser=new User(rs.getString("brukernavn"));
			newUser.setFirstname(rs.getString("fornavn"));
			newUser.setPassword(rs.getString("passord"));
			newUser.setLastname(rs.getString("etternavn"));
			newUser.setTlfnr(rs.getInt("tlf"));
			userList.add(newUser);
		}
		rs.close();	
		return userList;
	}


	public static Appointment getAppointment(int ID){
		Appointment newAppointment = new Appointment(ID);
		//		
		//		Statement st=conn.createStatement();
		//		if(!appointmentNotExists(ID)){
		//			ResultSet rs = st.executeQuery("SELECT * FROM avtale WHERE avtaleID LIKE '"+brukernavn+"'");
		//			newAppointment = new Appointment(ID, title, dateStart, dateEnd, place, owner, room, null, description)	
		//			newUser.setFirstname(rs.getString("brukernavn"));
		//				newUser.setPassword(rs.getString("passord"));
		//				newUser.setLastname(rs.getString("etternavn"));
		//				newUser.setTlfnr(rs.getInt("tlf"));
		//			return newUser;
		//		}
		//		else
		//			throw new SQLException();
		return newAppointment;
	}

	//lage change status


	public static boolean appointmentNotExists(Appointment appointment)throws SQLException
	{
		String st="SELECT avtaleID FROM avtale WHERE avtaleID LIKE='"+appointment.getID()+"'";
		ResultSet rs= conn.createStatement().executeQuery(st);
		if(rs.wasNull())
			return false;
		return true;
	}

	public static void addRoom(Room room)throws SQLException{
		if(roomNotExists(room.getName())){
			Statement st=conn.createStatement();

			ResultSet rs=st.executeQuery("INSERT INTO rom VALUES('"+room.getName()+"')");

			rs.close();
		}

		else
			throw new SQLException();
	}

	public static void removeRoom(Room room) throws SQLException{

		if(roomNotExists(room.getName())){
			throw new SQLException();
		}

		else{
			Statement st= conn.createStatement();
			ResultSet rs= st.executeQuery("DELETE FROM rom WHERE navn='"+ room.getName()+"'");

			rs.close();
		}

	}

	/**
	 * Checks if a user exists with the specified username and password.
	 * 
	 * @return The user with this username and password or <code>null</code> if
	 * no such user exists.
	 */
	public static User logIn(String username, String password) {
		if (username.equals("Test") && password.equals("testpw")) {
			return new User("Test", "Testsen");
		}
		return null;
	}


	/**
	 * Tests to demonstrate how to interpret result sets
	 */
	public static void tests() throws SQLException {
		Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet result = s.executeQuery("SELECT brukernavn, tlf FROM bruker");

		//Print out every user and his phone number
		while (result.next()) {
			System.out.print("Bruker: " + result.getString("brukernavn"));
			System.out.println("tlf: " + result.getInt("tlf"));
		}
	}


	public static void main(String[] args) throws SQLException {
		open();
		tests();
	}
}
