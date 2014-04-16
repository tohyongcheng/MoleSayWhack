package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class SpawnMoleKing extends PowerUp {

	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.MOLEKING;
		effectDuration = powerUpType.getEffectDuration();
	}


	public static void invoke() {
		inEffect = true;
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						gameWorld.spawnMole(MoleType.MOLEKING, r.nextInt(9));
					}
				});
			}
		}).start();
	}
	
	public static void update(float delta) {
		return;
	}


}
