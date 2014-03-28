package com.deny.GameObjects;

import com.badlogic.gdx.graphics.Color;

public enum MoleType {
	//MOLETYPES
	ONETAP(1,0.5f), // 1 hit, -1 hp, 0.5 seconds CD
	THREETAP(3,2f), // 3 hit, -1 hp, 2 seconds CD
	FIVETAP(5,4f), // 5 hit, -1 hp, 4 seconds CD
	SABOTAGE(1,3f); // dont hit, -1 hp, 3 seconds CD
	
	int HP;
	float coolDown;
	
	MoleType(int HP, float coolDown) {
		this.HP = HP;
		this.coolDown = coolDown;
	}
	
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
	
	
	public Color getColor() {
		switch(this) {
    	case ONETAP:
    		return Color.GREEN;
    	case THREETAP:
    		return Color.BLUE;
    	case FIVETAP:
    		return Color.YELLOW;
    	case SABOTAGE:
    		return Color.RED;
    	default:
    		return null;
    	}
	}
	
}