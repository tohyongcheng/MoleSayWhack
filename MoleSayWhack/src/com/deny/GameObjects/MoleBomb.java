package com.deny.GameObjects;

import com.badlogic.gdx.math.Circle;

/**
 * MoleBomb is the 3rd mole type. 
 * It has 1 hp AND MUST NOT BE KILLED during its MOLE_APPERANCE_TIME.
 * Damages player for 3 hp.
 * @author Edward Loke
 *
 */

public class MoleBomb extends Mole 
{
	private static final long MOLE_APPEARANCE_TIME = (long) 2f;
	int HP;
	private float cooldown;
	private float timeExisted;
	
	public MoleBomb(Player player) 
	{
		//Invoking the MoleClass constructor//
		super(player);
		this.HP = 1;
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
		//if got tapped, it will damage the player 3 times//
		else if (timeExisted <= MOLE_APPEARANCE_TIME && this.isDead)
		{
			damage(player);
			damage(player);
			damage(player);
			
		}
	}
	
	
	public void minusHP() 
	{
		if (this.isAlive()) 
		{
			System.out.println("mole kena attack!");
			HP -= 1;
			if (HP==0) {
				System.out.println("Mole is dead!");
				this.isDead = true;
				
			}
		}
	}
	
	public boolean isTouchDown(int screenX, int screenY) 
	{
		return super.isTouchDown(screenX, screenY);
	}
}
