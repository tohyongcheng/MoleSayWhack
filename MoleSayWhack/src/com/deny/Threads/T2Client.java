package com.deny.Threads;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;			//Base64 decoding
import sun.misc.BASE64Encoder;


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

public class T2Client {
	private Socket server;
	private String Password;
	private String serverPassword;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey serverPublicKey;
	
	public T2Client(Socket Server, String myPassword, String serverPassword){
		this.server = Server;
		this.Password = myPassword;
		this.serverPassword = serverPassword;
	}
	
	public boolean doAuthentication() throws Exception{
		
		/******************1. GENERATING NONCE***************************/
		byte[] nonce = new byte[16];
		Random rand;
		rand = SecureRandom.getInstance("SHA1PRNG");
		rand.nextBytes(nonce);
		System.out.println("Client nonce : \n" + new String(nonce, "UTF-8"));
		
		/*******************2. GENERATING RSA PRIV-PUB KEY PAIR******************/
		System.out.println("Client is generating RSA key  now");
		KeyPairGenerator RSAKeyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		RSAKeyGen.initialize(1024, random);
		KeyPair pair = RSAKeyGen.generateKeyPair();
		publicKey = pair.getPublic();
		privateKey = pair.getPrivate();
		
		System.out.println("Client finishes generating RSA key");
		
		
		/****************3. SENDING PUBLIC KEY AND NONCE*******************/
		
		System.out.println("Sending public key now.");
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(publicKey.getEncoded().length);
		server.getOutputStream().write(bb.array());
		server.getOutputStream().write(publicKey.getEncoded());
		
		System.out.println("Sending nonce");
		server.getOutputStream().write(nonce);		
		server.getOutputStream().flush();
		
		/**************** 4. RECEIVING PUBLIC KEY AND NONCE ******************/
		System.out.println("Receiving public key and nonce from server.");
		byte[] lenb = new byte[4];
		server.getInputStream().read(lenb, 0, 4);
		ByteBuffer buffer = ByteBuffer.wrap(lenb);
		int len = buffer.getInt();
		
		byte[] clientPubKeybytes = new byte[len];
		server.getInputStream().read(clientPubKeybytes);
		
		X509EncodedKeySpec ks = new X509EncodedKeySpec(clientPubKeybytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		serverPublicKey = kf.generatePublic(ks);
		
		byte[] nonceServer = new byte[16];
		server.getInputStream().read(nonceServer);
		
		System.out.println("Public key and nonce from server received.");
		
		/***************5. Password Encryption************************/
		
		System.out.println("Start encryption of password + nonce");
		//use nonceClient
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] pass = Password.getBytes("UTF-8");
		outputStream.write(nonceServer);
		outputStream.write(pass);
		byte[] combineText = outputStream.toByteArray();
		
		cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		byte[] cipherText = cipher.doFinal(combineText);
		
		@SuppressWarnings("restriction")
		BASE64Encoder base64 = new BASE64Encoder();
		@SuppressWarnings("restriction")
		String encryptedValue = base64.encode(cipherText);
		System.out.println("Base64 encoded is " + encryptedValue);
		
		/*****************6. SENDING FIRST HALF OF ENCRYPTED VALUE*********************************/
		System.out.println("Sending first half of encrypted value");
		String encryptedValue1 = encryptedValue.substring(0, encryptedValue.length()/2);
		String encryptedValue2 = encryptedValue.substring(encryptedValue.length()/2, encryptedValue.length());
		
		ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
		out.writeObject(encryptedValue1);
		out.flush();
		
		/*******************7. RECEIVE THE FIRST HALF OF ENCRYPTED VALUE***************************/
		ObjectInputStream in = new ObjectInputStream(server.getInputStream());
		Object obj1 = in.readObject();
		


		/******************8. SENDING SECOND HALF OF ENCRYPTED VALUE*********************/
		System.out.println("Sending second half of encrypted value");
		out.writeObject(encryptedValue2);
		out.flush();
		
		/******************9. RECEIVE THE SECOND HALF OF ENCRYPTED VALUE****************/
		Object obj2 = in.readObject();
	
		/*****************10. COMBINE AND DECRYPT**********************************/
		System.out.println("Begin decryption");
		String obj = (String) obj1 + (String) obj2;
		@SuppressWarnings("restriction")
		byte[] deco = new BASE64Decoder().decodeBuffer(obj);
		
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] fromServer = cipher.doFinal(deco);
		
		String s = new String(fromServer, "UTF-8");
		System.out.println(s);
		/********************11. CONFIRM AUTHENTICITY*************************************/
		byte[] serverPass = serverPassword.getBytes("UTF-8");
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byteout.write(nonce);
		byteout.write(serverPass);
		byte[] toCompare = byteout.toByteArray();
		if (Arrays.equals(fromServer, toCompare)){

			System.out.println("true");
			return true;
		}
		else{
			System.out.println("false");
			return false;
		}
	}
}
