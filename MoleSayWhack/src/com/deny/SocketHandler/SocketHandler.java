package com.deny.SocketHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameWorld.GameWorld;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.SendThread;

public class SocketHandler extends Thread {
	
	int port = 5000;
	ServerSocketHints serverHints;
	Socket client;
	ServerSocket server;
	SocketHints clientHints;
	boolean isServer = false;
	GameWorld gameWorld;
	MultiplayerScreen multiS;
	
	private ReadThread readThread;
	private SendThread sendThread;
	
	public SocketHandler(MultiplayerScreen ms) {
		this.multiS = ms;
	}
	

	
	public void run() {
			//Create client to check if there is an existing server connection
			clientHints = new SocketHints();
			clientHints.connectTimeout = 10000;
			
			if (isServer == false) {
				try {
					client = Gdx.net.newClientSocket(Protocol.TCP, "192.168.81.229", port, clientHints);
					System.out.println("Trying to find existing server to join...");
				} catch (GdxRuntimeException e) {
					System.out.println("There is no current server running! I'm going to be a server now!");
					isServer = true;
				}
			}
			
			if (isServer) {
				while(true) {
					if (isInterrupted()) {
						System.out.println("Is interrupted!");
						dispose();
						return;
					}
					else {
						serverHints = new ServerSocketHints();
						serverHints.acceptTimeout = 500;	//set to 0 for infinite waiting
						System.out.println("I'm a server and I'm waiting for new opponents!");
						try {
							if (server == null)
								server = Gdx.net.newServerSocket(Protocol.TCP, port , serverHints);
							client = server.accept(clientHints);
							break;
						} catch (GdxRuntimeException e) {
							
						}
					}
				}
			}
			
			if (client.isConnected()) {
				System.out.println("Connected to server!");
				this.multiS.setReadyToPlay();
			}
		
			
		
		
		
		
//		readThread = (new ReadThread(client, gameWorld));
//		sendThread = (new SendThread(client));
//		readThread.start();
//		sendThread.start();
	}
	
	
	public ServerSocket getServerSocket() {
		return server;
	}
	
	public Socket getClientSocket() {
		return client;
	}

	public SendThread getSendThread() {
		return sendThread;
	}

	public void setSendThread(SendThread sendThread) {
		this.sendThread = sendThread;
	}

	public ReadThread getReadThread() {
		return readThread;
	}

	public void setReadThread(ReadThread readThread) {
		this.readThread = readThread;
	}
	
	public void setGameWorld(GameWorld gw) {
		this.gameWorld = gw;
	}
	
	public void dispose() {
		if (server!=null) server.dispose();
		if (client!=null) client.dispose();
	}

}



