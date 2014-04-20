package com.deny.Threads;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.security.Key;
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
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
/**
 * In this case, we send the MAC secretKey.
 * But we assume that both have already shared a known secretKey a priori
 * @author Crimsonlycans
 *
 */
public class T4HmacClient {
	private Socket server;
	private String Password;
	private String serverPassword;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey serverPublicKey;
	public static Key symmetricKey;
	
	
	
	public T4HmacClient(Socket Server, String myPassword, String serverPassword){
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
		System.out.println("Encrypted myself: " + new String(cipherText, "UTF-8"));
		
		@SuppressWarnings("restriction")
		BASE64Encoder base64 = new BASE64Encoder();
		@SuppressWarnings("restriction")
		String encryptedValue = base64.encode(cipherText);
		System.out.println("Base64 encoded is " + encryptedValue);
		
		/*****************6. GENERATING MAC SECRETKEY *********************************/
		
		
		//DOING MAC digest
		//USE server's public key
		

		KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
		SecretKey MD5key = keyGen.generateKey();
		
		System.out.println("Sending secret key: " + new String(MD5key.getEncoded(),"UTF-8"));
		
		/*****7. ENCRYPT AND SEND SECRET KEY TO SERVER****/
		
		System.out.println("Encrypting symmetric Key using clientPublicKey");
		byte[] symK = MD5key.getEncoded();
		cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		byte[] encryptedSymK = cipher.doFinal(symK);
		
		System.out.println("Sending encrypted secret key length now.");
		ByteBuffer symKeyEnc = ByteBuffer.allocate(4);
		symKeyEnc.putInt(encryptedSymK.length);
		server.getOutputStream().write(symKeyEnc.array());
		
		System.out.println("Sending encrypted secret key now.");
		
		server.getOutputStream().write(encryptedSymK);
		server.getOutputStream().flush();
		System.out.println("secret Key sent");
		
		/**************8. SENDING MAC OF THE VALUE*********************/
		System.out.println("Sending mac now");
		byte[] EV = combineText;
		Mac mac = Mac.getInstance("HmacMD5");
		mac.init(MD5key);
		mac.update(EV);
		@SuppressWarnings("restriction")
		String macTOSEND = base64.encode(EV);
		
		ObjectOutputStream out = new ObjectOutputStream (server.getOutputStream());
		out.writeObject(macTOSEND);
		out.flush();
		
		/*****************9. GET MAC FROM SERVER**************************/
		System.out.println("Getting mac now");
		ObjectInputStream inObject = new ObjectInputStream(server.getInputStream());
		Object macInput = inObject.readObject();
		String macIn = (String) macInput;
		
		/********************10. SEND ENCRYPTEDVALUE TO SERVER*****************************/
		System.out.println("Sending encrypted value now");
		out.writeObject(encryptedValue);
		out.flush();
		
		/**************11. GET ENCRYPTEDVALUE FROM SERVER******************************/
		System.out.println("Getting encrypted value now");
		Object encryptedObject = inObject.readObject();
		String fromServerstring = (String) encryptedObject;
		
		
		/*************13. VERIFYING ENCRYPTED VALUE***************************/
		
		@SuppressWarnings("restriction")
		byte[] deco = new BASE64Decoder().decodeBuffer(fromServerstring);
		
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] fromClient = cipher.doFinal(deco);
		
		byte[] serverPass = serverPassword.getBytes();
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byteout.write(nonce);
		byteout.write(serverPass);
		byte[] toCompare = byteout.toByteArray();
		
		System.out.println("ENCRYPTED VALUE STATUS: " +Arrays.equals(fromClient, toCompare));
		/***********12. Verifying mac*********/
		

		byte[] EV2 = fromClient;
		mac.init(MD5key);
		mac.update(EV2);
		@SuppressWarnings("restriction")
		String myMac = base64.encode(EV2);
		
		System.out.println("myMac is " + myMac + "inMac is " + macIn);
		boolean macBool = myMac.matches(macIn);
		
		boolean authenticity = (macBool == Arrays.equals(fromClient, toCompare));
		System.out.println("Authenticity status is : " + authenticity) ;
		
		
		
		
		/******************14. ACCEPT KEY ONLY IF ITS AUTHENTIC************************************/
		if (authenticity){

			System.out.println("Receiving encrypted key length now");
			byte[] encryptedKeyLength = new byte[4];
			server.getInputStream().read(encryptedKeyLength, 0 ,4);
			ByteBuffer symBBenc = ByteBuffer.wrap(encryptedKeyLength);
			int lengthEncrypted = symBBenc.getInt();
			
			System.out.println("Receiving encrypted key now");
			byte[] encryptedSymkey = new  byte[lengthEncrypted];
			server.getInputStream().read(encryptedSymkey);
			
			/*************Decrypt the key****************/
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] symKeyBytes = cipher.doFinal(encryptedSymkey);
			DESKeySpec EKS = new DESKeySpec(symKeyBytes);
			SecretKeyFactory KF = SecretKeyFactory.getInstance("DES");
			symmetricKey = KF.generateSecret(EKS);
			
			System.out.println(new String(symmetricKey.getEncoded(),"UTF-8"));
			
			
			
			return true;
		}
		else{
			System.out.println("false");
			return false;
		}
	}
}
