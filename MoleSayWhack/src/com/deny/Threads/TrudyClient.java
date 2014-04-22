package com.deny.Threads;
import com.badlogic.gdx.net.Socket;

/**
 * In this case, we send the MAC secretKey.
 * But we assume that both have already shared a known secretKey a priori
 * @author Crimsonlycans
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
