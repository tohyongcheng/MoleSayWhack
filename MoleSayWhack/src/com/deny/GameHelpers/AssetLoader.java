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
	public static Texture texture, logoTexture;
	public static TextureRegion logo, mswlogo, startButtonUp, startButtonDown;
	public static TextureRegion bg, Hm, title, startButt, optButt, scoreButt, 
		mole1, mole3, mole5, moleSabo, pause, play, cancel, replay, ipButt;
	
	//128 x 128
	public static Sound  hit, explode, clicksound, gameover, button, spawn;
	public static Sound music;
	public static Sequence sequence,sequence1;
	public static Sequencer sequencer,sequencer1;
	
	public static void load()
	{
		
	
		try {
			
			//the midi is in relative path
			URL url = AssetLoader.class.getResource("BTNSummer.mid");
			URL url2 = AssetLoader.class.getResource("BTNANN.mid");
			File file1 = new File(url2.getPath());
			File file = new File(url.getPath());
			//sequence = MidiSystem.getSequence(new File("./BTNANN.mid"));
			sequence = MidiSystem.getSequence(file);
		
			try {
				sequence = MidiSystem.getSequence(file);
				sequencer = MidiSystem.getSequencer();
				sequence1 = MidiSystem.getSequence(file1);
				sequencer1 = MidiSystem.getSequencer();
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				sequencer.open();
				sequencer1.open();
			} catch (MidiUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    sequencer.setSequence(sequence);
		    sequencer.setLoopCount(1000);
		    
		    sequencer1.setSequence(sequence1);
		    sequencer1.setLoopCount(1000);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
        spawn = Gdx.audio.newSound(Gdx.files.internal("data/button-09.wav"));
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
