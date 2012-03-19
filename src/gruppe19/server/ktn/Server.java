package gruppe19.server.ktn;

import gruppe19.client.ktn.ClientMessage;
import gruppe19.client.ktn.ServerAPI;
import gruppe19.model.Appointment;
import gruppe19.model.User;
import gruppe19.server.db.DatabaseAPI;
import gruppe19.server.ktn.ServerMessage.Type;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import no.ntnu.fp.net.admin.Settings;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;
import no.ntnu.fp.net.co.SimpleConnection;

/**
 * A static class accepting incoming connections and requests.
 */
public class Server {
	private static List<Client> clients = new ArrayList<Client>();
	private static Connection server = null;
	private static Base64 stringEncoder = new Base64();
	
	public final static String serverAddress;
	public final static int listeningPort;
	public final static boolean usingSimpleConn;
	
	//Parse the settings file
	static {
		Settings s = new Settings();
		
		serverAddress = s.getServerAddress();
		listeningPort = s.getServerPort();
		usingSimpleConn = s.useSimpleConnection();
	}
	
	/**
	 * A class saving information about each connected client.
	 */
	private static class Client {
		private RecieveThread recieveThread;
		private Connection conn;
		private User connectedUser;

		public Client(Connection conn) {
			this.conn = conn;
			connectedUser = null;
			recieveThread = new RecieveThread();
			recieveThread.start();
		}

		/**
		 * A thread waiting for incoming messages.
		 */
		private class RecieveThread extends Thread {
			public boolean run = true;

			public void run() {
				while (run) {
					try {
						ObjectInputStream byteStream = 
								new ObjectInputStream(
										new ByteArrayInputStream(
												stringEncoder.decode(
														conn.receive())));
						
						handleMessage((ClientMessage)byteStream.readObject());
					} 
					catch (ConnectException e) {
						e.printStackTrace();
					}
					catch (InvalidClassException e) {
						System.err.println("[Error] Failed to convert the received" +
								" byte stream to ClientMessage.");
						e.printStackTrace();
					}
					catch (ClassNotFoundException e) {
						System.err.println("[Error] Failed to convert the received" +
								" byte stream to ClientMessage.");
						e.printStackTrace();
					}
					catch (Exception e) {
						//Client requested disconnect
						run = false;
						
						try {
							conn.close();
						} 
						catch (IOException ioe) {
							System.err.println(
								"[Error] Server: IOException while"
								+ "closing connection: "
								+ ioe.getMessage());
						}
					} 
				}
			}
		}

		/**
		 * Maps a request from the client to the appropriate method.
		 * 
		 * @param request The request to handle.
		 */
		private void handleMessage(ClientMessage msg) {
			if (msg == null) {
				return;
			}
			
			switch (msg.ID) {
			case 'a':
			{
				/* Sends a server message containing a valid user object if
				 * the username and password are valid, or a server message
				 * containing null if not.
				 */
				String loginInfo = (String)msg.payload;
				int i = loginInfo.indexOf(0);
				send (new ServerMessage('\0', new User(loginInfo.substring(0, i), "Ola", "Nordmann", "5342523", "123"), Type.Response));
				this.connectedUser = new User(loginInfo.substring(0, i), "Ola", "Nordmann", "5342523", "123");
//				send(new ServerMessage(
//						DatabaseAPI.logIn(	loginInfo.substring(0, i),
//											loginInfo.substring(i + 1)),
//											Type.Response));
				break;
			}
			case 'b':
			{
				Appointment a = (Appointment)msg.payload;
				
				for (User u : a.getUserList()) {
					for (Client c : clients) {
						
						if (u.getUsername().equals(c.connectedUser.getUsername())) {
							c.send(new ServerMessage('a', a, Type.Request));
						}
					}
				}
				break;
			}
			case 'c':
							
				break;
						
			case 'd':
				
				break;
			
			case 'e':
				
				break;
			
			case 'f':
				
				break;
			
			case 'g':
				
				break;
			
			case 'h':
				
				break;
			case 'i':
				
				break;
			
			case 'j':
				
				break;
			
			case 'k':
				
				break;
			
			case 'l':
				
				break;
			
			case 'm':
				
				break;
			
			case 'n':
				
				break;
			
			case 'o':
				
				break;
			
			case 'p':
				
				break;
			case 'q':
				
				break;
			case 'r':
				
				break;
			case 's':
				
				break;
			
			case 't':
				
				break;
			
			case 'u':
				
				break;
			case 'v':
				
				break;
			
			case 'w':
				
				break;
			default:
				//Invalid request. Ignore
			}
		}

		/**
		 * Sends a <code>ServerMessage</code> to the client.
		 * 
		 * @param msg The <code>ServerMessage</code> to send.
		 */
		private void send(ServerMessage msg) {
			try {
		        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(baos);
		        oos.writeObject(msg);
		        oos.close();
				conn.send(stringEncoder.encodeToString(baos.toByteArray()));
			} 
			catch (Exception e) {
				System.err.println("[Error] Failed to send object to client");
				e.printStackTrace();
			}
		}
	}//End of Client class
	
	/**
	 * Sends a message to all clients connected to this server.
	 * 
	 * @param msg The message to broadcast.
	 */
	public static synchronized void broadcast(String msg) {
		for (Client c : clients) {
//			c.send(new ServerMessage(msg, Type.BroadCast));
		}
	}
	
	/**
	 * Causes the server to start listening for incoming connections on
	 * the port specified in the settings file and opens a connection
	 * to the database.
	 */
	public static void startServer() {
		if (server != null) {
			System.err.println("[Error] Tried to start an already started server.");
			return;
		}
		
		if (usingSimpleConn) {
			server = new SimpleConnection(listeningPort);
		}
		else {
			server = new ConnectionImpl(listeningPort);
		}
		DatabaseAPI.open();
		
		//Start listening
		new Thread() {
			private Connection newConn;

			public void run() {
				while (true) {
					try {
						newConn = server.accept();
						System.out.println("[Debug] Handling incoming connection...");
						clients.add(new Client(newConn));
					} 
					catch (SocketTimeoutException e) {
						System.err.println("[Error] Socket timed out while" +
								" waiting for new connections");
						e.printStackTrace();
					} 
					catch (BindException e) {
						System.err.println("[Error] A server is already listening" +
								" on this IP and port. Exiting...");
						System.exit(1);
					}
					catch (IOException e) {
						System.err.println("[Error] IOException occurred while" +
								" waiting for new connections");
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public static void main(String[] args) {
		startServer();
	}
}