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

public class AssetLoader 
{
	//The Textures//
	public static BitmapFont font;
	public static Texture texture, logoTexture, win, invulnerable, mismatch, optionsSC, trudy;
	public static TextureRegion  fxON, mscOFF, tdyopt, optBG, changename, changemusic, changefx, noprotocol, t2, t3, t4, t5, refresh, tdy;
	public static Texture Main1024, Game1024, moleDep1024, lose, PS, powerup, powerupDep2, powerupDep1;
	public static TextureRegion powerless, earthquake, moleshower, silencer, molejam, kingmole, mouldy, dummymole, moleshield;
	public static TextureRegion gameBG, ps, rpl, ext, rsm, psBG, borderVert, borderHor, hmr, titlepuDep, shield;
	public static TextureRegion background, strB, optB, hghB, Title, m1, m3, m5, sm,king,
	enterIP, loading, cnl, mdm1, mdm3, mdm5, mdsm, titleDep, Lose, dplyOK, dplyCD, dplySEL, blockgrid;
	public static TextureRegion eq, ms, ns, np, nc, sc, fog, km, inv, cdown;
	public static TextureRegion logo, mswlogo, startButtonUp, startButtonDown, winTextR;
	public static TextureRegion fogEffect, dummyMOLE, dc;
	public static TextureRegion mh1, mh2, mh3, mh4, mh5, op1, op2, op3, op4, op5;
	public static TextureRegion bg, Hm, title, startButt, optButt, scoreButt, mp, 
		mole1, mole3, mole5, moleSabo, pause, play, cancel, replay, ipButt;
	
	//128 x 128

