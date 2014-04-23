package com.deny.Threads;
import com.badlogic.gdx.net.Socket;

/**
 * Client code of authentication type TRUDY
 *
 */
public class TrudyClient {
	private Socket server;

	
	
	
	public TrudyClient(Socket Server){
		this.server = Server;

	}
	
	public boolean doAuthentication() throws Exception{
	return false;
	}
}
