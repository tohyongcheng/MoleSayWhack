package com.deny.GameObjects;

import com.badlogic.gdx.math.Circle;

public class Mole {
	private static final long MOLE_APPEARANCE_TIME = (long) 2f;
	private int HP;
	private float cooldown;
	private float timeExisted;
	int attack;
	boolean isDead;
	Player player;
	Circle boundingCircle;
	
	public Mole(Player player) {
		boundingCircle = new Circle();
		isDead = false;
		HP = 1;
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
	
	public void update(float delta) {
		if (timeExisted < MOLE_APPEARANCE_TIME && isAlive()) {
			timeExisted += delta;
		} else if (timeExisted >= MOLE_APPEARANCE_TIME && isAlive()){
			damage(player);
			isDead = true;
		}
	}

	
	public Circle getBoundingCircle() {
		return boundingCircle;
	}
	
	
	public boolean isTouchDown(int screenX, int screenY) {
		
		//minusHP
		if (boundingCircle.contains(screenX, screenY)) {
			if (isAlive()) {
				System.out.println("mole kena attack!");
				HP -= 1;
				if (HP==0) {
					System.out.println("Mole is dead!");
					isDead = true;
				}
			}
			return true;
		}
		return false;
	}
}
