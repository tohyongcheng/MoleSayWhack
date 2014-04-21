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
		powerUpType = PowerUpType.DUMMY;
		effectDuration = powerUpType.getEffectDuration();
		loadPreferences();
	}

	
	public static void invoke() {
		inEffect = true;
		if (enableSFX) AssetLoader.dummy.play();
		//generates 6 dummy moles
		gameWorld.generateRandomSpawns(effectDuration,  MoleType.DUMMY);
		gameWorld.generateRandomSpawns(effectDuration,  MoleType.DUMMY);
		gameWorld.generateRandomSpawns(effectDuration,  MoleType.DUMMY);
		gameWorld.generateRandomSpawns(effectDuration,  MoleType.DUMMY);
		gameWorld.generateRandomSpawns(effectDuration,  MoleType.DUMMY);
		gameWorld.generateRandomSpawns(effectDuration, MoleType.DUMMY);
	}
	
	public static void update(float delta) {
		if (isInEffect()) {
			if (runningTime > effectDuration) {
		        runningTime = 0;
				inEffect = false;

			} else{
				runningTime += delta;
			}
		}
	}
}
