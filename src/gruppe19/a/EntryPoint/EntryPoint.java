package gruppe19.a.EntryPoint;

import gruppe19.gui.LoginScreen;
import gruppe19.server.ktn.Server;

public class EntryPoint {
	public static void main(String[] args) {
		//Start server
		Server.startServer();
		
		//Start client
		new LoginScreen();
	}
}
