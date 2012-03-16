package gruppe19.server.db;

import gruppe19.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public static boolean existsUser(String brukernavn) throws SQLException{
		String st="SELECT brukernavn FROM bruker where brukernavn like ='"+brukernavn+"'";
		ResultSet rs= conn.createStatement().executeQuery(st);
		if(rs.wasNull()){
			return false;
		}
		return true;
	}
	
	public static void insertBruker(User a){
		
		try {
			if(existsUser(a.getName())){
				
			
			if(a.getTlfnr()!=null){
				String st="INSERT INTO bruker IF NOT EXISTS VALUES("+
				a.getName()+","+a.getTlfnr()+","+")";
			}
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
	
	public static User logIn(String username, String password) {
		if (username.equals("Test") && password.equals("testpw")) {
			return new User("Test", "Testsen");
		}
		return null;
	}
	
	
	/**
	 * Tests to demonstrate how to interpret result sets
	 */
	private static void tests() throws SQLException {
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
