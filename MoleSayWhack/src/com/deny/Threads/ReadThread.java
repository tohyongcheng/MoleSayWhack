package com.deny.Threads;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.channels.ClosedByInterruptException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Base64Coder;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.Screens.DisconnectScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.OptionsScreen.AuthenticationType;
import com.deny.molewhack.WMGame;

/**
 * This is a thread that reads all the input given
 * from the other player's inputStream
 *
 */

public class ReadThread  extends Thread{
	private WMGame game;
	private Socket client;
	private BufferedReader in;
	private ServerClientThread socketHandler;
	private GameWorld gameWorld;
	private Cipher cipher;
	private ObjectInputStream inObject;
	private Key  symmetricKey;
	private AuthenticationType authType;

	/**
	 * The constructor of the thread, takes in the socket of the other player
	 * and the thread that sends information back to the other player
	 * @param sh
	 * @param client
	 */
	public ReadThread(ServerClientThread sh, Socket client) {
		socketHandler = sh;
		game = socketHandler.getGame();
		this.client = client;
		authType = ServerClientThread.authType;
		if(authType == AuthenticationType.T3 || authType == AuthenticationType.T4){
			symmetricKey = sh.getKey();
		}
	}
	/**
	 * Reads all the messages sent by the other player
	 * and calls the right method accordingly based on the
	 * message received
	 */
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			inObject = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			goToDisconnectedScreen();
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
						System.out.println("EOFE exception");
						goToDisconnectedScreen();
						return;
					} 
					catch (IOException e) {
						System.out.println("Socket is closed.");
						if (Thread.interrupted() == false){
							System.out.println("Thread hasnt been interrupted");	
							gameWorld.setGameState(GameState.EXIT);
							socketHandler.dispose();
							goToDisconnectedScreen();
						}
					
						return;
						
					}
					catch (InvalidKeyException e) {
						goToDisconnectedScreen();
						e.printStackTrace();
						return;
					} catch (ClassNotFoundException e) {
						goToDisconnectedScreen();
						e.printStackTrace();
						return;
					} catch (IllegalBlockSizeException e) {
						goToDisconnectedScreen();
						e.printStackTrace();
						return;
					} catch (BadPaddingException e) {
						goToDisconnectedScreen();
						e.printStackTrace();
						return;
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
				
			
			}
			catch(ClosedByInterruptException e){
				System.out.println("I am Interuppted");
			}
			catch (IOException e) {
				System.out.println("Socket is closed.");
				if (Thread.interrupted() == false)
				{
				System.out.println("Player Leaves game illegally");	
				gameWorld.setGameState(GameState.EXIT);
				socketHandler.dispose();
				goToDisconnectedScreen();
				}
				return;
			} catch(NullPointerException e) {
				goToDisconnectedScreen();
				return;
			}
		}
	}

	/**
	 * Return the gameWorld of the game
	 * @return gameWorld
	 */
	public GameWorld getGameWorld() {
		return gameWorld;
	}
	/**
	 * Sets the gameWorld of which this readThread is sending 
	 * information to
	 * @param gameWorld
	 */
	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	/**
	 * A  method that allows the player to go to the disconnected screen
	 * should the socket connection fails
	 */
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
