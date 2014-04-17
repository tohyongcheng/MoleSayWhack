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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader 
{
	//The Textures//

	public static Texture texture, logoTexture, win;
	public static Texture Main1024, Game1024, moleDep1024, lose, PS, powerup, powerupDep2, powerupDep1;
	public static TextureRegion powerless, earthquake, moleshower, silencer, molejam, kingmole, mouldy, dummymole, moleshield;
	public static TextureRegion gameBG, ps, rpl, ext, rsm, psBG, borderVert, borderHor, hmr, titlepuDep;
	public static TextureRegion background, strB, optB, hghB, Title, m1, m3, m5, sm,
	enterIP, loading, cnl, mdm1, mdm3, mdm5, mdsm, titleDep, Lose, dplyOK, dplyCD, dplySEL;
	public static TextureRegion eq, ms, ns, np, nc, sc, fog, km;
	public static TextureRegion logo, mswlogo, startButtonUp, startButtonDown, winTextR;

	public static TextureRegion bg, Hm, title, startButt, optButt, scoreButt, 
		mole1, mole3, mole5, moleSabo, pause, play, cancel, replay, ipButt;
	
	//128 x 128

	public static Sound  hit, explode, clicksound, gameover, button, back,summer, ann, sent, slctd, popup;


	
	public static void load()
	{
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
		
		powerupDep2 = new Texture(Gdx.files.internal("data/powerdep2.png"));
		powerupDep2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		kingmole = new TextureRegion(powerupDep2, 11, 15, 445, 140);
		kingmole.flip(false,true);
		
		mouldy = new TextureRegion(powerupDep2, 10,173,445,140);
		mouldy.flip(false, true);
		
		dummymole = new TextureRegion(powerupDep2, 11,335, 445, 140);
		dummymole.flip(false,true);
		
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
        summer = Gdx.audio.newSound(Gdx.files.internal("data/summer.mp3"));
        //in game music
        ann = Gdx.audio.newSound(Gdx.files.internal("data/ann.mp3"));
        sent = Gdx.audio.newSound(Gdx.files.internal("data/button-7.wav"));
        slctd = Gdx.audio.newSound(Gdx.files.internal("data/button-28.wav"));
        popup =Gdx.audio.newSound(Gdx.files.internal("data/button-10.wav"));

	}
	
	public static void dispose() {
        // We must dispose of the texture when we are finished.
		logoTexture.dispose();

//        // Dispose sounds
//        dead.dispose();
//        flap.dispose();
//        coin.dispose();
//
//        font.dispose();
//        shadow.dispose();
    }

}
