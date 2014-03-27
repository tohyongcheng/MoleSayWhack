package com.deny.GameObjects;

import com.badlogic.gdx.math.Circle;

public abstract class Mole 
{
	static final long MOLE_APPEARANCE_TIME = (long) 2f;
	int HP;
//	private float cooldown;
	float timeExisted;
	int attack;
	boolean isDead;
	Player player;
	Circle boundingCircle;
	
	public Mole(Player player) 
	{
		boundingCircle = new Circle();
		isDead = false;
		timeExisted = 0;
		this.player= player; 
	}
	
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isAlive() {
		return !isDead;
	}
	
	public void damage(Player player) {
		player.damage();
	}
	
	public void update(float delta) 
	{
//		if (timeExisted < MOLE_APPEARANCE_TIME && isAlive()) {
//			timeExisted += delta;
//		} else if (timeExisted >= MOLE_APPEARANCE_TIME && isAlive()){
//			damage(player);
//			isDead = true;
//		}
	}

	
	public Circle getBoundingCircle() {
		return boundingCircle;
	}
	
	public void minusHP() {
//		if (isAlive()) {
//			System.out.println("mole kena attack!");
//			HP -= 1;
//			if (HP==0) {
//				System.out.println("Mole is dead!");
//				isDead = true;
//				
//			}
//		}
	}
	
	public boolean isTouchDown(int screenX, int screenY) {
		if (boundingCircle.contains(screenX, screenY)) {
			return true;
		}
		return false;
	}
}
