package com.deny.PowerUpObjects;

import java.util.Random;

import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;

abstract class PowerUp {
	private float runningTime;
	private float effectDuration;
	private boolean inEffect;
	private PowerUpType powerUpType;
	private GameWorld gameWorld;
	private Random r;
	
	public static void resetRunningTime() {
		
	}
	
	public static boolean isInEffect() {
		return false;
	}
	
	public static void unload() {
		
	}
}
