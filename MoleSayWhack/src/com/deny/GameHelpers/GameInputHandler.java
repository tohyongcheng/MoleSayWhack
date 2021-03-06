package com.deny.GameHelpers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;
/**
 * 
 * Handles all the touch screen/click input when playing the game
 *
 */
public class GameInputHandler implements InputProcessor {
	public static boolean moleTouched;
	public static Mole moleT;
	private GameWorld myWorld;
	private ArrayList<Rectangle> placeHolders;
	private Preferences prefs;
	private boolean enableSFX;
/**
 * Constructor for GameInputHandler.
 * @param myWorld accepts gameWorld object which is the game screen
 */
	public GameInputHandler(GameWorld myWorld) {
		this.myWorld = myWorld;
		placeHolders = myWorld.getPlaceHolders();
	
		//Get options
		prefs = Gdx.app.getPreferences("Options");
		enableSFX = prefs.getBoolean("enableSFX", true);
	}


	
	@Override
	/**
	 * A method which contains all the logic when
	 * there's a touch detected on the screen when game 
	 * is  running, depending on the state of the game.
	 * @param screenX : x coordinate of the screen
	 * @param screenY : y coordinate of the screen
	 * @param pointer
	 * @param button
	 * 
	 */
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		moleTouched = false;
		//HAMMER DISSAPEARS WHEN U TOUCH SOMETHING ELSE

		GameState currentState = myWorld.getGameState();

		if (currentState == GameState.DEPLOYMENT) {
			for (int i=0; i<placeHolders.size(); i++) { 
				if (placeHolders.get(i).contains(screenX, screenY)) {

					if (enableSFX) AssetLoader.sent.play();
					myWorld.getCurrentMoleDeployer().deployMole(i);
					myWorld.setCurrentMoleDeployer(null);
					myWorld.setGameState(GameState.READY);
				}
			}

			for (MoleDeployer md: myWorld.getMoleDeployers()) {
				if (md!= null) {
					if (md.isTouchDown(screenX,screenY)) {
						myWorld.setGameState(GameState.READY);
						myWorld.setCurrentMoleDeployer(null);
					}
				}
			}

			if (myWorld.getPauseButton().contains(screenX,screenY)) {
				if (enableSFX) AssetLoader.button.play();
				myWorld.getSocketHandler().exitGame();
				myWorld.setGameState(GameState.MENU);
			}

			for (PowerUpDeployer pd: myWorld.getPowerUpDeployers()) {
				if (pd!= null) {
					if (pd.isTouchDown(screenX,screenY) && pd.isAvailable()) {
						pd.deploy();
						myWorld.setGameState(GameState.RUNNING);
					}
				}
			}

		} 

		else if (currentState == GameState.RUNNING ){

			for(Mole m: myWorld.getMoleGrid()) {
				if (m!=null) {
					moleT = m;
					moleTouched = m.isTouchDown(screenX, screenY);
				}
			}

			for (MoleDeployer md: myWorld.getMoleDeployers()) {
				if (md!= null) {
					md.setSelected(false);
					if (md.isTouchDown(screenX,screenY)) {
						if (md.isAvailable()) {
							md.setSelected(true);
							if (enableSFX) AssetLoader.slctd.play();
							myWorld.setGameState(GameState.DEPLOYMENT);
							myWorld.setCurrentMoleDeployer(md);
						}
					}
				}
			}

			for (PowerUpDeployer pd: myWorld.getPowerUpDeployers()) {
				if (pd!= null) {

					if (pd.isTouchDown(screenX,screenY) && pd.isAvailable()) {
						pd.deploy();
					}
				}
			}

			if (myWorld.getPauseButton().contains(screenX,screenY)) {
				if (enableSFX) AssetLoader.button.play();
				myWorld.pauseGame();
			}
		} 

		else if (currentState == GameState.WIN || currentState == GameState.LOSE ) {
			Rectangle playAgainBounds = myWorld.getPlayAgainBounds();
			Rectangle exitBounds = myWorld.getExitBounds();

			if (playAgainBounds.contains(screenX,screenY)) {
				if (enableSFX) AssetLoader.button.play();
				myWorld.getSocketHandler().restartGame();
				myWorld.setGameState(GameState.RESTART);
			}

			if (exitBounds.contains(screenX,screenY)) {
				if (enableSFX) AssetLoader.back.play();
				//TODO: put this inside GameWorld instead?
				myWorld.getSocketHandler().exitGame();
				myWorld.setGameState(GameState.EXIT);
			}
		} 

		else if (currentState == GameState.PAUSE) {
			//No inputs allowed till other player resumes game
		} 

		else if (currentState == GameState.MENU) {

			Rectangle exitBounds = myWorld.getExitBounds();
			if (exitBounds.contains(screenX,screenY)) {
				//TODO: put this inside GameWorld instead?
				if (enableSFX) AssetLoader.back.play();
				myWorld.exitGame();
			}

			Rectangle continueButton = myWorld.getContinueButton();
			if (continueButton.contains(screenX,screenY)) {
				if (enableSFX) AssetLoader.button.play();
				myWorld.continueGame();
			}
		}

		return false;
	}
	
	/**
	 * All the methods implemented below are 
	 * the methods that are necessary from
	 * InputProcessor interface but are not used in
	 * our game
	 * 
	 */
	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.BACK){
			myWorld.setGameState(GameState.MENU);
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
