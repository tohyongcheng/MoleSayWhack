package com.deny.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.Threads.ServerClientThread;

public class MultiplayerScreen implements Screen {
	
	private static final int GAME_WIDTH = 136;
	private static final int GAME_HEIGHT = 204;
	private enum MultiplayerState {
		READY, CONNECTED, START
	}
	private MultiplayerState currentState;
	private Game game;
	private OrthographicCamera multiplayerCam;
	private Rectangle backBounds;
	private Rectangle playBounds;
	private ServerClientThread socketHandler;
	private ShapeRenderer shapeRenderer;
	Vector3 touchPoint;
	
	
	public MultiplayerScreen(Game game) {
		this.game = game;
		this.multiplayerCam = new OrthographicCamera();
		multiplayerCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		backBounds = new Rectangle(0,188,16,16);
		playBounds = new Rectangle(23,50,90,50);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(multiplayerCam.combined);
		touchPoint = new Vector3();
		socketHandler = new ServerClientThread(this);
		socketHandler.start();
		currentState = MultiplayerState.READY;
	
	}
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
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
	
	public void update() {	
		switch (currentState) {
		case READY:			
			if(Gdx.input.justTouched()) {
				multiplayerCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					if (socketHandler !=null) socketHandler.interrupt();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
			break;
		
		case CONNECTED:
			if(Gdx.input.justTouched()) {
				multiplayerCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (playBounds.contains(touchPoint.x,touchPoint.y)) {
					//send msg to opponent
					socketHandler.toChooseMolesScreen();
					setStateToStart();

				}
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					if (socketHandler !=null) socketHandler.interrupt();
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}
			break;
			
		case START:
			game.setScreen(new PreGameScreen(game, socketHandler));
			break;
		}
		
	}
	
	public void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        shapeRenderer.begin(ShapeType.Filled);
        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from myWorld (Using ShapeType.Filled)
        
        if (currentState == MultiplayerState.CONNECTED) {
	        shapeRenderer.rect(playBounds.x, playBounds.y,
	        		playBounds.width, playBounds.height);
        }
        
        shapeRenderer.rect(backBounds.x, backBounds.y,
        		backBounds.width, backBounds.height);

        shapeRenderer.end();
		
	}
	
	public void setStateToStart() {
		this.currentState = MultiplayerState.START;
	}

	public void setStateToConnected() {
		this.currentState = MultiplayerState.CONNECTED;
	}

}
