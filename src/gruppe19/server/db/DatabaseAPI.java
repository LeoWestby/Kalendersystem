package gruppe19.server.db;

import gruppe19.client.ktn.ServerAPI.Status;
import gruppe19.gui.UserListRenderer;
import gruppe19.model.Appointment;
import gruppe19.model.Room;
import gruppe19.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A static class used for communication between with the server and the SQL server.
 */
public class DatabaseAPI {
	private static Connection conn = null;

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

	/**
	 * Opens a connection to a MySQL database.
	 * 
	 * @param host The URL where the database is hosted.
	 * @param port The server port hosting the database.
	 * @param name The name of the database.
	 * @param user The user name used to gain access to the database.
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

	public static boolean appointmentNotExists(Appointment appointment)throws SQLException {
		String st="SELECT avtaleID FROM avtale WHERE avtaleID ="+appointment.getID()+";";
		ResultSet rs= conn.createStatement().executeQuery(st);
		return !rs.first();
	}
	
	public static boolean appointmentNotExists(int appointmentID)throws SQLException {
		String st="SELECT avtaleID FROM avtale WHERE avtaleID ="+appointmentID+";";
		ResultSet rs= conn.createStatement().executeQuery(st);
		return !rs.first();
	}
	
	public static void changeParticipantStatus(User user, Appointment appointment, Status status) throws SQLException{
		Statement st=conn.createStatement();
		String query= String.format("UPDATE deltager SET status=%d WHERE brukernavn='%s' AND avtaleID=%d;", status.ordinal(), user.getUsername(),appointment.getID());
		st.executeUpdate(query);
	}

	/**
	 * Clear all tables and optionally insert example data.
	 * 
	 * @param insertExampleData Whether or not example data should be inserted.
	 */
	public static void clearDatabase(boolean insertExampleData) throws SQLException {
		Statement s = conn.createStatement();

		//Clear all tables
		s.executeUpdate("TRUNCATE avtale;");
		s.executeUpdate("TRUNCATE bruker;");
		s.executeUpdate("TRUNCATE deltager;");
		s.executeUpdate("TRUNCATE rom;");

		if (insertExampleData) {
			createExampleData();
		}
	}

