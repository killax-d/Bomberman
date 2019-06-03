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

	public static GuiMainMenu instance;
	
	// Image
	private BufferedImage background = Assets.getImage("title_background.png");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage character = Assets.getImage("title_character.png");
	private BufferedImage one_player = Assets.getImage("one_player.png");
	private BufferedImage close_texture = Assets.getImage("close.jpg");
	private BufferedImage resume_texture = Assets.getImage("resume.png");
	
	// Demo Mode
	private BufferedImage demo_on_texture = Assets.getImage("demo_on.png");
	private BufferedImage demo_off_texture = Assets.getImage("demo_off.png");
	
	// Sound
	private BufferedSound music = Assets.getSound("sounds/menu.wav");
	private BufferedSound pikachu = Assets.getSound("sounds/pikachu.wav");

	private GuiButton play = new GuiButton(one_player, GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2-32, 128, 64);
	private GuiButton resume = new GuiButton(resume_texture,  GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2-32, 128, 64);
	private GuiButton close = new GuiButton(close_texture, 0, 0, 64, 32);
	private GuiButton demo = new GuiButton(demo_off_texture, 50, GameWindow.HEIGHT-(640+50), 640, 640);

	public GuiMainMenu() {
		instance = this;
		music.setLoop(true);
		music.setLoopPoint(14, -1);
		music.play();
		pikachu.setVolume(0.40F);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(character, 0, 0, getWidth(), getHeight(), null);
		close.paint(g);
		demo.paint(g);
		if(GuiIngame.instance != null)
			resume.paint(g);
		else
			play.paint(g);
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
		if (demo.isHovered()) {
			GameWindow.instance().switchDemo();
			pikachu.play();
			if (GameWindow.instance().isInDemoMode())
				demo.setImage(demo_on_texture);
			else
				demo.setImage(demo_off_texture);
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
		
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
		resume.mouseMoved(event);
		demo.mouseMoved(event);
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
		resume.mouseMoved(event);
		demo.mouseMoved(event);
	}

}
