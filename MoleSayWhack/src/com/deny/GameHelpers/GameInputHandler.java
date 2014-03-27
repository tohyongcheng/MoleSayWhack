package com.deny.GameHelpers;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.Mole;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameWorld.GameWorld;
import com.deny.GameWorld.GameWorld.GameState;

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
		
		if (myWorld.getGameState() == GameState.DEPLOYMENT) {
			for (int i=0; i<placeHolders.size(); i++) { 
				if (placeHolders.get(i).contains(screenX, screenY)) {
					myWorld.getCurrentMoleDeployer().deployMole(i);
					myWorld.setCurrentMoleDeployer(null);
					myWorld.setGameState(GameState.READY);
				}
			}
		} else {
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
