package com.deny.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.deny.Threads.ServerClientThread;

public class PreGameScreen implements Screen {
	private static final int NO_OF_DEPLOYERS = 3;
	private static final int GAME_WIDTH = 136;
	private static final int GAME_HEIGHT = 204;

	private enum preGameState {
		READY, COUNTING, GO;
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
	private ArrayList<Rectangle> selectedMolesRectangles;
	private float countDownTime = 20f;
	private preGameState currentState;

	public PreGameScreen(Game game, ServerClientThread socketHandler) {
		this.game = game;
		this.socketHandler = socketHandler;
		this.mainMenuCam = new OrthographicCamera();
		currentState = preGameState.READY;
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);
		font = new BitmapFont();
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
//		playBounds = new Rectangle(23,80,90,30);
		backBounds = new Rectangle(0,188,16,16);
		
		selectedMoles = new ArrayList<MoleType>();
		selectedMolesRectangles = new ArrayList<Rectangle>();
		for (int i=0; i <NO_OF_DEPLOYERS; i++) {
			selectedMoles.add(MoleType.ONETAP);
			selectedMolesRectangles.add(new Rectangle( 23f+i*30, 40f, 30f,30f));
		}		
		
	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        shapeRenderer.begin(ShapeType.Filled);
        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.rect(backBounds.x, backBounds.y,
        		backBounds.width, backBounds.height);
//        shapeRenderer.rect(playBounds.x, playBounds.y,
//        		playBounds.width, playBounds.height);
        

        for (int i = 0; i< NO_OF_DEPLOYERS; i++) {
        	MoleType moleType = selectedMoles.get(i);
        	Rectangle r = selectedMolesRectangles.get(i);
    		shapeRenderer.setColor(moleType.getColor());
            shapeRenderer.rect(r.x,r.y,r.width,r.height);
    	}
        
        
        shapeRenderer.end();
        
        batcher.begin();
//        for (int i = 0; i< NO_OF_DEPLOYERS; i++) {
//        	MoleType moleType = selectedMoles.get(i);
//            font.draw(batcher, moleType.toString(), selectedMolesRectangles.get(i).x, selectedMolesRectangles.get(i).y);
//    	}
//        
        
        font.draw(batcher, String.valueOf((int)countDownTime), 68, 20);
        batcher.end();
		
	}


	private void update(float delta) {
		
		switch(currentState) {
		
		case READY:
			currentState = preGameState.COUNTING;
			break;
			
		case COUNTING:
			if (countDownTime <= 0 ) {
				currentState = preGameState.GO;
			} else {
				countDownTime -= delta;
			}
			
			if(Gdx.input.justTouched()) {
				mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					if (socketHandler !=null) socketHandler.interrupt();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
				
				for (int i =0; i<NO_OF_DEPLOYERS; i++) {
					if (selectedMolesRectangles.get(i).contains(touchPoint.x, touchPoint.y)) {
						MoleType nextMoleType = selectedMoles.get(i).next();
						selectedMoles.remove(i);
						selectedMoles.add(i,nextMoleType);
						AssetLoader.clicksound.play();
					}
				}
			}
			break;
			
		case GO:
			game.setScreen(new GameScreen(game, socketHandler, selectedMoles));
			this.dispose();
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
