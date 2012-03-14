package gruppe19.server.ktn;

import java.io.Serializable;

/**
 * All messages sent from the server are of this class.
 */
public class ServerMessage implements Serializable {
	public static enum Type {
		Response, Request, BroadCast
	};
	
	public final Type type;
	public final boolean isString;
	public final Object payload;
	
	public ServerMessage(Object payload, Type type) {
		this.payload = payload;
		this.type = type;
		isString = payload instanceof String;
	}
}
