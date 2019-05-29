package fr.bomberman.assets;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class BufferedSound {
	
	private Clip clip;
	private AudioInputStream ais;

	public BufferedSound(AudioInputStream ais) {
		this.ais = ais;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		open();
		setVolume(0.1F);
	}
	
	public void open() {
		try {
			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setLoop(boolean loop) {
		if(loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		else
			clip.loop(0);
	}
	
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void setVolume(float volume) {
	    if (volume < 0f || volume > 1f)
	        throw new IllegalArgumentException("Volume invalide: " + volume);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
}
