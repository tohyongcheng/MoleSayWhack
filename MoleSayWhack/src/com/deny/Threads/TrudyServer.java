package com.deny.Threads;

import com.badlogic.gdx.net.Socket;
/**
 * Server code of authentication type TRUDY
 *
 */
public class TrudyServer {

	private Socket client;


	public TrudyServer (Socket client){
		this.client = client;

	}
	
	public  boolean doAuthentication() throws Exception{
		return false;
	}
}
