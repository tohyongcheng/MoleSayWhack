package com.deny.MoleObjects;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

/**
 * Dummy Moles are meant to fool the opponent. They pose no threat to the opponent at all.
 * It has 1 hp and will appear for a shorter time of 1s.
 * @author Edward Loke
 *
 */

public class DummyMole extends Mole 
{
	private final long MOLE_APPEARANCE_TIME = 1;
	
	public DummyMole(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
		this.moleType = MoleType.DUMMY;
	}
	
	
	//isDead returns mole status - true is alive, false is dead//
	public boolean isDead() 
	{
		return this.isDead;
	}
	
	public boolean isAlive() {
		return !this.isDead;
	}
	
	public Rectangle getBoundingCircle() 
	{
		return this.boundingRectangle;
	}
	
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
	
	
	public boolean isTouchDown(int screenX, int screenY) 
	{
		return super.isTouchDown(screenX, screenY);
	}
}
