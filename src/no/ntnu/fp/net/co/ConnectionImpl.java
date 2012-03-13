/*
 * Created on Oct 27, 2004
 */
package no.ntnu.fp.net.co;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.cl.ClSocket;
import no.ntnu.fp.net.cl.KtnDatagram;
import no.ntnu.fp.net.cl.KtnDatagram.Flag;

/**
 * Implementation of the Connection-interface. <br>
 * <br>
 * This class implements the behaviour in the methods specified in the interface
 * {@link Connection} over the unreliable, connectionless network realised in
 * {@link ClSocket}. The base class, {@link AbstractConnection} implements some
 * of the functionality, leaving message passing and error handling to this
 * implementation.
 * 
 * @author Sebjørn Birkeland and Stein Jakob Nordbø
 * @see no.ntnu.fp.net.co.Connection
 * @see no.ntnu.fp.net.cl.ClSocket
 */
public class ConnectionImpl extends AbstractConnection {

	/** Keeps track of the used ports for each server port. */
	private static Map<Integer, Boolean> usedPorts = Collections
			.synchronizedMap(new HashMap<Integer, Boolean>());

	/**
	 * Initializes initial sequence number and setup state machine.
	 * 
	 * @param myPort
	 *            - the local port to associate with this connection
	 */
	public ConnectionImpl(int myPort) {
		super(); //Initializes sequence number and sets state to disabled
		myAddress = getIPv4Address();
		this.myPort = myPort;
		usedPorts.put(myPort, true);
	}
	
