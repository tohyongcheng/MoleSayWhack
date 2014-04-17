package com.deny.GameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deny.GameHelpers.AssetLoader;

public enum PowerUpType {
	//POWERUPS
		
	EARTHQUAKE(10,0),
	MOLESHOWER(10,5),
	MOLEKING(10,2),
	DISABLEONEMOLEDEPLOYER(10,5),
	DISABLEALLPOWERUPS(10,1.5f),
	FOG(10,3),
	INVULNERABILITY(10,5),
	BLOCKGRID(10,5),
	DUMMY(10,5);
	
	float coolDown;
	float effectDuration;
	
	PowerUpType(float coolDown, float duration) {
		this.coolDown = coolDown;
		this.effectDuration = duration;
	}
	
	public PowerUpType next() {
		return values()[(ordinal()+1) % values().length];
	}
	
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
	
	
	public Color getColor() {
		switch(this) {
		case BLOCKGRID:
			return Color.BLACK;
		case DISABLEALLPOWERUPS:
			return Color.BLUE;
		case DISABLEONEMOLEDEPLOYER:
			return Color.GREEN;
		case DUMMY:
			return Color.YELLOW;
		case EARTHQUAKE:
			return Color.ORANGE;
		case FOG:
			return Color.WHITE;
		case INVULNERABILITY:
			return Color.CYAN;
		case MOLEKING:
			return Color.MAGENTA;
		case MOLESHOWER:
			return Color.RED;
    	}
		return null;
	}
	
	public float getCoolDown() {
		return coolDown;
	}
	
	public float getEffectDuration() {
		return effectDuration;
	}
	
	

}
