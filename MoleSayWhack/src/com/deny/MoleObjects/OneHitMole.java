package com.deny.MoleObjects;

import com.badlogic.gdx.math.Circle;
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
	
	
	public OneHitMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.ONETAP;
	}
	
	
	//isDead returns mole status - true is alive, false is dead//
	public boolean isDead() 
	{
		return this.isDead;
	}
	
	//Method isAlive for easier reference//
	public boolean isAlive() 
	{
		return !isDead;
	}
	
	//Temporary code - for now the mole is a circle//
	public Rectangle getBoundingCircle() 
	{
		return this.boundingRectangle;
	}
	
	//Invoking Mole (super) class methods//
	//damage will call the player.damage method which minuses 1 life//
	public void damage(Player player) 
	{
		super.damage(player);
	}
	
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
			damage(player);
			isDead = true;
		}
	}

	
	public boolean isTouchDown(int screenX, int screenY) 
	{
		return super.isTouchDown(screenX, screenY);
	}
}
