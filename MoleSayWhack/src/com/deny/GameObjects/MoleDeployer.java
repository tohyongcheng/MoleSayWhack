package com.deny.GameObjects;

import com.badlogic.gdx.math.Rectangle;

//contains moles
//have cooldowns
//deploy moles by sending string, initialized at the beginning of the game
//graphic ?? 

public class MoleDeployer {
	private final long COOLDOWN;
	private float timeDeployed;
	private Rectangle boundingRectangle;
	boolean availability;
	TokenType MoleType;
	
	/**
	 * Initialize cooldown & HP, as well as rectangle size and timeDeployed.
	 * @param HP
	 * @param COOLDOWN
	 */
	public MoleDeployer(TokenType MoleType, long COOLDOWN){
		this.MoleType = MoleType;
		this.COOLDOWN = COOLDOWN;
		boundingRectangle = new Rectangle();
		
		//where to specify the rectangle size?
		boundingRectangle.setHeight(5);
		boundingRectangle.setWidth(5);
		
		timeDeployed = 0;
	}
	
	public TokenType getMoleType(){
		return MoleType;
	}
	
	public String deploy(){
		availability = false;
		switch(MoleType){
		case ONEtap: 
			return "ONEtap";
		case THREEtap:
			return "THREEtap";
		case FIVEtap:
			return "FIVEtap";
		case SABOTAGE:
			return "SABOTAGE";
		default:
			return "ERROR";
		}
		
	}
	public void update(float delta) {
		if (timeDeployed < COOLDOWN && !(valid())) {
			timeDeployed += delta;
		} else if (timeDeployed >= COOLDOWN && !(valid())){
			timeDeployed = 0;
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
