package gamelogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Queue;

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
import com.deny.MoleObjects.DummyMole;
import com.deny.MoleObjects.Mole;
import com.deny.MoleObjects.MoleKing;
import com.deny.MoleObjects.OneHitMole;
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
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class PowerUpTest {
	static WMGame game;
	static LwjglApplication app;
	static PreGamePowerUpScreen preGamePowerUpScreen;
	static MultiplayerScreen multiplayerScreen;
	static GameScreen gameScreen;
	static ServerClientThread serverClientThread;
	static Player player;
	static GameWorld gameWorld;
	static ArrayList<MoleType> moleTypes;
	static ArrayList<PowerUpType> powerUpTypes; 

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
		
		multiplayerScreen = new MultiplayerScreen(game);

	}


	@Before
	public void setUp() throws Exception {
		serverClientThread = new ServerClientThread(multiplayerScreen, "127.0.0.1");
		serverClientThread.setReadThread(new ReadThread(serverClientThread, serverClientThread.getClient()));
	    gameScreen = new GameScreen(game, serverClientThread, moleTypes, powerUpTypes);
		game.setScreen(gameScreen);
		gameWorld = gameScreen.getGameWorld(); 
		gameWorld.setGameState(GameState.RUNNING);
	}
	

	@Test
	public void testEarthQuake() throws Exception {
		Mole[] moleGrid = gameWorld.getMoleGrid();
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid(); i++) {
			gameWorld.getMoleGrid()[i] = new OneHitMole(player);
			assertNotNull(moleGrid[i]);
		}
		gameWorld.invokePowerUp(PowerUpType.EARTHQUAKE);
		assertEquals(true, Earthquake.isInEffect());

		//Mock Earthquake effect
		Earthquake.causeEffect();
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid(); i++) {
			assertEquals(null,moleGrid[i]);
		}
		
	}
	
	@Test
	public void testBlockMoleGrid() {
		boolean[] blockedMoleGrids = gameWorld.getBlockedGrids();
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			assertFalse(blockedMoleGrids[i]);
		}

		gameWorld.invokePowerUp(PowerUpType.BLOCKGRID);
		assertEquals(true, BlockMoleGrid.isInEffect());
		//Mock Block Mole Grid Effect
		BlockMoleGrid.causeEffect();
		
		int pos = -1;
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			if (blockedMoleGrids[i] == true) {
				pos = i;
			}
		}
		
		if (pos == -1) {
			fail("BlockMoleGrid Test failed");
		}

		
	}
	
	@Test
	public void testBlockMoleDeployer() {
		MoleDeployer[] moleDeployers = gameWorld.getMoleDeployers();
		for (int i=0; i<gameWorld.getNumberOfMoleDeployers();i++) {
			assertFalse(moleDeployers[i].isDisabled());
		}
		
		gameWorld.invokePowerUp(PowerUpType.DISABLEONEMOLEDEPLOYER);
		assertTrue(DisableOneMoleDeployer.isInEffect());
		
		DisableOneMoleDeployer.causeEffect();
		int pos = -1;
		for (int i=0; i<gameWorld.getNumberOfMoleDeployers();i++) {
			if(moleDeployers[i].isDisabled() == true) {
				pos = i;
			}
		}
		
		if (pos == -1) {
			fail("DisableMoleDeployer failed");
		}
		
	}
	
	@Test
	public void testDisableAllPowerUps() {
		PowerUpDeployer[] powerUpDeployers = gameWorld.getPowerUpDeployers();
		for (int i=0; i<gameWorld.getNumberOfPowerUpDeployers();i++) {
			assert(!powerUpDeployers[i].isDisabled());
		}
		assertEquals(false, DisableAllPowerUps.isInEffect());
		gameWorld.invokePowerUp(PowerUpType.DISABLEALLPOWERUPS);
		assertEquals(true, DisableAllPowerUps.isInEffect());
		//Mock Block Mole Grid Effect
		DisableAllPowerUps.causeEffect();

		int pos = -1;
		for (int i=0; i<gameWorld.getNumberOfPowerUpDeployers();i++) {
			if (powerUpDeployers[i].isDisabled() == true) {
				pos = i;
			}
		}
		if (pos==-1) fail("No Dummy Mole found");
	}
	
	@Test
	public void testFog() {
		assertFalse(gameWorld.getFog());
		gameWorld.invokePowerUp(PowerUpType.FOG);
		assertEquals(true, EnableFog.isInEffect());
		EnableFog.causeEffect();
		assertTrue(gameWorld.getFog());
	}
	
	@Test
	public void testDummyMoles() {
		Queue<Mole>[] moleQueues = gameWorld.getMoleQueues();
		
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			assertNull(moleQueues[i].peek());
		}
		assertEquals(false, GenerateDummyMoles.isInEffect());
		gameWorld.invokePowerUp(PowerUpType.DUMMY);
		assertEquals(true, GenerateDummyMoles.isInEffect());
		GenerateDummyMoles.causeEffect();
		
		int pos = -1;
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			if ((moleQueues[i].peek()!=null) && (moleQueues[i].peek().getClass() == DummyMole.class)) {
				pos = i;
			}
		}
		if (pos==-1) fail("No Dummy Mole found");
		
	}
	
	@Test
	public void testInvulnerability() {
		assertFalse(gameWorld.getPlayer().isInvulnerable());
		assertEquals(false, Invulnerability.isInEffect());
		gameWorld.invokePowerUp(PowerUpType.INVULNERABILITY);
		assertEquals(true, Invulnerability.isInEffect());
		Invulnerability.causeEffect();
		assertTrue(gameWorld.getPlayer().isInvulnerable());
	}
	
	@Test
	public void testMoleShower() {
		Queue<Mole>[] moleQueues = gameWorld.getMoleQueues();
		
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			assertNull(moleQueues[i].peek());
		}
		
		gameWorld.invokePowerUp(PowerUpType.MOLESHOWER);
		MoleShower.causeEffect();

		
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			assertNotNull(moleQueues[i].peek());
		}
		
	}
	
	@Test
	public void testMoleKing() {
		Queue<Mole>[] moleQueues = gameWorld.getMoleQueues();
		
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			assertNull(moleQueues[i].peek());
		}
		gameWorld.invokePowerUp(PowerUpType.MOLEKING);
		SpawnMoleKing.causeEffect();
		
		
		int pos = -1;
		for (int i=0; i<gameWorld.getNumberOfMolesPerGrid();i++) {
			if ((moleQueues[i].peek()!=null) && (moleQueues[i].peek().getClass() == MoleKing.class)) {
				pos = i;
			}
		}
		if (pos==-1) fail("No King Mole found");
	}

}
