package com.deny.GameWorld;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameObjects.PowerUpType;
import com.deny.MoleObjects.DummyMole;
import com.deny.MoleObjects.FiveHitMole;
import com.deny.MoleObjects.Mole;
import com.deny.MoleObjects.MoleKing;
import com.deny.MoleObjects.OneHitMole;
import com.deny.MoleObjects.SabotageMole;
import com.deny.MoleObjects.ThreeHitMole;
import com.deny.PowerUpObjects.BlockMoleGrid;
import com.deny.PowerUpObjects.DisableAllPowerUps;
import com.deny.PowerUpObjects.DisableOneMoleDeployer;
import com.deny.PowerUpObjects.Earthquake;
import com.deny.PowerUpObjects.EnableFog;
import com.deny.PowerUpObjects.GenerateDummyMoles;
import com.deny.PowerUpObjects.Invulnerability;
import com.deny.PowerUpObjects.MoleShower;
import com.deny.PowerUpObjects.SpawnMoleKing;
import com.deny.Screens.GameScreen;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.PreGameScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;

public class GameWorld {
	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	private double scaleW = (double) GAME_WIDTH/544;
	private double scaleH = (double) GAME_HEIGHT/816;
	
	//STATIC FINAL VARIABLES
	private final static int NUMBER_OF_GRIDS = 9;
	private final static int NUMBER_OF_MOLE_DEPLOYERS = 3;
	private final static int NUMBER_OF_POWERUP_DEPLOYERS = 3;
	private static final float DELAY_BETWEEN_MOLE_APPEARANCE = 1f;
	private static final int STARTING_HP_FOR_PLAYER = 5;
	
	//PREFERENCES
	private Preferences prefs;
	private boolean enableBGM;
	private boolean enableSFX;
	
	//GAME
	private Game game;
	private GameScreen gameScreen;
	
	//GAME MECHANICS
	private GameState gameState;
	private Player player;
	private int opponentHP;
	
	private ReadThread readThread;
	private Mole[] moleGrid;
	private Mole[] moleDeck;
	private LinkedBlockingQueue<Mole>[] moleQueues;
	private Rectangle board;
	private ArrayList<Rectangle> placeHolders;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	private ArrayList<PowerUpType> selectedPowerUps;
	
	private MoleDeployer[] moleDeployers;
	private MoleDeployer currentMoleDeployer;
	private PowerUpDeployer[] powerUpDeployers;
	private float[] moleDelay;
	
	//PAUSE MENU
	private Rectangle pauseButton;
	private Rectangle pauseOverlay;	
	private Rectangle continueButton;
	
	//GAMEOVER MENU
	private Rectangle gameOverMenu;
	private Rectangle playAgainBounds;
	private Rectangle exitBounds;
	
	//POWERUPS
	private boolean hasFog;
	private boolean[] blockedGrids;
	
	//Generate random spawns
	private Random r;
	
	//LOCKS
	private Object gameStateLock = new Object();
	private Object opponentHPLock = new Object();
	
	public enum GameState {
		READY, RUNNING, DEPLOYMENT, WIN, LOSE, PAUSE, MENU, EXIT, RESTART;
	}


