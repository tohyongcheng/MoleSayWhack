package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class EnableFog extends PowerUp {

	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.FOG;
		effectDuration = powerUpType.getEffectDuration();
	}

	
	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						//Set fog to be true!
						gameWorld.setFog(true);
					}
				});
			}
		}).start();
		inEffect = true;
		//play sounds at invoke.
    	AssetLoader.fogg.play();
		
	}

	
	public static void update(float delta) {
		
		if (isInEffect()) {	
			System.out.println(runningTime);
			if (runningTime > effectDuration) {
				
				inEffect = false;
				new Thread(new Runnable() {
					public void run() {
						Gdx.app.postRunnable(new Runnable() {
							public void run() {
								//Set fog to be true!
								System.out.println(runningTime);
								System.out.println("Thread runs");
								gameWorld.setFog(false);
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
