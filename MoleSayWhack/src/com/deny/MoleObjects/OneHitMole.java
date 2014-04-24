package com.deny.MoleObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * OneHitMole is the basic mole that will be used most commonly throughout the game.
 * It has 1 hp and requires 1 tap to be killed. 
 * Damages player for 1hp.
 * 
 * @author Edward Loke
 *
 */
public class OneHitMole extends Mole 
{
	
	/**
	 * The constructor that instantiate this
	 * object. Takes in the player of which this
	 * mole belongs to
	 * @param player
	 */
	public OneHitMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.ONETAP;
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
	/**
	 * The method which updates the status of
	 * the mole on the board. Called 60 times
	 * every second
	 * @param delta : the period of which
	 * this method is called
	 */
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
