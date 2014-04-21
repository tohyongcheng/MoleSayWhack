package com.deny.GameHelpers;

import com.badlogic.gdx.Input.TextInputListener;
import com.deny.Screens.OptionsScreen;

public class NameInputListener implements TextInputListener {

	private OptionsScreen optionsS;
	
	public NameInputListener(OptionsScreen optionsScreen) {
		this.optionsS = optionsScreen;
	}
	
	@Override
	public void input(String text) {
		optionsS.setName(text);
		
	}

	@Override
	public void canceled() {
		return;
	}


}
