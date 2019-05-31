package fr.bomberman.gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;

public class GuiButton implements MouseMotionListener {

	private String text;
	private int x;
	private int y;
	private int width;
	private int height;
	private BufferedImage image;
	private Color color;
	private Color colorHovered;
	private boolean hovered;
	private static BufferedSound SFX_ButtonHover = Assets.getSound("sounds/button.wav");

	public GuiButton(BufferedImage image, int x, int y, int width, int height) {
		this("", x, y, width, height);
		this.image = image;
	}

	public GuiButton(String text, int x, int y, int width, int height, Color color, Color colorHovered) {
		this("", x, y, width, height);
		this.color = color;
		this.colorHovered = colorHovered;
	}

	public GuiButton(String text, int x, int y, int width, int height) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hovered = false;
		this.image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		this.color = new Color(255, 255, 255, 200);
		this.colorHovered = new Color(255, 255, 255, 230);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (!hovered) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F));
		}
		if (image != null) {
			g2d.drawImage(image, x, y, width, height, null);
		} else {
			g.setColor(hovered ? colorHovered : color);
			g.fillRect(x, y, width, height);

		}

	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public String getText() {
		return this.text;
	}
	
	public boolean isHovered() {
		return hovered;
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (event.getX() >= x && event.getX() <= x + width && event.getY() >= y && event.getY() <= y + height) {
			if(!hovered)
				SFX_ButtonHover.play();
			hovered = true;
		} else {
			hovered = false;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		if (event.getX() >= x && event.getX() <= x + width && event.getY() >= y && event.getY() <= y + height) {
			if(!hovered)
				SFX_ButtonHover.play();
			hovered = true;
		} else {
			hovered = false;
		}
	}

}
