package com.deny.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deny.GameHelpers.AssetLoader;
/**
 * 
 * The list of the poweruptypes  that
 * are available in the game
 *
 */
public enum PowerUpType {
	//POWERUPS
		
	EARTHQUAKE(10,1),
	MOLESHOWER(10,5),
	MOLEKING(20,10),
	DISABLEONEMOLEDEPLOYER(10,5),
	DISABLEALLPOWERUPS(10,2),
	FOG(10,3),
	INVULNERABILITY(20,5),
	BLOCKGRID(10,5),
	DUMMY(10,5);
	
	private float coolDown;
	private float effectDuration;
	/**
	 * The constructor for the powerupType
	 * It takes in the coolDown value
	 * and the duration of the powerUP effect
	 * @param coolDown
	 * @param duration
	 */
	private PowerUpType(float coolDown, float duration) {
		this.coolDown = coolDown;
		this.effectDuration = duration;
	}
	/**
	 * Returns the next powerupType associated with 
	 * the current powerupType
	 * @return
	 */
	public PowerUpType next() {
		return values()[(ordinal()+1) % values().length];
	}
	/**
	 * Returns the AssetLoader associated with
	 * this powerupType
	 * @return
	 */
	public TextureRegion getAsset(){
		switch(this){
		case BLOCKGRID:
			return AssetLoader.molejam;
		
		case DISABLEALLPOWERUPS:
			return AssetLoader.powerless;
		
		case DISABLEONEMOLEDEPLOYER:
			return AssetLoader.silencer;
		
		case DUMMY:
			return AssetLoader.dummymole;
			
		case EARTHQUAKE:
			return AssetLoader.earthquake;
			
		case FOG:
			return AssetLoader.mouldy;
			
		case INVULNERABILITY:
			return AssetLoader.moleshield;
		
		case MOLEKING:
			return AssetLoader.kingmole;
		
		case MOLESHOWER:
			return AssetLoader.moleshower;
		
		default:
			return AssetLoader.molejam;
		
		
		}
			
	}
	
	
	/**
	 * Returns the Cooldown of the
	 * powerupType
	 * @return coolDown
	 */
	
	public float getCoolDown() {
		return coolDown;
	}
	/**
	 * Return the effect duration of this
	 * current power up
	 * @return effectDuration
	 */
	public float getEffectDuration() {
		return effectDuration;
	}
	
	

}
