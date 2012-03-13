package gruppe19.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * A static class used for communication with the SQL server.
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
		
		try {
			createTables();
			insertExampleData();
		}
		catch (Exception e) {
			System.err.println("[Error] Failed to set up the database.");
			e.printStackTrace();
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
	
	/**
	 * Creates all the needed tables if the do not already exist.
	 */
	private static void createTables() throws SQLException {
		Statement s = conn.createStatement();
		
		s.executeUpdate("CREATE TABLE IF NOT EXISTS TestTable(id int, text varchar(32))");
	}
	
	/**
	 * Clear all tables and insert example data.
	 */
	private static void insertExampleData() throws SQLException {
		Statement s = conn.createStatement();
		
		//Clear the tables
		s.executeUpdate("DELETE FROM TestTable");
		
		//Insert example data
		s.executeUpdate("INSERT INTO TestTable VALUES(1, 'TESTING1'), (2, 'TESTING2'), (3, 'TESTING3')");
	}
	
	
	/**
	 * Tests
	 */
	private static void tests() throws SQLException {
		Statement s = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		ResultSet result;
		
		result = s.executeQuery("SELECT * FROM TestTable");
		
		//Print out the results
		for (int i = 1; result.next(); i++) {
			System.out.printf("*** ROW %d ***\n" +
					"COLUMN 1: %d\n" +
					"COLUMN 2: %s\n", i, result.getInt(1), result.getString(2));
			
		}
		
	}
	
	public static void main(String[] args) throws SQLException {
		open();
		tests();
	}
}
