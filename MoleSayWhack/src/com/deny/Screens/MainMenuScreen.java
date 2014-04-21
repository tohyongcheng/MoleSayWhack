package com.deny.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.Screens.OptionsScreen.AuthenticationType;

public class MainMenuScreen implements Screen {

	
	private static final int GAME_WIDTH = (int) Gdx.graphics.getWidth();//544; //136 to 272
	private static final int GAME_HEIGHT = (int) Gdx.graphics.getHeight(); //204 to 408
	double scaleW = (double)GAME_WIDTH/544.0;
	double scaleH = (double) GAME_HEIGHT/816;

	Game game;
	
	OrthographicCamera mainMenuCam;
	SpriteBatch batcher;
	Rectangle startBounds;
	Rectangle optionsBounds;
	Rectangle scoreBounds;
	ShapeRenderer shapeRenderer;
	Vector3 touchPoint;
	static AuthenticationType previous;
	
	public MainMenuScreen(Game game) {
		
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		
		batcher = new SpriteBatch();
		
		//Create bounds. We are to edit the Position and the Width/Height Here!


		int boxLength = (int) Math.round(((260/2)*scaleW)) ;

		System.out.println(boxLength);
		startBounds = new Rectangle((int)Math.ceil(GAME_WIDTH/2 - boxLength), (int) (Math.ceil(GAME_HEIGHT/2) + GAME_HEIGHT*0.1*scaleH), (int)(260*scaleW), (int) (91*scaleH));
		optionsBounds = new Rectangle((int)Math.ceil(GAME_WIDTH/2 - boxLength), (int) (Math.ceil(GAME_HEIGHT/2)+ scaleH*(GAME_HEIGHT*0.1 + 120)), (int)(260*scaleW),(int) (91*scaleH));
		scoreBounds = new Rectangle((int)Math.ceil(GAME_WIDTH/2 - boxLength), (int) (Math.ceil(GAME_HEIGHT/2) + scaleH*(GAME_HEIGHT*0.05+ 200)),(int)(260*scaleW),(int) (91*scaleH));

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		
        // Attach batcher to camera
        batcher.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();	

		AssetLoader.ann.stop();
		AssetLoader.summer.stop();
		AssetLoader.summer.setLooping(true);
		AssetLoader.summer.play();

	}
	
	private void draw() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        // Disable transparency 
        // This is good for performance when drawing images that do not require transparency.
        batcher.enableBlending();
        //put the position here

        
        batcher.draw(AssetLoader.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        double Titlewidth = 336*scaleW;
        double Titleheight = 170*scaleH;
        batcher.draw(AssetLoader.Title, (int)(Math.ceil(GAME_WIDTH/2 - Titlewidth/2)), (int)(GAME_HEIGHT*0.1*scaleH), (int)Titlewidth, (int)Titleheight);
        batcher.draw(AssetLoader.sm, (int)(GAME_WIDTH/2 - (359)*scaleW/2), (int)( 284*scaleH), (int)(113*scaleW), (int)(114*scaleH));
        batcher.draw(AssetLoader.m1, (int)(GAME_WIDTH/2 - (359)*scaleW/2 + 123*scaleW), (int)(274*scaleH), (int)(113*scaleW), (int)(114*scaleH));
        batcher.draw(AssetLoader.m3, (int)(GAME_WIDTH/2 - (359)*scaleW/2 + 123*scaleW + 123*scaleH), (int)(284*scaleH), (int)(113*scaleW)	, (int)(114*scaleH));

        
        batcher.draw(AssetLoader.strB, startBounds.x, 
        		startBounds.y, startBounds.width, startBounds.height);
        batcher.draw(AssetLoader.optB, optionsBounds.x, optionsBounds.y
        		, optionsBounds.width, optionsBounds.height);
        /*batcher.draw(AssetLoader.hghB, scoreBounds.x, scoreBounds.y, 
        		scoreBounds.width, scoreBounds.height);
      */

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
		
		if(Gdx.input.isKeyPressed(Keys.BACK)) {
			Gdx.app.exit();
		}
		
		else if(Gdx.input.justTouched()) {
			mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
			if (startBounds.contains(touchPoint.x, touchPoint.y)) {
				AssetLoader.button.play();
				
				
				game.setScreen(new MultiplayerScreen(game));
				
				
				this.dispose();
				return;
			}
			if (optionsBounds.contains(touchPoint.x, touchPoint.y)) {
				game.setScreen(new OptionsScreen(game));
				AssetLoader.button.play();
				this.dispose();
				return;
			}
/*			if(scoreBounds.contains(touchPoint.x, touchPoint.y)){
				game.setScreen(new HighScoreScreen(game));
				AssetLoader.button.play();
				this.dispose();
				return;
			}*/
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
