package com.deny.GameObjects;

import com.badlogic.gdx.math.Circle;

/**
 * Mole3 is the 2nd mole type. 
 * It has 3 hp and requires 3 taps to be killed. 
 * Damages player for 2hp.
 * @author Edward Loke
 *
 */

public class Mole3 extends Mole 
{
	public Mole3(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 3;
	}
	
	
	//isDead returns mole status - true is alive, false is dead//
	public boolean isDead() 
	{
		return this.isDead;
	}
	
	public boolean isAlive() 
	{
		return !isDead;
	}
	
	public Circle getBoundingCircle() 
	{
		return this.boundingCircle;
	}
	
	//Invoking Mole (super) class methods//
	//damage will call the player.damage method which minuses 1 life//
	public void damage(Player player) 
	{
		super.damage(player);
		super.damage(player); //minus 2 hp - ought to cange the player class method//
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
	
	
	public void minusHP() 
	{
		if (!isDead) 
		{
			System.out.println("mole kena attack!");
			HP -= 1;
			if (HP==0) {
				System.out.println("Mole is dead!");
				isDead = true;
				
			}
		}
	}
	
	public boolean isTouchDown(int screenX, int screenY) 
	{
		return super.isTouchDown(screenX, screenY);
	}
}
