package com.deny.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;

//contains moles
//have cooldowns
//deploy moles by sending string, initialized at the beginning of the game
//graphic ?? 

public class MoleDeployer {
	private final long COOLDOWN = 2;
	private float timeDeployed;
	private Rectangle boundingRectangle;
	private ServerClientThread socketHandler;
	private GameWorld gameWorld;
	boolean availability;
	MoleType MoleType;
	
	/**
	 * Initialize cooldown & HP, as well as rectangle size and timeDeployed.
	 * @param HP
	 * @param COOLDOWN
	 */
	
	public MoleDeployer(GameWorld gw, MoleType moleType) {
		this.gameWorld = gw;
		this.socketHandler = gw.getSocketHandler();
		this.MoleType = moleType;
		boundingRectangle = new Rectangle();
		timeDeployed = 0;;
	}
	
	
//	public MoleDeployer(TokenType MoleType, long COOLDOWN){
//		this.MoleType = MoleType;
//		this.COOLDOWN = COOLDOWN;
//		boundingRectangle = new Rectangle();
//		timeDeployed = 0;
//	}
	
	public MoleType getMoleType(){
		return MoleType;
	}
	
	public void deployMole(int pos){
		availability = false;
    	socketHandler.deployMole(1, pos);
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
