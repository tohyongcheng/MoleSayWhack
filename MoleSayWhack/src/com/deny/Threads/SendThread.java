package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.deny.GameObjects.Player;

public class SendThread extends Thread {
	private Socket client;
	private boolean running;
	private BufferedReader br;
	private BufferedReader stdin;
	private Player player;
	private PrintWriter out;
	
	public SendThread(Socket s) {
		client = s;
		running = true;
		stdin = new BufferedReader(new InputStreamReader(System.in));
		br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		out = new PrintWriter(client.getOutputStream(), true);
		
		System.out.println("sendThread succesfully instantiated");
	}
	
	public void stopRunning() {
		running = false;
	}


	public void setPlayer(Player player) {
		this.player = player;
	}
	
//Depending on where is touched, then send string
	//constantly read touch input
//have state of mole deployer & cast deployer ON / OFF

	public void deployMole(int mole, int pos) {
		out.write("[SPAWN] " + mole + " " + pos);
	}

}
