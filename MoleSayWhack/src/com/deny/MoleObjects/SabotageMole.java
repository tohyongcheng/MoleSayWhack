package com.deny.MoleObjects;

import com.badlogic.gdx.math.Circle;
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
	
	public SabotageMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.SABOTAGE;
	}
	
	
	//isDead returns mole status - true is alive, false is dead//
	public boolean isDead() 
	{
		return this.isDead;
	}
	
	public boolean isAlive() {
		return !this.isDead;
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
	}
	
	//This will constantly update whether the mole shud appear or not//
	//override superclass method//
	public void update(float delta) 
	{
		if (timeExisted < MOLE_APPEARANCE_TIME && this.isAlive()) 
		{
			timeExisted += delta;
		} 
		else if (timeExisted <= MOLE_APPEARANCE_TIME && this.isAlive())
		{
			isDead = true;
		}
	}
	
	
	public boolean isTouchDown(int screenX, int screenY) 
	{
		if (super.isTouchDown(screenX, screenY)) {
			damage(player);
			return true;
		}
		return false;
		
	}
}