	@SuppressWarnings("unchecked")
	public GameWorld(Game game, GameScreen gameScreen) {
		
		this.game = game;
		this.gameScreen = gameScreen;
		
		//Get options
		prefs = Gdx.app.getPreferences("Options");
		enableBGM = prefs.getBoolean("enableBGM", true);
		enableSFX = prefs.getBoolean("enableSFX", true);
		
		//Setup player and threads
		this.socketHandler = gameScreen.getSocketHandler();
		this.readThread = socketHandler.getReadThread();
		this.readThread.setGameWorld(this);
		this.player = new Player(STARTING_HP_FOR_PLAYER,this);
		setOpponentHP(STARTING_HP_FOR_PLAYER);
		
		this.selectedMoles = gameScreen.getSelectedMoles();
		this.selectedPowerUps = gameScreen.getSelectedPowerUps();
		
		//Setup GameState, Grid and Delays.
		setGameState(GameState.READY);
		moleGrid = new Mole[NUMBER_OF_GRIDS];
		moleQueues = (LinkedBlockingQueue<Mole>[]) new LinkedBlockingQueue<?>[NUMBER_OF_GRIDS];
		
		moleDelay = new float[NUMBER_OF_GRIDS];
		for(int i=0; i<NUMBER_OF_GRIDS; i++ ) {
			moleQueues[i] = new LinkedBlockingQueue<Mole>();
			moleDelay[i] = 1f;
		}
		
		//Setup Powerups
		
		BlockMoleGrid.load(this);
		DisableAllPowerUps.load(this);
		DisableOneMoleDeployer.load(this);
		Earthquake.load(this);
		EnableFog.load(this);
		GenerateDummyMoles.load(this);
		Invulnerability.load(this);
		SpawnMoleKing.load(this);
		MoleShower.load(this);
		
		
		//Setup PowerUp Boolean Variables
		hasFog = false;
		blockedGrids = new boolean[NUMBER_OF_GRIDS];
		for (int i =0; i<NUMBER_OF_GRIDS;i++) {
			blockedGrids[i] = false;
		}

		
		//Setup Mole Deployers.
		moleDeployers = new MoleDeployer[NUMBER_OF_MOLE_DEPLOYERS];
		for (int i =0; i<NUMBER_OF_MOLE_DEPLOYERS;i++) {
			//change here
			moleDeployers[i] = new MoleDeployer(this, selectedMoles.get(i));
			moleDeployers[i].getRectangle().set((int)(GAME_WIDTH/2-(340*scaleW/2)+(i*120*scaleW)),(int)(565*scaleH),(int)(100*scaleW),(int)(100*scaleH));
		}
		
		//Setup Powerup Deployers
		powerUpDeployers = new PowerUpDeployer[NUMBER_OF_POWERUP_DEPLOYERS];
		for (int i =0; i<NUMBER_OF_POWERUP_DEPLOYERS;i++) {
			powerUpDeployers[i] = new PowerUpDeployer(this, selectedPowerUps.get(i));
			powerUpDeployers[i].getRectangle().set((int)(GAME_WIDTH/2-(475*scaleW/2)+(i*165*scaleW)),(int)(670*scaleH),(int)(145*scaleW),(int)(75*scaleH));
		}
		
		//setup board and overlay
		board = new Rectangle(0, 0, (int)(150*scaleW), (int)(150*scaleH));
		pauseOverlay = new Rectangle(0,0,GAME_WIDTH,GAME_HEIGHT);
		pauseButton = new Rectangle(10,GAME_HEIGHT -10 - (int)(52*scaleH),(int)(53*scaleW),(int)(52*scaleH));
		placeHolders = new ArrayList<Rectangle>();
		
		//Making placeholder RECTANGLES	
		Rectangle one = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2)), (int)(100*scaleH),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle two = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2)+150*scaleW), (int)(100*scaleH),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle three = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2) + 300*scaleW), (int)(100*scaleH),(int)(150*scaleW),(int)(150*scaleH));	
		Rectangle four = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2)), (int)(100*scaleH+150*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle five = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2) + 150*scaleW), (int)(100*scaleH+150*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle six = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2) + 300*scaleW), (int)(100*scaleH+150*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle seven = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2)), (int)(100*scaleH+300*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle eight = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2) + 150*scaleW), (int)(100*scaleH+300*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		Rectangle nine = new Rectangle((int)(GAME_WIDTH/2-(450*scaleW/2) + 300*scaleW), (int)(100*scaleH+300*scaleH ),(int)(150*scaleW),(int)(150*scaleH));
		placeHolders.add(one);placeHolders.add(two);placeHolders.add(three);placeHolders.add(four);
		placeHolders.add(five);placeHolders.add(six);placeHolders.add(seven);placeHolders.add(eight);
		placeHolders.add(nine);
		
		//GameOverMenu
		gameOverMenu = new Rectangle(0,30, 136, 136);
		continueButton = new Rectangle(GAME_WIDTH/2-(int)(82*scaleW/2), GAME_HEIGHT/2, (int)(83*scaleW), (int)(82*scaleH));
		playAgainBounds = new Rectangle(GAME_WIDTH/2-(int)(82*scaleW/2), GAME_HEIGHT/2, (int)(83*scaleW), (int)(82*scaleH));
		exitBounds = new Rectangle(GAME_WIDTH/2-(int)(82*scaleW/2), GAME_HEIGHT/2 + (int)(115*scaleH), (int)(83*scaleW), (int)(82*scaleH));
		
		//setup random for random spawning
		this.r = new Random();
		
		if (enableBGM) {
			AssetLoader.summer.stop();
			AssetLoader.ann.stop();
			AssetLoader.ann.setLooping(true);
			AssetLoader.ann.setVolume(0.7f);
			AssetLoader.ann.play();
		}
		
	}

	
    public void update(float delta) {    	
    	switch(getGameState()) {
    	case READY:
    		setGameState(GameState.RUNNING);
    		break;
    	case LOSE:
    		break;
    	case MENU:
    		break;
    	case PAUSE:
    		break;
    	case DEPLOYMENT:
    		updateRunning(delta);
    		break;
    	case RUNNING:
    		updateRunning(delta);
    		break;
    	case RESTART:
    		updateRestart();
    		break;
    	case EXIT:
    		updateExit(delta);
    		break;
		case WIN:
			break;
		default:
			break;
    	}
    }
    
    private void updateExit(float delta) {
    	socketHandler.dispose();
		socketHandler = null;
		game.setScreen(new MainMenuScreen(game));
		gameScreen.dispose();
		BlockMoleGrid.unload();
		DisableAllPowerUps.unload();
		DisableOneMoleDeployer.unload();
		Earthquake.unload();
		EnableFog.unload();
		GenerateDummyMoles.unload();
		Invulnerability.unload();
		SpawnMoleKing.unload();
		MoleShower.unload();
	}

	public void updateRunning(float delta) {
    	if (player.isDead()) {
    		setGameState(GameState.LOSE);
    		System.out.println("GameState changed to LOSE");
    		socketHandler.gameOver();
    		if (enableSFX) AssetLoader.gameover.play();
    	}
    	
    	for (int i=0; i<NUMBER_OF_GRIDS; i++) {
        	if ((moleGrid[i] == null)) {
        		if (moleQueues[i].peek() != null && moleDelay[i] > DELAY_BETWEEN_MOLE_APPEARANCE ) {
        			moleGrid[i] = moleQueues[i].remove();
        			if (moleGrid[i].getMoleType() != MoleType.MOLEKING) { 
        				if (enableSFX) AssetLoader.popup.play();
        			}
        			moleDelay[i] = 0;
        		} else {
        			moleDelay[i] += delta;
        		}
        	}
        }
        
        for (int i=0; i<NUMBER_OF_GRIDS; i++) {
        	if (moleGrid[i] != null) {
        		moleGrid[i].update(delta);
        		moleGrid[i].getBoundingCircle().set((int)(placeHolders.get(i).x + (30*scaleW)), (int) (placeHolders.get(i).y + (40*scaleH)), (int)(120*scaleW), (int)(120*scaleH)); 

        		if (moleGrid[i].isDead()) {
	        		//JVM will automatically clean up unreachable objects
	        		moleGrid[i] = null;
        		}
        	}
        }
        
        for (int i =0; i<NUMBER_OF_MOLE_DEPLOYERS;i++) {
			moleDeployers[i].update(delta);
		}
        
        for (int i =0; i<NUMBER_OF_POWERUP_DEPLOYERS;i++) {
			powerUpDeployers[i].update(delta);
		}
        
        BlockMoleGrid.update(delta);
		DisableAllPowerUps.update(delta);
		DisableOneMoleDeployer.update(delta);
		EnableFog.update(delta);
		GenerateDummyMoles.update(delta);
		Earthquake.update(delta);
		Invulnerability.update(delta);
		SpawnMoleKing.update(delta);
		MoleShower.update(delta);
    }
	
	public void updateRestart() {
		game.setScreen(new PreGameScreen(game, socketHandler));
		gameScreen.dispose();
	}
    
    public void spawnMole(MoleType moleType, int pos) {
		switch(moleType) {
		case ONETAP:
			moleQueues[pos].add(new OneHitMole(player));
			break;
		case THREETAP:
			moleQueues[pos].add(new ThreeHitMole(player));
			break;
		case FIVETAP:
			moleQueues[pos].add(new FiveHitMole(player));
			break;
		case SABOTAGE:
			moleQueues[pos].add(new SabotageMole(player));
			break;
		case MOLEKING:
			moleQueues[pos].add(new MoleKing(player));
			break;
		case DUMMY:
			moleQueues[pos].add(new DummyMole(player));
			break;
		default:
			break;
		}
    }
    
    public Rectangle getBoard() {
        return board;
    }
    
	public static int getNumberOfMolesPerGrid() {
		return NUMBER_OF_GRIDS;
	}

	public static int getNumberOfMoleDeployers() {
		return NUMBER_OF_MOLE_DEPLOYERS;
	}
	
	public static int getNumberOfPowerUpDeployers() {
		return NUMBER_OF_POWERUP_DEPLOYERS;
	}

	public Player getPlayer() {
		return player;
	}

	public ReadThread getReadThread() {
		return readThread;
	}

	public Mole[] getMoleGrid() {
		return moleGrid;
	}

	public Mole[] getMoleDeck() {
		return moleDeck;
	}

	public Queue<Mole>[] getMoleQueues() {
		return moleQueues;
	}

	public ArrayList<Rectangle> getPlaceHolders() {
		return placeHolders;
	}
    
	public MoleDeployer[] getMoleDeployers() {
		return moleDeployers;
	}

	public void setGameState(final GameState gs) {
		synchronized(gameStateLock) {
			gameState = gs;
		}
	}
	
	public GameState getGameState() {
		synchronized(gameStateLock) {
			return gameState;
		}
	}

	public void setCurrentMoleDeployer(MoleDeployer md) {
		currentMoleDeployer = md;
	}
	
	public MoleDeployer getCurrentMoleDeployer() {
		return currentMoleDeployer;
	}
	
	public ServerClientThread getSocketHandler() {
		return socketHandler;
	}
	public Rectangle getGameOverMenu() {
		return gameOverMenu;
	}

	public Rectangle getPlayAgainBounds() {
		return playAgainBounds;
	}

	public Rectangle getExitBounds() {
		return exitBounds;
	}
	
	public Game getGame() {
		return game;
	}

	public Rectangle getPauseOverlay() {
		return pauseOverlay;
	}

	public Rectangle getPauseButton() {
		return pauseButton;
	}

	public void pauseGame() {
		socketHandler.pauseGame();
		setGameState(GameState.MENU);
	}

	public Rectangle getContinueButton() {
		return continueButton;
	}

	public void exitGame() {
		getSocketHandler().exitGame();
		setGameState(GameState.EXIT);
		
	}

	public void continueGame() {
		getSocketHandler().continueGame();
		setGameState(GameState.RUNNING);
		
	}

	public PowerUpDeployer[] getPowerUpDeployers() {
		return powerUpDeployers;
	}

	public void invokePowerUp(PowerUpType powerUp) {
		switch(powerUp) {
		case BLOCKGRID:
			BlockMoleGrid.invoke();
			break;
		case DISABLEALLPOWERUPS:
			DisableAllPowerUps.invoke();
			break;
		case DISABLEONEMOLEDEPLOYER:
			DisableOneMoleDeployer.invoke();
			break;
		case DUMMY:
			GenerateDummyMoles.invoke();
			break;
		case FOG:
			EnableFog.invoke();
			break;
		case MOLEKING:
			SpawnMoleKing.invoke();
			break;
		case MOLESHOWER:
			MoleShower.invoke();
			break;
		case EARTHQUAKE:
			Earthquake.invoke();
			break;
		case INVULNERABILITY:
			Invulnerability.invoke();
			break;
		default:
			break;
		}
	}	
	
	public boolean[] getBlockedGrids() {
		return blockedGrids;
	}

	public void setFog(boolean b) {
		this.hasFog = true;
	}
	
	public boolean getFog() {
		return hasFog;
	}

	public void setOpponentHP(int hp) {
		synchronized(opponentHPLock) {
			opponentHP = hp;
		}
	}
	
	public int getOpponentHP() {
		synchronized(opponentHPLock) {
			return opponentHP;
		}
	}
}
