package com.deny.Threads;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Base64Coder;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.Screens.DisconnectScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.OptionsScreen.AuthenticationType;
import com.deny.Screens.PreGameScreen;

public class ReadThread  extends Thread{
	private Game game;
	private Socket client;
	private BufferedReader in;
	private ServerClientThread socketHandler;
	private GameWorld gameWorld;
	private PreGameScreen preGameScreen;
	private MultiplayerScreen multiPlayerScreen;
	private Cipher cipher;
	private ObjectInputStream inObject;
	private Key  symmetricKey;
	private AuthenticationType authType;


	public ReadThread(ServerClientThread sh, Socket client) {
		socketHandler = sh;
		game = socketHandler.getGame();
		this.client = client;
		authType = ServerClientThread.authType;
		if(authType == AuthenticationType.T3 || authType == AuthenticationType.T4){
			symmetricKey = sh.getKey();
		}
	}

	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			inObject = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("ReadThread running");
		
		while(true) {
			if (isInterrupted()) {
				return;
			}
			try {
				String message = "";
				if (authType == AuthenticationType.T3 || authType == AuthenticationType.T4){
					try {
						System.out.println("Doing Decryption:");
						Object messageObject = inObject.readObject();
						String messageString = (String) messageObject;
						byte[] messageByte = Base64Coder.decode(messageString);
						cipher.init(Cipher.DECRYPT_MODE, symmetricKey);

						byte[] newMessageByte = cipher.doFinal(messageByte);
						String messageTemp = new String(newMessageByte, "UTF-8");
						message = messageTemp.substring(0);
					}  catch (EOFException e) {
						goToDisconnectedScreen();
						e.printStackTrace();
						return;
					} 
					catch (SocketException e) {
						System.out.println("Socket Closed");
					} catch (InvalidKeyException e) {
						goToDisconnectedScreen();
					} catch (ClassNotFoundException e) {
						goToDisconnectedScreen();
					} catch (IllegalBlockSizeException e) {
						goToDisconnectedScreen();
					} catch (BadPaddingException e) {
						goToDisconnectedScreen();
					}
				}
				else{
					message = in.readLine();
				}
				System.out.println("Received Message: " + message);
				String[] messages = message.split(" ");
				messages[0] = messages[0].trim();
				switch(messages[0]) {
				//GAMESCREEN
				
				case "[SPAWN]":
					MoleType moleType = MoleType.valueOf(messages[1].trim());
					System.out.println(messages[2]);
					int pos =  	Integer.valueOf(messages[2].trim());
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
					PowerUpType powerUp = PowerUpType.valueOf(messages[1].trim());
					gameWorld.invokePowerUp(powerUp);
					break;
				case "[OPPONENTHP]":
					System.out.println("Received message about opponent HP: " + messages[1]);
					char hp = messages[1].charAt(0);
					gameWorld.setOpponentHP(Character.getNumericValue(hp));
					break;
				}
			} catch (IOException e) {
				System.out.println("Socket Closed");
			} catch(NullPointerException e) {
				goToDisconnectedScreen();
				return;
			}
		}
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
