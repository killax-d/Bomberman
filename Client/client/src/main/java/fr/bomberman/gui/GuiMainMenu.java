package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;

public class GuiMainMenu extends Container implements MouseListener, MouseMotionListener {

	// Image
	private BufferedImage background = Assets.getImage("title_background.png");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage one_player = Assets.getImage("one_player.png");
	private BufferedImage close_texture = Assets.getImage("close.jpg");
	private BufferedImage resume_texture = Assets.getImage("resume.png");
	
	// Sound
	private BufferedSound music = Assets.getSound("sounds/menu.wav");

	private GuiButton play = new GuiButton(one_player, GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2-32, 128, 64);
	private GuiButton resume = new GuiButton(resume_texture,  GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2+32, 128, 64);
	private GuiButton close = new GuiButton(close_texture, 0, 0, 64, 32);

	public GuiMainMenu() {
		music.setLoop(true);
		music.setLoopPoint(14, -1);
		music.play();
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		play.paint(g);
		close.paint(g);
		if(GuiIngame.instance != null)
			resume.paint(g);
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (play.isHovered()) {
			music.stop();
			GameWindow.instance().setCurrentGui(new GuiIngame());
		}
		if (resume.isHovered()) {
			music.stop();
			if(GuiIngame.instance != null) {
				GameWindow.instance().setCurrentGui(GuiIngame.instance);
				GuiIngame.instance.resume();
			}
			else
				GameWindow.instance().setCurrentGui(new GuiIngame());
		}
		if (close.isHovered()) {
			System.exit(0);
		}
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
		if (play.isHovered()) {
			music.stop();
			GameWindow.instance().setCurrentGui(new GuiIngame());
		}
		if (resume.isHovered()) {
			music.stop();
			if(GuiIngame.instance != null) {
				GameWindow.instance().setCurrentGui(GuiIngame.instance);
				GuiIngame.instance.resume();
			}
			else
				GameWindow.instance().setCurrentGui(new GuiIngame());
		}
		if (close.isHovered()) {
			System.exit(0);
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
		resume.mouseMoved(event);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
		resume.mouseMoved(event);
	}

}
