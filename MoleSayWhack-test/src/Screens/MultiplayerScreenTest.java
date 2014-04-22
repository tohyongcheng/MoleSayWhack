package screens;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.molewhack.WMGame;



public class MultiplayerScreenTest {
	MultiplayerScreen multiplayerScreen;
	LwjglApplication app;
	WMGame game;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MoleSay" +"Whack";
		cfg.width = 136*3;
		cfg.height = 204*3;
	    game = new WMGame();
	    app = new LwjglApplication(game, cfg);
	    multiplayerScreen = new MultiplayerScreen(game);
	    game.setScreen(multiplayerScreen);
	}


	@Test
	public void testStates() {
		
		//Test for current screen
		assertEquals(MultiplayerScreen.class,game.getScreen().getClass());
		
		//Test starting state to be ready
		assertEquals(MultiplayerState.READY, multiplayerScreen.getState());
		
		//Test if state is changed to CONNECTED
		multiplayerScreen.setState(MultiplayerState.CONNECTED);
		assertEquals(MultiplayerState.CONNECTED, multiplayerScreen.getState());
		
		//Test if state remains unchanged
		multiplayerScreen.update();
		assertEquals(MultiplayerState.CONNECTED, multiplayerScreen.getState());
		
		//test restart serverclientthread
		multiplayerScreen.restartSocketHandler();
		assertEquals(MultiplayerState.READY,multiplayerScreen.getState());
		
		//Test Restart State. It will remain on the same screen.
		multiplayerScreen.setState(MultiplayerState.RESTART);
		assertEquals(MultiplayerScreen.class,game.getScreen().getClass());
		multiplayerScreen.update();
		assertEquals(MultiplayerState.READY, multiplayerScreen.getState());
		
		//Test Quit State. Will change screen to MainMenuScreen
		multiplayerScreen.setState(MultiplayerState.QUIT);
		multiplayerScreen.update();
		assertEquals(MainMenuScreen.class,game.getScreen().getClass());
		
	}
	
	public void testIPAddress() {
		
		//test default case
		assertEquals("127.0.0.1",multiplayerScreen.getIPAddress());
		
		//set IP Address
		multiplayerScreen.setIPAddress("192.168.0.1");
		assertThat("127.0.0.1",not(multiplayerScreen.getIPAddress()));
		assertThat("192.168.0.1", is(multiplayerScreen.getIPAddress()));
		
		
	}
	
	
	
	

}