package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class BlockMoleGrid extends PowerUp{

	
	public static void load	(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.BLOCKGRID;
		effectDuration = powerUpType.getEffectDuration();
	}

	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						boolean[] blockedGrids = gameWorld.getBlockedGrids();
						blockedGrids[r.nextInt(2)] = true;
					}
				});
			}
		}).start();
		inEffect = true;
	}

	public static void update(float delta) {
		if (isInEffect()) {			
			if (runningTime > effectDuration) {
				inEffect = false;
				new Thread(new Runnable() {
					public void run() {
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								boolean[] blockedGrids = gameWorld.getBlockedGrids();
								for (int i =0 ; i< gameWorld.getNumberOfMolesPerGrid(); i++) {
									blockedGrids[i] = false;
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
