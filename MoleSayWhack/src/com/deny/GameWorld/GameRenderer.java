package com.deny.GameWorld;

import java.util.ArrayList;

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
import com.deny.GameObjects.MoleType;
import com.deny.GameObjects.PowerUpDeployer;
import com.deny.GameWorld.GameWorld.GameState;
import com.deny.MoleObjects.Mole;
import com.deny.PowerUpObjects.BlockMoleGrid;
import com.deny.PowerUpObjects.DisableAllPowerUps;
import com.deny.PowerUpObjects.DisableOneMoleDeployer;
import com.deny.PowerUpObjects.Earthquake;
import com.deny.PowerUpObjects.EnableFog;
import com.deny.PowerUpObjects.GenerateDummyMoles;
import com.deny.PowerUpObjects.Invulnerability;

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
        
        // DRAW opponent hp
       switch(world.getOpponentHP()){
       case 1:
    	   batcher.draw(AssetLoader.op1, (int)(10*scaleW), (int)(10*scaleH), (int)(30*scaleW), (int)(25*scaleH));
    	   break;
       case 2:
    	   batcher.draw(AssetLoader.op2, (int)(10*scaleW), (int)(10*scaleH), (int)(47*scaleW), (int)(25*scaleH));
    	   break;
       case 3:
    	   batcher.draw(AssetLoader.op3, (int)(10*scaleW), (int)(10*scaleH), (int)(72*scaleW), (int)(25*scaleH));
    	   break;
       case 4:
    	   batcher.draw(AssetLoader.op4, (int)(10*scaleW), (int)(10*scaleH), (int)(98*scaleW), (int)(25*scaleH));
    	   break;
       case 5:
    	   batcher.draw(AssetLoader.op5, (int)(10*scaleW), (int)(10*scaleH), (int)(112*scaleW), (int)(25*scaleH));
    	   break;
       }
       //DRAW MYHP
       switch(world.getPlayer().getHP()){
       case 1:
    	   batcher.draw(AssetLoader.mh1, (int)((544-123)*scaleW), (int)(10*scaleH), (int)(113*scaleW), (int)(20*scaleH));
    	   break;
       case 2:
    	   batcher.draw(AssetLoader.mh2, (int)((544-123)*scaleW), (int)(10*scaleH), (int)(113*scaleW), (int)(20*scaleH));
    	   break;
       case 3:
    	   batcher.draw(AssetLoader.mh3, (int)((544-123)*scaleW), (int)(10*scaleH), (int)(113*scaleW), (int)(20*scaleH));
    	   break;
       case 4:
    	   batcher.draw(AssetLoader.mh4, (int)((544-123)*scaleW), (int)(10*scaleH), (int)(113*scaleW), (int)(20*scaleH));
    	   break;
       case 5:
    	   batcher.draw(AssetLoader.mh5, (int)((544-123)*scaleW), (int)(10*scaleH), (int)(113*scaleW), (int)(20*scaleH));
    	   break;

       }
        // Draw MOLES
        for(Mole m : world.getMoleGrid()) {
        	if (m!= null) {
        		if (m.getMoleType() == MoleType.DUMMY){
        			System.out.println("Dummy rendering");
        			batcher.draw(AssetLoader.dummyMOLE, m.getBoundingCircle().x - (int)(17*scaleW), m.getBoundingCircle().y - (int)(17*scaleH), m.getBoundingCircle().width, m.getBoundingCircle().height );
        		}
        		else{
        		batcher.draw(m.getAsset(), m.getBoundingCircle().x - (int)(17*scaleW), m.getBoundingCircle().y - (int)(17*scaleH), m.getBoundingCircle().width, m.getBoundingCircle().height );
        		}
        	}
        }
       //Draw MOLEDEPLOYER
        for (MoleDeployer md: world.getMoleDeployers()) {
        	//shapeRenderer.setColor(md.getMoleType().getColor());
        	if (md!=null) {
        		batcher.draw(md.getAsset(), md.getRectangle().x + (int)(7*scaleW), md.getRectangle().y + (int)(7*scaleH),md.getRectangle().width - (int)(12*scaleW),md.getRectangle().height - (int)(12*scaleH));

        		
        	}
        }
        
        //Draw POWERUPDEPLOYER
        for (PowerUpDeployer pd: world.getPowerUpDeployers()) {
        	if (pd!=null) {
        		//shapeRenderer.setColor(pd.getPowerupType().getColor());
        		Rectangle r = pd.getRectangle();
        		if (pd.isAvailable()){
        		batcher.draw(pd.getAsset(), r.x, r.y, r.width, r.height);}
        		else{
        			//cooldown
        			batcher.draw(pd.getAsset(), r.x, r.y, r.width, r.height);
        			batcher.draw(AssetLoader.cdown, r.x, r.y, r.width, r.height);
        		}
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
        
        
          //DRAW ANY PU EFFECTS
        batcher.begin();
        batcher.enableBlending();
        if (Earthquake.isInEffect()){
        	for (Rectangle r : world.getPlaceHolders()){
        		batcher.draw(AssetLoader.hmr, r.x+ (int)(scaleW*20), r.y+(int)(scaleH*20), (int)(90*scaleW),(int)(69*scaleH));
        	}
        }
        if (EnableFog.isInEffect()){
        	//draw the fog
        	System.out.println("FOG");
        	batcher.draw(AssetLoader.fogEffect, world.getPlaceHolders().get(0).x,world.getPlaceHolders().get(0).y, (int)(450*scaleW), (int)(450*scaleH));
        }
        
        if (DisableAllPowerUps.isInEffect()){
        	//add the gray box overlay
        	  for (PowerUpDeployer pd: world.getPowerUpDeployers()) {
        		  Rectangle r = pd.getRectangle();
        		  batcher.draw(AssetLoader.cdown, r.x, r.y, r.width, r.height);
      		}
        	  
        }
        
        if(Invulnerability.isInEffect()){
        	//draw the circular shield
        	batcher.draw(AssetLoader.shield, GAME_WIDTH/2 -(int) (529/2 * scaleW) + 8, (int)(15*scaleH), (int)(529*scaleW), (int)(556*scaleH));
        }
        
        if(BlockMoleGrid.isInEffect()){
        	ArrayList<Rectangle> arrays = world.getPlaceHolders();
        	boolean[] bool = world.getBlockedGrids();
        	for (int i =0; i<bool.length; i++){
        		if (bool[i] == true){
        			batcher.draw(AssetLoader.blockgrid, arrays.get(i).x-(int)(15*scaleW), arrays.get(i).y, 141,129);
        		}
        	}
        }

        
     
        batcher.end();
        //CONDITIONS
        batcher.begin();
        if (world.getGameState() == GameState.RUNNING ) {
	        batcher.draw(AssetLoader.ps,world.getPauseButton().x,world.getPauseButton().y,world.getPauseButton().width,world.getPauseButton().height);
	        batcher.enableBlending();
	        for (MoleDeployer md: world.getMoleDeployers()) {
	        	
	        	if (md!=null && md.isAvailable() == true) {
	        		batcher.draw(AssetLoader.dplyOK, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!= null && md.isAvailable() == false){
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
	        	
	        	if (md!=null && !md.isSelected() && md.isAvailable() == true) {
	        		batcher.draw(AssetLoader.dplyOK, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!=null && md.isSelected() && md.isAvailable() == true){
	        		batcher.draw(AssetLoader.dplySEL, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if (md!=null && md.isSelected() && md.isAvailable() == false){
	        		batcher.draw(AssetLoader.dplyCD, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
	        	}
	        	else if(md!=null && !md.isSelected() && md.isAvailable() == false){
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
        if (DisableOneMoleDeployer.isInEffect() && ((world.getGameState() != GameState.WIN)
        		&& (world.getGameState() != GameState.LOSE) && (world.getGameState() != GameState.PAUSE))){
       	 for (MoleDeployer md: world.getMoleDeployers()){
       		 System.out.println(md.isDisabled());
       		 
       		 if (md.isDisabled()){
       			 System.out.println("i am dsiabled");
       			 batcher.draw(AssetLoader.dplyCD, md.getRectangle().x, md.getRectangle().y,md.getRectangle().width,md.getRectangle().height);
       		 }
       	 }
       }
        
  
        batcher.end();
    

        
        
	}
	

}
