package gruppe19.client.ktn;

import gruppe19.server.ktn.ServerMessage.Type;

import java.io.Serializable;

/**
 * All messages sent from the client are of this class.
 */
public class ClientMessage implements Serializable {
	/**
	 * The request ID recognized by the server.
	 */
	public final char ID;
	public final Object payload;
	public ClientMessage(char ID, Object payload) {
		this.ID = ID;
		this.payload = payload;
	}
}
