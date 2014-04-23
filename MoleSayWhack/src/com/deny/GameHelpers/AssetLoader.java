package com.deny.GameHelpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
/**
 * 
 * AssetLoader is a class that stores music, pictures, logo, and sound effects for the entire game
 *
 */
public class AssetLoader 
{
	//The Textures//
	public static BitmapFont font;
	public static Texture logoTexture, win, invulnerable, mismatch, optionsSC, trudy;
	public static Texture Main1024, Game1024, moleDep1024, lose, PS, powerup, powerupDep2, powerupDep1;
	
	public static TextureRegion  fxON, mscOFF, tdyopt, optBG, changename, changemusic, changefx, noprotocol, t2, t3, t4, t5, refresh, tdy;
	public static TextureRegion powerless, earthquake, moleshower, silencer, molejam, kingmole, mouldy, dummymole, moleshield;
	public static TextureRegion gameBG, ps, rpl, ext, rsm, psBG, borderVert, borderHor, hmr, titlepuDep, shield;
	public static TextureRegion background, strB, optB, hghB, Title, m1, m3, m5, sm,king,
	enterIP, loading, cnl, mdm1, mdm3, mdm5, mdsm, titleDep, Lose, dplyOK, dplyCD, dplySEL, blockgrid;
	public static TextureRegion eq, ms, ns, np, nc, sc, fog, km, inv, cdown;
	public static TextureRegion logo, mswlogo, startButtonUp, startButtonDown, winTextR;
	public static TextureRegion fogEffect, dummyMOLE, dc;
	public static TextureRegion mh1, mh2, mh3, mh4, mh5, op1, op2, op3, op4, op5, mp;



	public static Sound  dummy, shower, blockG, shieldS,kinglaugh, hit, explode, clicksound, gameover, 
	button, back, sent, slctd, popup, earthQk,fogg, block, damage;
	public static Music summer, ann;

