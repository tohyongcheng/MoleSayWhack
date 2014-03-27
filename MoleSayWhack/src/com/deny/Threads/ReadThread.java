package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.deny.GameObjects.Player;
import com.deny.GameWorld.GameWorld;

public class ReadThread  extends Thread{
	private Socket client;
	private boolean running;
	private BufferedReader in;
	private String message;
	private GameWorld gameWorld;
	
	
	public ReadThread(Socket s) {
		client = s;
		running = true;
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	
	public void run() {
		System.out.println("ReadThread running");
		
//		try {
//			client.getOutputStream().write("CONNECTED\n".getBytes());
//			String response = in.readLine();
//			Gdx.app.log("PingPongSocketExample", "got server message: " + response);
//		} catch (IOException e) {
//			Gdx.app.log("PingPongSocketExample", "an error occured", e);
//		}
		
		
		
		//add the regex to read different inputs.
		//Use tokens, still send strings, use enum . valueof...
		//Mole spawn
		//Powerup spawn
		//score update HP
		//enum
		
		while(running) {
			try {
				String message = in.readLine();
				System.out.println("Received Message: " + message);
				String[] messages = message.split(" ");
				
				if (messages[0].equals("[SPAWN]")) {
					gameWorld.spawnMole(Integer.valueOf(messages[1]), Integer.valueOf(messages[2]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch(NullPointerException e) {
				//TODO: To reconnect
				
				e.printStackTrace();
			}
		}
		
	}

	public void stopRunning() {
		running = false;
	}


	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public void setGameWorld(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
}
