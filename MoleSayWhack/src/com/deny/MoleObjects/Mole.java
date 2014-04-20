package com.deny.MoleObjects;


import com.badlogic.gdx.graphics.Color;


import com.badlogic.gdx.graphics.g2d.TextureRegion;




import com.badlogic.gdx.math.Rectangle;
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
	protected Rectangle boundingRectangle;
	protected MoleType moleType;

	
	
	public Mole(Player player) 
	{

		boundingRectangle = new Rectangle();
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
		if (!player.isInvulnerable()) {
			player.damage();
		}
	}
	
	public void update(float delta) 	{
	}
	
	public TextureRegion getAsset(){
		switch(moleType){
		case ONETAP:
			return AssetLoader.m1;
		case THREETAP:
			return AssetLoader.m3;
		case FIVETAP:
			return AssetLoader.m5;
		case SABOTAGE:
			return AssetLoader.sm;
		case MOLEKING:
			return AssetLoader.king;
		default:
			return AssetLoader.m1;

		} 
	}

	
	public Rectangle getBoundingCircle() {
		return boundingRectangle;
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
		if (boundingRectangle.contains(screenX, screenY)) {
			if (isAlive()) 
			{

				//PLAY IT WHEN IT IS HIT.
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
public MoleType getMoleType(){
	return moleType;
}
}
