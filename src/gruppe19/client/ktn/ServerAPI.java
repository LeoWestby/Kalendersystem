package gruppe19.client.ktn;

import gruppe19.server.ktn.ServerMessage;
import gruppe19.server.ktn.ServerMessage.Type;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import no.ntnu.fp.model.Person;
import no.ntnu.fp.net.admin.Settings;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;
import no.ntnu.fp.net.co.SimpleConnection;

/**
 * A class used for communication between the client and the server.
 */
public class ServerAPI {
	private static Connection conn = null;
	private static int myPort = 9999;
	private static String serverAddress;
	private static int serverPort;
	private static boolean usingSimpleConn;
	private static ReceiveThread receiver;
	
	/**
	 * The amount of ms to wait for a response from the server
	 * after sending a request.
	 */
	private static final int timeout = 5000;
	
	/**
	 * The last response received from the server.
	 */
	private static ServerMessage response = null;
	
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
		try {
			conn.close();
		} 
		catch (IOException e) {
			System.err.println("[Error] IOException while closing connection" +
					" from client side");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a request to the server.
	 * 
	 * @param msg The request to send.
	 */
	public static void send(String msg) {
		try {
			conn.send(msg);
		} catch (Exception e) {
			System.err.println("[Error] Failed to contact server. Exiting...");
			System.exit(1);
		}
	}
	
	/**
	 * This method blocks until a <code>ServerMessage</code> of
	 * type Response is received or the method has blocked
	 * for 'timeout' ms.
	 * 
	 * @param msg The server's response.
	 */
	private static ServerMessage getResponse() {
		int waited = 0;
		
		while (response == null) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {/*Ignore*/}
			
			if ((waited += 20) > timeout) {
				System.err.println("[Error] Timed out while waiting " +
						"for response from server");
				System.exit(1);
			}
		}
		ServerMessage tmp = response;
		response = null;
		return tmp;
	}

	/**
	 * Attempts to log the user into the database.
	 * 
	 * @param username The user-specified username.
	 * @param password The user-specified password.
	 * @return 
	 * <ul>
	 * 		<li>A negative value if the username or password is incorrect.
	 * 		<li>A positive value if both the username and the password is correct.
	 * </ul>
	 */
	public static int login(String username, String password) {
		//Send login request to server
		send("a" + username + "\0" + password);
		
		//Parse response
		//TODO: Replace Person with whatever model we're using and return that instead
		Person p = (Person)getResponse().payload;
		
		if (p == null) {
			return -1;
		}
		return 1;
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
					ObjectInputStream byteStream = 
							new ObjectInputStream(
									new ByteArrayInputStream(
											ServerAPI.conn.receive().getBytes()));
					ServerMessage msg = (ServerMessage)byteStream.readObject();
					
					if (msg.type == Type.Response) {
						response = msg;
					}
					else {
						//Handle request
					}
				} 
				catch (ConnectException e) {
					e.printStackTrace();
				} 
				catch (SocketException e) {
					e.printStackTrace();
				} 
				catch (InvalidClassException e) {
					System.err.println("[Error] Failed to convert the received" +
							" byte stream to ServerMessage. Exiting...");
					e.printStackTrace();
					System.exit(1);
				}
				catch (ClassNotFoundException e) {
					System.err.println("[Error] Failed to convert the received" +
							" byte stream to ServerMessage. Exiting...");
					e.printStackTrace();
					System.exit(1);
				}
				catch (EOFException e) {
					System.err.println("[Error] Lost connection to server. Exiting...");
					e.printStackTrace();
					System.exit(1);
				}
				catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				} 
			}
		}
	}
}
