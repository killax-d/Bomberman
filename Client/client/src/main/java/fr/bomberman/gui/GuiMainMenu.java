package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;

public class GuiMainMenu extends Container implements MouseListener, MouseMotionListener {

	private BufferedImage background = Assets.getImage("title_background.jpg");
	private BufferedImage title = Assets.getImage("title_titletext.png");
	private BufferedImage one_player = Assets.getImage("one_player.png");
	private BufferedImage close_texture = Assets.getImage("close.jpg");

	private GuiButton play = new GuiButton(one_player, 50, 50, 100, 20);
	private GuiButton close = new GuiButton(close_texture, 0, 0, 64, 32);

	public GuiMainMenu() {

	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(title, 0, 0, getWidth(), getHeight(), null);
		play.paint(g);
		close.paint(g);
		super.paint(g);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (play.isHovered()) {
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
	}

	@Override
	public void mouseDragged(MouseEvent event) {

	}

	@Override
	public void mouseMoved(MouseEvent event) {
		play.mouseMoved(event);
		close.mouseMoved(event);
	}

}
