package com.deny.GameObjects;

public class Player {

	private int HP;
	private boolean isDead;
	
	public Player(int HP) {
		this.HP = HP;
		this.isDead = false;
	}
	
	public void damage() {
		if (isAlive()) {
			HP -= 1;
			System.out.println("player hp -1! Current player HP is "+ HP);
			
			if (HP==0) {
				System.out.println("Player is dead!");
				isDead = true;
			}
		}
	}
	
	public boolean isAlive() {
		return !isDead;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	
	public void update(float delta) {
		
	}

}
