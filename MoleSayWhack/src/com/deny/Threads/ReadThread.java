package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.deny.GameObjects.Player;
import com.deny.GameObjects.TokenType;
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
		
		//add the regex to read different inputs.
		//Use tokens, still send strings, use enum . valueof...
		//Mole spawn
		//Powerup spawn
		//score update HP
		//enum
		
		while(running) {
			try {
				if (in.ready()) {
					String message = in.readLine();
					TokenType messageToken = TokenType.valueOf(message);
					
					//create new moles here and deploy them to the board
					//create new powerups here and deploy them to the board
					//constantly read innputSocket
					switch (messageToken){
					case ONEtap:
						//gameworld..deploy (new Mole1(player))??
						break;
					case THREEtap:
						break;
					case FIVEtap:
						break;
					case CLONEMOLE:
						break;
					case DIVINESHIELD:
						break;
					case EARTHQUAKE:
						break;
					case KINGMOLE:
						break;
					case MOLESHOWER:
						break;
					case MOULDY:
						break;
					case SABOTAGE:
						break;
					case SUPERMOLE:
						break;
					case THEOWL:
						break;
					case SCORE:
						break;
					default:
						break;
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
