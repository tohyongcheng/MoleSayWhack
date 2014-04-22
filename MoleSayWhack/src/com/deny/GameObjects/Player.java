package com.deny.GameObjects;

import com.deny.GameWorld.GameWorld;

public class Player {

	private GameWorld gameWorld;
	private int HP;
	private boolean isDead;
	private boolean isInvulnerable;
	
	public Player(int HP, GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.HP = HP;
		this.isDead = false;
	}
	
	public int getHP(){
		return HP;
	}
	public void damage() {
		if (isAlive()) {
			if (!isInvulnerable()) {
				HP -= 1;
				sendClientMessage();
				System.out.println("player hp -1! Current player HP is "+ HP);
			}
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

	public boolean isInvulnerable() {
		return isInvulnerable;
	}
	
	public void setInvulnerability(boolean b) {
		isInvulnerable = b;
		System.out.println("Player's invulnerability is now " + b);
	}
	
	public void sendClientMessage() {
		try {
			gameWorld.getSocketHandler().sendHPMessage(HP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
