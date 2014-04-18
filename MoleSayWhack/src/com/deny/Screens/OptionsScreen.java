package com.deny.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameHelpers.NameInputListener;

public class OptionsScreen implements Screen {
	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	public double scaleW = (double)GAME_WIDTH/544;
	public double scaleH = (double) GAME_HEIGHT/816;
	
	private Game game;
	private OrthographicCamera mainMenuCam;
	private Preferences prefs;
	
	//Options
	private String name;
	private boolean enableBGM;
	private boolean enableSFX;
	private AuthenticationType authType;


	public enum AuthenticationType {
		NONE, RSA;
		//NATALIE, ADD THE AUTHENTICATION TYPES HERE
		
		public AuthenticationType next() {
			return values()[(ordinal()+1) % values().length];
		}
	}
	
	public enum OptionsScreenState {
		RUNNING, BACK;
	}
	private OptionsScreenState optionsState;
	
	
	private NameInputListener listener;
	private SpriteBatch batcher;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;
	private Vector3 touchPoint;
	private Rectangle backBounds;
	private Rectangle changeNameBtn;
	private Rectangle changeBGMBtn;
	private Rectangle changeSFXBtn;
	private Rectangle changeAuthBtn;
	
	

	public OptionsScreen(Game game) {
		this.game = game;
		this.mainMenuCam = new OrthographicCamera();
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);
		font = new BitmapFont();
		font.setScale(1, -1);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
		
		backBounds = new Rectangle(3,(int)(GAME_HEIGHT-9-82*scaleH),(int)(83*scaleW), (int)(82*scaleH));
		
		//NATALIE, put the position of buttons into the rectangles.
		changeNameBtn = new Rectangle();
		changeBGMBtn = new Rectangle();
		changeSFXBtn = new Rectangle();
		changeAuthBtn = new Rectangle();
		
		//Setup options
		prefs = Gdx.app.getPreferences("Options");
		name = (prefs.getString("Name", "Player"));
		enableBGM = prefs.getBoolean("enableBGM", true);
		enableSFX = prefs.getBoolean("enableSFX", true);
		authType = AuthenticationType.valueOf(prefs.getString("authType", "NONE"));
		
		
	}
	
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
     
      
        batcher.begin();
        batcher.enableBlending();

        batcher.draw(AssetLoader.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        
        //ADD OPTIONS BUTTONS HERE
        
        batcher.draw(AssetLoader.cnl, backBounds.x, backBounds.y,backBounds.width, backBounds.height);
        batcher.end();
	}
	
	public void update() {
		switch(getState()) {
		
		case RUNNING:
			if(Gdx.input.justTouched()) {
				mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					setState(OptionsScreenState.BACK);
				}
				
				else if (changeNameBtn.contains(touchPoint.x, touchPoint.y)) {
					Gdx.input.getTextInput(listener, "Set your name: ", name);
				}
				
				else if (changeBGMBtn.contains(touchPoint.x, touchPoint.y)) {
					setEnableBGM(!enableBGM);
					
				}
				
				else if (changeSFXBtn.contains(touchPoint.x, touchPoint.y)) {
					setEnableSFX(!enableSFX);
					
				}
				
				else if (changeAuthBtn.contains(touchPoint.x, touchPoint.y)) {
					setAuthType(authType.next());
				}
			}
			break;
		case BACK:
			game.setScreen(new MainMenuScreen(game));
			dispose();
			break;
		}
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
	
	public OptionsScreenState getState() {
		return optionsState;
	}
	
	public void setState(OptionsScreenState s) {
		this.optionsState = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		prefs.putString("name", name );
	}

	public boolean isEnableBGM() {
		return enableBGM;
	}

	public void setEnableBGM(boolean enableBGM) {
		this.enableBGM = enableBGM;
		prefs.putBoolean("enableBGM", enableBGM);
	}

	public boolean isEnableSFX() {
		return enableSFX;
	}

	public void setEnableSFX(boolean enableSFX) {
		this.enableSFX = enableSFX;
		prefs.putBoolean("enableSFX", enableSFX);
	}

	public AuthenticationType getAuthType() {
		return authType;
	}

	public void setAuthType(AuthenticationType authType) {
		this.authType = authType;
	}
}
