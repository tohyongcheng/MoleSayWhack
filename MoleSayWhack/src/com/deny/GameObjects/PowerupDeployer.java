package com.deny.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameWorld.GameWorld;
/**
 * Deploys powerups
 * Initialized at the beginning of the game
 *
 */
public class PowerUpDeployer {
	
	private PowerUpType powerupType;
	private final float coolDown;
	private float timeDeployed;
	private GameWorld gameWorld;
	private Rectangle boundingRectangle;
	private boolean isAvailable;
	private boolean disabled;

	/**
	 * The constructor for this class, takes in the gameWorld
	 * which instantiate this class, and the socketHandler,
	 * which is the thread that sends information to the
	 * other player
	 * 
	 * @param gameWorld
	 * @param powerupType
	 */
	public PowerUpDeployer(GameWorld gameWorld, PowerUpType powerupType){
		this.gameWorld = gameWorld;
		this.powerupType = powerupType;
		this.coolDown = powerupType.getCoolDown();
		boundingRectangle = new Rectangle();
		timeDeployed = 0;
		isAvailable = true;
		disabled = false;
	}
	
	/**
	 * Updates the cooldown time of the PowerUpDeployer
	 * @param delta : the time interval of which
	 * this method is checked
	 */
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
	
	/**
	 * Check whether this powerupdeployer is
	 * available and not disabled
	 * @return
	 */
	public boolean isAvailable() {
		return (isAvailable && !disabled);
	}
	/**
	 * Return the powerupType of this powerupDeployer
	 * @return powerupType
	 */
	public PowerUpType getPowerupType(){
		return powerupType;
	}
	/**
	 * Return the assetloader associated with this
	 * class's powerupType
	 * @return 
	 */
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
	/**
	 * Updates the cooldown time of the powerupdeployer
	 * @param delta : the time interval of which
	 * this method is checked
	 */
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

	/**
	 * Return the rectangle location of this 
	 * powerUpDeployer
	 * 
	 * @return boundingRectangle
	 */
	public Rectangle getRectangle(){
		return boundingRectangle;
	}
	/**
	 * Check whether the touch in the screen
	 * is within the boundary of the powerupDeployer
	 * box
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public boolean isTouchDown(int screenX, int screenY) {
		if (boundingRectangle.contains(screenX, screenY)) {
			return true;
		}
		return false;
	}
	/**
	 * Check whether this powerUp is disabled
	 * @return
	 */
	public boolean isDisabled() {
		return disabled;
	}
	/**
	 * Set whether this powerUp is enabled
	 * or disabled
	 * @param b
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}
}
