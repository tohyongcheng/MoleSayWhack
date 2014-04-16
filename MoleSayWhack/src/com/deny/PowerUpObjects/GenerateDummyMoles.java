package com.deny.PowerUpObjects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

public class GenerateDummyMoles extends PowerUp {

	public static void load(GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.DUMMY;
		effectDuration = powerUpType.getEffectDuration();
	}

	
	public static void invoke() {
		inEffect = true;
	}
	
	public static void update(float delta) {
		if (isInEffect()) {			
			if (runningTime > effectDuration) {
				inEffect = false;
				gameWorld.generateRandomSpawns(effectDuration, delta, MoleType.DUMMY);
			} else{
				runningTime += delta;
			}
		}
	}
}
