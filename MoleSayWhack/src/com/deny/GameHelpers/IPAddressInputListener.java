package com.deny.GameHelpers;

import com.badlogic.gdx.Input.TextInputListener;
import com.deny.Screens.MultiplayerScreen;
/**
 * An input listener that takes in a value from the
 * keyboard and set it to be the IPaddress
 * of the device this player is going to connect to.
 * 
 *
 */
public class IPAddressInputListener implements TextInputListener {

	private MultiplayerScreen multiS;
	/**
	 * Constructor for IPAddressInputListener
	 * @param multiplayerScreen : the screen which calls this method
	 * and set the IP address keyed in by the user
	 */
	public IPAddressInputListener(MultiplayerScreen multiplayerScreen) {
		this.multiS = multiplayerScreen;
	}
	
	@Override
	/**
	 * The method which takes in the text input
	 * and set it to be the IPaddress of the 
	 * other device this player connects to
	 * @param text
	 */
	public void input(String text) {
		
//		multiS.getSocketHandler().leaveGameRoom();
		multiS.setIPAddress(text);
		multiS.restartSocketHandler();
		
	}

	@Override
	/**
	 * default method from inputListener interface
	 */
	public void canceled() {
		return;
	}

}
