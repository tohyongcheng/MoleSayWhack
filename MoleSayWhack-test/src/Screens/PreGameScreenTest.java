package screens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.PreGameScreen;
import com.deny.Screens.PreGameScreen.PreGameState;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class PreGameScreenTest {
	static WMGame game;
	static LwjglApplication app;
	static PreGameScreen preGameScreen;
	static MultiplayerScreen multiplayerScreen;
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSay" +"Whack";
		cfg.width = 136*3;
		cfg.height = 204*3;
	    game = new WMGame();
	    app = new LwjglApplication(game, cfg);
	    multiplayerScreen = new MultiplayerScreen(game);
	    preGameScreen = new PreGameScreen(game, new ServerClientThread(multiplayerScreen, "127.0.0.1"));
	    game.setScreen(preGameScreen);
	}
	
	@Before
	public void setUp() throws Exception {
		preGameScreen = new PreGameScreen(game, new ServerClientThread(multiplayerScreen, "127.0.0.1"));
		game.setScreen(preGameScreen);
		preGameScreen.setState(PreGameState.READY);
	}



	@Test
	public void testChangeState() {
		assertNotNull(preGameScreen.getState());
		assertEquals(PreGameState.READY, preGameScreen.getState());
		preGameScreen.setState(PreGameState.COUNTING);
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
	}

	
	@Test
	public void testStateUpdateToCounting() {
		//Test if state READY changes to COUNTING
		assertEquals(PreGameState.READY, preGameScreen.getState());
		preGameScreen.update(0);
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
		
		//Test if state COUNTING changes within 10s
		preGameScreen.update(10f);
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
	}
	
	@Test
	public void testStateQuitToMainMenu() {
		//Unable to test touch, so test change state to QUIT immediately
		
		preGameScreen.setState(PreGameState.QUIT);
		preGameScreen.update(0);
		
		//Test change of screens
		assertNotSame(preGameScreen, game.getScreen());
		assertSame(MainMenuScreen.class,game.getScreen().getClass());
	}
	
	@Test
	public void testStateUpdateToGo() {
		//Test if state READY changes to COUNTING
		assertEquals(PreGameState.READY, preGameScreen.getState());
		preGameScreen.update(0);
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
		
		//Test if state COUNTING changes within 10s
		preGameScreen.update(20f);
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
		preGameScreen.update(0);	//update to change state
		assertEquals(PreGameState.GO, preGameScreen.getState());
	}
	
	@Test
	public void testChangeStateThreadSafety() {
		Thread[] threads = new Thread[1000];
		for (int i=0;i<1000;i++) {
			threads[i] = new Thread(new Runnable() {
					public void run() {
						preGameScreen.setState(PreGameState.READY);
						preGameScreen.getState();
						preGameScreen.setState(PreGameState.COUNTING);
						preGameScreen.getState();
						preGameScreen.setState(PreGameState.READY);
						preGameScreen.getState();
						preGameScreen.setState(PreGameState.COUNTING);
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
		assertEquals(PreGameState.COUNTING, preGameScreen.getState());
	}

}
