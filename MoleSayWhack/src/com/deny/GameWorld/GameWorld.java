package com.deny.GameWorld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.Mole;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;

public class GameWorld {
	private final static int NUMBER_OF_MOLES_PER_GRID = 9;
	private final static int NUMBER_OF_MOLES_PER_DECK = 4;
	private final static int NUMBER_OF_DEPLOYERS = 3;
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
	
	//Generate randome spawns
	private float runningTime;
	private Random r;
	private MoleDeployer currentMoleDeployer;
	
	public enum GameState {
		READY, RUNNING, DEPLOYMENT, GAMEOVER, HIGHSCORE, PAUSE, MENU;
	}


	public GameWorld(ServerClientThread sH, ArrayList<MoleType> selectedMoles) {
		
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
		
		
		//Setup Mole Deployers. TODO: have moledeployers to be dependent on choice
		moleDeployers = new MoleDeployer[NUMBER_OF_DEPLOYERS];
		for (int i =0; i<NUMBER_OF_DEPLOYERS;i++) {
			moleDeployers[i] = new MoleDeployer(this,selectedMoles.get(i));
			moleDeployers[i].getRectangle().set((float)(i*136/3.0), 136f, 45.33f, 30f);
		}
		

		board = new Rectangle(0, 0, 136, 136);
		pauseOverlay = new Rectangle(0,0,136,204);
		placeHolders = new ArrayList<Rectangle>();
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++) {
				placeHolders.add(new Rectangle((float) (i*136/3.0), (float)  (j*136/3.0), (float)  45.33, (float)  45.33));
			}
		}
		
		for(int i=0; i<NUMBER_OF_MOLES_PER_GRID; i++ ) {
			moleQueues[i] = new LinkedList<Mole>();
		}
		
		this.r = new Random();
	}

    public void update(float delta) {

    	if (gameState == GameState.READY) gameState = GameState.RUNNING;
    	
    	//Dequeue when possible
        for (int i=0; i<NUMBER_OF_MOLES_PER_GRID; i++) {
        	
        	if ((moleGrid[i] == null)) {
        		if (moleQueues[i].peek() != null) {
        			moleGrid[i] = moleQueues[i].remove();
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
    }
    
	
    public void deployMole(int moleType, int pos) {
    	//TODO: Change random
    	socketHandler.deployMole(moleType, r.nextInt(9));
    }
    
    public void spawnMole(int moleType, int pos) {
    	//doesn't care about moleType now
    	if (moleGrid[pos] == null) {
    		moleGrid[pos] = new Mole(player);
    	} else {
    		moleQueues[pos].add(new Mole(player));
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
        	spawnMole( 0, pos);
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

}
