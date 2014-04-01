package com.deny.molewhack;

import com.badlogic.gdx.Game;
import com.deny.GameHelpers.AssetLoader;
import com.deny.Screens.SplashScreen;

public class WMGame extends Game {
	
	@Override
	public void create() 
	{		
		AssetLoader.load();
		System.out.println("Game created");
		setScreen(new SplashScreen(this));
//		setScreen(new MainMenuScreen(this));
//		setScreen(new GameScreen());
	}
	
	@Override
    public void dispose() 
	{
        super.dispose();
        AssetLoader.dispose();
    }
	
}
