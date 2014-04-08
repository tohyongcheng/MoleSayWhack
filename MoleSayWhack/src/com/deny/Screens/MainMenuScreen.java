package com.deny.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.MoleObjects.Mole;

public class MainMenuScreen implements Screen {
	private static final int GAME_WIDTH = 272; //136 to 272
	private static final int GAME_HEIGHT = 408; //204 to 408
	Game game;
	
	OrthographicCamera mainMenuCam;
	SpriteBatch batcher;
	Rectangle startBounds;
	Rectangle optionsBounds;
	Rectangle scoreBounds;
	ShapeRenderer shapeRenderer;
	Vector3 touchPoint;
	private SpriteBatch batcherMain;
	
	public MainMenuScreen(Game game) {
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		
		//Create bounds. We are to edit the Position and the Width/Height Here!
		startBounds = new Rectangle(90, 200, 100, 40);
		optionsBounds = new Rectangle(90, 250, 100, 40);
		scoreBounds = new Rectangle(90, 300, 100, 40);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		
        batcherMain = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();	
	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        // Disable transparency 
        // This is good for performance when drawing images that do not require transparency.
        batcher.enableBlending();
        //put the position here
        batcher.draw(AssetLoader.bg, 0, 0, 272, 408);
        batcher.draw(AssetLoader.title, 50, 40, 170, 80);
        batcher.draw(AssetLoader.mole1, 44, 137, 48, 40);
        batcher.draw(AssetLoader.mole3, 92, 137, 48, 40);
        batcher.draw(AssetLoader.mole5, 140, 137, 48, 40);
        batcher.draw(AssetLoader.moleSabo, 188, 137, 48, 40);
        batcher.draw(AssetLoader.startButt, startBounds.x, 
        		startBounds.y, startBounds.width, startBounds.height);
        batcher.draw(AssetLoader.optButt, optionsBounds.x, optionsBounds.y
        		, optionsBounds.width, optionsBounds.height);
        batcher.draw(AssetLoader.scoreButt, scoreBounds.x, scoreBounds.y, 
        		scoreBounds.width, scoreBounds.height);
        
        batcher.end();
       /* shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.rect(playBounds.x, playBounds.y,
                playBounds.width, playBounds.height);
        shapeRenderer.rect(optionsBounds.x, optionsBounds.y,
        		optionsBounds.width, optionsBounds.height);
        shapeRenderer.end();*/
	}

	private void update() {
		if(Gdx.input.justTouched()) {
			mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (startBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new MultiplayerScreen(game));
				AssetLoader.button.play();
				this.dispose();
				return;
			}
			if (optionsBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new OptionsScreen(game));
				AssetLoader.button.play();
				this.dispose();
				return;
			}
			if(scoreBounds.contains(touchPoint.x, touchPoint.y)){
				game.setScreen(new HighScoreScreen(game));
				AssetLoader.button.play();
				this.dispose();
				return;
			}
		}
	
		AssetLoader.sequencer1.stop();
		AssetLoader.sequencer1.setTickPosition(0);
		AssetLoader.sequencer.start();
	}	
	
	@Override
	public void render(float delta) {
		update();
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
