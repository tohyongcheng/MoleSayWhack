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
	private BufferedReader br;
	private BufferedReader in;
	private Player player;
	private String message;
	private GameWorld gameWorld;
	
	
	public ReadThread(Socket s, GameWorld gw) {
		client = s;
		running = true;
		gameWorld = gw;
		br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		player = gw.getPlayer();
	}
	
	public void run() {
		System.out.println("Thread running");
		try {
			client.getOutputStream().write("PING\n".getBytes());
			String response = new BufferedReader(new InputStreamReader(client.getInputStream())).readLine();
			Gdx.app.log("PingPongSocketExample", "got server message: " + response);
		} catch (IOException e) {
			Gdx.app.log("PingPongSocketExample", "an error occured", e);
		}
		
		while(running) {
			try {
				if (in.ready()) {
					String message = in.readLine();
					String[] messageTokens = message.split(" ");
					if (messageTokens[0].equals("[SPAWN]")) {
						gameWorld.spawnMole(0, Integer.valueOf(messageTokens[2]));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void stopRunning() {
		running = false;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
