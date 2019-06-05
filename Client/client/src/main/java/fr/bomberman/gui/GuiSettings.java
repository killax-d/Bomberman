package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.game.Controls;

public class GuiSettings extends Container implements MouseListener, MouseMotionListener, KeyListener {

	private GuiSlider music_volume = new GuiSlider("Music Volume : %d", 50, 50+(int) (GuiControlLabel.caseHeight * 7.5), SettingsWindow.WIDTH - 100, GuiControlLabel.caseHeight, 0, 100, (int) (BufferedSound.MUSIC_VOLUME*100), BufferedSound.MUSIC);
	private GuiSlider sfx_volume = new GuiSlider("SFX Volume : %d", 50, 50+(int) (GuiControlLabel.caseHeight * 9), SettingsWindow.WIDTH - 100, GuiControlLabel.caseHeight, 0, 100, (int) (BufferedSound.SFX_VOLUME*100), BufferedSound.SFX);
	
	private boolean waitingForKey;
	private GuiControlLabel ControlWaiting;
	private int tmpKeyCode;
	
	private GuiControlLabel UP = new GuiControlLabel(Controls.FORWARD, 50, 50, GuiControlLabel.caseWidth, GuiControlLabel.caseHeight);
	private GuiControlLabel DOWN = new GuiControlLabel(Controls.BACKWARD, 50, 50+(int) (GuiControlLabel.caseHeight * 1.5), GuiControlLabel.caseWidth, GuiControlLabel.caseHeight);
	private GuiControlLabel LEFT = new GuiControlLabel(Controls.STRAFE_LEFT, 50, 50+(int) (GuiControlLabel.caseHeight * 3), GuiControlLabel.caseWidth, GuiControlLabel.caseHeight);
	private GuiControlLabel RIGHT = new GuiControlLabel(Controls.STRAFE_RIGHT, 50, 50+(int) (GuiControlLabel.caseHeight * 4.5), GuiControlLabel.caseWidth, GuiControlLabel.caseHeight);
	private GuiControlLabel BOMB = new GuiControlLabel(Controls.BOMB, 50, 50+(int) (GuiControlLabel.caseHeight * 6), GuiControlLabel.caseWidth, GuiControlLabel.caseHeight);
	
	public static enum Fields {
		MUSIC, SFX;
	}

	// texture
	private BufferedImage close_texture = Assets.getImage("close.jpg");
	
	// button
	private GuiButton close = new GuiButton(close_texture, 0, 0, 64, 32);
	
	
	public GuiSettings() {
		waitingForKey = false;
		ControlWaiting = null;
	}

	@Override
	public void paint(Graphics g) {
		close.paint(g);
		UP.paint(g);
		DOWN.paint(g);
		LEFT.paint(g);
		RIGHT.paint(g);
		BOMB.paint(g);
		music_volume.paint(g);
		sfx_volume.paint(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (close.isHovered()) {
			SettingsWindow.instance().close();
		}
		if (!waitingForKey) {
			if(UP.isHovered())
				ControlWaiting = UP;
			else if(DOWN.isHovered())
				ControlWaiting = DOWN;
			else if(LEFT.isHovered())
				ControlWaiting = LEFT;
			else if(RIGHT.isHovered())
				ControlWaiting = RIGHT;
			else if(BOMB.isHovered())
				ControlWaiting = BOMB;
			else
				return;
			if(ControlWaiting != null)
				waitingForKey = true;
				tmpKeyCode = ControlWaiting.getControlCode().getKeyCode();
				ControlWaiting.setControleCode(-1);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		music_volume.mouseDragged(e);
		sfx_volume.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		close.mouseMoved(e);
		UP.mouseMoved(e);
		DOWN.mouseMoved(e);
		LEFT.mouseMoved(e);
		RIGHT.mouseMoved(e);
		BOMB.mouseMoved(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		music_volume.mousePressed(e);
		sfx_volume.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		music_volume.mouseReleased(e);
		sfx_volume.mouseReleased(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(waitingForKey) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				ControlWaiting.setControleCode(tmpKeyCode);
				waitingForKey = false;
				ControlWaiting = null;
			}
			if (ControlWaiting != null) {
				ControlWaiting.setControleCode(e.getKeyCode());
				waitingForKey = false;
				ControlWaiting = null;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
