package com.deny.GameHelpers;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;
import com.deny.Screens.MainMenuScreen;
import com.deny.Screens.PreGameScreen;

public class GameInputHandler implements InputProcessor {
	
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
		
		screenX = scaleX(screenX);
		screenY = scaleY(screenY);
		
		GameState currentState = myWorld.getGameState();
		
		if (currentState == GameState.DEPLOYMENT) {
			for (int i=0; i<placeHolders.size(); i++) { 
				if (placeHolders.get(i).contains(screenX, screenY)) {
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
				myWorld.getSocketHandler().exitGame();
				myWorld.setGameState(GameState.MENU);
			}
		} 
		
		else if (currentState == GameState.RUNNING ){
			
			for(Mole m: myWorld.getMoleGrid()) {
				if (m!=null) {
					m.isTouchDown(screenX, screenY);
				}
			}
			
			for (MoleDeployer md: myWorld.getMoleDeployers()) {
				if (md!= null) {
					if (md.isTouchDown(screenX,screenY)) {
						myWorld.setGameState(GameState.DEPLOYMENT);
						myWorld.setCurrentMoleDeployer(md);
					}
				}
			}
			
			if (myWorld.getPauseButton().contains(screenX,screenY)) {
				myWorld.pauseGame();
			}
		} 
		
		else if (currentState == GameState.WIN || currentState == GameState.LOSE ) {
			Rectangle playAgainBounds = myWorld.getPlayAgainBounds();
			Rectangle exitBounds = myWorld.getExitBounds();
			
			if (playAgainBounds.contains(screenX,screenY)) {
				myWorld.getSocketHandler().restartGame();
				myWorld.setGameState(GameState.RESTART);
			}
			
			if (exitBounds.contains(screenX,screenY)) {
				//TODO: put this inside GameWorld instead?
				myWorld.getSocketHandler().exitGame();
				myWorld.setGameState(GameState.EXIT);
			}
		} 
		
		else if (currentState == GameState.PAUSE) {
			
			
			
		} 
		
		else if (currentState == GameState.MENU) {
			
			Rectangle exitBounds = myWorld.getExitBounds();
			if (exitBounds.contains(screenX,screenY)) {
				//TODO: put this inside GameWorld instead?
				myWorld.exitGame();
			}
			
			Rectangle continueButton = myWorld.getContinueButton();
			if (continueButton.contains(screenX,screenY)) {
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
