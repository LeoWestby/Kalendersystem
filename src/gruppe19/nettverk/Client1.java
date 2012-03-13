package gruppe19.nettverk;


import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

//import javax.swing.JOptionPane;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.admin.Settings;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;
import no.ntnu.fp.net.co.SimpleConnection;
//import no.ntnu.fp.net.separat.client.ChatClient;
//import no.ntnu.fp.net.separat.client.Gui;
//import no.ntnu.fp.net.separat.client.ChatClient.RecieveThread;


public class Client1 {
	private static int portUsed = 0;

	private String username = "default";

	private int port_to_server = 4444;

	private String addressServer = "localhost";

	private int thisPort = 5555;

	private RecieveThread recieveThread;

	private Connection connection;

	private static boolean SIMPLE_CONNECTION = true;
	
	
	public Client1(String address, int port) {
		port_to_server = port;
		addressServer = address;
		if (SIMPLE_CONNECTION) {
			connection = new SimpleConnection(thisPort);
		} else {
			connection = new ConnectionImpl(thisPort);
		}
		this.login(username);

	}
	
	
	public static void main(String[] args) {
		String address;
		int port;
		Log.setLogName("Klienten");
		Settings settings = new Settings();
		address = settings.getServerAddress();
		port = settings.getServerPort();
		SIMPLE_CONNECTION = settings.useSimpleConnection();

		if (SIMPLE_CONNECTION) {
			System.out.println("Using SimpleConnection");
		}
		Client1 client = new Client1(address, port);
	}
	
	
	private class RecieveThread extends Thread {
		public boolean run = true;
		
		public void run() {
			run = true;
			while (run) {
				try {
					Client1.this.recieve(Client1.this.connection.receive());
				} catch (ConnectException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void recieve(String message) {
//		Sett inn SQL-query meldinger hær.
		
		if (message.equals(" ")) {
			System.out.println("close message");
		}
		if (message.substring(0, 1).equals("[") && message.substring(message.length() - 1, message.length()).equals("]")) {
			String[] list = message.substring(1, message.length() - 1).split(", ");
		} else if (!message.substring(0, 1).equals("/")) {
			try {
//				gui.addMessage(message.substring(message.indexOf(":")),	message.substring(0, message.indexOf(":")));
			} catch (Exception e) {
				try {
					connection.close();
					System.out.println("conection.close()");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	
	public void login(String username) {
		String message;
		System.out.println("Logger inn " + username);
		try {
			connection.connect(InetAddress.getByName(addressServer),
					port_to_server);
			connection.send("Hello:" + username);
			recieveThread = new RecieveThread();
			recieveThread.start();
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void sendMessage(String message) {
		send(this.username + ": " + message);
	}
	
	public void send(String data) {
		try {
			connection.send(data);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		recieveThread.run = false;
		// XXX: Geir: Do not call suspend() on the receive thread, as this
		// stops all receives, including waiting for ACK on the packet that
		// is sent (username+"is closing") and the FIN...
		// --SJ 2006-01-01
		// recieveThread.suspend();
		send(username + " is closing");
		try {
			connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
