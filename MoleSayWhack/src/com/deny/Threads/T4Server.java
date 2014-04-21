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
import java.security.MessageDigest;
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

public class T4Server {
	
	
	private Socket client;
	private String Password;
	private String clientPassword;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey clientPublicKey;
	public static Key symmetricKey;
	
	public T4Server (Socket client, String myPassword, String clientPassword){
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
		outputStream.reset();
		
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] cipherText = cipher.doFinal(combineText);
		System.out.println("Encrypted myself: " + new String(cipherText, "UTF-8"));
		@SuppressWarnings("restriction")
		BASE64Encoder base64 = new BASE64Encoder();
		@SuppressWarnings("restriction")
		String encryptedValue = base64.encode(cipherText);
		System.out.println("Base64 encoded is " + encryptedValue);
		
		
		
		/*****************6. GENERATING DIGEST**************************/
		
		System.out.println("Creating MD5 digest now");
		outputStream.write(cipherText);
		byte[] toDigest = outputStream.toByteArray();
		outputStream.reset();
		
		MessageDigest MD = MessageDigest.getInstance("MD5");
		MD.update(toDigest);
		
		System.out.println("Digest now is: " + new String(toDigest, "UTF-8"));
		
		
		/************7. RECEIVING DIGEST****************************/
		System.out.println("Receiving digest");
		ObjectInputStream inObj = new ObjectInputStream(client.getInputStream());
		Object incomeDigest = inObj.readObject();
		String incomeDigestString = (String) incomeDigest;
		
		
		/**********8. SENDING DIGEST OVER*********/
		System.out.println("Sending digest over");
		
		@SuppressWarnings("restriction")
		String saltedPassString = base64.encode(toDigest);
		ObjectOutputStream obj = new ObjectOutputStream(client.getOutputStream());
		obj.writeObject(saltedPassString);
		obj.flush();
		
		/***************9. DECODE DIGEST***************************/
		System.out.println("Decode digest");
		@SuppressWarnings("restriction")
		byte[] incomeDigestByte = new BASE64Decoder().decodeBuffer(incomeDigestString);
		
		/************10. RECEIVING ENRYPTED PASSWORD********************/
		System.out.println("Receiving encrypted password");
		Object incomeEncryptedValue = inObj.readObject();
		String incomeEncryptedValueString = (String) incomeEncryptedValue;
		@SuppressWarnings("restriction")
		byte[] incomeEncryptedValueByte = new BASE64Decoder().decodeBuffer(incomeEncryptedValueString);
		
		
		/*************11. DIGESTING PASSWORD AND COMPARING*****************************************/
		System.out.println("Comparing digest and incoming encryptedtext");
		outputStream.write(incomeEncryptedValueByte);
		byte[] toCompareWithDigest = outputStream.toByteArray();
		outputStream.reset();
		MD.update(toCompareWithDigest);
		
		boolean digestAuthenticity = Arrays.equals(toCompareWithDigest, incomeDigestByte);
		
		System.out.println("Status of encryptedtext and Digest received is : " + digestAuthenticity);
		
		/*************12. SENDING ENCRYPTED PASSWORD***********************/
		if(digestAuthenticity){
		System.out.println("Sending encrypted password");
		obj.writeObject(encryptedValue);
		obj.flush();

		
	/***************13. DECRYPT ENCRYPTED PASSWORD***********************************/
	
		System.out.println("Decrypting password");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] incomeDecryptedValueByte = cipher.doFinal(incomeEncryptedValueByte);
		
		
		/******************14. COMPARING PASSWORDS*************************************/
		System.out.println("Comparison begins");
		byte[] clientPass = clientPassword.getBytes("UTF-8");
		outputStream.write(nonce);
		outputStream.write(clientPass);
		
		byte[] toCompare = outputStream.toByteArray();
		outputStream.reset();
		
		boolean compare = Arrays.equals(incomeDecryptedValueByte, toCompare);
		System.out.println("Result of encrypted passwords is : " + compare);

		

		/*********************12. GENERATE SYMMETRIC  KEY, DES ALGORITHM********************************/
		KeyGenerator symKey = KeyGenerator.getInstance("DES");
		SecureRandom r = new SecureRandom();
		symKey.init(56, r);
		symmetricKey = symKey.generateKey();
		
		/**********13. SEND KEY ONLY IF IT IS AUTHENTICATED*******************************/
		
		if (compare){

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
			System.out.println("Password is wrong.");
			return false;
		}
		}
		else{
			System.out.println("Digest and encrypted text doesnot match!");
			return false;
		}
	}
}
