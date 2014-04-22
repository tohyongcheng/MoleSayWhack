package com.deny.molewhack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.Screens.SplashScreen;

public class WMGame extends Game {
	
	@Override
	public void create() 
	{		
		AssetLoader.load();
		System.out.println("Game created");
		setScreen(new SplashScreen(this));
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
    public void dispose() 
	{
        super.dispose();
        AssetLoader.dispose();
    }
	
}
