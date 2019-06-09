package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GuiTextBox {

	public static enum Align{
		LEFT, CENTER, RIGHT;
	}
	
	public static enum BaseLine{
		TOP, CENTER, BOTTOM;
	}
	
	private String text;
	private Font font;
	private Align align;
	private BaseLine baseLine;
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean shadow;
	private Color color;
	private Color colorShadow;
	
	private String[] lines;
	
	public GuiTextBox(String text, Font font, Align align, BaseLine baseLine, int x, int y, int width, int height, boolean shadow, Color color, Color colorShadow) {
		this.text = text;
		this.font = font;
		this.align = align;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.baseLine = baseLine;
		this.shadow = shadow;
		this.color = color;
		this.colorShadow = colorShadow;

		lines = text.split("\n");
	}
	
	
	public GuiTextBox(String text, Font font, Align align, BaseLine baseLine, int x, int y, int width, int height, boolean shadow, Color color) {
		this(text, font, align, baseLine, x, y, width, height, shadow, color, Color.BLACK);
	}
	
	public GuiTextBox(String text, Font font, Align align, BaseLine baseLine, int x, int y, int width, int height, boolean shadow) {
		this(text, font, align, baseLine, x, y, width, height, shadow, Color.WHITE, Color.BLACK);
	}
	
	public GuiTextBox(String text, Font font, Align align, BaseLine baseLine, int x, int y, int width, int height, Color color) {
		this(text, font, align, baseLine, x, y, width, height, false, color, Color.BLACK);
	}
	
	public GuiTextBox(String text, Font font, Align align, BaseLine baseLine, int x, int y, int width, int height) {
		this(text, font, align, baseLine, x, y, width, height, false, Color.WHITE, Color.BLACK);
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String getText() {
		return text;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void paint(Graphics g) {
		g.setFont(font);
		g.setClip(x, y, width, height);
		int i = 0;
		for(String line : lines) {
			if (line != null) {
				int StrWidth = g.getFontMetrics().stringWidth(line);
				int strHeight = g.getFontMetrics().getHeight();

				int xLocation = x;
				if(align == Align.CENTER)
					xLocation = x+(width/2 - StrWidth/2);
				if(align == Align.RIGHT)
					xLocation = x+(width - StrWidth);
				
				int yLocation = y+((i+1)*strHeight);
				if (baseLine == BaseLine.CENTER)
					yLocation = y+((i*strHeight) + strHeight/2) + height/2;
				if (baseLine == BaseLine.BOTTOM)
					yLocation = y+height-((i*strHeight));

				if(shadow) {
					g.setColor(colorShadow);
					g.drawString(line, xLocation-2, yLocation-12);
				}

				g.setColor(color);
				g.drawString(line, xLocation, yLocation-10); // Décalage de 10 ?
				i++;
			}
		}
	}
}
