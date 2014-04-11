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

	//ADDED THIS TO INDICATE WHETHER IT IS SELEECTED / COOLDOWN
	public boolean selected;
	boolean isAvailable;

	public MoleDeployer(GameWorld gw, MoleType moleType) {
		this.socketHandler = gw.getSocketHandler();
		this.moleType = moleType;
		isAvailable = true;
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
			isAvailable = false;
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
			isAvailable = true;
		}
	}
	
	public TextureRegion getAsset(){
		switch(moleType){
		case ONETAP:

			return AssetLoader.m1;
		case THREETAP:
			return AssetLoader.m3;
		case FIVETAP:
			return AssetLoader.m5;
		case SABOTAGE:
			return AssetLoader.sm;
		default:
			return AssetLoader.m1;

		} 
	}
	
	public boolean isAvailable(){
		return isAvailable;
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
