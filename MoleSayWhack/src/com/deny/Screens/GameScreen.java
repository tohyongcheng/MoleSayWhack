package com.deny.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deny.GameHelpers.GameInputHandler;
import com.deny.GameObjects.MoleType;
import com.deny.GameWorld.GameRenderer;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;

public class GameScreen implements Screen {
	
	private Game game;
	private GameWorld world;
	private GameRenderer renderer;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	
	public GameScreen(Game game, ServerClientThread sH, ArrayList<MoleType> selectedMoles) {
		System.out.println("GameScreen attached.");
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameWidth = 136;
//		float gameHeight = screenHeight / (screenWidth / gameWidth);
		float gameHeight = 204;
		
		this.game = game;
		this.socketHandler = sH;
		this.selectedMoles = selectedMoles;
		this.world = new GameWorld(socketHandler,selectedMoles);
		this.renderer = new GameRenderer(world);
		
		
		Gdx.input.setInputProcessor(new GameInputHandler(world, screenWidth / gameWidth, screenHeight / gameHeight));
	}
	
	
	@Override
	public void render(float delta) {
		world.update(delta);
		renderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
