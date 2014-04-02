package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.badlogic.gdx.net.Socket;
import com.deny.GameObjects.MoleType;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.PreGameScreen.PreGameState;

public class ReadThread  extends Thread{
	private Socket client;
	private BufferedReader in;
	private ServerClientThread socketHandler;
	private GameWorld gameWorld;
	
	
	public ReadThread(ServerClientThread sh, Socket s) {
		socketHandler = sh;
		client = s;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	
	public void run() {
		System.out.println("ReadThread running");
		
		while(true) {
			if (isInterrupted()) {
				return;
			}
			
			try {
				String message = in.readLine();
				System.out.println("Received Message: " + message);
				String[] messages = message.split(" ");
				
				switch(messages[0]) {
				
				//GAMESCREEN
				case "[SPAWN]":
					MoleType moleType = MoleType.valueOf(messages[1]);
					int pos =  Integer.valueOf(messages[2]);
					gameWorld.spawnMole(moleType,pos);
					break;
				case "[CHOOSEMOLESCREEN]":
					System.out.println("Received message to go to select Moles Screen");
					socketHandler.getMultiS().setState(MultiplayerState.START);
					break;
				case "[GAMEOVER]":
					System.out.println("Received message that other player has died");
					gameWorld.setGameState(GameState.WIN);
					break;
				case "[RESTARTGAME]":
					System.out.println("Received message to restart the game");
					gameWorld.setGameState(GameState.RESTART);
					break;
				case "[EXITGAME]":
					System.out.println("Received message to exit the game");
					gameWorld.setGameState(GameState.EXIT);
					interrupt(); 
					break;
					
				//PREGAMESCREEN
				case "[MAINMENUSCREEN]":
					System.out.println("Received message to go back to main menu!");
					if (socketHandler.getPreGameS() !=null) socketHandler.getPreGameS().setState(PreGameState.QUIT);
					break;
					
				//MULTIPLAYERSCREEN
				case "[LEAVEMULTIPLAYERSCREEN]":
					System.out.println("Received message to restart server!");
					if (socketHandler.getMultiS() !=null) socketHandler.getMultiS().setState(MultiplayerState.RESTART);
					break;
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Socket Closed");
			} catch(NullPointerException e) {
				System.out.println("Lost connection!");
				//WAIT FOR RECONNECTION
			}
		}
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
}
