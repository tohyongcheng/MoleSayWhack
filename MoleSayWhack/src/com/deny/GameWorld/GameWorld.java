package com.deny.GameWorld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.Mole;
import com.deny.GameObjects.Player;
import com.deny.SocketHandler.SocketHandler;
import com.deny.Threads.ReadThread;
import com.deny.Threads.SendThread;

public class GameWorld {
	private final static int NUMBER_OF_MOLES_PER_GRID = 9;
	private final static int NUMBER_OF_MOLES_PER_DECK = 4;
	private GameState gameState;
	private Player player;
	private ReadThread readThread;
	private SendThread sendThread;
	private Mole[] moleGrid;
	private Mole[] moleDeck;
	private Queue<Mole>[] moleQueues;
	private Rectangle board;
	private ArrayList<Rectangle> placeHolders;
	private SocketHandler socketHandler;
	
	//Generate randome spawns
	private float runningTime;
	private Random r;
	
	public enum GameState {
		MENU, READY, RUNNING, DEPLOYMENT, GAMEOVER, HIGHSCORE;
	}

	public GameWorld() {
		
		//Setup GameState
		gameState = GameState.READY;
		moleGrid = new Mole[NUMBER_OF_MOLES_PER_GRID];
		moleDeck = new Mole[NUMBER_OF_MOLES_PER_DECK];
		moleQueues = (Queue<Mole>[]) new LinkedList<?>[NUMBER_OF_MOLES_PER_GRID];
		
		
		//Setup Server
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String message = null;
		try {
			message = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		boolean isServer = false;
		if (message.equals("s"))
			isServer = true;
		
		socketHandler = new SocketHandler(isServer, this);
		socketHandler.start();
		
		//Setup player and threads
		this.player = new Player(5);
		this.readThread = socketHandler.getReadThread();
		this.sendThread = socketHandler.getSendThread();
		

		board = new Rectangle(0, 0, 136, 136);
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
        
        generateRandomSpawns( 2f , delta);
    }
    
    public Rectangle getBoard() {
        return board;
    }
	
    public void deployMole(int moleType, int pos) {
    	sendThread.deployMole(moleType, pos);
    }
    
    public void spawnMole(int moleType, int pos) {
    	if (moleGrid[pos] == null) {
    		moleGrid[pos] = new Mole(player);
    	} else {
    		moleQueues[pos].add(new Mole(player));
    	}
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

	public SendThread getSendThread() {
		return sendThread;
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
}
