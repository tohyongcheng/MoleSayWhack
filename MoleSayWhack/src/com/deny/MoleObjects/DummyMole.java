package com.deny.MoleObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * Dummy Moles are meant to fool the opponent. They pose no threat to the opponent at all.
 * It has 1 hp and will appear for a shorter time of 1s.
 *
 */

public class DummyMole extends Mole 
{
	private final long MOLE_APPEARANCE_TIME = 1;

	/**
	 * The constructor that instantiate this
	 * object. Takes in the player of which this
	 * mole belongs to
	 * @param player
	 */
	public DummyMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.DUMMY;
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
	/**
	 * The method which updates the status of
	 * the mole on the board. Called 60 times
	 * every second
	 * @param delta : the period of which
	 * this method is called
	 */
	//Dummy Moles do not damage player at all
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
		return super.isTouchDown(screenX, screenY);
	}
}
