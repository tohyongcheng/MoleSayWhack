package com.deny.Threads;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Base64Coder;
/**
 *Server code of authentication type T3 
 *
 */
public class T3Server {

	private Socket client;
	private String Password;
	private String clientPassword;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey clientPublicKey;
	public static Key symmetricKey;
	
	public T3Server (Socket client, String myPassword, String clientPassword){
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
		
		String encryptedValue = String.valueOf(Base64Coder.encode(cipherText));
		System.out.println("Base64 encoded is " + encryptedValue);
		
		/*******************6. RECEIVE THE FIRST HALF OF ENCRYPTED VALUE***************************/
		ObjectInputStream in = new ObjectInputStream(client.getInputStream());
		Object obj1 = in.readObject();

		/*****************7. SENDING FIRST HALF OF ENCRYPTED VALUE*********************************/
		System.out.println("Sending first half of encrypted value");
		String encryptedValue1 = encryptedValue.substring(0, encryptedValue.length()/2);
		String encryptedValue2 = encryptedValue.substring(encryptedValue.length()/2, encryptedValue.length());
		
		ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
		out.writeObject(encryptedValue1);
		out.flush();

		/******************8. RECEIVE THE SECOND HALF OF ENCRYPTED VALUE****************/
		Object obj2 = in.readObject();


		
		/*****************10. COMBINE AND DECRYPT**********************************/
		System.out.println("Begin decryption");
		String obj = (String) obj1 + (String) obj2;
		
		@SuppressWarnings("restriction")
		byte[] deco = Base64Coder.decodeLines(obj);
		
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] fromClient = cipher.doFinal(deco);
		
		String s = new String(fromClient, "UTF-8");
		System.out.println(s);
		
		/********************11. CONFIRM AUTHENTICITY*************************************/
		byte[] clientPass = clientPassword.getBytes("UTF-8");
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byteout.write(nonce);
		byteout.write(clientPass);
		byte[] toCompare = byteout.toByteArray();
		
		boolean authenticity = Arrays.equals(fromClient, toCompare);
		System.out.println("Authenticity status is : " + authenticity) ;
		
		
		/*********************12. GENERATE SYMMETRIC  KEY, DES ALGORITHM********************************/
		KeyGenerator symKey = KeyGenerator.getInstance("DES");
		SecureRandom r = new SecureRandom();
		symKey.init(56, r);
		symmetricKey = symKey.generateKey();
		
		/**********13. SEND KEY ONLY IF IT IS AUTHENTICATED*******************************/
		
		if (authenticity){
			/******************9. SENDING SECOND HALF OF ENCRYPTED VALUE*********************/
			System.out.println("Sending second half of encrypted value because client is legit.");
			out.writeObject(encryptedValue2);
			out.flush();
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
			out.writeObject("You're not legit!");
			System.out.println("false");
			return false;
		}
	}
}
