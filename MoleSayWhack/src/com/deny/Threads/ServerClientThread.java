package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.OptionsScreen.AuthenticationType;
import com.deny.Screens.DisconnectScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Screens.PreGameScreen;
import com.deny.molewhack.WMGame;

/**
 * A thread that write messages to the other player based on inputs
 * and commmands given by the player or
 * current state of the game
 *
 */
public class ServerClientThread extends Thread {
	private WMGame game;
	private String address = "localhost";
	private int port = 5000;
	private ServerSocketHints serverHints;
	private Socket client;
	private ServerSocket server;
	private SocketHints clientHints;
	private SocketHints serverClientHints;
	private boolean isServer = false;
	private boolean isClient = false;
	private MultiplayerScreen multiS;
	private PreGameScreen preGameS;
	private PreGamePowerUpScreen preGamePowerUpScreen;
	private PrintWriter out;
	private String clientPassword = "strawberry";
	private String serverPassword = "grape";
	private ReadThread readThread;
	private Cipher cipher;
	private ObjectOutputStream outObject;
	private Preferences prefs; 

	public static AuthenticationType authType;
	public static boolean authenticityStatus;

	private Key SymmetricKey;
	/**
	 * The constructor of the game, takes in the multiplayer screen which
	 * will initialize and run this thread, as well as the IP address of
	 * the device this player will connect to
	 * @param ms
	 * @param IPAddress
	 */
	public ServerClientThread(MultiplayerScreen ms, String IPAddress) {
		this.setMultiplayerScreen(ms);
		this.game = ms.getGame();

		try {
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Connection lost.");
			goToDisconnectedScreen();
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			System.out.println("Connection lost.");
			goToDisconnectedScreen();
			e.printStackTrace();
		}

		if (IPAddress.equals("")) address = "localhost";
		else address = IPAddress;
		
		prefs = Gdx.app.getPreferences("Options");
		ServerClientThread.authenticityStatus = true;
		ServerClientThread.authType = AuthenticationType.valueOf(prefs.getString("authType","NOPROTOCOL"));
	}

