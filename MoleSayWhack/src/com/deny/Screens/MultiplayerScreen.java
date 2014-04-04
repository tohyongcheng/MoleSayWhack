package com.deny.Screens;

import java.awt.TextArea;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.deny.GameHelpers.IPAddressInputListener;
import com.deny.Threads.ServerClientThread;

public class MultiplayerScreen implements Screen {
	
	private static final int GAME_WIDTH = 136;
	private static final int GAME_HEIGHT = 204;
	public enum MultiplayerState {
		READY, CONNECTED, START, RESTART, QUIT
	}
	private MultiplayerState currentState;
	private Game game;
	private OrthographicCamera multiplayerCam;
	private SpriteBatch batcher;
	private Rectangle backBounds;
	private Rectangle playBounds;
	private Rectangle changeAddressBounds;
	private ServerClientThread socketHandler;
	private ShapeRenderer shapeRenderer;
	private String otherAddress;
	private String myAddress;
	private Preferences prefs;
	private IPAddressInputListener listener;
	private BitmapFont font;
	Vector3 touchPoint;
	
	
	public MultiplayerScreen(Game game) {
		this.game = game;
		this.multiplayerCam = new OrthographicCamera();
		multiplayerCam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		backBounds = new Rectangle(0,188,16,16);
		playBounds = new Rectangle(23,50,90,30);
		changeAddressBounds = new Rectangle(23,120,90,30);
		listener = new IPAddressInputListener(this);
		
		font = new BitmapFont();
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(multiplayerCam.combined);
		
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(multiplayerCam.combined);
		touchPoint = new Vector3();
		
		//Preferences
		prefs = Gdx.app.getPreferences("Multiplayer");
		otherAddress = (prefs.getString("IPAddress", ""));
				
		//Sockets!
		socketHandler = new ServerClientThread(this, otherAddress);
		socketHandler.start();
		currentState = MultiplayerState.READY;
		
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

        myAddress = addresses.get(0);
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
				
				else if (changeAddressBounds.contains(touchPoint.x, touchPoint.y)) {
					Gdx.input.getTextInput(listener, "Set IP Address", otherAddress);
				}
			}
			break;
		
		case CONNECTED:
			if(Gdx.input.justTouched()) {
				multiplayerCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
				
				if (playBounds.contains(touchPoint.x,touchPoint.y)) {
					socketHandler.toChooseMolesScreen();
					currentState = MultiplayerState.START;
				}
				
				else if (backBounds.contains(touchPoint.x, touchPoint.y)) {
					socketHandler.leaveGameRoom();
					currentState = MultiplayerState.QUIT;
				}
				
				else if (changeAddressBounds.contains(touchPoint.x, touchPoint.y)) {
					Gdx.input.getTextInput(listener, "Set IP Address", otherAddress);
				}
			}
			break;
			
		case QUIT:
			if (socketHandler !=null) {
				socketHandler.toMainMenuScreen();
				socketHandler.interrupt();
				socketHandler.dispose();
			}
			game.setScreen(new MainMenuScreen(game));
			dispose();
			break;
			
		case RESTART:
			restartSocketHandler();
			currentState = MultiplayerState.READY;
			break;
			
		case START:
			game.setScreen(new PreGameScreen(game, socketHandler));
			dispose();
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
        
        switch (currentState) {
        case CONNECTED:
        	shapeRenderer.rect(playBounds.x, playBounds.y,
	        		playBounds.width, playBounds.height);
        	shapeRenderer.rect(changeAddressBounds.x, changeAddressBounds.y,
        			changeAddressBounds.width, changeAddressBounds.height);
        	break;
        case READY:
        	shapeRenderer.rect(changeAddressBounds.x, changeAddressBounds.y,
        			changeAddressBounds.width, changeAddressBounds.height);
        	break;
        }
        
        shapeRenderer.rect(backBounds.x, backBounds.y,
        		backBounds.width, backBounds.height);

        shapeRenderer.end();
        
        batcher.begin();
        font.draw(batcher, myAddress.toString(), 0, 20);
	    batcher.end();
	}
	

	public void setState(MultiplayerState s) {
		currentState =s;
	}

	public String getIPAddress() {
		return otherAddress;
	}

	public synchronized void setIPAddress(String IPAddress) {
		this.otherAddress = IPAddress;
		prefs.putString("IPAddress", IPAddress);
		prefs.flush();
		System.out.println("Flushed!");
	}
	
	public synchronized void restartSocketHandler() {
		if (socketHandler !=null) {
			socketHandler.interrupt();
			socketHandler.dispose();
		}
		socketHandler = (new ServerClientThread(this, otherAddress));
		socketHandler.start();
		currentState = MultiplayerState.READY;
	}

	public ServerClientThread getSocketHandler() {
		return socketHandler;
	}

	public void setSocketHandler(ServerClientThread socketHandler) {
		this.socketHandler = socketHandler;
	}

	public Game getGame() {
		return game;
	}
}
