package gruppe19.a.EntryPoint;

import gruppe19.client.gui.LoginScreen;
import gruppe19.server.ktn.Server;

/**
 * Contains a single function that starts both a server and a client.
 * <p>
 * NB! Running this function more than once on the same computer will not work.
 * To create more than one client, run gruppe19.client.gui.LoginScreen.java.
 */
public class EntryPoint {
	/**
	 * A combined entry point for both the server and the client.
	 * 
	 * @param args Ignored
	 */
	public static void main(String[] args) {
		//Start server 
		Server.main(null);
		
		//Start client
		LoginScreen.main(null);
	}
}