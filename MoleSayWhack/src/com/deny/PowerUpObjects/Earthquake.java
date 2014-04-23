package com.deny.PowerUpObjects;

import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.MoleObjects.Mole;

public class Earthquake extends PowerUp {
	
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
	public static void load (GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.EARTHQUAKE;
		effectDuration = powerUpType.getEffectDuration();
		loadPreferences();
	}

	/**
	 * This function clears all the moles in the grid as well as in the queues of the grids. This is done
	 * by using LibGDX's postRunnable which passes a Runnable into rendering thread. This will run the code 
	 * in the Runnable in the rendering thread in the next frame, before ApplicationListener.render() is called.
	 * 
	 * It is called in the EARTHQUAKE PowerUp.
	 */
	/**
	 * A method that invokes or makes
	 * this powerUp effect present
	 * in the game
	 */
	public static void invoke() {
		inEffect = true;
		System.out.println("Invoke Earthquake!");
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						causeEffect();
					}
				});
			}
		}).start();
		if (enableSFX) AssetLoader.earthQk.play();
	}
	/**
	 * A method that cause the effect
	 * of this powerup to the game
	 */
	public static void causeEffect() {
		Mole[] moleGrid = gameWorld.getMoleGrid();
		Queue[] moleQueues = gameWorld.getMoleQueues();
		for (int i=0; i<GameWorld.getNumberOfMolesPerGrid(); i++) {
        	moleGrid[i] = null;
			moleQueues[i].clear();
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
				runningTime = 0;
				resetRunningTime();
			} else{
				runningTime += delta;
			}
		}
	}
}