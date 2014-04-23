package com.deny.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameHelpers.NameInputListener;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

/***
 * The screen which displays the options of the game
 * Displayed after the option button
 * is clicked on the main screen
 *
 */

public class OptionsScreen implements Screen {
	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	public double scaleW = (double)GAME_WIDTH/544;
	public double scaleH = (double) GAME_HEIGHT/816;
	
	private WMGame game;
	private OrthographicCamera mainMenuCam;
	private Preferences prefs;
	
	//Options
	private String name;
	private boolean enableBGM;
	private boolean enableSFX;
	private AuthenticationType authType;

	/**
	 *The states of the Authentication Protocols
	 *
	 */
	public enum AuthenticationType {
		NOPROTOCOL, T2,T3,T4,T5, TRUDY;
		
		/**
		 * Get the next authentication type
		 * @return
		 */
		public AuthenticationType next() {
			return values()[(ordinal()+1) % values().length];
		}
	}
	/**
	 *  The states of the optionsScreen
	 *
	 */
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
	

	/**
	 * The constructor of this screen
	 * Takes in the game that instantiate this
	 * class
	 */

	public OptionsScreen(WMGame game) {
		this.game = game;
		this.optionsState = optionsState.RUNNING;
		this.mainMenuCam = new OrthographicCamera();
		setState(OptionsScreenState.RUNNING);
		mainMenuCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(mainMenuCam.combined);
		font = new BitmapFont();
		font.setScale(1, -1);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(mainMenuCam.combined);
		touchPoint = new Vector3();
		
		backBounds = new Rectangle(3,(int)(GAME_HEIGHT-9-82*scaleH),(int)(83*scaleW), (int)(82*scaleH));
		
		//Setup Positions of the Buttons
		changeNameBtn = new Rectangle((int)(136*scaleW),(int)(scaleH* 181), (int)(scaleW*264), (int)(scaleH*93));
		changeBGMBtn = new Rectangle((int)(136*scaleW),(int)(scaleH* 315), (int)(scaleW*264), (int)(scaleH*93));
		changeSFXBtn = new Rectangle((int)(136*scaleW),(int)(scaleH* 458), (int)(scaleW*264), (int)(scaleH*93));
		changeAuthBtn = new Rectangle((int)(136*scaleW),(int)(scaleH* 596), (int)(scaleW*264), (int)(scaleH*93));
		
		//Setup options
		prefs = Gdx.app.getPreferences("Options");
		name = prefs.getString("Name", "Player");
		enableBGM = prefs.getBoolean("enableBGM", true);
		enableSFX = prefs.getBoolean("enableSFX", true);

		authType = AuthenticationType.valueOf(prefs.getString("authType","NOPROTOCOL"));
		ServerClientThread.authType = getAuthType();

		
		System.out.println("Options");
		System.out.println("name is: " + name);
		System.out.println("enableBGM is: " + enableBGM);
		System.out.println("enableSFX is: " + enableSFX);
		System.out.println("authType is: " + authType.toString());
	}
	
	
	/**
	 * Return the AssetLoader associated with the
	 * current authentication protocol
	 * @return
	 */
	
	public TextureRegion getProtocolAsset(){
		switch (authType){
		case NOPROTOCOL:
			return AssetLoader.noprotocol;
		case T2:
			return AssetLoader.t2;
		case T3:
			return AssetLoader.t3;
		case T4:
			return AssetLoader.t4;
		case T5:
			return AssetLoader.t5;
		case TRUDY:
			return AssetLoader.tdyopt;
		default:
			return AssetLoader.noprotocol;
		
		}
	}
	
	/**
	 * The method that draw all the pictures or images
	 * that are to be shown in this screen,
	 * depending on the state of this screen
	 */
	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
     
      
        batcher.begin();
        batcher.enableBlending();

