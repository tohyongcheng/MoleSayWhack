package com.deny.Threads;

import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameObjects.MoleType;
import com.deny.GameWorld.GameWorld;
import com.deny.Screens.MultiplayerScreen;

public class ServerClientThread extends Thread {
	
	final String address = "192.168.1.151";
	int port = 5000;
	ServerSocketHints serverHints;
	Socket client;
	ServerSocket server;
	SocketHints clientHints;
	boolean isServer = false;
	GameWorld gameWorld;
	MultiplayerScreen multiS;
	boolean isRunning = true;
	private PrintWriter out;
	
	private ReadThread readThread;
	
	public ServerClientThread(MultiplayerScreen ms) {
		this.multiS = ms;
	}
	
	public void run() {
		//Create client to check if there is an existing server connection
		clientHints = new SocketHints();
		clientHints.connectTimeout = 10000;
		
		if (isServer == false) {
			try {
				client = Gdx.net.newClientSocket(Protocol.TCP, address, port, clientHints);
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
			this.multiS.setStateToConnected();
		}
	
		
		isRunning = true;
		//stdin = new BufferedReader(new InputStreamReader(System.in));
		//br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		
		readThread = new ReadThread(this,client);
		readThread.start();
	}
	
	
	public ServerSocket getServerSocket() {
		return server;
	}
	
	public Socket getClientSocket() {
		return client;
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
		if (readThread != null) readThread.interrupt();
		if (multiS != null) multiS.dispose();
		if (server!=null) server.dispose();
		if (client!=null) client.dispose();
	}
	
	public void deployMole(MoleType moleType, int pos) {
		System.out.println("[SocketHandler] deployed mole! Sending " + "[SPAWN] " + moleType.toString() + " " + pos);
		out.write(("[SPAWN] " + moleType.toString() + " " + pos+"\n"));
		out.flush();
	}
	
	public void pauseGame() {
		System.out.println("[SocketHandler] Sending to Pause Game");
		out.write(("[PAUSE] \n"));
		out.flush();
	}
	
	public void continueGame() {
		System.out.println("[SocketHandler] Sending to Continue Game");
		out.write(("[CONTINUE] \n"));
		out.flush();
	}
	
	public void toChooseMolesScreen() {
		System.out.println("[SocketHandler] Sending to Change Screen to Choose Moles Screen");
		out.write("[CHOOSEMOLESCREEN] \n");
		out.flush();
	}
	
	public void gameOver() {
		System.out.println("[SocketHandler] Sending GameOver");
		out.write("[GAMEOVER] \n");
		out.flush();
	}

	public void restartGame() {
		System.out.println("[SocketHandler] Sending RestartGame");
		out.write("[RESTARTGAME] \n");
		out.flush();
	}

	public void exitGame() {
		System.out.println("[SocketHandler] Sending ExitGame");
		out.write("[EXITGAME] \n");
		out.flush();
	}

}



