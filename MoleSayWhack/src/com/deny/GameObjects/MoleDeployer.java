package com.deny.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;

/**
 * Mole Deployer is responsible for sending moles 
 * from player A to player B. Both
 * players will have mole deployer.
 * 
*/

public class MoleDeployer 
{
	private float timeDeployed;
	private Rectangle boundingRectangle;
	private ServerClientThread socketHandler;
	private float cooldown;
	private MoleType moleType;

	private boolean selected;
	private boolean disabled;
	private boolean isAvailable;
	
	/**
	 * The constructor for MoleDeployer
	 * @param gw : the gameWorld class that instantiate this class
	 * @param moleType : the input to indicate what moleType will be spawned 
	 * from the MoleDeployer
	 */
	public MoleDeployer(GameWorld gw, MoleType moleType) {
		this.socketHandler = gw.getSocketHandler();
		this.moleType = moleType;
		isAvailable = true;
		boundingRectangle = new Rectangle();
		timeDeployed = 0;
		cooldown = moleType.getCoolDown();
		disabled = false;
	}
	
	/**
	 * Returns the  moletype this moledeployer has
	 * @return
	 */
	public MoleType getMoleType(){
		return moleType;
	}
	
	/**
	 * Deploy the mole to the opponent
	 * if the moledeployer is available
	 * @param pos : the grid where the mole is deployed to
	 */
	public void deployMole(int pos)
	{
		//If it is available, deploy the mole and change availability to false//
		if (isAvailable()) {
			isAvailable = false;
	    	socketHandler.deployMole(moleType, pos);
		}
	}
	/**
	 * Updates the cooldown time of the moledeployer
	 * @param delta : the time interval of which
	 * this method is checked
	 */
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
	
	/**
	 * Return the assetloader associated with this
	 * class's moletype
	 * @return TextureRegion
	 */
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
	
	/**
	 * Check whether this moleDeployer is available
	 * @return isAvailable and not disabled
	 */
	public boolean isAvailable(){
		return (isAvailable && !disabled);
	}
	/**
	 * Returns the location in the screen
	 * of where this moleDeployer is located
	 * @return boundingRectangle
	 */
	public Rectangle getRectangle(){
		return boundingRectangle;
	}
	/**
	 * Check whether the touch in the screen
	 * is within the boundary of the moleDeployer
	 * box
	 * @param screenX
	 * @param screenY
	 * @return true/false
	 */
	public boolean isTouchDown(int screenX, int screenY) {
		if (boundingRectangle.contains(screenX, screenY)) {
			return true;
		}
		return false;
	}

	/**
	 * Disables the moleDeployer
	 * @param b
	 */
	public void setDisabled(boolean b) {
		disabled = b;
	}
	/**
	 * Set that  this mole is selected by  the
	 * player
	 * @param b
	 */
	public void setSelected(boolean b) {
		selected = b;
	}
	
	/**
	 * Check whether this moleDeployer is
	 * currently selected
	 * @return selected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * Change the selected status of the moleDeployer
	 * 
	 */
	public void triggerSelected() {
		if (selected==false)
			selected = true;
		else selected = false;
	}

	/**
	 * Check whether the moleDeployer is disabled
	 * @return disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	
}
