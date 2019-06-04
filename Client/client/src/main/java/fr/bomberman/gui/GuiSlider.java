package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;

public class GuiSlider implements MouseListener, MouseMotionListener {

	private final static int fontSize = 26;
	
	private boolean visible;
	private boolean hovered;
	private String text;
	private int x;
	private int y;
	private int width;
	private int height;
	private int min;
	private int max;
	private int value;
	private int type;
	private boolean editing;
	
	public GuiSlider(String text, int x, int y, int width, int height, int min, int max, int value, int type) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.min = min;
		this.max = max;
		this.value = value;
		this.type = type;
		editing = false;
		visible = false;
	}

	public void setValue(int value) {
		if (value < min)
			this.value = min;
		else if (value > max)
			this.value = max;
		else
			this.value = value;
	}
	
	public void paint(Graphics g) {
		visible = true;
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		int textWidth = g.getFontMetrics().stringWidth(text);

		g.setColor(Color.WHITE);
		
		g.drawString(String.format(text, value).concat("%") , x+width/2-textWidth/2, y+height/2+fontSize/3+2);
		g.drawRect(x, y, width, height);
		g.fillRect(x, y, (width/100)*value, height);
	}
	
	public boolean isHovered() {
		return hovered;
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (visible && event.getX() >= x && event.getX() <= x + width && event.getY() >= y && event.getY() <= y + height) {
			hovered = true;
		} else {
			hovered = false;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		editing = true;
		if (visible && event.getY() >= y && event.getY() <= y + height) {
			int x = event.getX()-50;
			float value = (float)x/(float)(width);
			int percent = (int) (value*100);
			setValue(percent);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		editing = true;
		if (visible && event.getY() >= y && event.getY() <= y + height) {
			int x = event.getX()-50;
			float value = (float)x/(float)(width);
			int percent = (int) (value*100);
			setValue(percent);
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
		if(editing) {
			BufferedSound.setVolumeType(type, (float) value/100);
			Assets.adjustVolume();
		}
		editing = false;
	}

}