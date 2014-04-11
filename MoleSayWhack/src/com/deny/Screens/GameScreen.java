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
import com.deny.Threads.ReadThread.ScreenState;

public class GameScreen implements Screen {
	
	private Game game;
	private GameWorld world;
	private GameRenderer renderer;
	private ServerClientThread socketHandler;
	
	public GameScreen(Game game, ServerClientThread sH, ArrayList<MoleType> selectedMoles) {
		System.out.println("GameScreen attached.");
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();

		
		this.game = game;
		this.socketHandler = sH;
		this.socketHandler.getReadThread().setCurrentState(ScreenState.PREGAME);
		this.world = new GameWorld(game, this, socketHandler,selectedMoles);
		this.renderer = new GameRenderer(world);
		
		Gdx.input.setInputProcessor(new GameInputHandler(world, screenWidth, screenHeight));
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
	}
	

}
