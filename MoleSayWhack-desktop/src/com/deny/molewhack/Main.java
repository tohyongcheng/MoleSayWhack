package com.deny.molewhack;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSayWhack";
		cfg.width = 136;
	//	cfg.height = 1620 / 3;
		cfg.height = 204;
		
		new LwjglApplication(new WMGame(), cfg);
	}
}
