package fr.bomberman.assets;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import fr.bomberman.gui.GameWindow;

public class Assets {
	
	/**
	 * @author Donné Dylan
	 * Simple assets loader
	 */

	private static Map<String, BufferedImage> assets = new HashMap<String, BufferedImage>();
	private static Map<String, BufferedSound> sounds = new HashMap<String, BufferedSound>();

	// The fail-safe default texture for missing assets
	private static BufferedImage NO_TEXTURE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	static {
		NO_TEXTURE.setRGB(0, 0, new Color(140, 87, 113).getRGB());
	}
	
	
	/**
	 * Image finder and loader
	 * @param path
	 * @return BufferedImage
	 */
	public static BufferedImage getImage(String path) {
		if (assets.containsKey(path)) {
			return assets.get(path);
		}
		try (InputStream is = Assets.class.getResourceAsStream(path)) {
			if (is != null) {
				BufferedImage img = ImageIO.read(is);
				assets.putIfAbsent(path, img);
				return img;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		assets.putIfAbsent(path, NO_TEXTURE);
		return NO_TEXTURE;
	}

	
	/**
	 * Sound finder and loader
	 * @param path
	 * @return BufferedSound
	 */
	public static BufferedSound getSound(String path, int type) {
		if (sounds.containsKey(path)) {
			return sounds.get(path);
		}
		try (InputStream is = Assets.class.getResourceAsStream(path)) {
			if (is != null) {
				BufferedSound sound = new BufferedSound(is, type);
				sounds.putIfAbsent(path, sound);
				return sound;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void adjustVolume() {
		for (BufferedSound sound : sounds.values()) {
			if(sound != null) {
				if(sound.getType() == BufferedSound.MUSIC)
					sound.setVolume(GameWindow.MUSIC_VOLUME);
				if(sound.getType() == BufferedSound.SFX)
					sound.setVolume(GameWindow.SFX_VOLUME);
			}
		}
	}

	
	/**
	 * Sprite cropper
	 * @param path
	 * @return BufferedImage
	 */
	public static BufferedImage getTile(String path, int width, int height, int x, int y) {
		BufferedImage tileset = getImage(path);
		boolean validTexture = tileset.getWidth()>= width && tileset.getHeight() >= height;
		if (validTexture) {
			return tileset.getSubimage(x * width, y * height, width, height);
		}
		return NO_TEXTURE;
	}

}
