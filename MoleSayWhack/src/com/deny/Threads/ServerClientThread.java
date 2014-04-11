package com.deny.Threads;

import java.io.PrintWriter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld.PowerUpState;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Screens.PreGameScreen;

public class ServerClientThread extends Thread {
	private Game game;
	private String address = "localhost";
	private int port = 5000;
	private ServerSocketHints serverHints;
	private Socket client;
	private ServerSocket server;
	private SocketHints clientHints;
	private SocketHints serverClientHints;
	private boolean isServer = false;
	private MultiplayerScreen multiS;
	private PreGameScreen preGameS;
	private PreGamePowerUpScreen preGamePowerUpScreen;
	private PrintWriter out;
	
	private ReadThread readThread;
	
	
	public ServerClientThread(MultiplayerScreen ms, String IPAddress) {
		this.setMultiS(ms);
		this.game = ms.getGame();
		if (IPAddress.equals("")) address = "localhost";
		else address = IPAddress;
	}
	
	public void run() {
		//Create client to check if there is an existing server connection
		clientHints = new SocketHints();
		clientHints.connectTimeout = 1000;
		
		try {
			System.out.println("Trying to find existing server to join...");
			client = Gdx.net.newClientSocket(Protocol.TCP, address, port, null);
		} catch (GdxRuntimeException e) {
			System.out.println("There is no current server running! I'm going to be a server now!");
			isServer = true;
		}
		
		if (isServer) {
			serverClientHints = new SocketHints();
			serverClientHints.connectTimeout = 10000;
			while(true) {
				if (isInterrupted()) {
					System.out.println("Is interrupted!");
					dispose();
					return;
				}
				else {
					
//					System.out.println(address);
//					System.out.println("I'm a server and I'm waiting for new opponents!");
					try {
						serverHints = new ServerSocketHints();
						serverHints.acceptTimeout = 1000;	//set to 0 for infinite waiting
						if (server == null)
							server = Gdx.net.newServerSocket(Protocol.TCP, port , serverHints);
						client = server.accept(serverClientHints);
						
						//stop any more incoming connections after it is connected.
						server.dispose();
						break;
					} catch (GdxRuntimeException e) {
						System.out.println("Error with creating ServerSocket");
					}
					
				}
			}
		}
		
		if (client.isConnected()) {
			System.out.println("Connected to other player!");
			this.getMultiS().setState(MultiplayerState.CONNECTED);
		}
		
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
	
	public void dispose() {
		if (readThread != null) readThread.interrupt();
		
		if (server!=null) server.dispose();
		if (client!=null) client.dispose();
	}
	
	public void setPreGameScreen(PreGameScreen preGameScreen) {
		setPreGameS(preGameScreen);
	}
	
	public PreGameScreen getPreGameScreen() {
		return getPreGameS();
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
	
//	public void toMainMenuScreen() {
//		System.out.println("[SocketHandler] Sending to Change Screen to MainMenuScreen");
//		out.write("[MAINMENUSCREEN] \n");
//		out.flush();
//	}
	
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

	public void leaveGameRoom() {
		System.out.println("[SocketHandler] Sending to Leave GameRoom");
		out.write("[LEAVEMULTIPLAYERSCREEN] \n");
		out.flush();
	}
	
	public void sendPowerUp(PowerUpType powerUpType) {
		System.out.println("[SocketHandler] deployed mole! Sending " + "[SPAWN] " + powerUpType.toString());
		out.write(("[SPAWN] " + powerUpType.toString()));
		out.flush();
	}

	public MultiplayerScreen getMultiS() {
		return multiS;
	}

	public void setMultiS(MultiplayerScreen multiS) {
		this.multiS = multiS;
	}

	public PreGameScreen getPreGameS() {
		return preGameS;
	}

	public void setPreGameS(PreGameScreen preGameS) {
		this.preGameS = preGameS;
	}
	
	public Game getGame() {
		return game;
	}

	public void setPreGamePowerUpScreen(PreGamePowerUpScreen preGamePowerUpScreen) {
		this.preGamePowerUpScreen = preGamePowerUpScreen;
	}

}



