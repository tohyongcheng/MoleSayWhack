package com.deny.MoleObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * FiveHitMole is one of the moles available for spawn.
 * It has 5 hp and requires 5 taps to be killed. 
 * Damages player for 1hp.
 */
public class FiveHitMole extends Mole 
{
	
	/**
	 * The constructor that instantiate this
	 * object. Takes in the player of which this
	 * mole belongs to
	 * @param player
	 */
	public FiveHitMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 5;
		this.moleType = MoleType.FIVETAP;
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
	//Method isAlive for easier reference//
	public boolean isAlive() 
	{
		return !isDead;
	}
	/**
	 * Returns the area where the mole resides
	 * on the screen 
	 * @return boundingRectangle
	 */
	//Temporary code - for now the mole is a circle//
	public Rectangle getBoundingCircle() 
	{
		return this.boundingRectangle;
	}
	/**
	 * The method that damage the player
	 * if the mole is not killed on time 
	 * and only if the player is not
	 * invulnerable
	 */
	//Invoking Mole (super) class methods//
	//damage will call the player.damage method which minuses 1 life//
	public void damage() 
	{
		super.damage();
	}
	/**
	 * The method which updates the status of
	 * the mole on the board. Called 60 times
	 * every second
	 * @param delta : the period of which
	 * this method is called
	 */
	//This will constantly update whether the mole shud appear or not//
	public void update(float delta) 
	{
		// if timeexisted still less than what it is supposed to appear for AND 
		// still alive//
		if (timeExisted < MOLE_APPEARANCE_TIME && isAlive()) 
		{
			timeExisted += delta;
		} 
		// if timeexisted more than what it is supposed to appear for AND 
		// havent been killed by player. damage the player//
		else if (timeExisted >= MOLE_APPEARANCE_TIME && isAlive())
		{
			damage();
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
