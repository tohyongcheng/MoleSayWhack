package com.deny.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class StartButton 
{
	private float x, y, width, height;

    private TextureRegion buttonUp;
    private TextureRegion buttonDown;

    private Rectangle bounds;

    private boolean isPressed = false;

    public StartButton(float x, float y, float width, float height,
            TextureRegion buttonUp, TextureRegion buttonDown) 
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonUp = buttonUp;
        this.buttonDown = buttonDown;

        bounds = new Rectangle(x, y, width, height);

    }

    //If the clicked area is within the bounds of the rectangle then it
    //returns true. This is mainly for desktop
    public boolean isClicked(int screenX, int screenY) 
    {
        return bounds.contains(screenX, screenY);
    }

    //This draws the UI images for the button//
    public void draw(SpriteBatch batcher) 
    {
        if (isPressed) {
            batcher.draw(buttonDown, x, y, width, height);
        } else {
            batcher.draw(buttonUp, x, y, width, height);
        }
    }

    //This is for mobile devices - if it is touched within the bounds//
    public boolean isTouchDown(int screenX, int screenY) {

        if (bounds.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }

        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) 
    {
        
        // It only counts as a touchUp if the button is in a pressed state.
        if (bounds.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }
        
        // Whenever a finger is released, we will cancel any presses.
        isPressed = false;
        return false;
    }


}
