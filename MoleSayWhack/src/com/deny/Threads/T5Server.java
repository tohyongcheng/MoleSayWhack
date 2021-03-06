package com.deny.Threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.Base64Coder;
/**
 *Server code of authentication type T5
 *
 */
public class T5Server {

	private Socket client;
	public static PublicKey publicKey;
	public static PrivateKey privateKey;
	public static PublicKey clientPublicKey;
	public static Key symmetricKey;
	
	private String ClientPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDAOm4BywXWQ6i3SN2oOZbukxABDwnsEBn2ppSD"+
			"TBSh4EFm0Mj/3jB0xqZqNJwHlphRKWD1ag0+p+j+W4yXjaFd8QmGRCMmSZAii4b+d8S76AeHfX2a"+
			"BWN33MqvL8yTXpoptS/hvdOdYzcfMINSOTGfjULsuLXNR1ckZE2Qwx8PAwIDAQAB";
	
	private String myPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEkVhGitYdTOOLaAM8I38V425CZIRYLOrcRudv"+
			"h3Ho7a/NttTLuXJShqux+A47ZkNSf/FpmRsRYkoiVf9yi+6RsEyDTZif3k1EJ2I8ADNKBvhfoV7/"+
			"2q1HqbCh9nkUiOv6ELV0S1deFW+hS+NkHJtvs+ILbbfUW/kcaDdF11dx8wIDAQAB";
	
	private String myPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAISRWEaK1h1M44toAzwjfxXjbkJk"+
			"hFgs6txG52+Hcejtr8221Mu5clKGq7H4DjtmQ1J/8WmZGxFiSiJV/3KL7pGwTINNmJ/eTUQnYjwA"+
			"M0oG+F+hXv/arUepsKH2eRSI6/oQtXRLV14Vb6FL42Qcm2+z4gttt9Rb+RxoN0XXV3HzAgMBAAEC"+
			"gYBG8Py7a0qfjWjrTjME+L3mebmkX+QOx6K7VFo/bc0AaEm/HUsM5mWOUjEJYQREtznpqTwIVX2N"+
			"tPqVG05C/y0gFlthMdAF/hvSRP0zK1yCv8XJsGcfLy0cgT2RTMe/cAZoV80d8DL1Sco9xl2OaMod"+
			"VLBQsqW3K5yY2QXMlUoGuQJBAL748zFk9RR3umC4ZQzpxKDqHzYPWsUDoD4Bku/WaY6dIkdyKee1"+
			"kzmEboZEBOPH/dK7Ai+wgcgQPUAfENSa8f0CQQCxtUMt95gHHVckFCfH2a9anuqn/jjh3Qttn/0W"+
			"rAnwNsQ8lX3tsfFOp8a2nqTGs7wq8C195WGenqPUSNbNj/6vAkEApwXIfLzVdC86vHjJOEAEDtB0"+
			"t606L/D55wEtZ8HY1Dgt0NrSN6sgtj9TAu7eb/Fr1zBjux3ehAFf8IK7D5bsaQJAUuMg+ofJuUfO"+
			"JV3hUC4QxEvrV4AUOAVKF0/QVIrZ99kTns57dirRSsamwPcuFCAxXDW6gkZDSiDkeVegGTFYsQJA"+
			"D/HCw+lUo22lOxndLfXzaJQziv2HuZ/NYaP0pMt3Teuv2pSSPmsuRP+pgqYyofxJaDgsMEWELEvi"+
			"OiOEZ6EqeA==";
	
	public T5Server (Socket client){
		this.client = client;

	}
	
	public  boolean doAuthentication() throws Exception{
		
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
		
		System.out.println("Constructing client's public key");
		byte[] serverReconstructed = Base64Coder.decodeLines(ClientPublicKey);
		X509EncodedKeySpec XksS = new X509EncodedKeySpec(serverReconstructed);
		clientPublicKey = kf.generatePublic(XksS);
		
		System.out.println("All key reconstruction done");
		
		/*******************2. Receiving Hello from client and signature**********************************************/
		System.out.println("Receiving message and signature");
		ObjectInputStream inObj = new ObjectInputStream(client.getInputStream());
		Object m = inObj.readObject();
		Object ms = inObj.readObject();
		String messageFromClient = (String) m;
		String clientSignature = (String) ms;
		
		/****************3. Decoding signature and Encrypting it************************/
		System.out.println("Verifying authenticity");
		@SuppressWarnings("restriction")
		byte[] clientSignatureBytes = Base64Coder.decodeLines(clientSignature);
		cipher.init(Cipher.DECRYPT_MODE, clientPublicKey);
		byte[] clientSignatureBytesDecrypted = cipher.doFinal(clientSignatureBytes);
		
		byte[] messageFromClientBytes = messageFromClient.getBytes("UTF-8");
		
		boolean authenticity = Arrays.equals(clientSignatureBytesDecrypted, messageFromClientBytes);
		System.out.println("Signature verification is: " + authenticity);
		
		

			/**********REPLY ONLY IF CLIENT IS LEGITIMATE*******************************/
		
		if (authenticity){
	
			/********************4. Sending Hello Message to client with signed encryption************************************/
			System.out.println("Sending reply Hello message");
			String message = "Hello client";
			ObjectOutputStream obj = new ObjectOutputStream(client.getOutputStream());
			obj.writeObject(message);
			obj.flush();
			
			System.out.println("Sending reply Signature");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] messageByte = message.getBytes("UTF-8");
			byte[] signedMessage = cipher.doFinal(messageByte);
			
			@SuppressWarnings("restriction")
			String signedMesageString = String.valueOf(Base64Coder.encode(signedMessage));
			obj.writeObject(signedMesageString);
			obj.flush();
			
			/*********************5. GENERATE SYMMETRIC  KEY, DES ALGORITHM********************************/
			KeyGenerator symKey = KeyGenerator.getInstance("DES");
			SecureRandom r = new SecureRandom();
			symKey.init(56, r);
			symmetricKey = symKey.generateKey();
			
			
			System.out.println("Encrypting symmetric Key using clientPublicKey");
			byte[] symK = symmetricKey.getEncoded();
			cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
			byte[] encryptedSymK = cipher.doFinal(symK);
			
		
			/********************6. SENDING SYMMETRIC KEY**************************/
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
			System.out.println("Client does not appear to be legit.");
			return false;
		}
	}
}
