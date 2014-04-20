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
import sun.misc.BASE64Decoder;			//Base64 decoding
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

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

public class T4HmacServer {

	private Socket client;
	private String Password;
	private String clientPassword;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey clientPublicKey;
	public static Key symmetricKey;
	
	public T4HmacServer (Socket client, String myPassword, String clientPassword){
		this.client = client;
		this.Password  = myPassword;
		this.clientPassword = clientPassword;
	}
	
	public  boolean doAuthentication() throws Exception{
		
		/******************1. GENERATING NONCE***************************/
		byte[] nonce = new byte[16];
		
		Random rand;
		rand = SecureRandom.getInstance("SHA1PRNG");
		rand.nextBytes(nonce);
		System.out.println("Server nonce : \n" + new String(nonce, "UTF-8"));
		
		/*******************2. GENERATING RSA PRIV-PUB KEY PAIR******************/
		System.out.println("Server is generating RSA key  now");
		KeyPairGenerator RSAKeyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = new SecureRandom();
		RSAKeyGen.initialize(1024, random);
		KeyPair pair = RSAKeyGen.generateKeyPair();
		publicKey = pair.getPublic();
		privateKey = pair.getPrivate();
		
		System.out.println("Server finishes generating RSA key");
		
		/**************** 3. RECEIVING PUBLIC KEY AND NONCE ******************/
		System.out.println("Receiving public key and nonce from client.");
		byte[] lenb = new byte[4];
		client.getInputStream().read(lenb, 0, 4);
		ByteBuffer buffer = ByteBuffer.wrap(lenb);
		int len = buffer.getInt();
		
		byte[] clientPubKeybytes = new byte[len];
		client.getInputStream().read(clientPubKeybytes);
		
		X509EncodedKeySpec ks = new X509EncodedKeySpec(clientPubKeybytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		clientPublicKey = kf.generatePublic(ks);
		
		byte[] nonceClient = new byte[16];
		client.getInputStream().read(nonceClient);
		
		System.out.println("Public key and nonce from client received.");
		
		
		/****************4. SENDING PUBLIC KEY AND NONCE*******************/
		
		System.out.println("Sending public key now.");
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(publicKey.getEncoded().length);
		client.getOutputStream().write(bb.array());
		client.getOutputStream().write(publicKey.getEncoded());
		
		System.out.println("Sending nonce");
		client.getOutputStream().write(nonce);		
		client.getOutputStream().flush();
		
		
		/***************5. Password Encryption************************/
		
		System.out.println("Start encryption of password + nonce");
		//use nonceClient
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] pass = Password.getBytes("UTF-8");
		outputStream.write(nonceClient);
		outputStream.write(pass);
		byte[] combineText = outputStream.toByteArray();
		
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] cipherText = cipher.doFinal(combineText);
		System.out.println("Encrypted myself: " + new String(cipherText, "UTF-8"));
		@SuppressWarnings("restriction")
		BASE64Encoder base64 = new BASE64Encoder();
		@SuppressWarnings("restriction")
		String encryptedValue = base64.encode(cipherText);
		System.out.println("Base64 encoded is " + encryptedValue);
		
		
		/*******************6. RECEIVE THE MAC KEY ***************************/
		System.out.println("Receiving secret key length now");
		byte[] encryptedKeyLength = new byte[4];
		client.getInputStream().read(encryptedKeyLength, 0 ,4);
		ByteBuffer symBBenc = ByteBuffer.wrap(encryptedKeyLength);
		int lengthEncrypted = symBBenc.getInt();
		
		System.out.println("Receiving encrypted secret key now");
		byte[] encryptedSymkey = new  byte[lengthEncrypted];
		client.getInputStream().read(encryptedSymkey);
		
		/*************7. Decrypt the key****************/
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] symKeyBytes = cipher.doFinal(encryptedSymkey);
		SecretKeySpec EKS = new SecretKeySpec(symKeyBytes, "HmacMD5");
		
		
		System.out.println("Received secret key: " + new String(EKS.getEncoded(),"UTF-8"));
		
		/**********8. GET MAC FROM CLIENT*******************/
		System.out.println("Getting mac now");
		ObjectInputStream inObject = new ObjectInputStream(client.getInputStream());
		Object macInput = inObject.readObject();
		String macIn = (String) macInput;
		
		
		/*********************9. SEND MAC TO CLIENT*********************************/
		System.out.println("Sending mac now");
		byte[] EV = combineText;
		Mac mac = Mac.getInstance("HmacMD5");
		mac.init(EKS);
		mac.update(EV);
		@SuppressWarnings("restriction")
		String macOut = base64.encode(EV);
		
		ObjectOutputStream out = new ObjectOutputStream (client.getOutputStream());
		out.writeObject(macOut);
		out.flush();
		
		/******************10. GET ENCRYPTED VALUE FROM CLIENT*******************************/
		System.out.println("Sending encrypted value now");
		Object encryptedObject = inObject.readObject();
		String ES = (String) encryptedObject;
		
		/*************11. SEND ENCRYPTED VALUE TO CLIENT*************/
		System.out.println("Getting encrypted value now");
		out.writeObject(encryptedValue);
		out.flush();
	
		/*********13. VERIFYING ENCRYPTED VALUE*****************************/
		@SuppressWarnings("restriction")
		byte[] deco = new BASE64Decoder().decodeBuffer(ES);
		
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] fromServer = cipher.doFinal(deco);
		
		
		byte[] serverPass = clientPassword.getBytes();
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byteout.write(nonce);
		byteout.write(serverPass);
		byte[] toCompare = byteout.toByteArray();
		
		System.out.println("ENCRYPTED VALUE STATUS: " + Arrays.equals(fromServer, toCompare));
		
		/********12. VERIFYING MAC*************/
		
		byte[] EV2 = fromServer;
		mac.init(EKS);
		mac.update(EV2);
		@SuppressWarnings("restriction")
		String myMac = base64.encode(EV2);
		
		System.out.println("myMac is " + myMac + "inMac is " + macIn);
		boolean macBool = myMac.matches(macIn);
		
		boolean authenticity = (macBool == Arrays.equals(fromServer, toCompare));
		System.out.println("Authenticity status is : " + authenticity) ;
		
		
		/*********************12. GENERATE SYMMETRIC  KEY, DES ALGORITHM********************************/
		KeyGenerator symKey = KeyGenerator.getInstance("DES");
		SecureRandom r = new SecureRandom();
		symKey.init(56, r);
		symmetricKey = symKey.generateKey();
		
		/**********13. SEND KEY ONLY IF IT IS AUTHENTICATED*******************************/
		
		if (authenticity){

			/************** 14. ENCRYPT KEY******************/
			System.out.println("Encrypting symmetric Key using clientPublicKey");
			byte[] symK = symmetricKey.getEncoded();
			cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
			byte[] encryptedSymK = cipher.doFinal(symK);
	
			System.out.println("Sending encrypted symmetric key length now.");
			ByteBuffer symKeyEnc = ByteBuffer.allocate(4);
			symKeyEnc.putInt(encryptedSymK.length);
			client.getOutputStream().write(symKeyEnc.array());
			
			System.out.println("Sending encrypted symmetric key now.");
			client.getOutputStream().write(encryptedSymK);
			client.getOutputStream().flush();
			System.out.println("Symmetric Key sent");
			
			System.out.println(new String(symmetricKey.getEncoded(),"UTF-8"));
			return true;

		}
		else{
			System.out.println("false");
			return false;
		}
	}
}
