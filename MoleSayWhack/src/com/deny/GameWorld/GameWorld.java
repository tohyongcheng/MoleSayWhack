package com.deny.GameWorld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.MoleObjects.FiveHitMole;
import com.deny.MoleObjects.Mole;
import com.deny.MoleObjects.OneHitMole;
import com.deny.MoleObjects.SabotageMole;
import com.deny.MoleObjects.ThreeHitMole;
import com.deny.Screens.GameScreen;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.PreGameScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;

public class GameWorld {
	//STATICFINAL VARIABLES
	private final static int NUMBER_OF_MOLES_PER_GRID = 9;
	private final static int NUMBER_OF_MOLES_PER_DECK = 4;
	private final static int NUMBER_OF_DEPLOYERS = 3;
	private static final float DELAY_BETWEEN_MOLE_APPEARANCE = 1f;
	
	private Game game;
	private GameScreen gameScreen;
	
	//GAME MECHANICS
	private GameState gameState;
	private Player player;
	private ReadThread readThread;
	private Mole[] moleGrid;
	private Mole[] moleDeck;
	private Queue<Mole>[] moleQueues;
	private Rectangle board;
	private ArrayList<Rectangle> placeHolders;
	private ServerClientThread socketHandler;
	private ArrayList<MoleType> selectedMoles;
	private Rectangle pauseOverlay;
	private MoleDeployer[] moleDeployers;
	private float[] moleDelay;
	
	
	//GAMEOVER MENU
	private Rectangle gameOverMenu;
	private Rectangle playAgainBounds;
	private Rectangle exitBounds;
	


	//Generate randome spawns
	private float runningTime;
	private Random r;
	private MoleDeployer currentMoleDeployer;
	
	
	public enum GameState {
		READY, RUNNING, DEPLOYMENT, WIN, LOSE, HIGHSCORE, PAUSE, MENU, EXIT, RESTART;
	}


	public GameWorld(Game game, GameScreen gameScreen, ServerClientThread sH, ArrayList<MoleType> selectedMoles) {
		this.game = game;
		this.gameScreen = gameScreen;
		
		//Setup player and threads
		this.socketHandler = sH;
		this.readThread = socketHandler.getReadThread();
		this.readThread.setGameWorld(this);
		this.player = new Player(5);
		
		
		
		//Setup GameState
		gameState = GameState.READY;
		moleGrid = new Mole[NUMBER_OF_MOLES_PER_GRID];
		moleDeck = new Mole[NUMBER_OF_MOLES_PER_DECK];
		moleQueues = (Queue<Mole>[]) new LinkedList<?>[NUMBER_OF_MOLES_PER_GRID];
		moleDelay = new float[NUMBER_OF_MOLES_PER_GRID];
		for(int i=0; i<NUMBER_OF_MOLES_PER_GRID; i++ ) {
			moleQueues[i] = new LinkedList<Mole>();
			moleDelay[i] = 1f;
		}
		
		
		//Setup Mole Deployers.
		moleDeployers = new MoleDeployer[NUMBER_OF_DEPLOYERS];
		for (int i =0; i<NUMBER_OF_DEPLOYERS;i++) {
			moleDeployers[i] = new MoleDeployer(this,selectedMoles.get(i));
			moleDeployers[i].getRectangle().set((float)(i*136/3.0), 136f, 45.33f, 30f);
		}
		
		//setup board and overlay
		board = new Rectangle(0, 0, 136, 136);
		pauseOverlay = new Rectangle(0,0,136,204);
		placeHolders = new ArrayList<Rectangle>();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++) {
				placeHolders.add(new Rectangle((float) (i*136/3.0), (float)  (j*136/3.0), (float)  45.33, (float)  45.33));
			}
		}
		
		//GameOverMenu
		gameOverMenu = new Rectangle(0,30, 136, 136);
		playAgainBounds = new Rectangle(20, 50, 96, 30);
		exitBounds = new Rectangle(20, 100, 96, 30);
		
		//setup random for random spawning
		this.r = new Random();
	}

    public void update(float delta) {
    	if (gameState == GameState.READY) gameState = GameState.RUNNING;
    	
    	else if (gameState == GameState.LOSE) {
    		// stop updating everything. 
    	}
    	
    	else if (gameState == GameState.DEPLOYMENT || gameState == GameState.RUNNING) {
	        updateRunning(delta);
    	}
    	
    	else if (gameState == GameState.RESTART) {
    		game.setScreen(new PreGameScreen(game, socketHandler));
    		gameScreen.dispose();
    	}
    	
    	else if (gameState == GameState.EXIT) {    		
    		socketHandler.dispose();
    		socketHandler = null;
    		game.setScreen(new MainMenuScreen(game));
    		gameScreen.dispose();
    		
    	}
    	
    }
    
    public void updateRunning(float delta) {
    	
    	if (player.isDead()) {
    		gameState = GameState.LOSE;
    		socketHandler.gameOver();
    	}
    	
    	for (int i=0; i<NUMBER_OF_MOLES_PER_GRID; i++) {
        	if ((moleGrid[i] == null)) {
        		if (moleQueues[i].peek() != null && moleDelay[i] > DELAY_BETWEEN_MOLE_APPEARANCE ) {
        			moleGrid[i] = moleQueues[i].remove();
        			moleDelay[i] = 0;
        		} else {
        			moleDelay[i] += delta;
        		}
        	}
        }
        
        for (int i=0; i<NUMBER_OF_MOLES_PER_GRID; i++) {
        	if (moleGrid[i] != null) {
        		moleGrid[i].update(delta);
        		moleGrid[i].getBoundingCircle().set((float) (placeHolders.get(i).x + 45.33/2), 
						(float) (placeHolders.get(i).y + 45.33/2), 
						12f);
        		
        		if (moleGrid[i].isDead()) {
	        		//JVM will automatically clean up unreachable objects
	        		moleGrid[i] = null;
        		}
        	}
        }
        
        for (int i =0; i<NUMBER_OF_DEPLOYERS;i++) {
			moleDeployers[i].update(delta);
		}
    }
    
    public void spawnMole(MoleType moleType, int pos) {
    	if (moleGrid[pos] == null) {
    		switch(moleType) {
    		case ONETAP:
    			moleGrid[pos] = new OneHitMole(player);
				break;
    		case THREETAP:
    			moleGrid[pos] = new ThreeHitMole(player);
				break;
			case FIVETAP:
				moleGrid[pos] = new FiveHitMole(player);
				break;
			case SABOTAGE:
				moleGrid[pos] = new SabotageMole(player);
				break;
			default:
				break;
    		}
    	} else {
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
			default:
				break;
    		}
    	}
    }

    
    public Rectangle getBoard() {
        return board;
    }
    
	public static int getNumberOfMolesPerGrid() {
		return NUMBER_OF_MOLES_PER_GRID;
	}

	public static int getNumberOfMolesPerDeck() {
		return NUMBER_OF_MOLES_PER_DECK;
	}

	public GameState getGameState() {
		return gameState;
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
    
	public void generateRandomSpawns(float cooldown, float delta) {
		//Generate random spawns
        runningTime += delta;
        int pos = r.nextInt(9);
        if(runningTime > cooldown) {
        	runningTime = 0;
        	spawnMole(MoleType.ONETAP, pos);
        }
	}

	public MoleDeployer[] getMoleDeployers() {
		return moleDeployers;
	}

	public void setGameState(GameState gameState) {
		System.out.println("GameState changed to " + gameState.toString());
		this.gameState = gameState;
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

}
