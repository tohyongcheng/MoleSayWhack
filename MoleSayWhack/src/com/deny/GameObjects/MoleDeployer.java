package com.deny.GameObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;

//contains moles
//have cooldowns
//deploy moles by sending string, initialized at the beginning of the game
//graphic ?? 

public class MoleDeployer {
	private float timeDeployed;
	private Rectangle boundingRectangle;
	private ServerClientThread socketHandler;
	private float cooldown;
	private MoleType moleType;
	boolean availability;
	
	/**
	 * Initialize cooldown & HP, as well as rectangle size and timeDeployed.
	 * @param HP
	 * @param COOLDOWN
	 */
	
	public MoleDeployer(GameWorld gw, MoleType moleType) {
		this.socketHandler = gw.getSocketHandler();
		this.moleType = moleType;
		availability = true;
		boundingRectangle = new Rectangle();
		timeDeployed = 0;
		cooldown = moleType.getCoolDown();
	}
	
	
	public MoleType getMoleType(){
		return moleType;
	}
	
	public void deployMole(int pos){
		if (isAvailable()) {
			availability = false;
	    	socketHandler.deployMole(moleType, pos);
		}
	}
	
	public void update(float delta) {
		if (timeDeployed < cooldown && !(isAvailable())) {
			timeDeployed += delta;
		} else if (timeDeployed >= cooldown && !(isAvailable())){
			timeDeployed = 0;
			availability = true;
		}
	}
	
	public boolean isAvailable(){
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
