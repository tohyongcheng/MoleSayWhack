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
	private static final int GAME_WIDTH = 136;
	private static final int GAME_HEIGHT = 204;
	Game game;
	
	OrthographicCamera mainMenuCam;
	SpriteBatch batcher;
	Rectangle playBounds;
	Rectangle optionsBounds;
	ShapeRenderer shapeRenderer;
	Vector3 touchPoint;

	public MainMenuScreen(Game game) {
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		
		
		//Create bounds. We are to edit the Position and the Width/Height Here!
		playBounds = new Rectangle(20,40,96, 40);
		optionsBounds = new Rectangle(20, 100, 96, 40);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();

	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        shapeRenderer.begin(ShapeType.Filled);
        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from myWorld (Using ShapeType.Filled)
        shapeRenderer.rect(playBounds.x, playBounds.y,
                playBounds.width, playBounds.height);
        
        shapeRenderer.rect(optionsBounds.x, optionsBounds.y,
        		optionsBounds.width, optionsBounds.height);

        shapeRenderer.end();
		
	}


	private void update() {
		if(Gdx.input.justTouched()) {
			mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			
			if (playBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new MultiplayerScreen(game));
				this.dispose();
				AssetLoader.clicksound.play();
				return;
			}
			
			if (optionsBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new OptionsScreen(game));
				this.dispose();
				AssetLoader.clicksound.play();
				return;
			}
			
			
		}
		
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
