package com.deny.Threads;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Base64Coder;
/**
 * Client code of authentication type T5
 *
 */
public class T5Client {
	private Socket server;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey serverPublicKey;
	public static Key symmetricKey;
	
	private String ServerPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEkVhGitYdTOOLaAM8I38V425CZIRYLOrcRudv"+
			"h3Ho7a/NttTLuXJShqux+A47ZkNSf/FpmRsRYkoiVf9yi+6RsEyDTZif3k1EJ2I8ADNKBvhfoV7/"+
			"2q1HqbCh9nkUiOv6ELV0S1deFW+hS+NkHJtvs+ILbbfUW/kcaDdF11dx8wIDAQAB";
	
	private String myPublicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAOm4BywXWQ6i3SN2oOZbukxABDwnsEBn2ppSD"+
			"TBSh4EFm0Mj/3jB0xqZqNJwHlphRKWD1ag0+p+j+W4yXjaFd8QmGRCMmSZAii4b+d8S76AeHfX2a"+
			"BWN33MqvL8yTXpoptS/hvdOdYzcfMINSOTGfjULsuLXNR1ckZE2Qwx8PAwIDAQAB";		
			
	private String myPrivateKey	= "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMA6bgHLBdZDqLdI3ag5lu6TEAEP"+
			"CewQGfamlINMFKHgQWbQyP/eMHTGpmo0nAeWmFEpYPVqDT6n6P5bjJeNoV3xCYZEIyZJkCKLhv53"+
			"xLvoB4d9fZoFY3fcyq8vzJNemim1L+G9051jNx8wg1I5MZ+NQuy4tc1HVyRkTZDDHw8DAgMBAAEC"+
			"gYBlh7cI3WnU1OGtBSCGNPzCQAzI8l+EPrDn9O4mNy4CngcNPiOb+YhQdYf6OpVa8LC81YUvuncK"+
			"a/eFOWeVuKnMntr36OVBZk1aaGgIXc1R5XIeIld6jRBF4C1CEZxl9xC+gAX6vEiMsuX8xgUvur5l"+
			"GHB40datfm16XVXrMEXmwQJBAP8p5SGc61PxSLE9wzMXnkdkrSF78tIGgynP7+edaR217R9Mis9r"+
			"dKfAT6TTHe9PBsqHnj9czH6naA22+YT9QiMCQQDA27ntEUQgPGrNMy3tgZRwrOJyY3YTJiWxISxR"+
			"G3k+LA8U9NMcB0imBsbRmXyYpdP04nq0fKXHovFZ/tumo52hAkAQOVK3U2nnGXVcx6eMrRjEQ1N6"+
			"yI3E6uMWGoGPgYO4fuPH1K8SxytQqzqlKUNC/wxYfk0CjFz4RJ6qGJM+8HdvAkAEgt9FgVrjmiYx"+
			"aJoRbS+ItzKQ1GzFo+XLz+fWedsLLkKoDdYGWNFFOPFbo8h7VZ2wo7+VCzlhgxk8Lq76pechAkEA"+
			"3dXPLKIF2MWnHwQUEGuJFAQadbmmohyaIihfk6gQ1RlsNMtnmG27QqdP3HOpAd8oB+U9dgIM3zi1"+
			"FRGFv9EoUg==";	
			
	public T5Client(Socket Server){
		this.server = Server;

	}
	
	public boolean doAuthentication() throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		

		/*********************1. CONSTRUCTING ALL THE KEYS******************************************/
		KeyFactory kf = KeyFactory.getInstance("RSA");
		
		System.out.println("Constructing my private key");
		byte[] privReconstructed = Base64Coder.decodeLines(myPrivateKey);
		PKCS8EncodedKeySpec  Pks = new PKCS8EncodedKeySpec (privReconstructed);
		privateKey = kf.generatePrivate(Pks);
		
		System.out.println("Constructing my public key");
		byte[] publicReconstructed = Base64Coder.decodeLines(myPublicKey);
		X509EncodedKeySpec Xks = new X509EncodedKeySpec (publicReconstructed);
		publicKey = kf.generatePublic(Xks);
		
		System.out.println("Constructing server's public key");
		byte[] serverReconstructed = Base64Coder.decodeLines(ServerPublicKey);
		X509EncodedKeySpec XksS = new X509EncodedKeySpec(serverReconstructed);
		serverPublicKey = kf.generatePublic(XksS);
		
		System.out.println("All key reconstruction done");
		
		/********************2. Sending Hello Message to server with signed encryption************************************/
		System.out.println("Sending Hello message");
		String message = "Hello server";
		ObjectOutputStream obj = new ObjectOutputStream(server.getOutputStream());
		obj.writeObject(message);
		obj.flush();
		
		System.out.println("Sending Signature");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] messageByte = message.getBytes("UTF-8");
		byte[] signedMessage = cipher.doFinal(messageByte);
		
		@SuppressWarnings("restriction")
		String signedMesageString = String.valueOf(Base64Coder.encode(signedMessage));
		obj.writeObject(signedMesageString);
		obj.flush();
		
		/*******************3. Receiving Hello from server and signature**********************************************/
		System.out.println("Receiving reply and signature");
		ObjectInputStream inObj = new ObjectInputStream(server.getInputStream());
		Object m = inObj.readObject();
		Object ms = inObj.readObject();
		String messageFromServer = (String) m;
		String serverSignature = (String) ms;
		
		/****************4. Decoding signature and Encrypting it************************/
		System.out.println("Verifying authenticity");
		@SuppressWarnings("restriction")
		byte[] serverSignatureBytes = Base64Coder.decodeLines(serverSignature);
		cipher.init(Cipher.DECRYPT_MODE, serverPublicKey);
		byte[] serverSignatureBytesDecrypted = cipher.doFinal(serverSignatureBytes);
		
		byte[] messageFromServerBytes = messageFromServer.getBytes("UTF-8");
		
		boolean authenticity = Arrays.equals(serverSignatureBytesDecrypted, messageFromServerBytes);
		System.out.println("Signature verification is: " + authenticity);
		
		/******************5. ACCEPT KEY ONLY IF ITS AUTHENTIC************************************/
		if (authenticity){
			System.out.println("Receiving encrypted key length now");
			byte[] encryptedKeyLength = new byte[4];
			server.getInputStream().read(encryptedKeyLength, 0 ,4);
			ByteBuffer symBBenc = ByteBuffer.wrap(encryptedKeyLength);
			int lengthEncrypted = symBBenc.getInt();
			
			System.out.println("Receiving encrypted key now");
			byte[] encryptedSymkey = new  byte[lengthEncrypted];
			server.getInputStream().read(encryptedSymkey);
			
			/*************6. Decrypt the key****************/
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
