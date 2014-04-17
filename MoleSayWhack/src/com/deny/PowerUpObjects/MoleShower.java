package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class MoleShower extends PowerUp {

	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.MOLESHOWER;
		effectDuration = powerUpType.getEffectDuration();
	}

	
	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						for (int i=0; i<gameWorld.getNumberOfMolesPerGrid(); i++) {
				        	gameWorld.spawnMole(MoleType.ONETAP,i);
						}
					}
				});
			}
		}).start();
	}
	
	public static void update(float delta) {
	}


}
