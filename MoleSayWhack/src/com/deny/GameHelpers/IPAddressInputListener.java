package com.deny.GameHelpers;

import com.badlogic.gdx.Input.TextInputListener;
import com.deny.Screens.MultiplayerScreen;

public class IPAddressInputListener implements TextInputListener {

	private MultiplayerScreen multiS;
	
	public IPAddressInputListener(MultiplayerScreen multiplayerScreen) {
		this.multiS = multiplayerScreen;
	}
	
	@Override
	public void input(String text) {
		multiS.getSocketHandler().leaveGameRoom();
		multiS.setIPAddress(text);
		multiS.restartSocketHandler();
		
	}

	@Override
	public void canceled() {
		
	}

}
