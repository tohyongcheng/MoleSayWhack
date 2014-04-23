package com.deny.molewhack;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.Screens.SplashScreen;
/**
 * The class that creates the whole game
 * when the application is first run
 *
 */
public class WMGame extends Game {
	
	@Override
	/**
	 * Loads all the asset loader,
	 * and go to splashscreen,
	 * which will begin the game
	 */
	public void create() 
	{		
		AssetLoader.load();
		System.out.println("Game created");
		setScreen(new SplashScreen(this));
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	/**
	 * Dispose all the assetLoaders
	 * and itself when game exits
	 */
    public void dispose() 
	{
        super.dispose();
        AssetLoader.dispose();
    }
	
}
