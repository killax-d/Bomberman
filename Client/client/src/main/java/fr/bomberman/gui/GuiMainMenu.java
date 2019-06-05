package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.App;
import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;

public class GuiMainMenu extends Container implements MouseListener, MouseMotionListener {
	
	// Credit
	private final String author = "Donné Dylan";
	private String credit;
	
	// Image
	private BufferedImage background = Assets.getImage("title_background.png");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage character = Assets.getImage("title_character.png");
	private BufferedImage one_player = Assets.getImage("one_player.png");
	private BufferedImage settings_texture = Assets.getImage("settings.png");
	private BufferedImage close_texture = Assets.getImage("close.jpg");
	private BufferedImage resume_texture = Assets.getImage("resume.png");
	
	// Demo Mode
	private BufferedImage demo_on_texture = Assets.getImage("demo_on.png");
	private BufferedImage demo_off_texture = Assets.getImage("demo_off.png");
	
	// Sound
	private BufferedSound music = Assets.getSound("sounds/menu.wav", BufferedSound.MUSIC);
	private BufferedSound pikachu = Assets.getSound("sounds/pikachu.wav", BufferedSound.SFX);

	private GuiButton settings = new GuiButton(settings_texture, GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2-32, 128, 64);
	private GuiButton play = new GuiButton(one_player, GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2+32, 128, 64);
	private GuiButton resume = new GuiButton(resume_texture,  GameWindow.WIDTH/2-64, GameWindow.HEIGHT/2+96, 128, 64);
	private GuiButton close = new GuiButton(close_texture, 0, 0, 64, 32);
	private GuiButton demo = new GuiButton(demo_off_texture, 50, GameWindow.HEIGHT-(640+50), 640, 640);

	public GuiMainMenu() {
		music.setLoop(true);
		music.setLoopPoint(14, -1);
		music.play();
		credit = String.format("Developed by %s\n"
				+ "Version : %s",
				author, App.version);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(character, 0, 0, getWidth(), getHeight(), null);


		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.ITALIC, 25));
		int i = 0;
		for(String line : credit.split("\n")) {
			if(line != null) {
				int textWidth = g.getFontMetrics().stringWidth(line);
				g.drawString(line, getWidth()-textWidth-15, getHeight()-i++*25-15);
			}
		}
		
		close.paint(g);
		if (GameWindow.instance().isInDemoMode())
			demo.setImage(demo_on_texture);
		demo.paint(g);
		settings.paint(g);
		if (GuiIngame.instance != null)
			resume.paint(g);
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
		if (settings.isHovered()) {
			SettingsWindow.instance();
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
		
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
		resume.mouseMoved(event);
		demo.mouseMoved(event);
		settings.mouseMoved(event);
	}

}
