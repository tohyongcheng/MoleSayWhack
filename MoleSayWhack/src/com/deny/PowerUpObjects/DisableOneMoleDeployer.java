package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class DisableOneMoleDeployer extends PowerUp {
	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.DISABLEONEMOLEDEPLOYER;
		effectDuration = powerUpType.getEffectDuration();
	}

	
	public static void invoke() {
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						//Set a random Mole Deployer to be disabled
						MoleDeployer[] moleDeployers = gameWorld.getMoleDeployers();
						moleDeployers[r.nextInt(2)].setDisabled(true);
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
								//Set a random Mole Deployer to be disabled
								MoleDeployer[] moleDeployers = gameWorld.getMoleDeployers();
								for (int i =0 ; i < GameWorld.getNumberOfMoleDeployers() ; i++) {
									moleDeployers[i].setDisabled(false);	
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