	public static void close(){
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			System.out.println("Klarte ikke å lukke forbindelse");
		}
	}

	public static Appointment createAppointment(Appointment a) throws SQLException{
		
		Statement st= conn.createStatement();
		Date s = a.getDateStart(), e = a.getDateEnd();

		String string =String.format("insert into avtale values " +
				"(0, '%s', %s,%s,{d '%d-%d-%d'}, " +
				"{t '%d:%d:%d'}, {t '%d:%d:%d'}, '%s', %s); ",
				a.getTitle(), 
				a.getDescription() == null ? "null" : "'" + a.getDescription() + "'",
				a.getPlace() == null ? "null" : "'" + a.getPlace() + "'",
				s.getYear() + 1900, s.getMonth() + 1, s.getDate(),
				s.getHours(), s.getMinutes(), s.getSeconds(),
				e.getHours(), e.getMinutes(), e.getSeconds(),
				a.getOwner().getUsername(), 
				a.getRoom() == null ? "null" : 
						(a.getRoom().getName() == null ? "null" :
						(a.getRoom().getName().equals("") ? "null" : "'" + a.getRoom().getName() + "'")));
		
		st.executeUpdate(string);
		ResultSet res=st.executeQuery("SELECT last_insert_id() avtale;");
		res.first();
		
		int ID = res.getInt(1);
		Map<User, Status> userList = new HashMap<User, Status>();
		a.setIdD(ID);

		for (User u : a.getUserList().keySet()) {
			createParticipant(u, a, Status.PENDING);
			userList.put(u, Status.PENDING);
		}
		a.setUserList(userList);
		return a;
	}

	public static void createExampleData() throws SQLException{
		Statement s = conn.createStatement();

		s.executeUpdate("INSERT INTO `avtale` VALUES " +
				"(1,'Forelesing i KTN',NULL,NULL,'2012-03-26','15:00:00','18:00:00','vegard','F1')," +
				"(2,'Kino','Skal se Hunger Games','Nova Kinosenter','2012-03-30','20:00:00','22:00:00','dagrun',NULL)," +
				"(3,'Frisørtime','Vil bli kortere!','Frisøren i Kongens Gate','2012-03-29','12:00:00','13:00:00','leo',NULL)," +
				"(4,'Lunj',NULL,'På hangaren','2012-04-3','12:00:00','14:00:00','dagrun',NULL)," +
				"(5,'Abakus LAN','PARTEEEY',NULL,'2012-03-24','00:00:00','23:59:59','kristian','P15 421')," +
				"(6,'Forelesing i Java',NULL,NULL,'2012-03-19','12:00:00','14:00:00','tt','R1')," +
				"(7,'Jobbe med fellesprosjektet','Heldigvis snart ferdig nå!',NULL,'2012-03-27','10:00:00','15:00:00','leo','P15 421');");

		s.executeUpdate("INSERT INTO `bruker` VALUES " +
				"('kristian','passord','Kristian','Snare', 62813295)," +
				"('dagrun','passord','Dagrun','Haugland', 22913336)," +
				"('leo','passord','Leo','Westby',62859990)," +
				"('victor','passord','Victor','Fielding',82902305)," +
				"('vegard','passord','Vegard','Harper',61184753)," +
				"('tt','passord','Jennifer','Lee',82929407)," +
				"('ola','passord','Ola','Nordmann',11111111);");

		s.executeUpdate("INSERT INTO `deltager` VALUES " +
				"('leo',1,1)," +
				"('dagrun',1,1)," +
				"('victor',1,1)," +
				"('tt',1,1)," +
				"('kristian',1,1)," +
				
				"('leo',2,0)," +
				
				"('leo',4,0)," +
				"('victor',4,0)," +
				"('tt',4,0)," +
				"('vegard',4,0)," +
				"('kristian',4,0)," +
				
				"('leo',5,1)," +
				"('dagrun',5,1)," +
				"('victor',5,1)," +
				"('tt',5,1)," +
				"('vegard',5,1)," +
				
				"('victor',6,0)," +
				
				"('kristian',7,1)," +
				"('dagrun',7,1)," +
				"('victor',7,1)," +
				"('tt',7,1)," +
				"('vegard',7,1);");

		s.executeUpdate("INSERT INTO `rom` VALUES " +
				"('S1')," +
				"('S5')," +
				"('S7')," +
				"('EL5')," +
				"('EL6')," +
				"('F1')," +
				"('P15 421')," +
				"('P15 415')," +
				"('P15 321')," +
				"('P15 315')," +
				"('R1')," +
				"('R2');");
	}

	public static void createParticipant(User user, Appointment appointment, Status s) throws SQLException{
//		if(!userNotExists(user.getUsername()) && !appointmentNotExists(appointment)){
			Statement st= conn.createStatement();
			String string = "INSERT INTO deltager VALUES('"+user.getUsername()+"',"+appointment.getID()+","+s.ordinal()+");";
			st.executeUpdate(string);
//		}
	}
	
	public static void createRoom(Room room)throws SQLException{
		Statement st=conn.createStatement();

		ResultSet rs=st.executeQuery("INSERT INTO rom VALUES('"+room.getName()+"');");

		rs.close();
	} 

	public static ArrayList<Appointment> findAppointments(User user) throws SQLException{
		ArrayList<Appointment> liste = new ArrayList<Appointment>();
		String st = "SELECT * FROM avtale WHERE lederBrukernavn = '"+user.getUsername()+"';";
		ResultSet rs = conn.createStatement().executeQuery(st);
		while(rs.next()){
			Map<User, Status> userList = getUserList(rs.getInt("avtaleID"));
			Room rom = new Room(rs.getString("romNavn"));
			User leder = getUser(rs.getString("lederBrukernavn"));
			

			java.sql.Date start = rs.getDate("dato"), end = rs.getDate("dato");
			java.sql.Time tstart = rs.getTime("start"), tend = rs.getTime("slutt");
			
			Date datestart = new Date(start.getYear(), start.getMonth(), start.getDate(), tstart.getHours(), tstart.getMinutes(), tstart.getSeconds());
			Date dateend = new Date(end.getYear(), end.getMonth(), end.getDate(), tend.getHours(), tend.getMinutes(), tend.getSeconds());

			liste.add(new Appointment(rs.getInt("avtaleID"), rs.getString("avtalenavn"), 
					datestart, dateend, rs.getString("sted"),
					leder, rom , userList, rs.getString("beskrivelse")));
		}
		return liste;
	}

	public static ArrayList<Appointment> findAppointmentsParticipant(User user) throws SQLException{
		ArrayList<Appointment> liste = new ArrayList<Appointment>();
		String st = "SELECT * FROM deltager,avtale WHERE deltager.brukernavn = '"+ user.getUsername()+"' and deltager.avtaleID = avtale.avtaleID;";
		ResultSet rs = conn.createStatement().executeQuery(st);
		while(rs.next()){
			Map<User, Status> userList = getUserList(rs.getInt("avtaleID"));
			Room rom = new Room(rs.getString("romNavn"));
			User leder = getUser(rs.getString("lederBrukernavn"));
			
			java.sql.Date start = rs.getDate("dato"), end = rs.getDate("dato");
			java.sql.Time tstart = rs.getTime("start"), tend = rs.getTime("slutt");
			
			Date datestart = new Date(start.getYear(), start.getMonth(), start.getDate(), tstart.getHours(), tstart.getMinutes(), tstart.getSeconds());
			Date dateend = new Date(end.getYear(), end.getMonth(), end.getDate(), tend.getHours(), tend.getMinutes(), tend.getSeconds());
			
			liste.add(new Appointment(rs.getInt("avtaleID"), rs.getString("avtalenavn"), 
					datestart, dateend, rs.getString("sted"),
					leder, rom , userList, rs.getString("beskrivelse")));
		}
		return liste;		
	}
	
	public static ArrayList<User> findUsers(String searchString) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		
		ResultSet result = conn.createStatement().executeQuery
			("SELECT * FROM bruker WHERE brukernavn LIKE '%" + searchString + "%';");
		
		while (result.next()) {
			users.add(
					new User(result.getString(1),
							result.getString(3), 
							result.getString(4), 
							result.getInt(5), 
							result.getString(2)));
		}
		return users;
	}

	public static Appointment getAppointment(int ID) throws SQLException{
        Appointment newAppointment;
            Statement st=conn.createStatement();
                if(!appointmentNotExists(ID)){
                    ResultSet rs = st.executeQuery("SELECT * FROM avtale WHERE avtaleID = '"+ID+"';");
                    rs.first();
                    User leder = getUser(rs.getString("lederBrukernavn"));
                    Room rom = new Room(rs.getString("romNavn"));
                    Map<User, Status> userList = getUserList(rs.getInt("avtaleID"));
                   
                    java.sql.Date start = rs.getDate("dato"), end = rs.getDate("dato");
                    java.sql.Time tstart = rs.getTime("start"), tend = rs.getTime("slutt");
                   
                    Date datestart = new Date(start.getYear(), start.getMonth(), start.getDate(), tstart.getHours(), tstart.getMinutes(), tstart.getSeconds());
                    Date dateend = new Date(end.getYear(), end.getMonth(), end.getDate(), tend.getHours(), tend.getMinutes(), tend.getSeconds());
                       
                    newAppointment = new Appointment(ID, rs.getString("avtalenavn"), datestart, dateend, rs.getString("sted"), leder, rom, userList, rs.getString("beskrivelse"));   

                }
                else{
                    return null;
                }
                return newAppointment;
    }
	
	public static ArrayList<Room> getFreeRooms(Date start, Date end) throws SQLException {
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		String query = String.format(
						"SELECT navn " +
						"FROM rom " +
						"WHERE navn NOT IN ( " +
							"SELECT navn " +
							"FROM avtale JOIN rom ON romNavn = navn " +
							"WHERE dato = {d '%d-%d-%d'} " +
							"AND ( " +
								"(start BETWEEN {t '%4$d:%5$d:%6$d' } AND {t '%7$d:%8$d:%9$d' } " +
								"OR slutt BETWEEN {t '%4$d:%5$d:%6$d' } AND {t '%7$d:%8$d:%9$d' }) " +
	
		   			 		"OR " +
	
		   			 			"start < {t '%4$d:%5$d:%6$d' } AND slutt > {t '%7$d:%8$d:%9$d' } " +
						"));",
						start.getYear() + 1900, start.getMonth() + 1, start.getDate(),
						start.getHours(), start.getMinutes(), start.getSeconds(),
						end.getHours(), end.getMinutes(), end.getSeconds());
		
		ResultSet results = conn.createStatement().executeQuery(query);

		while (results.next()) {
			rooms.add(new Room(results.getString(1)));
		}
		return rooms;
	}
	
	public static int getParticipantStatus(User user, Appointment appointment)throws SQLException{
		Statement st=conn.createStatement();
		String query= String.format("SELECT status From deltager WHERE user='%s' AND avtaleID='%d';", user.getUsername(),appointment.getID());
		ResultSet rs=st.executeQuery(query);
		return rs.getInt(0);
	}
	
	public static ArrayList<Room> getRooms() throws SQLException {
		ArrayList<Room> rooms = new ArrayList<Room>();
		
		ResultSet rs = conn.createStatement()
				.executeQuery("SELECT * FROM rom;");
		
		while (rs.next()) {
			rooms.add(new Room(rs.getString(1)));
		}
		return rooms;
	}

	public static User getUser(String brukernavn)throws SQLException{
		User newUser = null;

		Statement st=conn.createStatement();
		
		if(!userNotExists(brukernavn)){
			newUser = new User("");
			ResultSet rs = st.executeQuery("SELECT * FROM bruker WHERE brukernavn = '"+brukernavn+"';");
			rs.first();
			newUser.setUsername(rs.getString("brukernavn"));
			newUser.setFirstname(rs.getString("fornavn"));
			newUser.setPassword(rs.getString("passord"));
			newUser.setLastname(rs.getString("etternavn"));
			newUser.setTlfnr(rs.getInt("tlf"));
			rs.close();
		}
		return newUser;
	}
	
	public static Map<User, Status> getUserList(int appointmentID) throws SQLException{
		Map<User, Status> userList = new HashMap<User, Status>();
		Status status = null;

		String st = "SELECT * FROM deltager,bruker WHERE deltager.avtaleID ="+appointmentID+" AND bruker.brukernavn = deltager.brukernavn;";
		ResultSet rs = conn.createStatement().executeQuery(st);	
		
		while(rs.next()){
			switch (rs.getInt("status")) {
				case 0:
					status = Status.PENDING;
					break;
				case 1:
					status = Status.APPROVED;
					break;
				case 2:
					status = Status.REJECTED;
					break;
			}
			
			userList.put(
					new User(
							rs.getString("brukernavn"), 
							rs.getString("fornavn"), 
							rs.getString("etternavn"), 
							rs.getInt("tlf"),
							rs.getString("passord")),
					status);

		}
		return userList;
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

	public static void insertUser(User user){

		try {
			if(userNotExists(user.getName())){
				String st="";

				if(user.getTlfnr()!=0){
					st="INSERT INTO bruker VALUES('"+
							user.getUsername()+"','"+user.getPassword()+"','"+user.getFirstname()+"','"
							+user.getLastname()+"',"+user.getTlfnr()+");";

				}
				else{
					st="INSERT INTO bruker VALUES('"+
							user.getUsername()+"','"+user.getPassword()+"','"
							+user.getFirstname()+"','"+user.getLastname()+"');";
				}
				conn.createStatement().executeUpdate(st);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a user exists with the specified username and password.
	 *
	 * @return The user with this username and password or <code>null</code> if
	 * no such user exists.
	 */
	public static User logIn(String username, String password) throws SQLException {
		User u = getUser(username.toLowerCase());

		if (u != null && u.getPassword().equals(password)) {
			return u;
		}
		return null;
	}

	public static void removeAppointment(Appointment a) throws SQLException {
		conn.createStatement().executeUpdate
		("DELETE FROM avtale WHERE avtaleID="+a.getID()+";");
		
		for (User u : a.getUserList().keySet()) {
			removeParticipant(u, a);
		}
	}

	public static void removeParticipant(User user, Appointment appointment) throws SQLException{
		Statement st= conn.createStatement();
		st.executeUpdate("DELETE FROM deltager WHERE brukernavn='"+user.getUsername()+"' AND avtaleID="+appointment.getID()+";");
	}

	public static void removeRoom(Room room) throws SQLException{
			Statement st= conn.createStatement();
			st.executeUpdate("DELETE FROM rom WHERE navn='"+ room.getName()+"';");
	}

	public static void removeUser(User user) throws SQLException{
		conn.createStatement().executeUpdate
		("DELETE FROM bruker WHERE brukernavn = '"+ user.getUsername()+"';");
	}

	public static boolean roomNotExists(String navn)throws SQLException{
		String st="SELECT navn FROM rom WHERE navn ='"+navn+"'";
		ResultSet rs= conn.createStatement().executeQuery(st);
		return !rs.first();
	}

	public static Appointment updateAppointment(Appointment a) throws SQLException {
		Date s = a.getDateStart(), e = a.getDateEnd();
		String string =String.format("update avtale " +
				"set avtalenavn = '%s', beskrivelse = %s, sted = %s, dato = {d '%d-%d-%d'}, " +
				"start = {t '%d:%d:%d'}, slutt = {t '%d:%d:%d'}, lederBrukernavn = '%s', romNavn = %s " +
				"where avtaleID = %d;",
				a.getTitle(), 
				a.getDescription() == null ? "null" : "'" + a.getDescription() + "'",
				a.getPlace() == null ? "null" : "'" + a.getPlace() + "'",
				s.getYear() + 1900, s.getMonth() + 1, s.getDate(),
				s.getHours(), s.getMinutes(), s.getSeconds(),
				e.getHours(), e.getMinutes(), e.getSeconds(),
				a.getOwner().getUsername(), 
				a.getRoom() == null ? "null" : 
					(a.getRoom().getName() == null ? "null" :
						(a.getRoom().getName().equals("") ? "null" : "'" + a.getRoom().getName() + "'")),
				a.getID());
		conn.createStatement().executeUpdate(string);
		
		conn.createStatement().executeUpdate("DELETE FROM deltager WHERE avtaleID = " + a.getID() + ";");
		
		for (Entry<User, Status> entry : a.getUserList().entrySet()) {
			createParticipant(entry.getKey(), a, entry.getValue());
		}
		return a;
	}

	public static void updateRoom(Room room) throws SQLException {
		conn.createStatement().executeUpdate("DELETE FROM Rom WHERE Navn = " + room.getName() + ";");
		createRoom(room);
	}

	public static void updateUser(User user) throws SQLException {
		conn.createStatement().executeUpdate("DELETE FROM Bruker WHERE Brukernavn = " + user.getUsername() + ';');
		insertUser(user);
	}

	public static boolean userNotExists(String brukernavn) throws SQLException{
		String st="SELECT brukernavn FROM bruker WHERE brukernavn = '"+brukernavn+"';";
		ResultSet rs= conn.createStatement().executeQuery(st);
		return !rs.first();
	}

	public static void main(String[] args) throws SQLException {
		open();
		clearDatabase(true);
	}
}
