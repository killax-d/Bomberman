package fr.bomberman.client.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import fr.bomberman.skins.PlayerSkin;

public class Player implements IGuiElement {

	private int x;
	private int y;
	private PlayerSkin skin;
	
	public Player(int x, int y, PlayerSkin skin) {
		this.x = x;
		this.y = y;
		this.skin = skin;
	}
	
	public Player(int x, int y) {
		this(x, y, new PlayerSkin("default", 1));
	}
	
	public Player() {
		this(0, 0, new PlayerSkin("default", 1));
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(skin.getImage(), x, y, skin.getWidth(), skin.getHeight(), null);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch(event.getKeyCode()) {
			default:
				System.out.println(event.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	}

	@Override
	public void keyTyped(KeyEvent event) {
		
	}

}
