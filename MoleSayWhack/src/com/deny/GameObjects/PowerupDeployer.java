package com.deny.GameObjects;

import com.badlogic.gdx.math.Rectangle;
//Deploys powerups
//initialized at the begiinning of the game
public class PowerupDeployer {
	private final long COOLDOWN;
	private float timeCast;
	TokenType powerupType;
	private Rectangle boundingRectangle;
	boolean availability;
	
	public PowerupDeployer(long COOLDOWN, TokenType powerupType){
		this.powerupType = powerupType;
		this.COOLDOWN = COOLDOWN;
		boundingRectangle = new Rectangle();
		boundingRectangle.setHeight(5);
		boundingRectangle.setWidth(5);
		timeCast = 0;
	}
	
	public TokenType getPowerupType(){
		return powerupType;
	}
	
	public String cast(){
		
		availability = false;
		switch (powerupType){
		case 	EARTHQUAKE:
			return "EARTHQUAKE";
		case DIVINESHIELD:
			return "DIVINESHIELD";
		case MOLESHOWER:
			return "MOLESHOWER";
		case KINGMOLE:
			return "KINGMOLE";
		case THEOWL:
			return "THEOWL";
		case SUPERMOLE:
			return "SUPERMOLE";
		case CLONEMOLE:
			return "CLONEMOLE";
		case MOULDY:
			return "MOULDY";
		default:
			return "ERROR";
		}
	}
	
	public void update(float delta) {
		if (timeCast < COOLDOWN && !(valid())) {
			timeCast += delta;
		} else if (timeCast >= COOLDOWN && !(valid())){
			timeCast = 0;
			availability = true;
		}
	}
	public boolean valid(){
		return availability;
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
