package com.deny.GameObjects;

import com.deny.GameWorld.GameWorld;
/**
 * 
 * The player class, which represents the
 * player of the game
 *
 */
public class Player {

	private GameWorld gameWorld;
	private int HP;
	private boolean isDead;
	private boolean isInvulnerable;
	/**
	 * The constructor of the class,
	 * takes in the HP value and the
	 * gameWorld that instantiate this
	 * class
	 * 
	 * @param HP
	 * @param gameWorld
	 */
	public Player(int HP, GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.HP = HP;
		this.isDead = false;
	}
	/**
	 * Returns the current HP of the player 
	 * @return HP
	 */
	public int getHP(){
		return HP;
	}
	/**
	 * A method which damage the player due to
	 * not killing a mole, only if the player is not
	 * invulnerable
	 * If HP == 0, the the player is dead
	 * Game should be over if HP == 0
	 */
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
	/**
	 * Check whether the player is alive
	 * @return !isDead
	 */
	public boolean isAlive() {
		return !isDead;
	}
	/**
	 * Check whether the player is dead
	 * @return isDead
	 */
	public boolean isDead() {
		return isDead;
	}
	
	/**
	 * Check whether player is currently invulnerable
	 * @return isInvulnerable
	 */
	public boolean isInvulnerable() {
		return isInvulnerable;
	}
	/**
	 * Set the invulnerability status of the Player
	 * @param b
	 */
	public void setInvulnerability(boolean b) {
		isInvulnerable = b;
		System.out.println("Player's invulnerability is now " + b);
	}
	/**
	 * Lets the other player know the current HP
	 * of this player
	 */
	public void sendClientMessage() {
		try {
			gameWorld.getSocketHandler().sendHPMessage(HP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