	public static Sound  dummy, shower, blockG, shieldS,kinglaugh, hit, explode, clicksound, gameover, 
	button, back, sent, slctd, popup, earthQk,fogg, block;
	public static Music summer, ann;

	
	public static void load()
	{
		trudy = new Texture(Gdx.files.internal("data/trudy.png"));
		trudy.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		tdy = new TextureRegion(trudy, 0, 0, 544, 816);
		tdy.flip(false,true);
		
		optionsSC = new Texture(Gdx.files.internal("data/optionsSC.png"));
		optionsSC.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		optBG = new TextureRegion(optionsSC, 0, 0,  544, 816);
		optBG.flip(false, true);
		
		
		tdyopt = new TextureRegion(optionsSC, 12,829, 258,90);
		tdyopt.flip(false,true);
		
		refresh = new TextureRegion(optionsSC, 556,937,260,87);
		refresh.flip(false,true);
		
		changename = new TextureRegion(optionsSC, 558, 829, 258, 90);
		changename.flip(false, true);
		
		changemusic = new TextureRegion(optionsSC, 558, 717, 258, 90);
		changemusic.flip(false,true);
		
		fxON = new TextureRegion(optionsSC, 15,929, 258,90);
		fxON.flip(false,true);
		
		mscOFF= new TextureRegion(optionsSC, 290,827, 258, 90);
		mscOFF.flip(false,true);
		
		changefx = new TextureRegion(optionsSC, 558, 605, 258, 90);
		changefx.flip(false, true);
		
		noprotocol = new TextureRegion(optionsSC, 558, 494, 258, 90);
		noprotocol.flip(false,true);
		
		t2 = new TextureRegion(optionsSC, 558,46, 258,90);
		t2.flip(false,true);
		
		t3 = new TextureRegion(optionsSC, 558,158, 258, 90);
		t3.flip(false,true);
		
		t4 = new TextureRegion(optionsSC, 558,271, 258, 90);
		t4.flip(false,true);
		
		t5 = new TextureRegion(optionsSC, 556, 381, 258, 90);
		t5.flip(false,true);
		
		mismatch = new Texture(Gdx.files.internal("data/mp.png"));
		mismatch.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		mp = new TextureRegion(mismatch	, 0, 0, 544, 816);
		mp.flip(false,true);
		
		invulnerable = new Texture(Gdx.files.internal("data/invulnerability.png"));
		invulnerable.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		shield = new TextureRegion(invulnerable, 9, 9, 529, 556);
		shield.flip(false,true);
		
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
		font.setScale(1f, -1f);
		
		powerupDep1 = new Texture(Gdx.files.internal("data/powerdep1.png"));
		powerupDep1.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		powerless = new TextureRegion(powerupDep1, 11,14, 445, 140);
		powerless.flip(false,true);
		
		earthquake = new TextureRegion(powerupDep1, 12, 170, 445, 140);
		earthquake.flip(false,true);
		
		moleshower = new TextureRegion(powerupDep1, 12, 330, 445, 140);
		moleshower.flip(false,true);
		
		silencer = new TextureRegion(powerupDep1, 12, 490, 445, 140);
		silencer.flip(false,true);
		
		molejam = new TextureRegion(powerupDep1, 12, 654, 445, 140);
		molejam.flip(false,true);
		
		moleshield = new TextureRegion(powerupDep1, 12, 815, 445, 140);
		moleshield.flip(false,true);
		
		titlepuDep=  new TextureRegion(powerupDep1, 512,30,230,98);
		titlepuDep.flip(false,true);
		
		blockgrid = new TextureRegion(powerupDep1, 485,160, 141,129);
		blockgrid.flip(false, true);
		
		dummyMOLE = new TextureRegion(powerupDep1, 494,316, 113,114);
		dummyMOLE.flip(false,true);
		
		powerupDep2 = new Texture(Gdx.files.internal("data/powerdep2.png"));
		powerupDep2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		kingmole = new TextureRegion(powerupDep2, 11, 15, 445, 140);
		kingmole.flip(false,true);
		
		mouldy = new TextureRegion(powerupDep2, 10,173,445,140);
		mouldy.flip(false, true);
		
		dummymole = new TextureRegion(powerupDep2, 11,335, 445, 140);
		dummymole.flip(false,true);
		
		fogEffect = new TextureRegion(powerupDep2, 452,457, 546, 551);
		powerup = new Texture(Gdx.files.internal("data/Powerup.png"));
		powerup.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		eq = new TextureRegion(powerup, 10, 8, 147, 76);
		eq.flip(false,true);
		
		ms = new TextureRegion(powerup, 10, 575, 147, 76);
		ms.flip(false, true);
		
		nc = new TextureRegion(powerup,10, 190, 147, 76);
		nc.flip(false,true);
		
		np = new TextureRegion(powerup, 10,107, 147, 76);
		np.flip(false,true);
		
		ns = new TextureRegion(powerup, 10,287, 147, 76);
		ns.flip(false,true);
		
		fog = new TextureRegion (powerup, 10,479, 147, 76);
		fog.flip(false,true);
		
		km = new TextureRegion(powerup, 12, 671, 147, 76);
		km.flip(false,true);
		
		sc = new TextureRegion(powerup, 11, 383, 147, 76);
		sc.flip(false, true);
		
		inv = new TextureRegion(powerup, 167,108, 147, 76);
		inv.flip(false,true);
		
		mh1 = new TextureRegion(powerup, 23,882, 113,20);
		mh1.flip(false,true);
		
		mh2 = new TextureRegion(powerup, 23, 850, 113, 20);
		mh2.flip(false, true);
		
		mh3 = new TextureRegion(powerup, 23, 821, 113, 20);
		mh3.flip(false,true);
		
		mh4 = new TextureRegion(powerup, 23, 794, 113, 20);
		mh4.flip(false,true);
		
		mh5 = new TextureRegion(powerup, 23, 762, 113, 20);
		mh5.flip(false,true);
		
		op1 = new TextureRegion(powerup, 279,906, 30, 25);
		op1. flip(false,true);
		
		op2 = new TextureRegion(powerup, 259, 875, 47, 25);
		op2.flip(false,true);
		
		op3 = new TextureRegion(powerup, 237, 837, 72, 25);
		op3.flip(false,true);
		
		op4 = new TextureRegion(powerup, 214, 801, 98, 25);
		op4.flip(false,true);
		
		op5 = new TextureRegion(powerup, 195, 761, 112, 25);
		op5.flip(false,true);
		
		cdown = new TextureRegion(powerup, 168, 12, 146, 75);
		cdown.flip(false,true);
		
		dc = new TextureRegion(powerup, 479, 0, 544, 816);
		dc.flip(false,true);
		
		PS = new Texture(Gdx.files.internal("data/pause.png"));
		PS.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		psBG = new TextureRegion(PS, 0, 0, 514, 816);
		psBG.flip(false,true);
		
		moleDep1024 = new Texture(Gdx.files.internal("data/moleDep.png"));
		moleDep1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		titleDep = new TextureRegion(moleDep1024, 40,740,324,133);
		titleDep.flip(false,true);
		
		mdm1 = new TextureRegion(moleDep1024, 0,0,457	,153);
		mdm1.flip(false,true);
		
		mdm3 = new TextureRegion(moleDep1024, 0,168,457	,153);
		mdm3.flip(false,true);
		
		mdm5 = new TextureRegion(moleDep1024, 0,345,457	,153);
		mdm5.flip(false,true);
		
		mdsm = new TextureRegion(moleDep1024, 0,528,457,153);
		mdsm.flip(false,true);
		
		Game1024 = new Texture(Gdx.files.internal("data/GameBackground.png"));
		Game1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		hmr = new TextureRegion(Game1024 , 562,644,90,69);
		hmr.flip(false,true);
		
		gameBG = new TextureRegion(Game1024, 0, 0, 543,816);
		gameBG.flip(false, true);
		
		cnl = new TextureRegion(Game1024, 563,735,83,82);
		cnl.flip(false,true);
		
		ps = new TextureRegion(Game1024, 559, 215, 83, 82);
		ps.flip(false,true);
		
		rpl = new TextureRegion(Game1024, 559, 379, 83, 82);
		rpl.flip(false,true);
		
		ext = new TextureRegion(Game1024, 559, 296, 83, 82);
		ext.flip(false,true);
		
		rsm = new TextureRegion(Game1024, 559, 137, 83, 82);
		rsm.flip(false,true);
		
		borderVert = new TextureRegion(Game1024, 690,294, 30, 489);
		borderVert.flip(false,true);
		
		borderHor = new TextureRegion(Game1024, 4, 822, 490,23);
		borderHor.flip(false,true);
		
		dplyOK = new TextureRegion(Game1024, 685, 92, 100,92);
		dplyOK.flip(false,true);
		
		dplySEL = new TextureRegion(Game1024, 690, 0, 99, 90);
		dplySEL.flip(false,true);
		
		dplyCD = new TextureRegion(Game1024, 689, 185, 99, 95);
		dplyCD.flip(false,true);
		
		
		
		Main1024 = new Texture(Gdx.files.internal("data/1024Main.png"));
		Main1024.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		background = new TextureRegion(Main1024, 0, 0, 543, 816);
		background.flip(false,true);
		
		Title = new TextureRegion(Main1024, 545, 0, 336, 170);
		Title.flip (false,true);
		
		king = new TextureRegion(Main1024, 35, 845, 138, 138);
		king.flip(false,true);
		
		strB = new TextureRegion(Main1024, 545, 172, 260,89 );
		strB.flip(false,true);
		
		optB = new TextureRegion(Main1024, 545, 260, 260, 91);
		optB.flip(false,true);
		
		hghB = new TextureRegion(Main1024, 545, 350, 260, 91);
		hghB.flip(false,true);
		
		sm = new TextureRegion(Main1024, 690, 564, 113,114);
		sm.flip(false,true);
		
		m1 = new TextureRegion(Main1024, 548, 445, 113,114);
		m1.flip(false, true);
		
		m3 = new TextureRegion(Main1024, 680,448, 113,114);
		m3.flip(false, true);
		
		m5 = new TextureRegion(Main1024, 554,565,113,114);
		m5.flip(false, true);
		
		enterIP = new TextureRegion(Main1024, 550,680,260,100);
		enterIP.flip(false, true);
		
		loading = new TextureRegion(Main1024, 550,800,260,89);
		loading.flip(false,true);
		
		lose = new Texture(Gdx.files.internal("data/lose.png"));
		lose.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		Lose = new TextureRegion(lose, 0,0,544,816);
		Lose.flip(false,true);
		
		win = new Texture(Gdx.files.internal("data/WIN_LOSEscreen.png"));
		win.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		winTextR = new TextureRegion(win, 0,0,544,816);
		winTextR.flip(false,true);
		

		logoTexture = new Texture(Gdx.files.internal("data/DenySplash.png"));
        logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        //815 is the width, 335 is the height//
        logo = new TextureRegion(logoTexture, 0, 0, 960, 470);
        
		texture = new Texture(Gdx.files.internal("data/texture.gif"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		bg = new TextureRegion(texture, 0, 0, 310, 452);
		bg.flip(false,  true);
		
		title = new TextureRegion(texture, 314, 0, 200, 98);
		title.flip(false, true);
		
		startButt = new TextureRegion(texture, 314, 102, 158, 56);
		startButt.flip(false, true);
		
		optButt = new TextureRegion(texture, 314, 162, 158, 56);
		optButt.flip(false, true);
		
		scoreButt = new TextureRegion(texture, 314, 222, 158, 56);
		scoreButt.flip(false, true);
		
		mole1 = new TextureRegion(texture, 320, 278, 56, 52);
		mole1.flip(false, true);
		
		mole3 = new TextureRegion(texture, 378, 282, 58, 48);
		mole3.flip(false, true);
		
		mole5 = new TextureRegion(texture, 322, 334, 58, 46);
		mole5.flip(false, true);
		
		moleSabo = new TextureRegion(texture, 372, 334, 58, 48);
		moleSabo.flip(false, true);
		
		pause = new TextureRegion(texture, 420, 282, 58, 48);
		pause.flip(false, true);
		
		play = new TextureRegion(texture, 435, 330, 58, 48);
		play.flip(false, true);
		
		cancel = new TextureRegion(texture, 322, 386, 58, 48);
		cancel.flip(false, true);
		
		replay = new TextureRegion(texture, 372, 382, 58, 48);
		replay.flip(false, true);
		
		ipButt = new TextureRegion(texture, 2, 455, 158, 56);
		ipButt.flip(false, true);
        

        //The Sound Effects//
        hit = Gdx.audio.newSound(Gdx.files.internal("data/Bash.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("data/Boom.mp3"));
        clicksound = Gdx.audio.newSound(Gdx.files.internal("data/ButtonClick.mp3"));
        gameover = Gdx.audio.newSound(Gdx.files.internal("data/gameover.wav"));

       
        button = Gdx.audio.newSound(Gdx.files.internal("data/button-20.wav"));
        //buttons for back
        back = Gdx.audio.newSound(Gdx.files.internal("data/button-09.wav"));
        //main screen and every other screen
        summer = Gdx.audio.newMusic(Gdx.files.internal("data/summer.mp3"));
        //in game music
        ann = Gdx.audio.newMusic(Gdx.files.internal("data/ann.mp3"));
        sent = Gdx.audio.newSound(Gdx.files.internal("data/button-7.wav"));
        slctd = Gdx.audio.newSound(Gdx.files.internal("data/button-28.wav"));
        popup =Gdx.audio.newSound(Gdx.files.internal("data/button-10.wav"));
        earthQk = Gdx.audio.newSound(Gdx.files.internal("data/grenade.mp3"));
        fogg = Gdx.audio.newSound(Gdx.files.internal("data/fog.mp3"));
        block= Gdx.audio.newSound(Gdx.files.internal("data/buzzer.mp3"));
        shieldS = Gdx.audio.newSound(Gdx.files.internal("data/shield.mp3"));
        kinglaugh = Gdx.audio.newSound(Gdx.files.internal("data/king.mp3"));
        blockG = Gdx.audio.newSound(Gdx.files.internal("data/block.mp3"));
        shower = Gdx.audio.newSound(Gdx.files.internal("data/shower.mp3"));
        dummy = Gdx.audio.newSound(Gdx.files.internal("data/dummy.mp3"));
	}
	
	public static void dispose() {
        // We must dispose of the texture when we are finished.
		logoTexture.dispose();
		font.dispose();
		texture.dispose(); logoTexture.dispose(); win.dispose();mismatch.dispose();optionsSC.dispose();
		Main1024.dispose(); Game1024.dispose(); moleDep1024.dispose(); lose.dispose();
		PS.dispose(); powerup.dispose(); powerupDep2.dispose(); powerupDep1.dispose();
		hit.dispose(); explode.dispose(); clicksound.dispose(); gameover.dispose();
		button.dispose(); back.dispose();summer.dispose(); ann.dispose(); sent.dispose(); slctd.dispose(); popup.dispose();
		 dummy.dispose(); shower.dispose(); blockG.dispose(); shieldS.dispose();kinglaugh.dispose();
		 earthQk.dispose();fogg.dispose(); block.dispose();
		//        // Dispose sounds
//        dead.dispose();
//        flap.dispose();
//        coin.dispose();
//
//        font.dispose();
//        shadow.dispose();
    }

}
