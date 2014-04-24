package com.deny.GameObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.deny.GameHelpers.AssetLoader;
/**
 * 
 * The list of the moletypes  that
 * are available in the game
 *
 */
public enum MoleType {
	//MOLETYPES
	ONETAP(1,0.5f), // 1 hit, -1 hp, 0.5 seconds CD
	THREETAP(3,2f), // 3 hit, -1 hp, 2 seconds CD
	FIVETAP(5,4f), // 5 hit, -1 hp, 4 seconds CD
	SABOTAGE(1,3f),  // dont hit, -1 hp, 3 seconds CD
	MOLEKING(20,10f),
	DUMMY(1,0f);
	
	int HP;
	float coolDown;
	/**
	 * The constructor of the moleType.
	 * Takes in the HP and the coolDown value
	 * of the moleType
	 * 
	 * @param HP
	 * @param coolDown
	 */
	MoleType(int HP, float coolDown) {
		this.HP = HP;
		this.coolDown = coolDown;
	}
	

	/**
	 * Returns the TextureRegion associated 
	 * with each moleType 
	 * 
	 * @return 
	 */
	public TextureRegion getAsset(){
		switch(this){
		case ONETAP:
			return AssetLoader.mdm1;
		case THREETAP:
			return AssetLoader.mdm3;
		case FIVETAP:
			return AssetLoader.mdm5;
		case SABOTAGE:
			return AssetLoader.mdsm;
		case MOLEKING:
			return AssetLoader.king;
		case DUMMY:
			return AssetLoader.dummyMOLE;
		default:
			return AssetLoader.mdm1;
		} 
	}
	
	/**
	 * Returns the next MoleType associated with 
	 * the current MoleType
	 * @return
	 */
	public MoleType next() {
		switch(this) {
		case ONETAP:
			return THREETAP;
		case THREETAP:
			return FIVETAP;
		case FIVETAP:
			return SABOTAGE;
		case SABOTAGE:
			return ONETAP;
		default:
			return ONETAP;
		}
	}
	
	
	
	/**
	 * Returns the cooldown value of this
	 * method
	 * @return coolDown
	 */
	public float getCoolDown() {
		return coolDown;
	}
	
}
