package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.transform.Result;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.Screens.DisconnectScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.PreGameScreen;
import com.deny.Screens.PreGameScreen.PreGameState;

public class ReadThread  extends Thread{
	private Game game;
	private Socket client;
	private BufferedReader in;
	private ServerClientThread socketHandler;
	private GameWorld gameWorld;
	private PreGameScreen preGameScreen;
	private MultiplayerScreen multiPlayerScreen;
	public enum ScreenState{
		MULTIPLAYER, PREGAME, GAME, PREGAMEPOWERUP
	}
	private ScreenState currentState;
	
	
	//NOT THREAD SAFE
	public ReadThread(ServerClientThread sh, Socket client) {
		socketHandler = sh;
		game = socketHandler.getGame();
		this.client = client;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.currentState = ScreenState.MULTIPLAYER;
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
					socketHandler.getMultiplayerScreen().setState(MultiplayerState.START);
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
				case "[PAUSE]":
					System.out.println("Received message to pause the game");
					gameWorld.setGameState(GameState.PAUSE);
					break;
				case "[CONTINUE]":
					System.out.println("Received message to unpause the game");
					gameWorld.setGameState(GameState.RUNNING);
					break;
				case "[POWERUP]":
					System.out.println("Received message about invoking a powerup on current player: " + messages[1]);
					PowerUpType powerUp = PowerUpType.valueOf(messages[1]);
					gameWorld.invokePowerUp(powerUp);
					break;
				case "[OPPONENTHP]":
					System.out.println("Received message about opponent HP: " + messages[1]);
					gameWorld.setOpponentHP(Integer.valueOf(messages[1]));
					break;
				//PREGAMESCREEN
				case "[MAINMENUSCREEN]":
					System.out.println("Received message to go back to main menu!");
					if (socketHandler.getPreGameScreen() !=null) socketHandler.getPreGameScreen().setState(PreGameState.QUIT);
					break;
				//MULTIPLAYERSCREEN
				case "[LEAVEMULTIPLAYERSCREEN]":
					System.out.println("Received message to restart server!");
					if (socketHandler.getMultiplayerScreen() !=null) socketHandler.getMultiplayerScreen().setState(MultiplayerState.RESTART);
					break;
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("Socket Closed");
			} catch(NullPointerException e) {
				
				goToDisconnectedScreen();
				return;
			}
		}
	}

	public ScreenState getCurrentState() {
		return currentState;
	}

	public void setCurrentState(ScreenState currentState) {
		this.currentState = currentState;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	public void goToDisconnectedScreen() {
		new Thread(new Runnable() {
			   @Override
			   public void run() {
				  
			      Gdx.app.postRunnable(new Runnable() {
			         @Override
			         public void run() {
			        	 game.setScreen(new DisconnectScreen(game));
			         }
			      });
			   }
			}).start();
		
		socketHandler.dispose();
	}
}
