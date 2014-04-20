package com.deny.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpType;
import com.deny.Screens.MultiplayerScreen;
import com.deny.Screens.MultiplayerScreen.MultiplayerState;
import com.deny.Screens.OptionsScreen.AuthenticationType;
import com.deny.Screens.OptionsScreen;
import com.deny.Screens.PreGamePowerUpScreen;
import com.deny.Screens.PreGameScreen;
import sun.misc.BASE64Decoder;			//Base64 decoding
import sun.misc.BASE64Encoder;

public class ServerClientThread extends Thread {
	private Game game;
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
	
	public static AuthenticationType authType = AuthenticationType.NOPROTOCOL;
	public static boolean authenticityStatus = true;
	
	private Key SymmetricKey;
	
	public Key getKey(){
		return SymmetricKey;
	}
	public ServerClientThread(MultiplayerScreen ms, String IPAddress) {
		this.setMultiplayerScreen(ms);
		this.game = ms.getGame();
		
		try {
			cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (IPAddress.equals("")) address = "localhost";
		else address = IPAddress;
	}
	
	
	public void run() {


		//Create client to check if there is an existing server connection
		clientHints = new SocketHints();
		//Cannot be too long
		clientHints.connectTimeout = 100;
		
		try {
			System.out.println("Trying to find existing server to join...");
			//THIS IS UR SERVER
			client = Gdx.net.newClientSocket(Protocol.TCP, address, port, null);
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
						serverHints.acceptTimeout = 1000;	//set to 0 for infinite waiting
						if (server == null)
							server = Gdx.net.newServerSocket(Protocol.TCP, port , serverHints);
						client = server.accept(serverClientHints);
						
						//stop any more incoming connections after it is connected.
						server.dispose();
						break;
					} catch (GdxRuntimeException e) {
						//System.out.println("Error with creating ServerSocket");
					}
					
				}
			}
		}
		
		if (client.isConnected()) {
			System.out.println("Connected to other player!");
			this.getMultiplayerScreen().setState(MultiplayerState.CONNECTED);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			clientProtocol = clientProtocol.trim();
			System.out.println(clientProtocol.matches(authType.toString()));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			serverProtocol = serverProtocol.trim();
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
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
				
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
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
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		else if (authType == AuthenticationType.T5 && isServer){
			T5Server t5 = new T5Server(client);
			try {
				boolean authenticity = t5.doAuthentication();
		
				System.out.println("The T4 protocol status is: "+ authenticity);
				if (authenticity) {
					SymmetricKey = T5Server.symmetricKey;
					System.out.println("Authentication granted.");
				}
				else{
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		else if (authType == AuthenticationType.T5 && isClient){
			T5Client t5 = new T5Client(client);
			try {
				boolean authenticity = t5.doAuthentication();
			
				System.out.println("The T4 protocol status is: "+ authenticity);
				if (authenticity) {
					SymmetricKey = T5Client.symmetricKey;
					System.out.println("Authentication granted.");
					
			
			
				}
				else{
					authenticityStatus = false;
					multiS.setState(MultiplayerState.QUIT);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else if(authType == AuthenticationType.NOPROTOCOL){
			System.out.println("NO AUTHENTICATION REQUIRED");
		}
	
		
		
		
		try {
			outObject = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		out = new PrintWriter(client.getOutputStream(), true);
		readThread = new ReadThread(this,client);
		readThread.start();
		}
	}
	
	
	public ServerSocket getServerSocket() {
		return server;
	}
	
	public Socket getClientSocket() {
		return client;
	}

	public ReadThread getReadThread() {
		return readThread;
	}

	public void setReadThread(ReadThread readThread) {
		this.readThread = readThread;
	}
	
	public void dispose() {
		if (readThread != null) readThread.interrupt();
		
		if (server!=null) server.dispose();
		if (client!=null) client.dispose();
	}
	
	//ENCRYPT HERE
	public void deployMole(MoleType moleType, int pos) {
		System.out.println("[SocketHandler] deployed mole! Sending " + "[SPAWN] " + moleType.toString() + " " + pos);
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[SPAWN] " + moleType.toString() + " " + pos+"\n";
			
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		
		out.write(("[SPAWN] " + moleType.toString() + " " + pos+"\n"));
		out.flush();
		}
	}
	
	public void pauseGame() {
		System.out.println("[SocketHandler] Sending to Pause Game");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[PAUSE] \n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write(("[PAUSE] \n"));
		out.flush();
		}
	}
	
	public void continueGame() {
		System.out.println("[SocketHandler] Sending to Continue Game");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[CONTINUE] \n";
			
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write(("[CONTINUE] \n"));
		out.flush();
		}
	}
	
	public void toChooseMolesScreen() {
		System.out.println("[SocketHandler] Sending to Change Screen to Choose Moles Screen");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[CHOOSEMOLESCREEN] \n";
			
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[CHOOSEMOLESCREEN] \n");
		out.flush();}
	}
	
	public void toMainMenuScreen() {
		System.out.println("[SocketHandler] Sending to Change Screen to MainMenuScreen");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[MAINMENUSCREEN] \n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[MAINMENUSCREEN] \n");
		out.flush();
		}
	}
	
	public void gameOver() {
		System.out.println("[SocketHandler] Sending GameOver");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[GAMEOVER] \n";
			
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[GAMEOVER] \n");
		out.flush();
		}
	}

	public void restartGame() {
		System.out.println("[SocketHandler] Sending RestartGame");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite = "[RESTARTGAME] \n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[RESTARTGAME] \n");
		out.flush();}
	}

	public void exitGame() {
		System.out.println("[SocketHandler] Sending ExitGame");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[EXITGAME] \n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[EXITGAME] \n");
		out.flush();}
	}

	public void leaveGameRoom() {
		System.out.println("[SocketHandler] Sending to Leave GameRoom");
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[LEAVEMULTIPLAYERSCREEN] \n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write("[LEAVEMULTIPLAYERSCREEN] \n");
		out.flush();}
	}
	
	public void sendPowerUp(PowerUpType powerUpType) {
		System.out.println("[SocketHandler] PowerUp Deployed! Sending " + "[SPAWN] " + powerUpType.toString());
		if (authType == AuthenticationType.T3|| authType == AuthenticationType.T4){
			String toWrite ="[POWERUP] " + powerUpType.toString()+"\n";

			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write(("[POWERUP] " + powerUpType.toString()+"\n"));
		out.flush();}
	}
	
	public void sendHPMessage(int hp) {
		System.out.println("[SocketHandler] Opponent got hit! Sending " + "[OPPONENTHP] " + hp);
		if (authType == AuthenticationType.T3 || authType == AuthenticationType.T4){
			String toWrite ="[OPPONENTHP] " + hp+"\n";
		
			try {
				byte[] toWriteByte = toWrite.getBytes("UTF-8");
				cipher.init(Cipher.ENCRYPT_MODE, SymmetricKey);
				byte[] cipherText = cipher.doFinal(toWriteByte);
				@SuppressWarnings("restriction")
				BASE64Encoder encode = new BASE64Encoder();
				
				@SuppressWarnings("restriction")
				String encryptedValue = encode.encode(cipherText);
				outObject.writeObject(encryptedValue);
				outObject.flush();
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		else{
		out.write(("[OPPONENTHP] " + hp+"\n"));
		out.flush();
		}
	}

	public MultiplayerScreen getMultiplayerScreen() {
		return multiS;
	}

	public void setMultiplayerScreen(MultiplayerScreen multiS) {
		this.multiS = multiS;
	}

	public PreGameScreen getPreGameScreen() {
		return preGameS;
	}

	public void setPreGameScreen(PreGameScreen preGameS) {
		this.preGameS = preGameS;
	}
	
	public Game getGame() {
		return game;
	}

	public void setPreGamePowerUpScreen(PreGamePowerUpScreen preGamePowerUpScreen) {
		this.preGamePowerUpScreen = preGamePowerUpScreen;
	}

}



