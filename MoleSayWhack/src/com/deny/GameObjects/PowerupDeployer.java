package com.deny.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;
//Deploys powerups
//initialized at the beginning of the game

public class PowerUpDeployer {
	
	private PowerUpType powerupType;
	private final float coolDown;
	private float timeDeployed;
	private GameWorld gameWorld;
	private Rectangle boundingRectangle;
	boolean isAvailable;
	private ServerClientThread socketHandler;
	
	public PowerUpDeployer(GameWorld gameWorld, PowerUpType powerupType){
		this.gameWorld = gameWorld;
		this.socketHandler = gameWorld.getSocketHandler();
		this.powerupType = powerupType;
		this.coolDown = powerupType.getCoolDown();
		boundingRectangle = new Rectangle();
		timeDeployed = 0;
	}
	
	
	public void update(float delta) 
	{
		//wait until time deployed exceeds cooldown to make it available again//
		if (timeDeployed < coolDown && !(isAvailable)) {
			timeDeployed += delta;
		} else if (timeDeployed >= coolDown && !(isAvailable)){
			timeDeployed = 0;
			isAvailable = true;
		}
	}
	
	public boolean isAvailable() {
		return isAvailable;
	}
	
	public PowerUpType getPowerupType(){
		return powerupType;
	}
	
	public void deploy(){
		//send message or change gameworld variables
		isAvailable = false;
		switch (powerupType){
		case BLOCKGRID:
			break;
		case DISABLEALLPOWERUPS:
			break;
		case DISABLEONEMOLEDEPLOYER:
			break;
		case DUMMY:
			break;
		case EARTHQUAKE:
			break;
		case FOG:
			break;
		case INVULNERABILITY:
			break;
		case MOLEKING:
			break;
		default:
			break;
		
		}
	}

	
	public Rectangle getRectangle(){
		return boundingRectangle;
	}
	
	public boolean isTouchDown(int screenX, int screenY) {
		if (boundingRectangle.contains(screenX, screenY)) {
			return true;
			
			
		}
		return false;
	}
}
