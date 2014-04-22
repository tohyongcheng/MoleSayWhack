package com.deny.Threads;

import com.badlogic.gdx.net.Socket;

public class TrudyServer {

	private Socket client;


	public TrudyServer (Socket client){
		this.client = client;

	}
	
	public  boolean doAuthentication() throws Exception{
		return false;
	}
}
