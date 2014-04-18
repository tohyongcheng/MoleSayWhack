package com.deny.GameHelpers;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;

public class GameInputHandler implements InputProcessor {
	public static boolean moleTouched;
	public static Mole moleT;
	private float scaleFactorX;
	private float scaleFactorY;
	private GameWorld myWorld;
	private ArrayList<Rectangle> placeHolders;
	
	public GameInputHandler(GameWorld myWorld, float scaleFactorX,
		float scaleFactorY) {
		this.myWorld = myWorld;
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		placeHolders = myWorld.getPlaceHolders();
	}
	
	
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
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		moleTouched = false;
		//HAMMER DISSAPEARS WHEN U TOUCH SOMETHING ELSE

		GameState currentState = myWorld.getGameState();
		
		if (currentState == GameState.DEPLOYMENT) {
			for (int i=0; i<placeHolders.size(); i++) { 
				if (placeHolders.get(i).contains(screenX, screenY)) {
					
					AssetLoader.sent.play();
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
				AssetLoader.button.play();
				myWorld.getSocketHandler().exitGame();
				myWorld.setGameState(GameState.MENU);
			}
		} 
		
		else if (currentState == GameState.RUNNING ){
			
			for(Mole m: myWorld.getMoleGrid()) {
				if (m!=null) {
					moleT = m;
					//to draw hammer
					moleTouched = m.isTouchDown(screenX, screenY);
				}
			}
			
			for (MoleDeployer md: myWorld.getMoleDeployers()) {
				if (md!= null) {
					md.setSelected(false);
					if (md.isTouchDown(screenX,screenY)) {
						if (md.isAvailable() && !md.isDisabled()) {
							md.setSelected(true);
							AssetLoader.slctd.play();
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
				AssetLoader.button.play();
				myWorld.pauseGame();
			}
		} 
		
		else if (currentState == GameState.WIN || currentState == GameState.LOSE ) {
			Rectangle playAgainBounds = myWorld.getPlayAgainBounds();
			Rectangle exitBounds = myWorld.getExitBounds();
			
			if (playAgainBounds.contains(screenX,screenY)) {
				AssetLoader.button.play();
				myWorld.getSocketHandler().restartGame();
				myWorld.setGameState(GameState.RESTART);
			}
			
			if (exitBounds.contains(screenX,screenY)) {
				AssetLoader.back.play();
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
				AssetLoader.back.play();
				myWorld.exitGame();
			}
			
			Rectangle continueButton = myWorld.getContinueButton();
			if (continueButton.contains(screenX,screenY)) {
				AssetLoader.button.play();
				myWorld.continueGame();
			}
		}
		
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
	
	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}


	

}
