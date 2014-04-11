package com.deny.MoleObjects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * To change!
 * FiveHitMole is one of the moles available for spawn.
 * It has 5 hp and requires 5 taps to be killed. 
 * Damages player for 1hp.
 * 
 * @author Edward Loke
 *
 */
public class FiveHitMole extends Mole 
{
	
	
	public FiveHitMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 5;
		this.moleType = MoleType.FIVETAP;
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
