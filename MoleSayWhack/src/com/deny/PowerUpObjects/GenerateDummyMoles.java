package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class GenerateDummyMoles extends PowerUp {
	static float runningTime;
	static float effectDuration;
	static boolean inEffect;
	static PowerUpType powerUpType;
	static GameWorld gameWorld;
	static Random r;
	
	static float intervalTime;
	/**
	 * A method that resets runningTime
	 * of this powerUp
	 */
	public static void resetRunningTime() {
		runningTime = 0;
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
		inEffect = false;
		runningTime = 0;
		powerUpType = null;
		effectDuration = 0;
		gameWorld = null;
	}
	/**
	 * Load this powerUp to this game
	 * @param gw
	 */
	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.DUMMY;
		effectDuration = powerUpType.getEffectDuration();
		loadPreferences();
		intervalTime = 0;
	}

	/**
	 * A method that invokes or makes
	 * this powerUp effect present
	 * in the game
	 */
	public static void invoke() {
		inEffect = true;
		if (enableSFX) AssetLoader.dummy.play();
		causeEffect();
	}
	/**
	 * A method that cause the effect
	 * of this powerup to the game
	 */
	public static void causeEffect() {
		gameWorld.spawnMole(MoleType.DUMMY, r.nextInt(9));
	}
	/**
	 * A method that update the state of this
	 * powerUp depending on the current state or
	 * the time this powerup has existed, as well as
	 * the cooldown time of this powerup
	 * @param delta
	 */
	public static void update(float delta) {
		if (isInEffect()) {
			if (runningTime > effectDuration) {
		        runningTime = 0;
				inEffect = false;
			} else{
				runningTime += delta;
				intervalTime += delta;
				if (intervalTime > 1) {
					gameWorld.spawnMole(MoleType.DUMMY, r.nextInt(9));
					intervalTime = 0;
				}
				
			}
		}
	}
}
