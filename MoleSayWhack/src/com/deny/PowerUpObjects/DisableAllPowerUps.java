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
	
	
	
	public static void resetRunningTime() {
		runningTime = 0;
	}
	
	public static boolean isInEffect() {
		return inEffect;
	}
	
	public static void unload() {
		inEffect = false;
		runningTime = 0;
		powerUpType = null;
		effectDuration = 0;
		gameWorld = null;
	}
	
	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.DISABLEALLPOWERUPS;
		effectDuration = powerUpType.getEffectDuration();
		loadPreferences();
	}

	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						PowerUpDeployer[] powerUpDeployers = gameWorld.getPowerUpDeployers();
						for (int i =0; i<gameWorld.getNumberOfPowerUpDeployers();i++) {
							powerUpDeployers[i].setDisabled(true);
						}
					}
				});
			}
		}).start();
		inEffect = true;
		//play sounds at invoke.
		if (enableSFX) AssetLoader.block.play();
	}

	public static void update(float delta) {
		if (isInEffect()) {			
			if (runningTime > effectDuration) {
				inEffect = false;
				new Thread(new Runnable() {
					public void run() {
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								PowerUpDeployer[] powerUpDeployers = gameWorld.getPowerUpDeployers();
								for (int i =0; i<gameWorld.getNumberOfPowerUpDeployers();i++) {
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
