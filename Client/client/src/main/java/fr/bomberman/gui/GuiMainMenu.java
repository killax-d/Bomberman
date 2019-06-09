package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.App;
import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.assets.Init;

public class GuiMainMenu extends Container implements MouseListener, MouseMotionListener, KeyListener {
	
	// Credit
	private final String author = "Donné Dylan";
	
	// Font
	private Font pokemon_font = Assets.getFont("Pokemon Solid.ttf", 25f);
	
	// Text
	private GuiTextBox credit = new GuiTextBox(String.format("Developed by %s\n"
			+ "Version : %s",
			author, App.version),
			new Font("Arial", Font.ITALIC, 25), GuiTextBox.Align.RIGHT, GuiTextBox.BaseLine.BOTTOM,
			0, 0, GameWindow.WIDTH-10, GameWindow.HEIGHT, true);
	private GuiTextBox playText = new GuiTextBox("Play",
			pokemon_font, GuiTextBox.Align.LEFT, GuiTextBox.BaseLine.CENTER,
			0, 0, 128, 64, Color.BLACK);
	private GuiTextBox settingsText = new GuiTextBox("Settings",
			pokemon_font, GuiTextBox.Align.LEFT, GuiTextBox.BaseLine.CENTER,
			0, 0, 192, 64, Color.BLACK);
	private GuiTextBox resumeText = new GuiTextBox("Resume",
			pokemon_font, GuiTextBox.Align.LEFT, GuiTextBox.BaseLine.CENTER,
			0, 0, 192, 64, Color.BLACK);
	
	// Image
	private BufferedImage background = Assets.getImage("title_background.png");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage character = Assets.getImage("title_character.png");
	private BufferedImage play_icon = Assets.getImage("play_icon.png");
	private BufferedImage settings_icon = Assets.getImage("settings_icon.png");
	private BufferedImage resume_icon = Assets.getImage("resume_icon.png");
	private BufferedImage close_texture = Assets.getImage("close.jpg");
	
	// Demo Mode
	private BufferedImage demo_on_texture = Assets.getImage("demo_on.png");
	private BufferedImage demo_off_texture = Assets.getImage("demo_off.png");
	
	// Sound
	private BufferedSound music = Assets.getSound("sounds/menu.wav", BufferedSound.MUSIC);
	private BufferedSound pikachu = Assets.getSound("sounds/pikachu.wav", BufferedSound.SFX);
	
	// Button
	private int button_height = 64;
	
	// Positions
	private int[] positionButton = new int[] {
		(int) (GameWindow.getCenter().getY()-(button_height/2)),
		(int) (GameWindow.getCenter().getY()+(button_height/2)),
		(int) (GameWindow.getCenter().getY()+(button_height/2)+button_height)
	};

	private GuiButton settings = new GuiButton(settings_icon, settingsText, 0, positionButton[0], 192, button_height).align(GuiButton.Align.CENTER);
	private GuiButton play = new GuiButton(play_icon, playText, 0, positionButton[1], 128, button_height).align(GuiButton.Align.CENTER);
	private GuiButton resume = new GuiButton(resume_icon, resumeText, 0, positionButton[2], 160, button_height).align(GuiButton.Align.CENTER);
	private GuiButton close = new GuiButton(close_texture, GameWindow.WIDTH-64, 0, 64, 32);
	private GuiButton demo = new GuiButton(demo_off_texture, 50, GameWindow.HEIGHT-(480+25), 480, 480);
	
	// Game Parameters
	private static GuiSpinner lives = new GuiSpinner("%d Live", 50, 150, 300, 50, 1, 5, (int) GameWindow.getFields(GameWindow.Fields.LIVES.ordinal()), GameWindow.Fields.LIVES.ordinal());
	private static GuiSpinner AIPlayer = new GuiSpinner("%d Player", 50, 200, 300, 50, 2, 4, (int) GameWindow.getFields(GameWindow.Fields.AIPLAYER.ordinal()), GameWindow.Fields.AIPLAYER.ordinal());
	private static GuiSpinner team = new GuiSpinner("Team : %s", 50, 250, 300, 50, GuiSpinner.FALSE, GuiSpinner.TRUE, (int) GameWindow.getFields(GameWindow.Fields.TEAM.ordinal()), GameWindow.Fields.TEAM.ordinal());
	private static GuiSlider plantChance = new GuiSlider("Plant chance : %d", 50, 300, 300, 50, 0, 100, (int) (GameWindow.getFields(GameWindow.Fields.PLANT_CHANCE.ordinal())), GameWindow.Fields.PLANT_CHANCE.ordinal(), GuiSlider.Theme.DARK.ordinal());
	private static GuiSlider itemChance = new GuiSlider("Item chance : %d", 50, 350, 300, 50, 0, 100, (int) (GameWindow.getFields(GameWindow.Fields.ITEM_CHANCE.ordinal())), GameWindow.Fields.ITEM_CHANCE.ordinal(), GuiSlider.Theme.DARK.ordinal());
	private static GuiInput playerName = new GuiInput("Name : %s", 50, 400, 300, 50, 0, 12, (String) (GameWindow.getFields(GameWindow.Fields.PLAYER_NAME.ordinal())), GameWindow.Fields.PLAYER_NAME.ordinal());
	
	public GuiMainMenu() {
		music.setLoop(true);
		music.setLoopPoint(14, -1);
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(character, 0, 0, getWidth(), getHeight(), null);
		
		close.paint(g);
		if (GameWindow.instance().isInDemoMode())
			demo.setImage(demo_on_texture);
		demo.paint(g);
		settings.paint(g);
		if (GuiIngame.instance() != null)
			resume.paint(g);
		play.paint(g);
		lives.paint(g);
		AIPlayer.paint(g);
		team.paint(g);
		plantChance.paint(g);
		itemChance.paint(g);
		playerName.paint(g);
		credit.paint(g);
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		lives.mouseClicked(event);
		AIPlayer.mouseClicked(event);
		team.mouseClicked(event);
		playerName.mouseClicked(event);
		if (play.isHovered()) {
			music.stop();
			GameWindow.instance().setCurrentGui(new GuiIngame());
		}
		if (resume.isHovered()) {
			music.stop();
			if(GuiIngame.instance() != null) {
				GameWindow.instance().setCurrentGui(GuiIngame.instance());
				GuiIngame.instance().resume();
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

	@Override
	public void keyPressed(KeyEvent event) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	}

	@Override
	public void keyTyped(KeyEvent event) {
		playerName.keyTyped(event);
	}

}
