package gruppe19.nettverk;


import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.admin.Settings;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;
import no.ntnu.fp.net.co.SimpleConnection;


public class Client1 {
	private static int portUsed = 0;

	private String username = "default";

	private int port_to_server = 4444;

	private String addressServer = "localhost";

	private int thisPort = 5555;

	private RecieveThread recieveThread;

	private Connection connection;

	private static boolean SIMPLE_CONNECTION = true;
}
