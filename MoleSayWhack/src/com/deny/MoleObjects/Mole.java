package com.deny.MoleObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
/**
 * The super class Mole,
 * representing all the mole objects
 * in this game

 *
 */
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
	protected Preferences prefs;
	protected boolean enableSFX;
	
	/**
	 * The constructor that instantiate this
	 * object. Takes in the player of which this
	 * mole belongs to
	 * @param player
	 */
	public Mole(Player player) 
	{
		prefs = Gdx.app.getPreferences("Options");
		enableSFX = prefs.getBoolean("enableSFX", true);
		boundingRectangle = new Rectangle();
		isDead = false;
		timeExisted = 0;
		this.player= player; 
	}
	
	/**
	 * Check whether the mole is dead
	 * @return isDead
	 */
	public boolean isDead() {
		return isDead;
	}
	/**
	 * Checks whether the mole is alive
	 * @return isAlive
	 */
	public boolean isAlive() {
		return !isDead;
	}
	/**
	 * The method that damage the player
	 * if the mole is not killed on time 
	 * and only if the player is not
	 * invulnerable
	 */
	public void damage() {
		if (!player.isInvulnerable()) {
			player.damage();
			AssetLoader.damage.play();
		}
	}
	
	/**
	 * Returns the picture associated with this moletype
	 * @return
	 */
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
	/**
	 * The method that reduces the HP
	 * of the mole in the current board
	 */
	public void minusHP() {
		if (isAlive()) 
		{
			if (enableSFX) AssetLoader.hit.play();
			System.out.println("mole kena attack!");
			HP -= 1;
			if (HP==0) {
				System.out.println("Mole is dead!");
				isDead = true;
			}
		}
	}
	/**
	 * Returns the area where the mole resides
	 * on the screen 
	 * @return boundingRectangle
	 */
	public Rectangle getBoundingCircle() {
		return boundingRectangle;
	}
	/**
	 * Check whether the mole has been touched
	 * by the player
	 * @param screenX
	 * @param screenY
	 * @return
	 */
	public boolean isTouchDown(int screenX, int screenY) {
		
		//minusHP
		if (boundingRectangle.contains(screenX, screenY)) {
			minusHP();
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the moleType associated with
	 * this mole
	 * @return
	 */
	public MoleType getMoleType(){
		return moleType;
	}

	/**
	 * Returns the current HP of this mole
	 * @return
	 */
	public int getHP() {
		return HP;
	}

	/**
	 * Set the HP of this mole
	 * @param hP
	 */
	public void setHP(int hP) {
		HP = hP;
	}

	/**
	 * Returns how long has this mole existed
	 * on the board
	 * @return timeExisted
	 */
	public float getTimeExisted() {
		return timeExisted;
	}

	/**
	 * Set how long the mole has existed on the board
	 * @param timeExisted
	 */
	public void setTimeExisted(float timeExisted) {
		this.timeExisted = timeExisted;
	}
	/**
	 * The method which updates the status of
	 * the mole on the board. Called 60 times
	 * every second
	 * @param delta : the period of which
	 * this method is called
	 */
	public void update(float delta){
		
	}

	/**
	 * Get how long the mole can appear on
	 * the board at maximum
	 * @return MOLE_APPEARANCE_TIME
	 */
	public long getMOLE_APPEARANCE_TIME() {
		return MOLE_APPEARANCE_TIME;
	}

	/**
	 * Set the type of the current mole
	 * @param moleType
	 */
	public void setMoleType(MoleType moleType) {
		this.moleType = moleType;
	}
}
