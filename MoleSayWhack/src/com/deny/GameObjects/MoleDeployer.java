package com.deny.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;

//contains moles
//have cooldowns
//deploy moles by sending string, initialized at the beginning of the game
//graphic ?? 
/**
 * Mole Deployer is responsible for sending moles from player A to player B. Both
 * players will have mole deployer.
 * @author Edward Loke
 *
 */

public class MoleDeployer 
{
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
	
	public void deployMole(int pos)
	{
		//If it is available, deploy the mole and change availability to false//
		if (isAvailable()) {
			availability = false;
	    	socketHandler.deployMole(moleType, pos);
		}
	}
	
	public void update(float delta) 
	{
		//wait until time deployed exceeds cooldown to make it available again//
		if (timeDeployed < cooldown && !(isAvailable())) {
			timeDeployed += delta;
		} else if (timeDeployed >= cooldown && !(isAvailable())){
			timeDeployed = 0;
			availability = true;
		}
	}
	
	public TextureRegion getAsset(){
		switch(moleType){
		case ONETAP:
			return AssetLoader.mole1;
		case THREETAP:
			return AssetLoader.mole3;
		case FIVETAP:
			return AssetLoader.mole5;
		case SABOTAGE:
			return AssetLoader.moleSabo;
		default:
			return AssetLoader.mole1;
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
