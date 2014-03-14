package com.deny.SocketHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ReadThread;
import com.deny.Threads.SendThread;

public class SocketHandler extends Thread {
	
	int port = 9999;
	ServerSocketHints serverHints;
	Socket client;
	ServerSocket server;
	SocketHints clientHints;
	boolean isServer;
	GameWorld gameWorld;
	
	private ReadThread readThread;
	private SendThread sendThread;
	
	public SocketHandler(boolean isServer, GameWorld gw) {
		this.isServer = isServer;
		gameWorld = gw;
	}
	
	public void run() {
		
		if (isServer) {
			serverHints = new ServerSocketHints();
			server = Gdx.net.newServerSocket(Protocol.TCP, port , serverHints);
		} else {
			clientHints = new SocketHints();
			
			//start client
			client = Gdx.net.newClientSocket(Protocol.TCP, "localhost", port, clientHints);
		}
		
		if (isServer) {
			client = server.accept(null);
		}
		
		if (client.isConnected()) {
			System.out.println("Connected to server!");
		}
		readThread = (new ReadThread(client, gameWorld));
		sendThread = (new SendThread(client));
		readThread.start();
		sendThread.start();
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

}



