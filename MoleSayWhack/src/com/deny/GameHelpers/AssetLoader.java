package com.deny.GameHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader 
{
	//The Textures//
	public static Texture texture, logoTexture;
	public static TextureRegion logo, mswlogo, startButtonUp, startButtonDown;

	public static Sound  hit, explode;
	
	public static void load()
	{
		logoTexture = new Texture(Gdx.files.internal("data/DenySplash.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //815 is the width, 335 is the height//
        logo = new TextureRegion(logoTexture, 0, 0, 815, 335);

	}
	
	public static void dispose() {
        // We must dispose of the texture when we are finished.
        texture.dispose();

//        // Dispose sounds
//        dead.dispose();
//        flap.dispose();
//        coin.dispose();
//
//        font.dispose();
//        shadow.dispose();
    }

}
