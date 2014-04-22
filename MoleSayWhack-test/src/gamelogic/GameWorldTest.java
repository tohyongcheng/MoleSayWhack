package gamelogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.OneHitMole;
import com.deny.Screens.GameScreen;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Screens.PreGameScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class GameWorldTest {
	static WMGame game;
	static LwjglApplication app;
	static PreGamePowerUpScreen preGamePowerUpScreen;
	static MultiplayerScreen multiplayerScreen;
	static GameScreen gameScreen;
	static ServerClientThread serverClientThread;
	static Player player;
	static ReadThread readThread;
	static GameWorld gameWorld;
	static ArrayList<PowerUpType> powerUpTypes;
	static ArrayList<MoleType> moleTypes;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSay" +"Whack";
		cfg.width = 136*3;
		cfg.height = 204*3;
	    game = new WMGame();
	    app = new LwjglApplication(game, cfg);
	    
	    moleTypes = new ArrayList<MoleType>();
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		
		powerUpTypes = new ArrayList<PowerUpType>();
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		
		
	}


	@Before
	public void setUp() throws Exception {
		multiplayerScreen = new MultiplayerScreen(game);
		serverClientThread = new ServerClientThread(multiplayerScreen, "127.0.0.1");
		readThread = new ReadThread(serverClientThread, null);
		serverClientThread.setReadThread(readThread);
	    gameScreen = new GameScreen(game, serverClientThread, moleTypes, powerUpTypes);
		game.setScreen(gameScreen);
		gameWorld = gameScreen.getGameWorld(); 
		player = gameWorld.getPlayer();
	}



	@Test
	public void testInitialConditions() {
		//Test that the following are not null and that state is at ready
		assertNotNull(gameWorld.getMoleGrid());
		assertNotNull(gameWorld.getMoleDeployers());
		assertNotNull(gameWorld.getBoard());
		assertNotNull(gameWorld.getMoleDeployers());
		assertNotNull(gameWorld.getPowerUpDeployers());
		assertNotNull(gameWorld.getMoleQueues());
		assertEquals(GameState.READY,gameWorld.getGameState());
	}
	
	@Test
	public void testChangeStateFromReadyToRunning() {
		gameWorld.update(0);
		assertEquals(GameState.RUNNING, gameWorld.getGameState());
	}
	
	@Test
	public void testSpawnMole() {
		gameWorld.update(0);
		
		assertNull(gameWorld.getMoleGrid()[0]);
		assertNull(gameWorld.getMoleQueues()[0].peek());
		
		gameWorld.spawnMole(MoleType.ONETAP, 0);
		
		//Mole is now in queue
		assertNotNull(gameWorld.getMoleQueues()[0].peek());
		assertEquals(OneHitMole.class,gameWorld.getMoleQueues()[0].peek().getClass());
		
		gameWorld.update(1f);
		gameWorld.update(0);
		
		//Mole is now not in queue but in MoleGrid
		assertNull(gameWorld.getMoleQueues()[0].peek());
		assertSame(OneHitMole.class, gameWorld.getMoleGrid()[0].getClass());
	}
	
	@Test
	public void testDeployMole() {
		//Test cooldown of MoleDeployer
		
		gameWorld.update(0);
		//test cooldown of moleDeployer
		MoleDeployer[] moleDeployers = gameWorld.getMoleDeployers();
		assertTrue(moleDeployers[0].isAvailable());
		moleDeployers[0].deployMole(0);
		assertFalse(moleDeployers[0].isAvailable());
		
		//MoleDeployer is available after 2s
		moleDeployers[0].update(2);
		moleDeployers[0].update(0);
		assertTrue(moleDeployers[0].isAvailable());
		
	}
	
	@Test
	public void testDeployPowerUp() {
		
		//Test cooldown of PowerUpDeployer
		gameWorld.update(0);
		PowerUpDeployer[] powerUpDeployer = gameWorld.getPowerUpDeployers();
		assertTrue(powerUpDeployer[0].isAvailable());
		powerUpDeployer[0].deploy();
		assertFalse(powerUpDeployer[0].isAvailable());
		
		//PowerUp Deployer is available after 10s
		powerUpDeployer[0].update(10);
		powerUpDeployer[0].update(0);
		assertTrue(powerUpDeployer[0].isAvailable());
	}
	
	
	@Test
	public void testSetCurrentMoleDeployer() {
		gameWorld.update(0);
		
		//currentMoleDeployer is at first null
		assertNull(gameWorld.getCurrentMoleDeployer());
		
		//it should not be null after preparing to deploy
		gameWorld.setCurrentMoleDeployer(gameWorld.getMoleDeployers()[0]);
		assertNotNull(gameWorld.getCurrentMoleDeployer());
		assertSame(gameWorld.getMoleDeployers()[0],gameWorld.getCurrentMoleDeployer());
		
		//After Mole Deployer is deployed, current mole deployer is set to null
		gameWorld.setCurrentMoleDeployer(null);
		assertNull(gameWorld.getCurrentMoleDeployer());
	}
	
	@Test
	public void testStateChanges() {
		gameWorld.update(0);
		assertEquals(GameState.RUNNING, gameWorld.getGameState());
		gameWorld.setGameState(GameState.DEPLOYMENT);
		assertEquals(GameState.DEPLOYMENT, gameWorld.getGameState());
		gameWorld.setGameState(GameState.RUNNING);
		assertEquals(GameState.RUNNING, gameWorld.getGameState());
	}
	
	public void testStateExit() {
		gameWorld.update(0);
		gameWorld.setGameState(GameState.EXIT);
		assertEquals(GameState.EXIT, gameWorld.getGameState());
		gameWorld.update(0);
		
		//After Exit, screen changes to MainMenuScreen
		assertEquals(MainMenuScreen.class, game.getScreen().getClass());
		
		//GameWorld and ServerClientThread is disposed and becomes null
		assertNull(gameWorld);
		assertNull(gameScreen);
		assertNull(serverClientThread);		
	}
	
	public void testStateRestart() {
		gameWorld.update(0);
		gameWorld.setGameState(GameState.RESTART);
		assertEquals(GameState.RESTART, gameWorld.getGameState());
		gameWorld.update(0);
		
		//After restart, screen changes to PreGameScreen
		assertEquals(PreGameScreen.class, game.getScreen().getClass());
		
		//GameWorld is disposed and becomes null
		assertNull(gameWorld);
		assertNull(gameScreen);
		
		//ServerClientThread is still the same and is still running
		assertNotNull(serverClientThread);
		assertSame(serverClientThread, ((PreGameScreen) game.getScreen()).getSocketHandler());
	}

	
	@Test
	public void testStateWin() {
		gameWorld.update(0);
		gameWorld.setGameState(GameState.WIN);
		assertEquals(GameState.WIN,gameWorld.getGameState());
		gameWorld.update(0);
		
		//GameState remains as WIN until player touches input (cant be tested here)
		assertEquals(GameState.WIN,gameWorld.getGameState());
	}
	
	@Test
	public void testStateLose(){
		gameWorld.update(0);
		assertEquals(GameState.RUNNING,gameWorld.getGameState());
		player.damage();
		player.damage();
		player.damage();
		player.damage();
		player.damage();
		assertEquals(0,player.getHP());
		assertTrue(player.isDead());
		gameWorld.update(0);
		assertEquals(GameState.LOSE, gameWorld.getGameState());
		
		//GameState remains as LOSE until user presses button (cant be tested here)
		gameWorld.update(0);
		assertEquals(GameState.LOSE, gameWorld.getGameState());
		
	}
}