	/**
	 * This method will be called at the beginning of the game, which
	 * is to load all the assets from the data folder to be shown 
	 * and rendered during the gameplay.
	 */
	public static void load()
	{
		//For trudy screen
		trudy = new Texture(Gdx.files.internal("data/trudy.png"));
		trudy.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		tdy = new TextureRegion(trudy, 0, 0, 544, 816);
		tdy.flip(false,true);
		//Option Screen items
		optionsSC = new Texture(Gdx.files.internal("data/optionsSC.png"));
		optionsSC.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//Option Screen background
		optBG = new TextureRegion(optionsSC, 0, 0,  544, 816);
		optBG.flip(false, true);
		//trudy protocol option
		tdyopt = new TextureRegion(optionsSC, 12,829, 258,90);
		tdyopt.flip(false,true);
		//refresh button 
		refresh = new TextureRegion(optionsSC, 556,937,260,87);
		refresh.flip(false,true);
		//change name button
		changename = new TextureRegion(optionsSC, 558, 829, 258, 90);
		changename.flip(false, true);
		//BGM off option
		changemusic = new TextureRegion(optionsSC, 558, 717, 258, 90);
		changemusic.flip(false,true);
		//FX on option
		fxON = new TextureRegion(optionsSC, 15,929, 258,90);
		fxON.flip(false,true);
		//BGM off option
		mscOFF= new TextureRegion(optionsSC, 290,827, 258, 90);
		mscOFF.flip(false,true);
		//FX off option
		changefx = new TextureRegion(optionsSC, 558, 605, 258, 90);
		changefx.flip(false, true);
		//no protocol option
		noprotocol = new TextureRegion(optionsSC, 558, 494, 258, 90);
		noprotocol.flip(false,true);
		//t2 protocol option
		t2 = new TextureRegion(optionsSC, 558,46, 258,90);
		t2.flip(false,true);
		//t3 protocol option
		t3 = new TextureRegion(optionsSC, 558,158, 258, 90);
		t3.flip(false,true);
		//t4 protocol option
		t4 = new TextureRegion(optionsSC, 558,271, 258, 90);
		t4.flip(false,true);
		//t5 protocol option
		t5 = new TextureRegion(optionsSC, 556, 381, 258, 90);
		t5.flip(false,true);
		//mismatch protocol screen
		mismatch = new Texture(Gdx.files.internal("data/mp.png"));
		mismatch.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		mp = new TextureRegion(mismatch	, 0, 0, 544, 816);
		mp.flip(false,true);
		//invulnerability powerup effects in game
		invulnerable = new Texture(Gdx.files.internal("data/invulnerability.png"));
		invulnerable.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		shield = new TextureRegion(invulnerable, 9, 9, 529, 556);
		shield.flip(false,true);
		//For powerup deployers screen
		powerupDep1 = new Texture(Gdx.files.internal("data/powerdep1.png"));
		powerupDep1.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//disable powerup powerup deployers screen
		powerless = new TextureRegion(powerupDep1, 11,14, 445, 140);
		powerless.flip(false,true);
		//earthquake powerup deployers screen
		earthquake = new TextureRegion(powerupDep1, 12, 170, 445, 140);
		earthquake.flip(false,true);
		//moleshower powerup deployers screen
		moleshower = new TextureRegion(powerupDep1, 12, 330, 445, 140);
		moleshower.flip(false,true);
		//disable mole deployer powerup deployers screen
		silencer = new TextureRegion(powerupDep1, 12, 490, 445, 140);
		silencer.flip(false,true);
		//disable mole to be spawned in one of the grids powerup deployers screen
		molejam = new TextureRegion(powerupDep1, 12, 654, 445, 140);
		molejam.flip(false,true);
		//invulnerability powerup deployers screen
		moleshield = new TextureRegion(powerupDep1, 12, 815, 445, 140);
		moleshield.flip(false,true);
		//Title for the powerup deployers screen
		titlepuDep=  new TextureRegion(powerupDep1, 512,30,230,98);
		titlepuDep.flip(false,true);
		//disable mole to be spawned in one of the grids in game powerup effect
		blockgrid = new TextureRegion(powerupDep1, 485,160, 141,129);
		blockgrid.flip(false, true);
		//The fake mole in game 
		dummyMOLE = new TextureRegion(powerupDep1, 494,316, 113,114);
		dummyMOLE.flip(false,true);
		//Another powerup Texture
		powerupDep2 = new Texture(Gdx.files.internal("data/powerdep2.png"));
		powerupDep2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//kingmole powerup deployers screen
		kingmole = new TextureRegion(powerupDep2, 11, 15, 445, 140);
		kingmole.flip(false,true);
		//fog powerup deployer screen
		mouldy = new TextureRegion(powerupDep2, 10,173,445,140);
		mouldy.flip(false, true);
		//spawn fake moles powerup deployer screen
		dummymole = new TextureRegion(powerupDep2, 11,335, 445, 140);
		dummymole.flip(false,true);
		//the effect of the fog powerup in game
		fogEffect = new TextureRegion(powerupDep2, 452,457, 546, 551);
		
		//Another powerup in game button
		powerup = new Texture(Gdx.files.internal("data/Powerup.png"));
		powerup.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//earthquake in game powerup button
		eq = new TextureRegion(powerup, 10, 8, 147, 76);
		eq.flip(false,true);
		//invulnerability in game powerup button
		ms = new TextureRegion(powerup, 10, 575, 147, 76);
		ms.flip(false, true);
		//disable one mole deployer in game powerup button
		nc = new TextureRegion(powerup,10, 190, 147, 76);
		nc.flip(false,true);
		//disable all powerups in game powerup button
		np = new TextureRegion(powerup, 10,107, 147, 76);
		np.flip(false,true);
		//disable spawning at one grid in game powerup button
		ns = new TextureRegion(powerup, 10,287, 147, 76);
		ns.flip(false,true);
		//fog in game powerup button
		fog = new TextureRegion (powerup, 10,479, 147, 76);
		fog.flip(false,true);
		//king mole in game powerup button
		km = new TextureRegion(powerup, 12, 671, 147, 76);
		km.flip(false,true);
		//spawn dummy moles in game powerup button
		sc = new TextureRegion(powerup, 11, 383, 147, 76);
		sc.flip(false, true);
		//invulnerability in game powerup button
		inv = new TextureRegion(powerup, 167,108, 147, 76);
		inv.flip(false,true);
		//1 my Hp
		mh1 = new TextureRegion(powerup, 23,882, 113,20);
		mh1.flip(false,true);
		//2 my Hp
		mh2 = new TextureRegion(powerup, 23, 850, 113, 20);
		mh2.flip(false, true);
		//3 my Hp
		mh3 = new TextureRegion(powerup, 23, 821, 113, 20);
		mh3.flip(false,true);
		//4 my Hp
		mh4 = new TextureRegion(powerup, 23, 794, 113, 20);
		mh4.flip(false,true);
		//4 my Hp
		mh5 = new TextureRegion(powerup, 23, 762, 113, 20);
		mh5.flip(false,true);
		//1 opponent Hp
		op1 = new TextureRegion(powerup, 279,906, 30, 25);
		op1. flip(false,true);
		//2 opponent Hp
		op2 = new TextureRegion(powerup, 259, 875, 47, 25);
		op2.flip(false,true);
		//3 opponent Hp
		op3 = new TextureRegion(powerup, 237, 837, 72, 25);
		op3.flip(false,true);
		//4 opponent Hp
		op4 = new TextureRegion(powerup, 214, 801, 98, 25);
		op4.flip(false,true);
		//5 opponent Hp
		op5 = new TextureRegion(powerup, 195, 761, 112, 25);
		op5.flip(false,true);
		//ingame powerup cooldown effect
		cdown = new TextureRegion(powerup, 168, 12, 146, 75);
		cdown.flip(false,true);
		//disconnect screen
		dc = new TextureRegion(powerup, 479, 0, 544, 816);
		dc.flip(false,true);
		//Paused screen
		PS = new Texture(Gdx.files.internal("data/pause.png"));
		PS.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		psBG = new TextureRegion(PS, 0, 0, 514, 816);
		psBG.flip(false,true);
		//Mole deployer Screen
		moleDep1024 = new Texture(Gdx.files.internal("data/moleDep.png"));
		moleDep1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		// title of Mole deployer Screen
		titleDep = new TextureRegion(moleDep1024, 40,740,324,133);
		titleDep.flip(false,true);
		//1 hit mole, Mole deployer Screen
		mdm1 = new TextureRegion(moleDep1024, 0,0,457	,153);
		mdm1.flip(false,true);
		//3 hit mole, Mole deployer Screen
		mdm3 = new TextureRegion(moleDep1024, 0,168,457	,153);
		mdm3.flip(false,true);
		//5 hit mole, Mole deployer Screen
		mdm5 = new TextureRegion(moleDep1024, 0,345,457	,153);
		mdm5.flip(false,true);
		//sabotage mole, Mole deployer Screen
		mdsm = new TextureRegion(moleDep1024, 0,528,457,153);
		mdsm.flip(false,true);
		//In game pictures
		Game1024 = new Texture(Gdx.files.internal("data/GameBackground.png"));
		Game1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//hammer 
		hmr = new TextureRegion(Game1024 , 562,644,90,69);
		hmr.flip(false,true);
		//in-game background
		gameBG = new TextureRegion(Game1024, 0, 0, 543,816);
		gameBG.flip(false, true);
		//cancel button
		cnl = new TextureRegion(Game1024, 563,735,83,82);
		cnl.flip(false,true);
		//pause button
		ps = new TextureRegion(Game1024, 559, 215, 83, 82);
		ps.flip(false,true);
		//replay button
		rpl = new TextureRegion(Game1024, 559, 379, 83, 82);
		rpl.flip(false,true);
		//exit button
		ext = new TextureRegion(Game1024, 559, 296, 83, 82);
		ext.flip(false,true);
		//resume button
		rsm = new TextureRegion(Game1024, 559, 137, 83, 82);
		rsm.flip(false,true);
		//picture of the vertical borders of the grid in game
		borderVert = new TextureRegion(Game1024, 690,294, 30, 489);
		borderVert.flip(false,true);
		//picture of the horizontal borders of the grid in game
		borderHor = new TextureRegion(Game1024, 4, 822, 490,23);
		borderHor.flip(false,true);
		//mole Deployer in ready state in game
		dplyOK = new TextureRegion(Game1024, 685, 92, 100,92);
		dplyOK.flip(false,true);
		//mole Deployer in selected state in game
		dplySEL = new TextureRegion(Game1024, 690, 0, 99, 90);
		dplySEL.flip(false,true);
		//mole Deployer in cooldown state in game
		dplyCD = new TextureRegion(Game1024, 689, 185, 99, 95);
		dplyCD.flip(false,true);
		//Main menu pictures
		Main1024 = new Texture(Gdx.files.internal("data/1024Main.png"));
		Main1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		//Background picture
		background = new TextureRegion(Main1024, 0, 0, 543, 816);
		background.flip(false,true);
		//Title of the game
		Title = new TextureRegion(Main1024, 545, 0, 336, 170);
		Title.flip (false,true);
		//Kingmole
		king = new TextureRegion(Main1024, 35, 845, 138, 138);
		king.flip(false,true);
		//Start button
		strB = new TextureRegion(Main1024, 545, 172, 260,89 );
		strB.flip(false,true);
		//option button
		optB = new TextureRegion(Main1024, 545, 260, 260, 91);
		optB.flip(false,true);
		//sabotage Mole
		sm = new TextureRegion(Main1024, 690, 564, 113,114);
		sm.flip(false,true);
		//one hit Mole
		m1 = new TextureRegion(Main1024, 548, 445, 113,114);
		m1.flip(false, true);
		//three hit Mole
		m3 = new TextureRegion(Main1024, 680,448, 113,114);
		m3.flip(false, true);
		//five hit Mole
		m5 = new TextureRegion(Main1024, 554,565,113,114);
		m5.flip(false, true);
		//Enter IP address button
		enterIP = new TextureRegion(Main1024, 550,680,260,100);
		enterIP.flip(false, true);
		//Loading text
		loading = new TextureRegion(Main1024, 550,800,260,89);
		loading.flip(false,true);
		//Lose screen
		lose = new Texture(Gdx.files.internal("data/lose.png"));
		lose.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Lose = new TextureRegion(lose, 0,0,544,816);
		Lose.flip(false,true);
		//Win screen
		win = new Texture(Gdx.files.internal("data/WIN_LOSEscreen.png"));
		win.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		winTextR = new TextureRegion(win, 0,0,544,816);
		winTextR.flip(false,true);
		
		//Splash screen logo
		logoTexture = new Texture(Gdx.files.internal("data/DenySplash.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //815 is the width, 335 is the height//
        logo = new TextureRegion(logoTexture, 0, 0, 960, 470);
        
		//Font for the game
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
		font.setScale(1f, -1f);

        //The Sound Effects//
        hit = Gdx.audio.newSound(Gdx.files.internal("data/Bash.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("data/Boom.mp3"));
        clicksound = Gdx.audio.newSound(Gdx.files.internal("data/ButtonClick.mp3"));
        gameover = Gdx.audio.newSound(Gdx.files.internal("data/gameover.wav"));

        //button clicked sound effect
        button = Gdx.audio.newSound(Gdx.files.internal("data/button-20.wav"));
        //sound effects for back button
        back = Gdx.audio.newSound(Gdx.files.internal("data/button-09.wav"));
        //main screen and every other screen
        summer = Gdx.audio.newMusic(Gdx.files.internal("data/summer.mp3"));
        //in game music
        ann = Gdx.audio.newMusic(Gdx.files.internal("data/ann.mp3"));
        //mole sent sound effect
        sent = Gdx.audio.newSound(Gdx.files.internal("data/button-7.wav"));
        //moleDeployer selected sound effect
        slctd = Gdx.audio.newSound(Gdx.files.internal("data/button-28.wav"));
        //mole popup on grid sound effect
        popup =Gdx.audio.newSound(Gdx.files.internal("data/button-10.wav"));
        //eartquake powerup sound effect
        earthQk = Gdx.audio.newSound(Gdx.files.internal("data/grenade.mp3"));
        //fog powerup sound effect
        fogg = Gdx.audio.newSound(Gdx.files.internal("data/fog.mp3"));
        //block grid powerup sound effect
        block= Gdx.audio.newSound(Gdx.files.internal("data/buzzer.mp3"));
        //invulnerability powerup sound effect
        shieldS = Gdx.audio.newSound(Gdx.files.internal("data/shield.mp3"));
        //kingmole powerup sound effect
        kinglaugh = Gdx.audio.newSound(Gdx.files.internal("data/king.mp3"));
        //grid block powerup sound effect
        blockG = Gdx.audio.newSound(Gdx.files.internal("data/block.mp3"));
        //moleshower sound effect
        shower = Gdx.audio.newSound(Gdx.files.internal("data/shower.mp3"));
        //Dummy mole sound effect
        dummy = Gdx.audio.newSound(Gdx.files.internal("data/dummy.mp3"));
        //Player damaged sound effect (when -1HP)
        damage = Gdx.audio.newSound(Gdx.files.internal("data/damage.mp3"));
        
	}
	/**
	 * A method which dispose all Texture and Sound after we are finished.
	 * We must dispose of the texture when we are finished.
	 */
	public static void dispose() {
       
		logoTexture.dispose();
		font.dispose();
		win.dispose();mismatch.dispose();optionsSC.dispose();
		Main1024.dispose(); Game1024.dispose(); moleDep1024.dispose(); lose.dispose();
		PS.dispose(); powerup.dispose(); powerupDep2.dispose(); powerupDep1.dispose();
		hit.dispose(); explode.dispose(); clicksound.dispose(); gameover.dispose();
		button.dispose(); back.dispose();summer.dispose(); ann.dispose(); sent.dispose(); slctd.dispose(); popup.dispose();
		dummy.dispose(); shower.dispose(); blockG.dispose(); shieldS.dispose();kinglaugh.dispose();
		earthQk.dispose();fogg.dispose(); block.dispose();

    }

}