        batcher.draw(AssetLoader.optBG, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        
        //ADD OPTIONS BUTTONS HERE
        batcher.draw(AssetLoader.changename, changeNameBtn.x, changeNameBtn.y, changeNameBtn.width, changeNameBtn.height);
        if(enableBGM == true){
        batcher.draw(AssetLoader.changemusic, changeBGMBtn.x, changeBGMBtn.y, changeBGMBtn.width, changeBGMBtn.height);}
        else if (enableBGM == false){
        	batcher.draw(AssetLoader.mscOFF, changeBGMBtn.x, changeBGMBtn.y, changeBGMBtn.width, changeBGMBtn.height);
        }
        if(enableSFX ==false){
        batcher.draw(AssetLoader.changefx, changeSFXBtn.x, changeSFXBtn.y, changeSFXBtn.width, changeSFXBtn.height);}

        else if(enableSFX==true){
        	 batcher.draw(AssetLoader.fxON, changeSFXBtn.x, changeSFXBtn.y, changeSFXBtn.width, changeSFXBtn.height);
        }

        batcher.draw(getProtocolAsset(), changeAuthBtn.x, changeAuthBtn.y, changeAuthBtn.width, changeAuthBtn.height);
        
        batcher.draw(AssetLoader.ext, backBounds.x, backBounds.y,backBounds.width, backBounds.height);
        batcher.end();
	}
	/**
	 * The method that update this screen, depending
	 * on the input or the states of this screen
	 * @param delta
	 */
	public void update() {
		switch(getState()) {
		
		case RUNNING:
			if(Gdx.input.justTouched()) {
				mainMenuCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX) AssetLoader.back.play();
					setState(OptionsScreenState.BACK);
				}
				
				else if (changeNameBtn.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX)AssetLoader.button.play();
					Gdx.input.getTextInput(listener, "Set your name: ", name);
				}
				
				else if (changeBGMBtn.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX)AssetLoader.button.play();
					setEnableBGM(!enableBGM);
				}
				
				else if (changeSFXBtn.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX)AssetLoader.button.play();
					setEnableSFX(!enableSFX);
				}
				
				else if (changeAuthBtn.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX)AssetLoader.button.play();
					setAuthType(authType.next());
					
					//set the serverClientThread authType
					ServerClientThread.authType = getAuthType();
					System.out.println("Authentication protocol is changed to : " + authType.toString() );
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
	/**
	 * The method that calls update and 
	 * draw
	 */
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
	/**
	 * The method that returns the 
	 * current state of this game
	 * @return
	 */
	public OptionsScreenState getState() {
		return optionsState;
	}
	/**
	 * The method that sets the state of
	 * this screen
	 * @param s
	 */
	public void setState(OptionsScreenState s) {
		this.optionsState = s;
	}
	/**
	 * Returns the name of the player
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the player
	 * from the user input
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		prefs.putString("Name", name );
		prefs.flush();
	}
	/**
	 * Check whether background music is enabled
	 * @return
	 */
	public boolean isEnableBGM() {
		return enableBGM;
	}
	/**
	 * Set the status of background music
	 * @param enableBGM
	 */
	public void setEnableBGM(boolean enableBGM) {
		if (enableBGM == false ){
			AssetLoader.ann.stop();
			AssetLoader.summer.stop();
		} else {
			AssetLoader.ann.stop();
			AssetLoader.summer.stop();
			AssetLoader.summer.setLooping(true);
			AssetLoader.summer.play();
		}
		this.enableBGM = enableBGM;
		prefs.putBoolean("enableBGM", enableBGM);
		prefs.flush();
	}
	/**
	 * Checks whether sound effects are enabled
	 * @return
	 */
	public boolean isEnableSFX() {
		return enableSFX;
	}
	/***
	 * Set the status of the sound effects
	 * @param enableSFX
	 */
	public void setEnableSFX(boolean enableSFX) {
		this.enableSFX = enableSFX;
		prefs.putBoolean("enableSFX", enableSFX);
		prefs.flush();
	}
	/**
	 * Get the current authentication type
	 * @return authType
	 */
	public AuthenticationType getAuthType() {
		return authType;
	}
	/**
	 * Set the authentication type of this game
	 * @param authType
	 */
	public void setAuthType(AuthenticationType authType) {
		this.authType = authType;
		prefs.putString("authType", authType.toString());
		prefs.flush();
	}
}