	private static String getIPv4Address() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "127.0.0.1";
		}
	}

	/**
	 * Establish a connection to a remote location.
	 * 
	 * @param remoteAddress
	 *            - the remote IP-address to connect to
	 * @param remotePort
	 *            - the remote portnumber to connect to
	 * @throws IOException
	 *             If there's an I/O error.
	 * @throws java.net.SocketTimeoutException
	 *             If timeout expires before connection is completed.
	 * @see Connection#connect(InetAddress, int)
	 */
	public void connect(InetAddress remoteAddress, int remotePort)
			throws IOException, SocketTimeoutException {
		this.remoteAddress = remoteAddress.getHostAddress();
		this.remotePort = remotePort;
		
        if (state != State.CLOSED) {
            throw new ConnectException("The socket is already connected");
        }
        Log.writeToLog("Trying to connect to: "
							+ remoteAddress.getHostAddress() 
							+ " : " 
							+ remotePort, 
							"ConnectionImpl");
		//Handshake
		try {
			//Send SYN to server
			int tries = 3;
			boolean sent = false;

			KtnDatagram synPacket = constructInternalPacket(Flag.SYN);

			// Send the SYN, trying at most `tries' times.
			Log.writeToLog(synPacket, "Sending SYN", "ConnectionImpl");

			do {
				try {
					new ClSocket().send(synPacket);
					sent = true;
				} catch (ConnectException e) {
					// Silently ignore: Maybe server was processing and didn't
					// manage to receive the syn before we were ready to send.
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {/*Ignore*/}
				}
			} while (!sent && (tries-- > 0));

			if (!sent) {
				nextSequenceNo--;
				throw new ConnectException("Unable to send SYN.");
			}
			state = State.SYN_SENT;
			
			//Receive SYNACK from server
			KtnDatagram response = receiveAck();
			
			while (response == null) {
				receiveAck();
			}

			//Connect to the new connection
			this.remoteAddress = response.getSrc_addr();
			this.remotePort = response.getSrc_port();
			
			//ACK the SYNACK
			sendAck(response, false);
		}
		catch (ConnectException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ConnectException("Could not establish connection with server");
		}
		state = State.ESTABLISHED;
		Log.writeToLog("Connection established", "ConnectionImpl");
	}
	
	/**
	 * Listen for, and accept, incoming connections.
	 * 
	 * @return A new ConnectionImpl-object representing the new connection.
	 * @see Connection#accept()
	 */
	public Connection accept() throws IOException, SocketTimeoutException {
		state = State.LISTEN;
		KtnDatagram response = null;
		Log.writeToLog("Listening for new connections on port " + myPort, 
						"ConnectionImpl");
		
		while (!isValid(response) || response.getFlag() != Flag.SYN) {
			//Wait for SYN package
			response = receivePacket(true);
		}
		
		//Set up the new connection
		ConnectionImpl newConnection = new ConnectionImpl(getFreePort());
		newConnection.lastValidPacketReceived = response;
		newConnection.state = State.SYN_RCVD;
		newConnection.remoteAddress = response.getSrc_addr();
		newConnection.remotePort = response.getSrc_port();

		//Send SYNACK
		newConnection.sendAck(response, true);

		while (newConnection.receiveAck() == null)
			;
		
		//Return the established connection to client
		newConnection.state = State.ESTABLISHED;
		state = State.CLOSED;
		return newConnection;
    }
	
	/**
	 * @return An unused, random port between 10k and 20k.
	 */
	private static int getFreePort() {
		int port;
		while (usedPorts.containsKey(
				port = (int)(Math.random() * 10000) + 10000))
			;
		return port;
	}

	/**
	 * Send a message from the application.
	 * 
	 * @param msg
	 *            - the String to be sent.
	 * @throws ConnectException
	 *             If no connection exists.
	 * @throws IOException
	 *             If no ACK was received.
	 * @see AbstractConnection#sendDataPacketWithRetransmit(KtnDatagram)
	 * @see no.ntnu.fp.net.co.Connection#send(String)
	 */
	public synchronized void send(String msg) throws ConnectException, IOException {
		if (state != State.ESTABLISHED) {
			throw new ConnectException("Cannot send without an established connection");
		}
		boolean sent = false;
		
		while (!sent) {
			try {
				sendDataPacketWithRetransmit(constructDataPacket(msg));
				sent = true;
			}
			catch (SocketException e) {/*Try again*/}
		}
		
	}

	/**
	 * Wait for incoming data.
	 * 
	 * @return The received data's payload as a String.
	 * @see Connection#receive()
	 * @see AbstractConnection#receivePacket(boolean)
	 * @see AbstractConnection#sendAck(KtnDatagram, boolean)
	 */
	public String receive() throws ConnectException, IOException {
		if (state != State.ESTABLISHED) {
			throw new ConnectException("Cannot receive without an established connection");
		}
		boolean done = false;
		KtnDatagram received = null;
		
		while (!isValid(received) || !done) {
			try {
				received = receivePacket(false);
				lastValidPacketReceived = received;
				done = true;
				sendAck(received, false);
			}
			catch (EOFException e) {
				//More code for closing the connection goes here
			}
			catch (SocketException e){/*Try again*/}
		}
		return (String)lastValidPacketReceived.getPayload();
	}

	/**
	 * Close the connection.
	 * 
	 * @see Connection#close()
	 */
	public void close() throws IOException {
		if (state != State.ESTABLISHED) {
			throw new ConnectException("Cannot disconnect an unestablished connection");
		}
		
		//if (1 == 1) throw new UnsupportedOperationException();
		//The below code crashes and needs fixing
		
		//Send FIN packet
		KtnDatagram finPacket = constructInternalPacket(Flag.FIN);
		sendDataPacketWithRetransmit(finPacket);
		state = State.FIN_WAIT_1;
		
		//Receive ACK
		if ((lastValidPacketReceived = receiveAck()) == null) {
			throw new ConnectException(
					"Unexpected response from server while disconnecting");
		}
		state = State.FIN_WAIT_2;
		
		//Receive FIN and send ACK
		lastValidPacketReceived = receivePacket(true);
		
		if (lastValidPacketReceived.getFlag() != Flag.FIN
				|| (lastValidPacketReceived = receiveAck()) == null) {
			throw new ConnectException(
					"Unexpected response from server while disconnecting");
		}
		state = State.TIME_WAIT;
		
		//Allow receiving end to close down
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {/*Ignore*/}
		
		//Shut down
		state = State.CLOSED;
	}

	/**
	 * Test a packet for transmission errors. This function should only called
	 * with data or ACK packets in the ESTABLISHED state.
	 * 
	 * @param packet
	 *            Packet to test.
	 * @return true if packet is free of errors, false otherwise.
	 */
	protected boolean isValid(KtnDatagram packet) {
		return packet != null && packet.calculateChecksum() == packet.getChecksum();
	}
}