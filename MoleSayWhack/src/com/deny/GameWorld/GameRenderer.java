package com.deny.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;

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
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
        shapeRenderer.rect(world.getBoard().x, world.getBoard().y,
                world.getBoard().width, world.getBoard().height);
        

        for(Mole m : world.getMoleGrid()) {
        	if (m!= null) {
        		shapeRenderer.setColor(m.getColor());
        		shapeRenderer.circle(m.getBoundingCircle().x, m.getBoundingCircle().y, m.getBoundingCircle().radius);
        	}
        }
        
        for (MoleDeployer md: world.getMoleDeployers()) {
        	shapeRenderer.setColor(md.getMoleType().getColor());
        	if (md!=null) {
        		shapeRenderer.rect(md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
        	}
        }
        
        
        //Render Pause Button
        if (world.getGameState() == GameState.DEPLOYMENT || world.getGameState() == GameState.RUNNING ) {
	        shapeRenderer.setColor(87 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);
	        shapeRenderer.rect(world.getPauseButton().x,world.getPauseButton().y,world.getPauseButton().width,world.getPauseButton().height);
        }
        
        shapeRenderer.end();

        shapeRenderer.begin(ShapeType.Line);
        // Chooses RGB Color of 255, 109, 120 at full opacity
        shapeRenderer.setColor(255 / 255.0f, 109 / 255.0f, 120 / 255.0f, 1);

        // Draws the rectangle from myWorld (Using ShapeType.Line)
        for (Rectangle r : world.getPlaceHolders()) {
        	shapeRenderer.rect(r.x,r.y,r.width,r.height);
        }
        
        for (MoleDeployer md: world.getMoleDeployers()) {
        	shapeRenderer.setColor(md.getMoleType().getColor());
        	if (md!=null) {
        		shapeRenderer.rect(md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
        	}
        }

        shapeRenderer.end();
        
        shapeRenderer.begin(ShapeType.Filled);
        
        if (world.getGameState() == GameState.LOSE || world.getGameState() == GameState.WIN ) {
			Rectangle menu = world.getGameOverMenu();
			Rectangle playAgainBounds = world.getPlayAgainBounds();
			Rectangle exitBounds = world.getExitBounds();
			shapeRenderer.setColor(Color.GRAY);
			shapeRenderer.rect(menu.x,menu.y,menu.width,menu.height);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(playAgainBounds.x,playAgainBounds.y,playAgainBounds.width,playAgainBounds.height);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(exitBounds.x,exitBounds.y,exitBounds.width,exitBounds.height);
			
		}
        
        else if (world.getGameState() == GameState.PAUSE ) {
        	Rectangle pauseOverlay = world.getPauseOverlay();
        	shapeRenderer.setColor(0f, 0f, 0f, 0.01f);
        	shapeRenderer.rect(pauseOverlay.x, pauseOverlay.y, pauseOverlay.width, pauseOverlay.height);
        	
        	//RENDER FONT, WAIT FOR OTHER PLAYER
        }
        
        else if (world.getGameState() == GameState.MENU ) {
        	Rectangle pauseOverlay = world.getPauseOverlay();
        	shapeRenderer.setColor(0f, 0f, 0f, 0.01f);
        	shapeRenderer.rect(pauseOverlay.x, pauseOverlay.y, pauseOverlay.width, pauseOverlay.height);
        	Rectangle exitBounds = world.getExitBounds();
        	shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(exitBounds.x,exitBounds.y,exitBounds.width,exitBounds.height);
			Rectangle continueButton = world.getContinueButton();
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.rect(continueButton.x,continueButton.y,continueButton.width,continueButton.height);	
        }
        
        shapeRenderer.end();
        
        
	}
	

}
