package com.deny.molewhack;

import com.badlogic.gdx.Game;
import com.deny.Screens.MainMenuScreen;

public class WMGame extends Game {
	
	@Override
	public void create() {		
		
		System.out.println("Game created");
		setScreen(new MainMenuScreen(this));
//		setScreen(new GameScreen());
	}


	
	
}
