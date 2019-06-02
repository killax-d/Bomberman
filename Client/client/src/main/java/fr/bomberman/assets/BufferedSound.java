package fr.bomberman.assets;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * 
 * @author Donné Dylan
 * Simple sound Class
 */

public class BufferedSound {
	
	private Clip clip;
	private AudioInputStream audioStream;

	/**
	 * Create an Clip Object
	 * @param is
	 */
	public BufferedSound(InputStream is) {
		try {
			InputStream bufferedIn = new BufferedInputStream(is);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			this.audioStream = audioStream;
			clip = AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
		open();
		setVolume(0.1F);
	}

	/**
	 * Open the clip
	 */
	public void open() {
		try {
			clip.open(audioStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set LOOP mode
	 * (MainMenu and Background Music)
	 * @param loop
	 */
	public void setLoop(boolean loop) {
		if(loop)
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		else
			clip.loop(0);
	}

	/**
	 * Set loop points
	 * @param start // point to start the loop in seconds
	 * @param end // point to start the loop in seconds
	 */
	public void setLoopPoint(int start, int end) {
		clip.setLoopPoints(start, end);
	}

	/**
	 * Play Music
	 * Reset currentFrame to 0 to avoid glitch sound
	 */
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * Stop the Clip music
	 */
	public void stop() {
		clip.stop();
	}

	/**
	 * Set gain Volume
	 * @param volume // change the gain volume with a float (ex 0.05F)
	 */
	public void setVolume(float volume) {
	    if (volume < 0f || volume > 1f)
	        throw new IllegalArgumentException("Volume invalide: " + volume);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
}
