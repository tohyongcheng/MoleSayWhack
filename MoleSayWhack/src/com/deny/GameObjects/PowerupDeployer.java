package com.deny.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
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
	private boolean isAvailable;
	private boolean disabled;

	
	private ServerClientThread socketHandler;
	
	public PowerUpDeployer(GameWorld gameWorld, PowerUpType powerupType){
		this.gameWorld = gameWorld;
		this.socketHandler = gameWorld.getSocketHandler();
		this.powerupType = powerupType;
		this.coolDown = powerupType.getCoolDown();
		boundingRectangle = new Rectangle();
		timeDeployed = 0;
		isAvailable = true;
		disabled = false;
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
		return (isAvailable && !disabled);
	}
	
	public PowerUpType getPowerupType(){
		return powerupType;
	}
	public TextureRegion getAsset(){
		switch (powerupType){
		case BLOCKGRID:
			return AssetLoader.ns;
		case DISABLEALLPOWERUPS:
			return AssetLoader.np;
		case DISABLEONEMOLEDEPLOYER:
			return AssetLoader.nc;
		case DUMMY:
			return AssetLoader.sc;
		case EARTHQUAKE:
			return AssetLoader.eq;
		case FOG:
			return AssetLoader.fog;
		case INVULNERABILITY:
			return AssetLoader.inv;
		case MOLEKING:
			return AssetLoader.km;
		case MOLESHOWER:
			return AssetLoader.ms;
		default:
			return AssetLoader.eq;
		}
	}
	public void deploy(){
		//send message or change gameworld variables
		isAvailable = false;
		switch (powerupType){
		case BLOCKGRID:
			gameWorld.invokePowerUp(powerupType);
			break;
		case DISABLEALLPOWERUPS:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
			break;
		case DISABLEONEMOLEDEPLOYER:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
			break;
		case DUMMY:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
			break;
		case EARTHQUAKE:
			gameWorld.invokePowerUp(powerupType);
			break;
		case FOG:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
			break;
		case INVULNERABILITY:
			gameWorld.invokePowerUp(powerupType);
			break;
		case MOLEKING:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
			break;
		case MOLESHOWER:
			gameWorld.getSocketHandler().sendPowerUp(powerupType);
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
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean b) {
		disabled = b;
	}
}
