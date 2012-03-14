package gruppe19.nettverk;

import gruppe19.ktn.Client1;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import no.ntnu.fp.net.admin.Settings;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;
import no.ntnu.fp.net.co.SimpleConnection;

/**
 * A class used for communication between the client and the server.
 */
public class ServerAPI {
	private static Connection conn = null;
	private static int myPort = 9000;
	private static String serverAddress = null;
	private static int serverPort = -1;
	private static boolean usingSimpleConn = false;
	private static ReceiveThread receiver = null;
	
	/**
	 * Opens a new connection to the server.
	 */
	public static void open() throws SocketTimeoutException, UnknownHostException, IOException {
		if (conn != null) {
			System.out.println("[Error] A server connection is already established.");
			return;
		}
		
		Settings settings = new Settings();
		usingSimpleConn = settings.useSimpleConnection();
		serverAddress = settings.getServerAddress();
		serverPort = settings.getServerPort();

		if (usingSimpleConn) {
			conn = new SimpleConnection(myPort);
		} 
		else {
			conn = new ConnectionImpl(myPort);
		}
			
		conn.connect(InetAddress.getByName(serverAddress), serverPort);
		
		receiver = new ReceiveThread();
		receiver.start();
	}
	
	/**
	 * Closes the connection to the server.
	 */
	public static void close() {
		
	}
	
	public static void send(String msg) {
		try {
			conn.send(msg);
		} catch (Exception e) {
			System.err.println("Failed to send message to server. Exiting...");
			System.exit(1);
		}
	}
	
	private static void receive(String msg) {
		System.out.println(msg);
	}

	/**
	 * Attempts to log the user into the database.
	 * 
	 * @param username The user-specified username.
	 * @param password The user-specified password.
	 * @return 
	 * <ul>
	 * 		<li>A negative value if the username does not exist.
	 * 		<li>0 if the username is correct, but the password is not.
	 * 		<li>A positive value if both the username and the password is correct.
	 * </ul>
	 */
	public static int login(String username, String password) {
		return -1;
	}
	
	/**
	 * Creates a thread waiting for incoming messages.
	 */
	private static class ReceiveThread extends Thread {
		public boolean run = true;
		
		public void run() {
			run = true;
			
			while (run) {
				try {
					ServerAPI.receive(ServerAPI.conn.receive());
				} catch (ConnectException e) {
					e.printStackTrace();
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
