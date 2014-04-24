package com.deny.Screens;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameHelpers.IPAddressInputListener;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

/**
 * A screenn that is called
 * right after the start game button is
 * pressed.
 *
 */

public class MultiplayerScreen implements Screen {
	

	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	/**
	 * The states of this screen
	 *
	 */
	public enum MultiplayerState {
		READY, CONNECTED, START, RESTART, QUIT, PROTOCOLMISMATCH, TRUDY;
	}
	
	//GuardedBy("multiplayerStateLock")
	private MultiplayerState currentState;
	
	private Object multiplayerStateLock = new Object();
	
	private WMGame game;
	private OrthographicCamera multiplayerCam;
	private SpriteBatch batcher;
	private Rectangle backBounds;
	private Rectangle playBounds;
	private Rectangle refreshBounds;
	private Rectangle changeAddressBounds;
	private ServerClientThread socketHandler;
	private ShapeRenderer shapeRenderer;
	private String otherAddress;
	private String myAddress;
	private Preferences prefs;
	private IPAddressInputListener listener;
	private Vector3 touchPoint;
	private boolean enableSFX;
	
	/**
	 * The constructor of this screen
	 * Takes in the game that instantiate this
	 * class
	 */
	public MultiplayerScreen(WMGame game) {
		this.game = game;
		this.multiplayerCam = new OrthographicCamera();
		multiplayerCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);

        double scaleW = (double) GAME_WIDTH/544;
        double scaleH = (double) GAME_HEIGHT/816;
		backBounds = new Rectangle(3,(int)(GAME_HEIGHT-9-82*scaleH),(int)(83*scaleW), (int)(82*scaleH));
		playBounds = new Rectangle((int)(GAME_WIDTH/2 - 260*scaleW/2),(int)(3*GAME_HEIGHT/6 - 90*scaleH/2 - 50*scaleH),(int)(260*scaleW), (int)(90*scaleH));
		
		refreshBounds = new Rectangle((int)(GAME_WIDTH/2 - 260*scaleW/2),(int)(3*GAME_HEIGHT/6 - 90*scaleH/2 - 50*scaleH),(int)(260*scaleW), (int)(90*scaleH));
		changeAddressBounds = new Rectangle((int)(GAME_WIDTH/2 - 260*scaleW/2),(int)(5*GAME_HEIGHT/6 - 90*scaleH/2 - 50*scaleH),(int)(260*scaleW), (int)(90*scaleH));

		listener = new IPAddressInputListener(this);
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(multiplayerCam.combined);
	
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(multiplayerCam.combined);
		touchPoint = new Vector3();
		
		//Preferences
		prefs = Gdx.app.getPreferences("Options");
		otherAddress = (prefs.getString("IPAddress", "127.0.0.1"));
		enableSFX = prefs.getBoolean("enableSFX", true);
				
		//Initialize Sockets!
		socketHandler = new ServerClientThread(this, otherAddress);
		socketHandler.start();
		setState(MultiplayerState.READY);
		
