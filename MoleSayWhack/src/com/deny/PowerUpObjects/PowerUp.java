package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
/**
 * Powerup Abstract Class, governs all the other power
 * ups that are available in this game
 *
 */
abstract class PowerUp {
	private float runningTime;
	private float effectDuration;
	private static boolean inEffect;
	private PowerUpType powerUpType;
	private GameWorld gameWorld;
	private Random r;
	private static Preferences prefs;
	protected static boolean enableSFX;
	/**
	 * a method that loads current preferences
	 * such as whether sound effects are enabled
	 */
	public static void loadPreferences() {
		//Get options
		prefs = Gdx.app.getPreferences("Options");
		enableSFX = prefs.getBoolean("enableSFX", true);
	}
	/**
	 * A method that resets runningTime
	 * of this powerUp
	 */
	public static void resetRunningTime() {
		
	}
	/**
	 * Checks whether this powerUp is in effect
	 * @return InEffect
	 */
	public static boolean isInEffect() {
		return inEffect;
	}
	/**
	 * Unload the powerUp of
	 * this game
	 */
	public static void unload() {
		
	}
}
