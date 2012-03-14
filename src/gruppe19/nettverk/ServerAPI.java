package gruppe19.nettverk;

/**
 * A class used for communication between the client and the server.
 */
public class ServerAPI {
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
}