		// The following code loops through the available network interfaces
        // Keep in mind, there can be multiple interfaces per device, for example
        // one per NIC, one per active wireless and the loopback
        // In this case we only care about IPv4 address ( x.x.x.x format )
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface ni : Collections.list(interfaces)){
                for(InetAddress address : Collections.list(ni.getInetAddresses()))
                {
                    if(address instanceof Inet4Address){
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        // Print the contents of our array to a string.  Yeah, should have used StringBuilder
        myAddress = "";
        for (int i=0; i<addresses.size();i++) {
        	myAddress += addresses.get(i) + "\n";
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
	 * The method that update this screen, depending
	 * on the input or the states of this screen
	 * @param delta
	 */
	public void update() {	
		
		switch (getState()) {
		case READY:		
			if(Gdx.input.isKeyPressed(Keys.BACK)) {
				if (enableSFX) AssetLoader.back.play();
				setState(MultiplayerState.QUIT);
			}
			
			else if(Gdx.input.justTouched()) {
				multiplayerCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					setState(MultiplayerState.QUIT);
				}
				
				else if (refreshBounds.contains(touchPoint.x,touchPoint.y)) {
					setState(MultiplayerState.RESTART);
					if (enableSFX) AssetLoader.button.play();
				}
				
				else if (changeAddressBounds.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX) AssetLoader.button.play();
					Gdx.input.getTextInput(listener, "Set IP Address", otherAddress);
				}
			}
			break;
		
		case CONNECTED:
			if(Gdx.input.isKeyPressed(Keys.BACK)) {
				if (enableSFX) AssetLoader.back.play();
				socketHandler.leaveGameRoom();
				setState(MultiplayerState.QUIT);
			}
			
			else if(Gdx.input.justTouched()) {

				multiplayerCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (playBounds.contains(touchPoint.x,touchPoint.y)) {
					if (enableSFX) AssetLoader.button.play();
					socketHandler.toChooseMolesScreen();
					setState(MultiplayerState.START);
				}
				
				else if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX) AssetLoader.back.play();
					socketHandler.leaveGameRoom();
					setState(MultiplayerState.QUIT);
				}
				
				else if (changeAddressBounds.contains(touchPoint.x, touchPoint.y)) {
					if (enableSFX) AssetLoader.button.play();
					Gdx.input.getTextInput(listener, "Set IP Address", otherAddress);
				}
			}
			break;
		case PROTOCOLMISMATCH:
			
			if(Gdx.input.justTouched()) {
				if (socketHandler !=null) {
					socketHandler.interrupt();
					socketHandler.dispose();
				}
				System.out.println("Disconnected because protocol type does not match. Please try again");
				game.setScreen(new MainMenuScreen(game));
				dispose();
			
			}
			break;
		case TRUDY:
			
			if(Gdx.input.justTouched()) {
				if (socketHandler !=null) {
					socketHandler.interrupt();
					socketHandler.dispose();
				}
				System.out.println("Disconnected because there is intruder! Please try again. ");
				game.setScreen(new MainMenuScreen(game));
				dispose();
				
				}
			break;
		case QUIT:
			System.out.println("QUITTING NOW.");
			if (socketHandler !=null) {
				socketHandler.interrupt();
				socketHandler.dispose();
			}
			game.setScreen(new MainMenuScreen(game));
			dispose();
			break;
			
		case RESTART:
			restartSocketHandler();
			setState(MultiplayerState.READY);
			break;
			
		case START:
			game.setScreen(new PreGameScreen(game, socketHandler));
			dispose();
			break;
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

        batcher.draw(AssetLoader.background, 0, 0, GAME_WIDTH, GAME_HEIGHT);

        
        switch (getState()) {
        case CONNECTED:
        	batcher.draw(AssetLoader.strB, playBounds.x, playBounds.y,playBounds.width, playBounds.height);
        	batcher.draw(AssetLoader.enterIP, changeAddressBounds.x, changeAddressBounds.y,changeAddressBounds.width, changeAddressBounds.height);
            AssetLoader.font.draw(batcher, "My address is: ", 20, 20);
            AssetLoader.font.drawMultiLine(batcher, myAddress.toString(), 20, 50);
        	break;
        case READY:
        	batcher.draw(AssetLoader.loading, playBounds.x, playBounds.y,playBounds.width, playBounds.height);
        	batcher.draw(AssetLoader.refresh, refreshBounds.x, refreshBounds.y,refreshBounds.width, refreshBounds.height);
        	batcher.draw(AssetLoader.enterIP, changeAddressBounds.x, changeAddressBounds.y,changeAddressBounds.width, changeAddressBounds.height);
            AssetLoader.font.draw(batcher, "My address is: ", 20, 20);
            AssetLoader.font.drawMultiLine(batcher, myAddress.toString(), 20, 50);
        	break;
		case QUIT:
			break;
		case RESTART:
			break;
		case START:
			break;
		case PROTOCOLMISMATCH:
			batcher.draw(AssetLoader.mp, 0, 0, GAME_WIDTH, GAME_HEIGHT);
			break;
		case TRUDY:
			batcher.draw(AssetLoader.tdy, 0,0, GAME_WIDTH, GAME_HEIGHT);
			break;
		default:
			break;
        }

        batcher.draw(AssetLoader.cnl, backBounds.x, backBounds.y,backBounds.width, backBounds.height);


	    batcher.end();
	}
	
	/**
	 * The method that sets the state of
	 * this screen
	 * @param s
	 */
	public void setState(final MultiplayerState s) {
		synchronized(multiplayerStateLock) {
			currentState = s;
		}
	}
	/**
	 * The method that returns the 
	 * current state of this game
	 * @return
	 */
	public MultiplayerState getState() {
		synchronized(multiplayerStateLock) {
			return currentState;
		}
	}
	/**
	 * Returns the IP address
	 * of the player you are
	 * connecting to
	 * @return otherAddress
	 */
	public String getIPAddress() {
		return otherAddress;
	}
	/**
	 * Set the ipaddress of the
	 * other player this player is connecting
	 * to
	 * @param IPAddress
	 */
	public void setIPAddress(String IPAddress) {
		this.otherAddress = IPAddress;
		prefs.putString("IPAddress", IPAddress);
		prefs.flush();
		System.out.println("Flushed!");
	}
	/**
	 * Restart the thread that is used
	 * to send messages between the two 
	 * players
	 */
	public void restartSocketHandler() {
		if (socketHandler !=null) {
			socketHandler.interrupt();
			socketHandler.dispose();
		}
		socketHandler = (new ServerClientThread(this, otherAddress));
		socketHandler.start();
		setState(MultiplayerState.READY);
	}
	/**
	 * Returns the thread that is used
	 * to send messages between the two 
	 * players
	 * @return
	 */
	
	public ServerClientThread getSocketHandler() {
		return socketHandler;
	}
	/**
	 * Sets the thread that is used
	 * to send messages between the two 
	 * players
	 * @param socketHandler
	 */
	public void setSocketHandler(ServerClientThread socketHandler) {
		this.socketHandler = socketHandler;
	}
	
	
	/**
	 * Returns the Game of this screen
	 * @return game
	 */
	public WMGame getGame() {
		return game;
	}
}
