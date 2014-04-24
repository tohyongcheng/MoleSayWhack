package com.deny.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.deny.GameHelpers.AssetLoader;
import com.deny.molewhack.WMGame;

/**
 * A screen that is called when
 * one player disconnects from the game
 *
 */
public class DisconnectScreen implements Screen {

	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	public double scaleW = (double)GAME_WIDTH/544;
	public double scaleH = (double) GAME_HEIGHT/816;
	/**
	 * The states that are present in this screen
	 *
	 */
	public enum DisconnectScreenStates {
		START, QUIT;
	}
	
	private WMGame game;
	private OrthographicCamera mainMenuCam;
	private SpriteBatch batcher;
	private Preferences prefs;
	private boolean enableSFX;
	private ShapeRenderer shapeRenderer;
	private DisconnectScreenStates currentState;

	/**
	 * The constructor of this screen
	 * Takes in the game that instantiate this
	 * class
	 */
	public DisconnectScreen(WMGame game) {
		System.out.println("I'm called!");
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		currentState = DisconnectScreenStates.START;
		mainMenuCam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);

		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		
		//Get options
		prefs = Gdx.app.getPreferences("Options");
		enableSFX = prefs.getBoolean("enableSFX", true);
	}
	/**
	 * The method that draw all the pictures or images
	 * that are to be shown in this screen,
	 * depending on the state of this screen
	 */
	private void draw() {
		//Gdx.gl.glClearColor(0, 0, 0, 1);
       // Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        batcher.enableBlending();
        batcher.draw(AssetLoader.dc, 0, 0, GAME_WIDTH, GAME_HEIGHT);

 
        
        batcher.end();
	}

	/**
	 * The method that update this screen, depending
	 * on the input or the states of this screen
	 * @param delta
	 */
	private void update(float delta) {
		switch(currentState) {
		case QUIT:
			game.setScreen(new MainMenuScreen(game));
			break;
		case START:
			if(Gdx.input.justTouched()) {
				if (enableSFX) AssetLoader.back.play();
				currentState = DisconnectScreenStates.QUIT;
			}
			break;
		default:
			break;
		}
	}

	
	
	@Override
	public void render(float delta) {
		update(delta);
		draw();
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
