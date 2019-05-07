package fr.bomberman.client.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;

public class GuiMainMenu extends GuiBase {

	private BufferedImage background = Assets.getImage("title_background.jpg");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage one_player = Assets.getImage("one_player.png");

	private GuiButton quit = new GuiButton(one_player, 50, 50, 100, 20);
	private Player player = new Player();

	public GuiMainMenu() {
		addElement(quit);
		addElement(player);
	}

	@Override
	public void paint(Graphics g) {
		//g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		//g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (quit.isHovered()) {
			System.exit(0);
		}
		super.mouseClicked(event);
	}

}
