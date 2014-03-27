package com.deny.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.Mole;

public class GameRenderer {
	
	private static final int GAME_WIDTH = 136;
	private static final int GAME_HEIGHT = 204;
	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	
	
	public GameRenderer(GameWorld world) {
		this.world = world;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
	}
	
	public void render() {
//		System.out.println("GameRenderer - render");
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        shapeRenderer.begin(ShapeType.Filled);
        // Chooses RGB Color of 87, 109, 120 at full opacity
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from myWorld (Using ShapeType.Filled)
        shapeRenderer.rect(world.getBoard().x, world.getBoard().y,
                world.getBoard().width, world.getBoard().height);

        
        shapeRenderer.setColor(200 / 255.0f, 12 / 255.0f, 35 / 255.0f, 1);
        
        for(Mole m : world.getMoleGrid()) {
        	if (m!= null) {
        		shapeRenderer.circle(m.getBoundingCircle().x, m.getBoundingCircle().y, m.getBoundingCircle().radius);
        	}
        }
        
        shapeRenderer.end();

        /*
         * 3. We draw the rectangle's outline
         */

        // Tells shapeRenderer to draw an outline of the following shapes
        shapeRenderer.begin(ShapeType.Line);

        // Chooses RGB Color of 255, 109, 120 at full opacity
        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from myWorld (Using ShapeType.Line)
        for (Rectangle r : world.getPlaceHolders()) {
        	shapeRenderer.rect(r.x,r.y,r.width,r.height);
        }

        shapeRenderer.end();
	}
	

}
