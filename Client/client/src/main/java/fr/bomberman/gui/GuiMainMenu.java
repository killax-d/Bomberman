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
import fr.bomberman.assets.Init;

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
	private GuiButton close = new GuiButton(close_texture, GameWindow.WIDTH-64, 0, 64, 32);
	private GuiButton demo = new GuiButton(demo_off_texture, 50, GameWindow.HEIGHT-(480+50), 480, 480);
	
	// Game Parameters
	private static GuiSpinner lives = new GuiSpinner("%d Live", 50, 150, 300, 50, 1, 5, GameWindow.getFields(GameWindow.Fields.LIVES.ordinal()), GameWindow.Fields.LIVES.ordinal());
	private static GuiSpinner AIPlayer = new GuiSpinner("%d Player", 50, 200, 300, 50, 2, 4, GameWindow.getFields(GameWindow.Fields.AIPLAYER.ordinal()), GameWindow.Fields.AIPLAYER.ordinal());
	private static GuiSpinner team = new GuiSpinner("Team : %s", 50, 250, 300, 50, GuiSpinner.FALSE, GuiSpinner.TRUE, GameWindow.getFields(GameWindow.Fields.TEAM.ordinal()), GameWindow.Fields.TEAM.ordinal());
	private static GuiSlider plantChance = new GuiSlider("Plant chance : %d", 50, 300, 300, 50, 0, 100, (GameWindow.getFields(GameWindow.Fields.PLANT_CHANCE.ordinal())), GameWindow.Fields.PLANT_CHANCE.ordinal(), GuiSlider.Theme.DARK.ordinal());
	private static GuiSlider itemChance = new GuiSlider("Item chance : %d", 50, 350, 300, 50, 0, 100, (GameWindow.getFields(GameWindow.Fields.ITEM_CHANCE.ordinal())), GameWindow.Fields.ITEM_CHANCE.ordinal(), GuiSlider.Theme.DARK.ordinal());
	
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

		g.setFont(new Font("Arial", Font.ITALIC, 25));
		int i = 0;
		for(String line : credit.split("\n")) {
			if(line != null) {
				int textWidth = g.getFontMetrics().stringWidth(line);
				g.setColor(Color.BLACK);
				g.drawString(line, getWidth()-textWidth-17, getHeight()-i*25-17);
				g.setColor(Color.WHITE);
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
		lives.paint(g);
		AIPlayer.paint(g);
		team.paint(g);
		plantChance.paint(g);
		itemChance.paint(g);
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
		lives.mouseClicked(event);
		AIPlayer.mouseClicked(event);
		team.mouseClicked(event);
		Init.writeIniFile();
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		plantChance.mousePressed(event);
		itemChance.mousePressed(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		plantChance.mouseReleased(event);
		itemChance.mouseReleased(event);
		Init.writeIniFile();
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		plantChance.mouseDragged(event);
		itemChance.mouseDragged(event);
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
