package com.deny.GameHelpers;

import com.badlogic.gdx.Input.TextInputListener;
import com.deny.Screens.OptionsScreen;
/**
 * A class which sets the name of the player
 * from the keyboard input keyed in by the user.
 * 
 *
 */
public class NameInputListener implements TextInputListener {

	private OptionsScreen optionsS;
	/**
	 * The constructor of this class
	 * @param optionsScreen, the class that instantiate
	 * this class to take in input from the keyboard
	 */
	public NameInputListener(OptionsScreen optionsScreen) {
		this.optionsS = optionsScreen;
	}
	
	@Override
	/**
	 * The method that takes in the string from the keyboard 
	 * and set it to the name of the player
	 * @param text
	 */
	public void input(String text) {
		optionsS.setName(text);
		
	}
	/**
	 * default method from inputListener interface
	 */
	@Override
	public void canceled() {
		return;
	}


}
