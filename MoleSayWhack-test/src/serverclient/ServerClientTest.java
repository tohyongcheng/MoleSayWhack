package serverclient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.Player;
import com.deny.GameObjects.PowerUpType;
import com.deny.GameWorld.GameWorld;
import com.deny.Screens.DisconnectScreen;
import com.deny.Screens.GameScreen;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.OptionsScreen.AuthenticationType;
import com.deny.Threads.ServerClientThread;
import com.deny.molewhack.WMGame;

public class ServerClientTest {
	static WMGame game;
	static LwjglApplication app;
	static MultiplayerScreen multiplayerScreen;
	static MultiplayerScreen otherMultiplayerScreen;
	static GameScreen gameScreen;
	static ServerClientThread serverClientThread;
	static MockServerClientThread mockServerClientThread;
	static Player player;
	static ArrayList<PowerUpType> powerUpTypes;
	static ArrayList<MoleType> moleTypes;
	static GameWorld gameWorld;
	
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
		game.setScreen(multiplayerScreen);
		serverClientThread = multiplayerScreen.getSocketHandler();
		//Give time for serverClientThread to decide it is going to be server
		Thread.sleep(10);
				
		mockServerClientThread = new MockServerClientThread();
		mockServerClientThread.start();
		Thread.sleep(10);
	}

	@Test
	public void testConnected() {
		//Test that it does not use any protocol
		assertEquals(AuthenticationType.NOPROTOCOL,serverClientThread.getAuthType());
		
		//Test that both are connected. one is server and the other is client
		assertTrue(serverClientThread.isServer());
		assertFalse(serverClientThread.isClient());
		
		//Test that it has created sockets and readThreads
		assertNotNull(serverClientThread.getClient());
		assertTrue(serverClientThread.getClient().isConnected());
		
		//Test that ReadThread has been created
		assertNotNull(serverClientThread.getReadThread());
	}
	
	@Test
	public void testDisconnected() throws InterruptedException {
		mockServerClientThread.client.dispose();
		Thread.sleep(10);
		
		assertFalse(serverClientThread.getClient().isConnected());
		assertFalse(serverClientThread.getReadThread().isAlive());
		game.setScreen(new DisconnectScreen(game));
		assertEquals(DisconnectScreen.class, game.getScreen().getClass());
	}
	
	@Test
	public void testThirdClientCannotConnect() {
		MockServerClientThread thirdClient = new MockServerClientThread();
		thirdClient.start();
		assertNull(thirdClient.client);
	}
	
	@Test
	public void testServerSendMethods() throws Exception {
		serverClientThread.deployMole(MoleType.ONETAP, 1);
		
		serverClientThread.deployMole(MoleType.ONETAP, 1);
		Thread.sleep(10);
		assertEquals("[SPAWN] ONETAP 1", mockServerClientThread.currentMessage);
		
		serverClientThread.deployMole(MoleType.MOLEKING, 0);
		Thread.sleep(10);
		assertEquals("[SPAWN] MOLEKING 0", mockServerClientThread.currentMessage);
		
		serverClientThread.pauseGame();
		Thread.sleep(10);
		assertEquals("[PAUSE]", mockServerClientThread.currentMessage);
		
		serverClientThread.continueGame();
		Thread.sleep(10);
		assertEquals("[CONTINUE]", mockServerClientThread.currentMessage);
		
		serverClientThread.toChooseMolesScreen();
		Thread.sleep(10);
		assertEquals("[CHOOSEMOLESCREEN]", mockServerClientThread.currentMessage);
		
		serverClientThread.toMainMenuScreen();
		Thread.sleep(10);
		assertEquals("[MAINMENUSCREEN]", mockServerClientThread.currentMessage);
		
		serverClientThread.exitGame();
		Thread.sleep(10);
		assertEquals("[EXITGAME]", mockServerClientThread.currentMessage);
		
		serverClientThread.gameOver();
		Thread.sleep(10);
		assertEquals("[GAMEOVER]", mockServerClientThread.currentMessage);
		
		serverClientThread.restartGame();
		Thread.sleep(10);
		assertEquals("[RESTARTGAME]", mockServerClientThread.currentMessage);
		
		serverClientThread.leaveGameRoom();
		Thread.sleep(10);
		assertEquals("[LEAVEMULTIPLAYERSCREEN]", mockServerClientThread.currentMessage);
		
		serverClientThread.sendHPMessage(5);
		Thread.sleep(10);
		assertEquals("[OPPONENTHP] 5", mockServerClientThread.currentMessage);
		
		serverClientThread.sendPowerUp(PowerUpType.EARTHQUAKE);
		Thread.sleep(10);
		assertEquals("[POWERUP] EARTHQUAKE", mockServerClientThread.currentMessage);
		
	}
	
	class MockServerClientThread extends Thread {
		Socket client;
		PrintWriter out;
		BufferedReader in;
		String currentMessage;
		public MockServerClientThread() {
			try {
				System.out.println("MockServerClient finding existing server to join...");
				client = Gdx.net.newClientSocket(Protocol.TCP, "localhost", 5000, null);
			} catch (GdxRuntimeException e) {
				
			}
			
			if (client != null && client.isConnected()) {
				System.out.println("MockServerClient connected");
				out = new PrintWriter(client.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String myProtocol = AuthenticationType.NOPROTOCOL.toString();
				out.println(myProtocol);
				out.flush();
				try {
					in.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void run() {
			while(true) {
				try {
					if (in.ready()) {
						currentMessage = in.readLine().trim();
						System.out.println("MockServerClient: "+currentMessage);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (isInterrupted()) {
					System.out.println("MockServerClientThread has stopped");
					try {
						out.close();
						in.close();
						client.dispose();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			
			
			
		}
	}

}
