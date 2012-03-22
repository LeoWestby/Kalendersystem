package gruppe19.a.EntryPoints;

import gruppe19.gui.LoginScreen;
import gruppe19.server.ktn.Server;

public class EntryPoint {
	public static void main(String[] args) {
		//Start server
		Server.main(args);
		
		//Start client
		LoginScreen.main(args);
	}
}
