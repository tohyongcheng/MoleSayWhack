package com.deny.Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleType;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Threads.ServerClientThread;

public class PreGameScreen implements Screen {
	private static final int NO_OF_DEPLOYERS = 3;

	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	public double scaleW = (double)GAME_WIDTH/544;
	public double scaleH = (double) GAME_HEIGHT/816;


	public enum PreGameState {
		READY, COUNTING, GO, QUIT;
	}
	
	private Game game;
	private OrthographicCamera mainMenuCam;
	private SpriteBatch batcher;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	private Vector3 touchPoint;
	private Rectangle backBounds;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	private ArrayList<Rectangle> selectedMolesRectangles;
	private float countDownTime = 20f;
	private PreGameState currentState;
	
	
	private Object preGameStateLock = new Object();

	public PreGameScreen(Game game, ServerClientThread socketHandler) {
		this.game = game;
		this.socketHandler = socketHandler;
		this.mainMenuCam = new OrthographicCamera();
		socketHandler.setPreGameScreen(this);
		setState(PreGameState.READY);
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);
		font = new BitmapFont();
		font.setScale(1, -1);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
		backBounds = new Rectangle(3,(int)(GAME_HEIGHT-9-82*scaleH),(int)(83*scaleW), (int)(82*scaleH));

		
		selectedMoles = new ArrayList<MoleType>();
		selectedMolesRectangles = new ArrayList<Rectangle>();

		for (int i=0; i <NO_OF_DEPLOYERS; i++) {
			selectedMoles.add(MoleType.ONETAP);

			selectedMolesRectangles.add(new Rectangle( (int)(GAME_WIDTH/2 - 474*scaleW/2),(int)(GAME_HEIGHT/4.5 +(i*(178*scaleH+7)-5)) , (int)(474*scaleW),(int)(178*scaleH )));

		}		
		
	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
     
      
        batcher.begin();
        batcher.enableBlending();

        batcher.draw(AssetLoader.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        batcher.draw(AssetLoader.titleDep, (int)(GAME_WIDTH/2 - (324*scaleW)/2), (int)(GAME_HEIGHT/15), (int)(324*scaleW) , (int)(133*scaleH));
        for (int i = 0; i< NO_OF_DEPLOYERS; i++) {
    	   MoleType moleType = selectedMoles.get(i);
    	   Rectangle r = selectedMolesRectangles.get(i);
    	   batcher.draw(moleType.getAsset(),r.x, r.y, r.width, r.height);
    	   //font.draw(batcher, moleType.toString(), selectedMolesRectangles.get(i).x, selectedMolesRectangles.get(i).y);
    	}     
        
        
        
        AssetLoader.font.draw(batcher, String.valueOf((int)countDownTime), GAME_WIDTH/2 - 8, 15);
        batcher.draw(AssetLoader.cnl, backBounds.x, backBounds.y,

        		backBounds.width, backBounds.height);
        batcher.end();
		
	}


	private void update(float delta) {
		
		switch(getState()) {
		
		case READY:
			setState(PreGameState.COUNTING);
			break;
			
		case COUNTING:
			if (countDownTime <= 0 ) {
				 setState(PreGameState.GO);
			} else {
				countDownTime -= delta;
			}
			
			if(Gdx.input.isKeyPressed(Keys.BACK)) {
				AssetLoader.back.play();
				setState(PreGameState.QUIT);
			}
			
			else if(Gdx.input.justTouched()) {
				mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					AssetLoader.back.play();
					setState(PreGameState.QUIT);
				}
				
				for (int i =0; i<NO_OF_DEPLOYERS; i++) {
					if (selectedMolesRectangles.get(i).contains(touchPoint.x, touchPoint.y)) {
						//AssetLoader.button.play();
						MoleType nextMoleType = selectedMoles.get(i).next();
						selectedMoles.remove(i);
						selectedMoles.add(i,nextMoleType);
						AssetLoader.clicksound.play();
					}
				}
			}
			break;
		case QUIT:
			socketHandler.dispose();
			socketHandler = null;
			dispose();
			game.setScreen(new MainMenuScreen(game));
			break;
			
		case GO:
			game.setScreen(new PreGamePowerUpScreen(game, socketHandler, selectedMoles));
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
		synchronized(preGameStateLock) {
			this.currentState = preGameState;
		}
	}
	
	public PreGameState getState() {
		synchronized(preGameStateLock) {
			return currentState;
		}
	}
}
