package gamelogic;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.MoleObjects.DummyMole;
import com.deny.MoleObjects.FiveHitMole;
import com.deny.MoleObjects.MoleKing;
import com.deny.MoleObjects.OneHitMole;
import com.deny.MoleObjects.SabotageMole;
import com.deny.MoleObjects.ThreeHitMole;
import com.deny.Screens.GameScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Threads.ReadThread;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class MoleTest {
	
	static WMGame game;
	static LwjglApplication app;
	static PreGamePowerUpScreen preGamePowerUpScreen;
	static MultiplayerScreen multiplayerScreen;
	static GameScreen gameScreen;
	static ServerClientThread serverClientThread;
	static Player player;
	static GameWorld gameWorld;
	
	OneHitMole oneHitMole;
	ThreeHitMole threeHitMole;
	FiveHitMole fiveHitMole;
	SabotageMole sabotageMole;
	MoleKing kingMole;
	DummyMole dummyMole;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSay" +"Whack";
		cfg.width = 136*3;
		cfg.height = 204*3;
	    game = new WMGame();
	    app = new LwjglApplication(game, cfg);
	    
	    ArrayList<MoleType> moleTypes = new ArrayList<MoleType>();
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		
		ArrayList<PowerUpType> powerUpTypes = new ArrayList<PowerUpType>();
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		powerUpTypes.add(PowerUpType.EARTHQUAKE);
		
		multiplayerScreen = new MultiplayerScreen(game);
		game.setScreen(multiplayerScreen);

		serverClientThread = new ServerClientThread(multiplayerScreen, "127.0.0.1");
		serverClientThread.setReadThread(new ReadThread(serverClientThread, null));
	    gameScreen = new GameScreen(game, serverClientThread, moleTypes, powerUpTypes);
		game.setScreen(gameScreen);
		gameWorld = gameScreen.getGameWorld(); 
		
	}


	@Before
	public void setUp() throws Exception {
		player = new Player(5, null);
		oneHitMole = new OneHitMole(player);
		threeHitMole = new ThreeHitMole(player);
		fiveHitMole = new FiveHitMole(player);
		sabotageMole = new SabotageMole(player);
		kingMole = new MoleKing(player); 
		dummyMole = new DummyMole(player);
	}

	
	@Test
	public void testGameWorld() {
		assertNotNull(gameWorld);
	}
	
	@Test
	public void testMoleType() {
		assertSame(MoleType.ONETAP, oneHitMole.getMoleType());
		assertSame(MoleType.THREETAP, threeHitMole.getMoleType());
		assertSame(MoleType.FIVETAP, fiveHitMole.getMoleType());
		assertSame(MoleType.SABOTAGE, sabotageMole.getMoleType());
		assertSame(MoleType.DUMMY, dummyMole.getMoleType());
		assertSame(MoleType.MOLEKING, kingMole.getMoleType());
	}
	
	@Test
	public void testReduceHP() {
		assertEquals(3, threeHitMole.getHP());
		threeHitMole.minusHP();
		assertEquals(2, threeHitMole.getHP());
	}
	
	@Test
	public void testDeathofMole() {
		assertEquals(false, oneHitMole.isDead());
		assertEquals(1, oneHitMole.getHP());
		oneHitMole.minusHP();
		assertEquals(0, oneHitMole.getHP());
		assertEquals(true, oneHitMole.isDead());
	}
	
	@Test
	public void testDamagePlayer() {
		assertEquals(5, player.getHP());
		oneHitMole.update(oneHitMole.getMOLE_APPEARANCE_TIME());
		oneHitMole.update(0);
		assertEquals(4, player.getHP());
	}
	
	@Test
	public void testInvulnerability() {
		player.setInvulnerability(true);
		assertEquals(5, player.getHP());
		oneHitMole.update(oneHitMole.getMOLE_APPEARANCE_TIME());
		oneHitMole.update(0);
		assertEquals(5, player.getHP());
	}
	
	@Test
	public void testSabotageMole() {
		//test that it does not hurt player after MOLE_APPEARANCE_TIME
		assertEquals(5, player.getHP());
		sabotageMole.update(sabotageMole.getMOLE_APPEARANCE_TIME());
		sabotageMole.update(0);
		assertEquals(5, player.getHP());
		
		//test that it hurts player it player touches it
		sabotageMole = new SabotageMole(player);
		sabotageMole.minusHP();
		sabotageMole.update(0);
		assertEquals(0, sabotageMole.getHP());
		assertTrue(sabotageMole.isDead());
		sabotageMole.update(0);
		sabotageMole.damage();
		assertEquals(4,player.getHP());
	}
	
	@Test
	public void testDummyMole() {
		//does not hurt player even after it stays for its duration
		assertEquals(5, player.getHP());
		sabotageMole.update(sabotageMole.getMOLE_APPEARANCE_TIME());
		sabotageMole.update(0);
		assertEquals(5, player.getHP());
		
	}
	
	@Test
	public void testMoleKingHP() {
		//requires 20 hits to die
		assertEquals(20, kingMole.getHP());
		assertEquals(false, kingMole.isDead());
		for (int i=0; i<19; i++) {
			kingMole.minusHP();
		}
		assertEquals(false, kingMole.isDead());
		kingMole.minusHP();
		assertEquals(true, kingMole.isDead());
		
	}
	
	public void testMoleKingInstantKO() {
		//moleking causes instant KO if not killed in 10s
		assertEquals(5, player.getHP());
		kingMole.update(kingMole.getMOLE_APPEARANCE_TIME());
		kingMole.update(0);
		assertEquals(0, player.getHP());
	}

}
