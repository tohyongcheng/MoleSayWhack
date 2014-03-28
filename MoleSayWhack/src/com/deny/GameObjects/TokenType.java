package com.deny.GameObjects;

public enum TokenType {
	//MOLETYPES
		ONEtap, // 1 hit, -1 hp, 0.5 seconds CD
		THREEtap, // 3 hit, -1 hp, 2 seconds CD
		FIVEtap, // 5 hit, -1 hp, 4 seconds CD
		SABOTAGE, // dont hit, -1 hp, 3 seconds CD

	//POWERUPS
		
		EARTHQUAKE,
		DIVINESHIELD,
		MOLESHOWER,
		KINGMOLE,
		THEOWL,
		SUPERMOLE,
		CLONEMOLE,
		MOULDY,
		
	//SCORE
		SCORE,
}
