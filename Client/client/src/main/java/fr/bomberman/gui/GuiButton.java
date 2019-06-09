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

	protected String text;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected BufferedImage image;
	protected Color color;
	protected Color colorHovered;
	protected boolean hovered;
	protected static BufferedSound SFX_ButtonHover = Assets.getSound("sounds/button.wav", BufferedSound.SFX);
	protected boolean visible;
	protected GuiTextBox textBox;
	
	public static enum Align {
		LEFT, CENTER, RIGHT;
	}

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
		this.visible = false;
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
	
	public GuiButton(BufferedImage icon, GuiTextBox textBox, int x, int y, int width, int height) {
		this("", x, y, width, height);
		textBox.setWidth(textBox.getWidth()-height);
		this.image = icon;
		this.textBox = textBox;
	}

	public void paint(Graphics g) {
		g.setClip(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);
		this.visible = true;
		Graphics2D g2d = (Graphics2D) g;
		if (!hovered) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F));
		}
		if (textBox == null)
			if (image != null) {
				g2d.drawImage(image, x, y, width, height, null);
			} else {
				g.setColor(hovered ? colorHovered : color);
				g.fillRect(x, y, width, height);
			}
		else {
			if (image != null) {
				g2d.drawImage(image, x, y, height, height, null); // Height x Height proportions;
			}
			if (textBox != null) {
				textBox.setXY(x+height, y);
				textBox.paint(g);
			}
		}

	}

	public GuiButton align(Align align) {
		switch(align) {
			case LEFT:
				this.x = 0;
				break;
			case CENTER:
				this.x = GameWindow.WIDTH/2-width/2;
				break;
			case RIGHT:
				this.x = GameWindow.WIDTH-width;
				break;
		}
		return this;
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
		if (visible && event.getX() >= x && event.getX() <= x + width && event.getY() >= y && event.getY() <= y + height) {
			if(!hovered)
				SFX_ButtonHover.play();
			hovered = true;
		} else {
			hovered = false;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		
	}

}
