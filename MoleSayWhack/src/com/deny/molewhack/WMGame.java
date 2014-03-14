package com.deny.molewhack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.Game;
import com.deny.Screens.GameScreen;
import com.deny.SocketHandler.SocketHandler;

public class WMGame extends Game {
	
	@Override
	public void create() {		
		
		System.out.println("Game created");
		setScreen(new GameScreen());
	}


	
	
}
