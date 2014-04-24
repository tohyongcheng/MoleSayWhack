package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class DisableAllPowerUps extends PowerUp {
	static float runningTime;
	static float effectDuration;
	static boolean inEffect;
	static PowerUpType powerUpType;
	static GameWorld gameWorld;
	static Random r;
	
	
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
		powerUpType = PowerUpType.DISABLEALLPOWERUPS;
		effectDuration = powerUpType.getEffectDuration();
		loadPreferences();
	}
	/**
	 * A method that invokes or makes
	 * this powerUp effect present
	 * in the game
	 */
	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						causeEffect();
					}
				});
			}
		}).start();
		inEffect = true;
		//play sounds at invoke.
		if (enableSFX) AssetLoader.block.play();
	}

	/**
	 * A method that cause the effect
	 * of this powerup to the game
	 */
	public static void causeEffect() {
		PowerUpDeployer[] powerUpDeployers = gameWorld.getPowerUpDeployers();
		for (int i =0; i<GameWorld.getNumberOfPowerUpDeployers();i++) {
			powerUpDeployers[i].setDisabled(true);
		}
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
				inEffect = false;
				new Thread(new Runnable() {
					public void run() {
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								PowerUpDeployer[] powerUpDeployers = gameWorld.getPowerUpDeployers();
								for (int i =0; i<GameWorld.getNumberOfPowerUpDeployers();i++) {
									powerUpDeployers[i].setDisabled(false);
								}
							}
						});
					}
				}).start();
				resetRunningTime();
			} else{
				runningTime += delta;
			}
		}
	}
}
