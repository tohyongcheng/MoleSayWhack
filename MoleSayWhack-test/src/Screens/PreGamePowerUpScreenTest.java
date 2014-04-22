package screens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deny.GameObjects.MoleType;
import com.deny.MoleObjects.OneHitMole;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Screens.PreGamePowerUpScreen.PreGameState;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class PreGamePowerUpScreenTest {
	static WMGame game;
	static LwjglApplication app;
	static PreGamePowerUpScreen preGamePowerUpScreen;
	static MultiplayerScreen multiplayerScreen;
	static ServerClientThread serverClientThread;
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSay" +"Whack";
		cfg.width = 136*3;
		cfg.height = 204*3;
	    game = new WMGame();
	    app = new LwjglApplication(game, cfg);
	    multiplayerScreen = new MultiplayerScreen(game);
	    serverClientThread = new ServerClientThread(multiplayerScreen, "127.0.0.1");
	    
	}
	
	@Before
	public void setUp() throws Exception {
		ArrayList<MoleType> moleTypes = new ArrayList<MoleType>();
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		moleTypes.add(MoleType.ONETAP);
		
		preGamePowerUpScreen = new PreGamePowerUpScreen(game, serverClientThread, moleTypes);
		game.setScreen(preGamePowerUpScreen);
	}



	@Test
	public void testChangeState() {
		assertNotNull(preGamePowerUpScreen.getState());
		assertEquals(PreGameState.READY, preGamePowerUpScreen.getState());
		preGamePowerUpScreen.setState(PreGameState.COUNTING);
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
	}

	
	@Test
	public void testStateUpdateToCounting() {
		//Test if state READY changes to COUNTING
		assertEquals(PreGameState.READY, preGamePowerUpScreen.getState());
		preGamePowerUpScreen.update(0);
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
		
		//Test if state COUNTING changes within 10s
		preGamePowerUpScreen.update(10f);
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
	}
	
	@Test
	public void testStateQuitToMainMenu() {
		//Unable to test touch, so test change state to QUIT immediately
		
		preGamePowerUpScreen.setState(PreGameState.QUIT);
		preGamePowerUpScreen.update(0);
		
		//Test change of screens
		assertNotSame(preGamePowerUpScreen, game.getScreen());
		assertSame(MainMenuScreen.class,game.getScreen().getClass());
	}
	
	@Test
	public void testStateUpdateToGo() {
		//Test if state READY changes to COUNTING
		assertEquals(PreGameState.READY, preGamePowerUpScreen.getState());
		preGamePowerUpScreen.update(0);
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
		
		//Test if state COUNTING changes within 10s
		preGamePowerUpScreen.update(20f);
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
		preGamePowerUpScreen.update(0);	//update to change state
		assertEquals(PreGameState.GO, preGamePowerUpScreen.getState());
	}
	
	@Test
	public void testChangeStateThreadSafety() {
		Thread[] threads = new Thread[1000];
		for (int i=0;i<1000;i++) {
			threads[i] = new Thread(new Runnable() {
					public void run() {
						preGamePowerUpScreen.setState(PreGameState.READY);
						preGamePowerUpScreen.getState();
						preGamePowerUpScreen.setState(PreGameState.COUNTING);
						preGamePowerUpScreen.getState();
						preGamePowerUpScreen.setState(PreGameState.READY);
						preGamePowerUpScreen.getState();
						preGamePowerUpScreen.setState(PreGameState.COUNTING);
					}
				}
			);
		}
		
		for(int i =0;i<1000;i++) {
			threads[i].start();
		}
		
		for(int i =0;i<1000;i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		assertEquals(PreGameState.COUNTING, preGamePowerUpScreen.getState());
	}

}
