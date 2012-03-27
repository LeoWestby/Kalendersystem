package gruppe19.server.ktn;

import java.io.Serializable;

/**
 * All messages sent from the server are of this class.
 */
public class ServerMessage implements Serializable {
	/**
	 * A response message is expected, a request or broad cast message is not.
	 */
	public static enum Type {
		Response, Request, BroadCast
	};
	
	/**
	 * The request ID recognized by the client.
	 */
	public final char ID;
	public final Type type;
	public final Object payload;
	
	public ServerMessage(char ID, Object payload, Type type) {
		this.payload = payload;
		this.type = type;
		this.ID = ID;
	}
}
