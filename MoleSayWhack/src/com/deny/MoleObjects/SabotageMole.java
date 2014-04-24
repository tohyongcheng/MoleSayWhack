package com.deny.MoleObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * MoleBomb is the 3rd mole type. 
 * It has 1 hp AND MUST NOT BE KILLED during its MOLE_APPERANCE_TIME.
 * Damages player for 1 hp.
 * @author Edward Loke
 *
 */

public class SabotageMole extends Mole 
{
	/**
	 * The constructor that instantiate this
	 * object. Takes in the player of which this
	 * mole belongs to
	 * @param player
	 */
	public SabotageMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.SABOTAGE;
	}
	
	/**
	 * Check whether the mole is dead
	 * @return isDead
	 */
	//isDead returns mole status - true is alive, false is dead//
	public boolean isDead() 
	{
		return this.isDead;
	}
	/**
	 * Checks whether the mole is alive
	 * @return isAlive
	 */
	public boolean isAlive() {
		return !this.isDead;
	}
	/**
	 * Returns the area where the mole resides
	 * on the screen 
	 * @return boundingRectangle
	 */
	public Rectangle getBoundingCircle() 
	{
		return this.boundingRectangle;
	}
	
	//Invoking Mole (super) class methods//
	//damage will call the player.damage method which minuses 1 life//
	/**
	 * The method that damage the player
	 * if the mole is not killed on time 
	 * and only if the player is not
	 * invulnerable
	 */
	public void damage() 
	{
		super.damage();
	}
	
	//This will constantly update whether the mole shud appear or not//
	//override superclass method//
	/**
	 * The method which updates the status of
	 * the mole on the board. Called 60 times
	 * every second
	 * @param delta : the period of which
	 * this method is called
	 */
	public void update(float delta) 
	{
		if (timeExisted < MOLE_APPEARANCE_TIME && this.isAlive()) 
		{
			timeExisted += delta;
		} 
		else if (timeExisted >= MOLE_APPEARANCE_TIME && this.isAlive())
		{
			isDead = true;
		}
	}
	
	/**
	 * Check whether the mole has been touched
	 * by the player
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public boolean isTouchDown(int screenX, int screenY) 
	{
		if (super.isTouchDown(screenX, screenY)) {
			damage();
			if (enableSFX) AssetLoader.explode.play();
			return true;			
		}
		return false;
		
	}
}
