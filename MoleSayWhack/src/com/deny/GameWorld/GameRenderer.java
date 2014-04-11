package com.deny.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.deny.GameHelpers.AssetLoader;
import com.deny.GameHelpers.GameInputHandler;
import com.deny.GameObjects.MoleDeployer;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;

public class GameRenderer {
	
	private static final int GAME_WIDTH = Gdx.graphics.getWidth();
	private static final int GAME_HEIGHT = Gdx.graphics.getHeight();
	private double scaleW = (double) GAME_WIDTH/544;
	private double scaleH = (double) GAME_HEIGHT/816;
	private GameWorld world;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
	
	public GameRenderer(GameWorld world) {
		this.world = world;
		
		cam = new OrthographicCamera();
		cam.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);
		
		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		
	}
	
	public void render() {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        batcher.enableBlending();
        batcher.draw(AssetLoader.gameBG, 0, 0, (int)(544*scaleW), (int)(816*scaleH));
        
        // Draw MOLES
        for(Mole m : world.getMoleGrid()) {
        	if (m!= null) {
        		batcher.draw(m.getAsset(), m.getBoundingCircle().x - (int)(17*scaleW), m.getBoundingCircle().y - (int)(17*scaleH), m.getBoundingCircle().width, m.getBoundingCircle().height );
        		
        	}
        }
       //Draw MOLEDEPLOYER
        for (MoleDeployer md: world.getMoleDeployers()) {
        	//shapeRenderer.setColor(md.getMoleType().getColor());
        	if (md!=null) {
        		batcher.draw(md.getAsset(), md.getRectangle().x + (int)(7*scaleW), md.getRectangle().y + (int)(7*scaleH),md.getRectangle().width - (int)(12*scaleW),md.getRectangle().height - (int)(12*scaleH));
        		//shapeRenderer.rect(md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
        	}
        }
       
       
        
        batcher.end();
       
        //Draw BORDERS
        Rectangle three = world.getPlaceHolders().get(3);
        Rectangle six = world.getPlaceHolders().get(6);
        batcher.begin();
        batcher.enableBlending();
        batcher.draw(AssetLoader.borderHor, three.x - (int)(15*scaleW) , three.y- (int)(7*scaleH), (int)(490*scaleW), (int)(23*scaleH));
        batcher.draw(AssetLoader.borderHor, six.x - (int)(15*scaleW) , six.y - (int)(7*scaleH), (int)(490*scaleW), (int)(23*scaleH));
        
        //get the first and second recctangle to draw vertical line
        Rectangle first = world.getPlaceHolders().get(1);
        Rectangle second = world.getPlaceHolders().get(2);
        batcher.draw(AssetLoader.borderVert, first.x-(int)(15*scaleW), first.y - (int)(21*scaleH), (int)(30*scaleW), (int)(489*scaleH));
        batcher.draw(AssetLoader.borderVert, second.x-(int)(15*scaleW), second.y - (int)(21*scaleH), (int)(30*scaleW), (int)(489*scaleH));
        batcher.end();
        
  
        //CONDITIONS
        batcher.begin();
        if (world.getGameState() == GameState.RUNNING ) {
	        batcher.draw(AssetLoader.ps,world.getPauseButton().x,world.getPauseButton().y,world.getPauseButton().width,world.getPauseButton().height);
	        batcher.enableBlending();
	        for (MoleDeployer md: world.getMoleDeployers()) {
	        	
	        	if (md!=null && md.availability == true) {
	        		batcher.draw(AssetLoader.dplyOK, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!= null && md.availability == false){
	        		batcher.draw(AssetLoader.dplyCD, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        }
	        
	        if (GameInputHandler.moleTouched){
	        	batcher.draw(AssetLoader.hmr, GameInputHandler.moleT.getBoundingCircle().x, GameInputHandler.moleT.getBoundingCircle().y, (int)(90*scaleW),(int)(69*scaleH));
	        	
	        }
        }
       
        if (world.getGameState() == GameState.DEPLOYMENT) {
	        batcher.draw(AssetLoader.ps,world.getPauseButton().x,world.getPauseButton().y,world.getPauseButton().width,world.getPauseButton().height);
	     
	        batcher.enableBlending();
	        for (MoleDeployer md: world.getMoleDeployers()) {
	        	
	        	if (md!=null && md.selected == false && md.availability == true) {
	        		batcher.draw(AssetLoader.dplyOK, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!=null && md.selected == true && md.availability == true){
	        		batcher.draw(AssetLoader.dplySEL, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!=null && md.selected == true && md.availability == false){
	        		batcher.draw(AssetLoader.dplyCD, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if(md!=null && md.selected == false && md.availability == false){
	        		batcher.draw(AssetLoader.dplyCD, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        }
	 
        }
        
        batcher.end();
        
        batcher.begin();
        batcher.enableBlending();
        
        
        for (MoleDeployer md: world.getMoleDeployers()) {

        	if (md!=null) {
        		batcher.draw(md.getAsset(), md.getRectangle().x + (int)(10*scaleW), md.getRectangle().y + (int)(7*scaleH),md.getRectangle().width - (int)(12*scaleW),md.getRectangle().height - (int)( 10*scaleH));
        		
        	}
        }
        batcher.end();
        
      
 
        batcher.begin();
        batcher.enableBlending();
        if (world.getGameState() == GameState.WIN){
        	batcher.draw(AssetLoader.winTextR, 0, 0, GAME_WIDTH, GAME_HEIGHT);
        	
			Rectangle playAgainBounds = world.getPlayAgainBounds();
			Rectangle exitBounds = world.getExitBounds();
			
			batcher.draw(AssetLoader.rpl,playAgainBounds.x,playAgainBounds.y,playAgainBounds.width,playAgainBounds.height);
			batcher.draw(AssetLoader.cnl, exitBounds.x,exitBounds.y,exitBounds.width,exitBounds.height);
	
			
        }
        
        if (world.getGameState() == GameState.LOSE) {
        	batcher.draw(AssetLoader.Lose, 0, 0, GAME_WIDTH, GAME_HEIGHT);
			
			Rectangle playAgainBounds = world.getPlayAgainBounds();
			Rectangle exitBounds = world.getExitBounds();

			batcher.draw(AssetLoader.rpl,playAgainBounds.x,playAgainBounds.y,playAgainBounds.width,playAgainBounds.height);
			batcher.draw(AssetLoader.cnl, exitBounds.x,exitBounds.y,exitBounds.width,exitBounds.height);
			
			
		}
        
        else if (world.getGameState() == GameState.PAUSE ) {
        	Rectangle pauseOverlay = world.getPauseOverlay();
        	batcher.draw(AssetLoader.psBG, pauseOverlay.x, pauseOverlay.y, pauseOverlay.width, pauseOverlay.height);

        }
        
        else if (world.getGameState() == GameState.MENU ) {
        	Rectangle pauseOverlay = world.getPauseOverlay();
        	batcher.draw(AssetLoader.psBG, pauseOverlay.x, pauseOverlay.y, pauseOverlay.width, pauseOverlay.height);

        	Rectangle exitBounds = world.getExitBounds();
        	batcher.draw(AssetLoader.ext, exitBounds.x,exitBounds.y,exitBounds.width,exitBounds.height);
        	
			Rectangle continueButton = world.getContinueButton();
			batcher.draw(AssetLoader.rsm,continueButton.x,continueButton.y,continueButton.width,continueButton.height);
			
        }
        
        batcher.end();
    
        
        
	}
	

}
