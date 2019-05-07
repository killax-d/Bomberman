package fr.bomberman.skins;

import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;

public class PlayerSkin {

	private String skin;
	private int width;
	private int height;
	
	// preparing footstep animation
	private char direction;
	private int totalState;
	private int currentState;
	
	public PlayerSkin(String skin, int totalState) {
		this.skin = skin;
		this.direction = 'U';
		this.totalState = totalState;
		this.currentState = 0;
		this.height = getImage().getHeight();
		this.width = getImage().getWidth();
	}
	
	// Exemple : assets/skins/default_U_1.png
	public BufferedImage getImage() {
		return Assets.getImage("skins/" + skin + "_" + direction + "_" + currentState + ".png");
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setDir(char dir) {
		direction = dir;
	}
	
	public char getDir() {
		return direction;
	}
	
	public void updateAnimation() {
		if (currentState == totalState)
			currentState = 0;
		else
			currentState++;
	}
}
