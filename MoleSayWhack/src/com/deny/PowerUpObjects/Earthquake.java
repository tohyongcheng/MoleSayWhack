package com.deny.PowerUpObjects;

import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.MoleObjects.Mole;

public class Earthquake extends PowerUp {
	

	public static void load (GameWorld gw) {
		gameWorld = gw;
		inEffect = false;
		runningTime = 0;
		r = new Random();
		powerUpType = PowerUpType.EARTHQUAKE;
		effectDuration = powerUpType.getEffectDuration();
	}

	/**
	 * This function clears all the moles in the grid as well as in the queues of the grids. This is done
	 * by using LibGDX's postRunnable which passes a Runnable into rendering thread. This will run the code 
	 * in the Runnable in the rendering thread in the next frame, before ApplicationListener.render() is called.
	 * 
	 * It is called in the EARTHQUAKE PowerUp.
	 */
	
	public static void invoke() {
		System.out.println("Invoke Earthquake!");
		new Thread(new Runnable() {
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					public void run() {
						Mole[] moleGrid = gameWorld.getMoleGrid();
						Queue[] moleQueues = gameWorld.getMoleQueues();
						for (int i=0; i<GameWorld.getNumberOfMolesPerGrid(); i++) {
				        	moleGrid[i] = null;
							moleQueues[i].clear();
						}
						
					}
				});
			}
		}).start();
		
	}
}