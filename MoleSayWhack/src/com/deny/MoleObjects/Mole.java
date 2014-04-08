package com.deny.MoleObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;

public abstract class Mole 
{
	protected final long MOLE_APPEARANCE_TIME = (long) 2f;
	protected int HP;
	protected float timeExisted;
	protected int attack;
	protected boolean isDead;
	protected Player player;
	protected Circle boundingCircle;
	protected MoleType moleType;
	
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
	public TextureRegion getAsset(){
		switch(moleType){
		case ONETAP:
			return AssetLoader.mole1;
		case THREETAP:
			return AssetLoader.mole3;
		case FIVETAP:
			return AssetLoader.mole5;
		case SABOTAGE:
			return AssetLoader.moleSabo;
		default:
			return AssetLoader.mole1;
		} 
	}

	
	public Circle getBoundingCircle() {
		return boundingCircle;
	}
	
	public void minusHP() {
		if (isAlive()) {
			System.out.println("mole kena attack!");
			HP -= 1;
			if (HP==0) {
				System.out.println("Mole is dead!");
				isDead = true;
			}
		}
	}
	
	public boolean isTouchDown(int screenX, int screenY) {
		
		//minusHP
		if (boundingCircle.contains(screenX, screenY)) {
			if (isAlive()) 
			{
				AssetLoader.hit.play();
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
	
	public Color getColor() {
		return moleType.getColor();
	}
}
