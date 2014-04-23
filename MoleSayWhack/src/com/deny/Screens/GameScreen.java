package com.deny.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.deny.GameHelpers.GameInputHandler;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameRenderer;
import com.deny.GameWorld.GameWorld;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

/**
 * A screen that is called when
 * a game begins
 *
 */
public class GameScreen implements Screen {
	
	private WMGame game;
	private GameWorld world;
	private GameRenderer renderer;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	private ArrayList<PowerUpType> selectedPowerUps;
	
	/**
	 * The constructor of this screen
	 * Takes in the game that instantiate this
	 * class and the thread that sends messages
	 * to the other player
	 */
	
	public GameScreen(WMGame game, ServerClientThread sH, ArrayList<MoleType> selectedMoles, ArrayList<PowerUpType> selectedPowerUps) {
		System.out.println("GameScreen attached.");
		this.game = game;
		this.socketHandler = sH;
		this.selectedMoles = selectedMoles;
		this.selectedPowerUps = selectedPowerUps;
		this.world = new GameWorld(game, this);
		this.renderer = new GameRenderer(world);
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		Gdx.input.setInputProcessor(new GameInputHandler(world, screenWidth, screenHeight));
	}
	
	@Override
	/**
	 * The method that draw all the pictures or images
	 * that are to be shown in this screen,
	 * depending on the state of this screen
	 */
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


	public Game getGame() {
		return game;
	}


	public void setGame(WMGame game) {
		this.game = game;
	}

	public ServerClientThread getSocketHandler() {
		return socketHandler;
	}

	public ArrayList<MoleType> getSelectedMoles() {
		return selectedMoles;
	}

	public ArrayList<PowerUpType> getSelectedPowerUps() {
		return selectedPowerUps;
	}

	public GameWorld getGameWorld() {
		return world;
	}
	

}
