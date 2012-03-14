package gruppe19.server.ktn;

import gruppe19.server.db.DatabaseAPI;
import gruppe19.server.ktn.ServerMessage.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

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
		public Connection conn;

		public Client(Connection conn) {
			this.conn = conn;
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
						recieve(conn.receive());
					} 
					catch (ConnectException e) {
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
		private void recieve(String request) {
			if (request == null || request.isEmpty()) {
				return;
			}
			
			switch (request.charAt(0)) {
			case 'a':
				send(new ServerMessage(
						DatabaseAPI.logIn(
								request.substring(1, request.indexOf(0)),
								request.substring(request.indexOf(0) + 1)),
								Type.Response));
				break;
			case 'b':
				
				break;
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
				//Invalid request
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
				conn.send(new String(baos.toByteArray()));
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
			c.send(new ServerMessage(msg, Type.BroadCast));
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