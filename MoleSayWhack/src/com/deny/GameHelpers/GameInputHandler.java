package com.deny.GameHelpers;

import java.util.Queue;

import com.badlogic.gdx.InputProcessor;
import com.deny.GameObjects.Mole;
import com.deny.GameWorld.GameWorld;

public class GameInputHandler implements InputProcessor {
	
	private float scaleFactorX;
	private float scaleFactorY;
	private GameWorld myWorld;
	
	
	public GameInputHandler(GameWorld myWorld, float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = myWorld;
		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
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
		System.out.println("screenX: " + screenX + " screenY: " + screenY);
		
		for(Mole m: myWorld.getMoleGrid()) {
			if (m!=null) {
				m.isTouchDown(screenX, screenY);
				
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
