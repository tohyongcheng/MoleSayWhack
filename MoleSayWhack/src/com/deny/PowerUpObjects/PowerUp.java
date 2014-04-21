package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

abstract class PowerUp {
	private float runningTime;
	private float effectDuration;
	private boolean inEffect;
	private PowerUpType powerUpType;
	private GameWorld gameWorld;
	private Random r;
	private static Preferences prefs;
	protected static boolean enableSFX;
	
	public static void loadPreferences() {
		//Get options
		prefs = Gdx.app.getPreferences("Options");
		enableSFX = prefs.getBoolean("enableSFX", true);
	}
	
	public static void resetRunningTime() {
		
	}
	
	public static boolean isInEffect() {
		return false;
	}
	
	public static void unload() {
		
	}
}