	/**
	 * A method that makes this player a server if there's no current 
	 * server to connect to, or otherwise a client if there's already
	 * an available server out there
	 * 
	 * Also, it does mutual authentication between the client and the server
	 * to make sure that their connection is legitimate and secure
	 */
	public void run() {
		//Create client to check if there is an existing server connection
		clientHints = new SocketHints();
		clientHints.connectTimeout = 1000;
		
		try {
			System.out.println("Trying to find existing server to join...");
			client = Gdx.net.newClientSocket(Protocol.TCP, address, port, clientHints);
			isClient = true;
		} catch (GdxRuntimeException e) {
			System.out.println("There is no current server running! I'm going to be a server now!");
			isClient = false;
			isServer = true;
		}

		if (isServer) {
			serverClientHints = new SocketHints();
			serverClientHints.connectTimeout = 10000;
			while(true) {
				if (isInterrupted()) {
					System.out.println("Is interrupted!");
					dispose();
					return;
				}
				else {
					try {
						serverHints = new ServerSocketHints();
						serverHints.acceptTimeout = 100;	//set to 0 for infinite waiting
						if (server == null)
							server = Gdx.net.newServerSocket(Protocol.TCP, port , serverHints);
						client = server.accept(serverClientHints);

						//stop any more incoming connections after it is connected.
						server.dispose();
						break;
					} catch (GdxRuntimeException e) {
						System.out.println("Error with creating ServerSocket");
					}

				}
			}
		}

		boolean matchedProtocol = false;
		///CHECKING AUTHENTICATION PROTOCOL
		if (isServer){
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String clientProtocol = "";
			try {
				clientProtocol = in.readLine();
				PrintWriter pw = new PrintWriter(client.getOutputStream());
				String myProtocol = authType.toString();
				pw.println(myProtocol);
				pw.flush();


			} catch (IOException e) {
				System.out.println("Connection lost.");
				goToDisconnectedScreen();
				e.printStackTrace();
			}

			clientProtocol = clientProtocol.trim();
			System.out.println("Protocol matching is " + clientProtocol.matches(authType.toString()));
			if (clientProtocol.matches(authType.toString())){
				matchedProtocol = true;
			}
			else{
				matchedProtocol = false;
				multiS.setState(MultiplayerState.PROTOCOLMISMATCH);

			}
		}

		if (isClient){
			PrintWriter pw = new PrintWriter(client.getOutputStream());
			String protocol = authType.toString();
			pw.println(protocol);
			pw.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			String serverProtocol = "";
			try {
				serverProtocol = in.readLine();
			} catch (IOException e) {
				System.out.println("Connection lost.");
				goToDisconnectedScreen();
				e.printStackTrace();
			}

			serverProtocol = serverProtocol.trim();
			System.out.println("Protocol matching is " + serverProtocol.matches(authType.toString()));
			if (serverProtocol.matches(authType.toString())){
				matchedProtocol = true;
			}
			else{
				matchedProtocol = false;
				multiS.setState(MultiplayerState.PROTOCOLMISMATCH);
			}
		}

		//DOING AUTHENTICATION PROTOCOL HERE, ONLY IF PROTOCOL MATCHES, if not, then quit to main menu
		if (matchedProtocol){
			if (authType == AuthenticationType.T2 && isServer){
				T2Server t2 = new T2Server(client, serverPassword, clientPassword);
				try {
					boolean authenticity = t2.doAuthentication();
					System.out.println("The T2 protocol status is: "+ authenticity);
					if (authenticity) {
						System.out.println("Authentication granted.");
					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}

			}

			else if (authType == AuthenticationType.T2 && isClient){
				T2Client t2 = new T2Client(client,clientPassword, serverPassword);
				try {
					boolean authenticity = t2.doAuthentication();
					System.out.println("The T2 protocol status is: "+ authenticity);
					if (authenticity) {
						System.out.println("Authentication granted.");
					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}
			}

			else if (authType == AuthenticationType.T3 && isServer){
				T3Server t3 = new T3Server(client, serverPassword, clientPassword);
				try {
					boolean authenticity = t3.doAuthentication();
					System.out.println("The T3 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T3Server.symmetricKey;
						System.out.println("Authentication granted.");

					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}
			}

			else if (authType == AuthenticationType.T3 && isClient){
				T3Client t3 = new T3Client(client, clientPassword,serverPassword );
				try {
					boolean authenticity = t3.doAuthentication();
					System.out.println("The T3 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T3Client.symmetricKey;
						System.out.println("Authentication granted.");
					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}
			}

			else if (authType == AuthenticationType.T4 && isServer){
				T4Server t4 = new T4Server(client, serverPassword, clientPassword);
				try {
					boolean authenticity = t4.doAuthentication();

					System.out.println("The T4 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T4Server.symmetricKey;
						System.out.println("Authentication granted.");
					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}

			}
			else if (authType == AuthenticationType.T4 && isClient){
				T4Client t4 = new T4Client(client, clientPassword, serverPassword);
				try {
					boolean authenticity = t4.doAuthentication();

					System.out.println("The T4 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T4Client.symmetricKey;
						System.out.println("Authentication granted.");
					}
					else{
						System.out.println("There's intruder!");
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}

			}

			else if (authType == AuthenticationType.T5 && isServer){
				T5Server t5 = new T5Server(client);
				try {
					boolean authenticity = t5.doAuthentication();

					System.out.println("The T5 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T5Server.symmetricKey;
						System.out.println("Authentication granted.");
					}
					else{
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}

			}

			else if (authType == AuthenticationType.T5 && isClient){
				T5Client t5 = new T5Client(client);
				try {
					boolean authenticity = t5.doAuthentication();

					System.out.println("The T5 protocol status is: "+ authenticity);
					if (authenticity) {
						SymmetricKey = T5Client.symmetricKey;
						System.out.println("Authentication granted.");



					}
					else{
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}
			}
			else if (authType == AuthenticationType.TRUDY && isServer){
				TrudyServer tr = new TrudyServer(client);
				try {
					boolean authenticity = tr.doAuthentication();

					System.out.println("The TRUDY protocol status is: "+ authenticity);
					if (authenticity) {
						System.out.println("Authentication granted.");
					}
					else{
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}

			}

			else if (authType == AuthenticationType.TRUDY && isClient){
				TrudyClient td = new TrudyClient(client);
				try {
					boolean authenticity = td.doAuthentication();

					System.out.println("The TRUDY protocol status is: "+ authenticity);
					if (authenticity) {

						System.out.println("Authentication granted.");
					}
					else{
						authenticityStatus = false;
						multiS.setState(MultiplayerState.TRUDY);
					}
				} catch (Exception e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				}
			}

			else if(authType == AuthenticationType.NOPROTOCOL){
				System.out.println("NO AUTHENTICATION REQUIRED");
			}
			
			
			
			if (authenticityStatus){
				try {
					out = new PrintWriter(client.getOutputStream(), true);
					outObject = new ObjectOutputStream(client.getOutputStream());
				} catch (IOException e) {
					System.out.println("Connection lost.");
					goToDisconnectedScreen();
					e.printStackTrace();
				} catch (GdxRuntimeException e) {
					e.printStackTrace();
				}
				
				System.out.println("Connected to other player!");
				this.getMultiplayerScreen().setState(MultiplayerState.CONNECTED);
				readThread = new ReadThread(this,client);
				readThread.start();
			}
		}
	}

	/**
	 * Returns the server socket if this player is  a client
	 * @return server
	 */
	public ServerSocket getServerSocket() {
		return server;
	}
	/**
	 * Returns the client socket if this player is a server
	 * @return
	 */
	public Socket getClientSocket() {
		return client;
	}
	/**
	 * Returns the readThread associated with this write thread
	 * @return readThread
	 */
	public ReadThread getReadThread() {
		return readThread;
	}
	/**
	 * Sets the readThread that will be associated with this write thread
	 * @param readThread
	 */
	public void setReadThread(ReadThread readThread) {
		this.readThread = readThread;
	}
	/**
	 * Dispose this thread after connection is terminated
	 */
	public void dispose() {
		System.out.println("Thread is disposing now");
		if (readThread != null) readThread.interrupt();
		if (server!=null) server.dispose();
		if (client!=null) client.dispose();
	}

	/**
	 * A method that deploys the mole to the other player
	 * @param moleType
	 * @param pos
	 */
	public void deployMole(MoleType moleType, int pos) {
		System.out.println("[SocketHandler] deployed mole! Sending " + "[SPAWN] " + moleType.toString() + " " + pos);
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[SPAWN] " + moleType.toString() + " " + pos+"\n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write(("[SPAWN] " + moleType.toString() + " " + pos+"\n"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that pauses the game
	 */
	public void pauseGame() {
		System.out.println("[SocketHandler] Sending to Pause Game");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[PAUSE] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write(("[PAUSE] \n"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that continues the game
	 */
	public void continueGame() {
		System.out.println("[SocketHandler] Sending to Continue Game");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[CONTINUE] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write(("[CONTINUE] \n"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that allows the player to go to the
	 * choosing of moleDeployer sreen
	 */
	public void toChooseMolesScreen() {
		System.out.println("[SocketHandler] Sending to Change Screen to Choose Moles Screen");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[CHOOSEMOLESCREEN] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		else{
			try {
				out.write("[CHOOSEMOLESCREEN] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	/**
	 * A method that allows the player to 
	 * return to the main menu	 
	 */
	public void toMainMenuScreen() {
		System.out.println("[SocketHandler] Sending to Change Screen to MainMenuScreen");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[MAINMENUSCREEN] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write("[MAINMENUSCREEN] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that is called when game is over
	 */
	public void gameOver() {
		System.out.println("[SocketHandler] Sending GameOver");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[GAMEOVER] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write("[GAMEOVER] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that allows the player to restart game after game is over
	 */
	public void restartGame() {
		System.out.println("[SocketHandler] Sending RestartGame");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[RESTARTGAME] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write("[RESTARTGAME] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that allows the player to exit the game
	 */
	public void exitGame() {
		System.out.println("[SocketHandler] Sending ExitGame");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[EXITGAME] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write("[EXITGAME] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * A method that allows the player to terminate connection
	 * after game is over
	 */
	public void leaveGameRoom() {
		System.out.println("[SocketHandler] Sending to Leave GameRoom");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[LEAVEMULTIPLAYERSCREEN] \n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write("[LEAVEMULTIPLAYERSCREEN] \n");
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
	}
	/**
	 * A method that allows the player to cast power up to the
	 * opponent
	 * @param powerUpType
	 */
	public void sendPowerUp(PowerUpType powerUpType) {
		System.out.println("[SocketHandler] PowerUp Deployed! Sending " + "[SPAWN] " + powerUpType.toString());
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[POWERUP] " + powerUpType.toString()+"\n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
		else{
			try {
				out.write(("[POWERUP] " + powerUpType.toString()+"\n"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	/**
	 * A method that allows the player to update the opponent
	 * about the current HP that this player has
	 * @param hp
	 */
	public void sendHPMessage(int hp) {
		System.out.println("[SocketHandler] Opponent got hit! Sending " + "[OPPONENTHP] " + hp);
		if (authType == AuthenticationType.T3 || authType == AuthenticationType.T4){
			String toWrite ="[OPPONENTHP] " + hp+"\n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);

				@SuppressWarnings("restriction")
				String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
				outObject.writeObject(encryptedValue);
				outObject.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			try {
				out.write(("[OPPONENTHP] " + hp+"\n"));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * get the multiplayerScreen of this thread
	 * @return
	 */
	public MultiplayerScreen getMultiplayerScreen() {
		return multiS;
	}
	/**
	 * Sets the multiplayerScreen of this thread
	 * @param multiS
	 */
	public void setMultiplayerScreen(MultiplayerScreen multiS) {
		this.multiS = multiS;
	}
	/**
	 * Gets the pre game screen of this thread
	 * @return
	 */
	public PreGameScreen getPreGameScreen() {
		return preGameS;
	}
	/**
	 * Sets the pre game screen of this thread
	 * @param preGameS
	 */
	public void setPreGameScreen(PreGameScreen preGameS) {
		this.preGameS = preGameS;
	}
	/**
	 * Get the Game of this thread
	 * @return
	 */

	public WMGame getGame() {
		return game;
	}

	/**
	 * sets the preGamePowerUpScreen to allow the ServerClientThread to have access to it
	 * 
	 * @param preGamePowerUpScreen
	 */
	public void setPreGamePowerUpScreen(PreGamePowerUpScreen preGamePowerUpScreen) {
		this.preGamePowerUpScreen = preGamePowerUpScreen;
	}


	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @return the serverHints
	 */
	public ServerSocketHints getServerHints() {
		return serverHints;
	}
	/**
	 * @return the client
	 */
	public Socket getClient() {
		return client;
	}
	/**
	 * @return the server
	 */
	public ServerSocket getServer() {
		return server;
	}
	/**
	 * @return the clientHints
	 */
	public SocketHints getClientHints() {
		return clientHints;
	}
	/**
	 * @return the serverClientHints
	 */
	public SocketHints getServerClientHints() {
		return serverClientHints;
	}
	/**
	 * @return the isServer
	 */
	public boolean isServer() {
		return isServer;
	}
	/**
	 * @return the isClient
	 */
	public boolean isClient() {
		return isClient;
	}
	/**
	 * @return the multiS
	 */
	public MultiplayerScreen getMultiS() {
		return multiS;
	}
	/**
	 * @return the preGameS
	 */
	public PreGameScreen getPreGameS() {
		return preGameS;
	}
	/**
	 * @return the preGamePowerUpScreen
	 */
	public PreGamePowerUpScreen getPreGamePowerUpScreen() {
		return preGamePowerUpScreen;
	}
	/**
	 * @return the out
	 */
	public PrintWriter getOut() {
		return out;
	}
	/**
	 * @return the clientPassword
	 */
	public String getClientPassword() {
		return clientPassword;
	}
	/**
	 * @return the serverPassword
	 */
	public String getServerPassword() {
		return serverPassword;
	}
	/**
	 * @return the cipher
	 */
	public Cipher getCipher() {
		return cipher;
	}
	/**
	 * @return the outObject
	 */
	public ObjectOutputStream getOutObject() {
		return outObject;
	}
	/**
	 * @return the authType
	 */
	public static AuthenticationType getAuthType() {
		return authType;
	}
	/**
	 * @return the authenticityStatus
	 */
	public static boolean isAuthenticityStatus() {
		return authenticityStatus;
	}
	/**
	 * @return the symmetricKey
	 */
	public Key getSymmetricKey() {
		return SymmetricKey;
	}
	/**
	 * Gets the symmetricKey of this thread
	 * @return
	 */
	public Key getKey(){
		return SymmetricKey;
	}
	/**
	 * A method that allows the player to go to the disconnect screen should
	 * the connection be terminated abruptly
	 */
	public void goToDisconnectedScreen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						game.setScreen(new DisconnectScreen(game));
					}
				});
			}
		}).start();
		this.dispose();
	}
	
}



