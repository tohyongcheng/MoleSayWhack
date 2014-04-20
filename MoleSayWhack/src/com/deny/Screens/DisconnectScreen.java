package com.deny.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;

public class DisconnectScreen implements Screen {

	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	public double scaleW = (double)GAME_WIDTH/544;
	public double scaleH = (double) GAME_HEIGHT/816;
	
	public enum DisconnectScreenStates {
		START, QUIT;
	}
	
	private Game game;
	private OrthographicCamera mainMenuCam;
	private SpriteBatch batcher;

	private ShapeRenderer shapeRenderer;
	private Vector3 touchPoint;
	private DisconnectScreenStates currentState;

	public DisconnectScreen(Game game) {
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		currentState = DisconnectScreenStates.START;
		mainMenuCam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);

		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
	}
	
	private void draw() {
		//Gdx.gl.glClearColor(0, 0, 0, 1);
       // Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        batcher.enableBlending();
        batcher.draw(AssetLoader.dc, 0, 0, GAME_WIDTH, GAME_HEIGHT);

 
        
        batcher.end();
	}


	private void update(float delta) {
		switch(currentState) {
		case QUIT:
			game.setScreen(new MainMenuScreen(game));
			break;
		case START:
			if(Gdx.input.justTouched()) {
				AssetLoader.back.play();
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
