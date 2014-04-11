package com.deny.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.Threads.ReadThread.ScreenState;
import com.deny.Threads.ServerClientThread;

public class PreGamePowerUpScreen implements Screen {
	private static final int NO_OF_DEPLOYERS = 3;
	private static final int GAME_WIDTH = 272;
	private static final int GAME_HEIGHT = 408;

	public enum PreGameState {
		READY, COUNTING, GO, QUIT;
	}
	
	Game game;
	
	private OrthographicCamera mainMenuCam;
	private SpriteBatch batcher;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	private Vector3 touchPoint;
	private Rectangle backBounds;
	private Rectangle playBounds;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	private ArrayList<PowerUpType> selectedPowerUps;
	private ArrayList<Rectangle> selectedPowerUpsRectangles;
	private float countDownTime = 10f;
	private PreGameState currentState;

	public PreGamePowerUpScreen(Game game, ServerClientThread socketHandler, ArrayList<MoleType> selectedMoles) {
		this.game = game;
		this.socketHandler = socketHandler;
		this.selectedMoles = selectedMoles;
		
		this.mainMenuCam = new OrthographicCamera();
		socketHandler.setPreGamePowerUpScreen(this);
		socketHandler.getReadThread().setCurrentState(ScreenState.PREGAMEPOWERUP);
		currentState = PreGameState.READY;
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);
		font = new BitmapFont();
		font.setScale(1, -1);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
		backBounds = new Rectangle(0,386,32,32);

		
		selectedPowerUps = new ArrayList<PowerUpType>();
		selectedPowerUpsRectangles = new ArrayList<Rectangle>();

		for (int i=0; i <NO_OF_DEPLOYERS; i++) {
			selectedPowerUps.add(PowerUpType.EARTHQUAKE);
			selectedPowerUpsRectangles.add(new Rectangle( 45f+i*30*2, 40f*2, 30f*2,30f*2));
	
		}		
		
	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
     

        
        batcher.begin();
        batcher.enableBlending();
        batcher.draw(AssetLoader.bg, 0, 0, 272, 408);
        
//        for (int i = 0; i< NO_OF_DEPLOYERS; i++) {
//    	   PowerupType powerupType = selectedPowerUps.get(i);
//    	   Rectangle r = selectedPowerUpsRectangles.get(i);
//    	   batcher.draw(powerupType.getAsset(),r.x, r.y, r.width, r.height);
//    	}     
        
        font.draw(batcher, String.valueOf((int)countDownTime), 68*2, 20*2);
        batcher.draw(AssetLoader.cancel, backBounds.x, backBounds.y,
        		backBounds.width, backBounds.height);
        batcher.end();
        
        
        //Draw the PowerupDeployers
        shapeRenderer.begin(ShapeType.Filled);
        for (int i = 0; i< NO_OF_DEPLOYERS; i++) {
        	PowerUpType powerUpType = selectedPowerUps.get(i);
        	Rectangle r = selectedPowerUpsRectangles.get(i);
    		shapeRenderer.setColor(powerUpType.getColor());
            shapeRenderer.rect(r.x,r.y,r.width,r.height);
    	}
        shapeRenderer.end();
		
	}


	private void update(float delta) {
		
		switch(currentState) {
		
		case READY:
			currentState = PreGameState.COUNTING;
			break;
			
		case COUNTING:
			if (countDownTime <= 0 ) {
				currentState = PreGameState.GO;
			} else {
				countDownTime -= delta;
			}
			
			if(Gdx.input.justTouched()) {
				mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					currentState = PreGameState.QUIT;
				}
				
				for (int i =0; i<NO_OF_DEPLOYERS; i++) {
					if (selectedPowerUpsRectangles.get(i).contains(touchPoint.x, touchPoint.y)) {
						PowerUpType nextPowerUpType = selectedPowerUps.get(i).next();
						selectedPowerUps.remove(i);
						selectedPowerUps.add(i,nextPowerUpType);
						AssetLoader.clicksound.play();
					}
				}
			}
			break;
			
		case QUIT:
			game.setScreen(new MainMenuScreen(game));
			socketHandler.dispose();
			socketHandler = null;
			dispose();
			break;
			
		case GO:
			game.setScreen(new GameScreen(game, socketHandler, selectedMoles, selectedPowerUps));
			dispose();
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
	
	public void setState(PreGameState preGameState) {
		this.currentState = preGameState; 
	}
}
