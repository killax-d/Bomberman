package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;

public class GuiSlider implements MouseListener, MouseMotionListener {

	private static Set<GuiSlider> sliders = new HashSet<GuiSlider>();
	private final static int fontSize = 26;
	
	public static enum Theme {
		LIGHT, DARK;
	}
	
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
	private int theme;
	
	public GuiSlider(String text, int x, int y, int width, int height, int min, int max, int value, int type, int theme) {
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
		this.theme = theme;
		sliders.add(this);
	}
	
	public GuiSlider(String text, int x, int y, int width, int height, int min, int max, int value, int type) {
		this(text, x, y, width, height, min, max, value, type, 0);
	}
	
	public static GuiSlider getSlider(int type) {
		for (GuiSlider slider : sliders) {
			if(slider != null && slider.getType() == type)
				return slider;
		}
		return null;
	}

	public int getTheme(int id) {
		return theme;
	}
	
	public int getThemeType(int id) {
	    switch (Theme.values()[id]) {
	    	case LIGHT:
	    		return 0;
	    	case DARK:
	    		return 1;
	    	default:
	    		return 0;
	    }
	}
	
	public int getType() {
		return type;
	}

	public void setValue(int value) {
		if (value < min)
			this.value = min;
		else if (value > max)
			this.value = max;
		else
			this.value = value;
		if(type == GameWindow.Fields.PLANT_CHANCE.ordinal() || type == GameWindow.Fields.ITEM_CHANCE.ordinal())
			GameWindow.setFields(type, value);
	}
	
	public void paint(Graphics g) {
		visible = true;
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		int textWidth = g.getFontMetrics().stringWidth(text);

		g.setColor(theme == getThemeType(Theme.LIGHT.ordinal()) ? Color.WHITE : Color.BLACK);
		g.drawRect(x, y, width, height);
		g.fillRect(x, y, (width/100)*value, height);

		g.setColor(Color.WHITE);
		g.drawString(String.format(text, value).concat("%") , x+width/2-textWidth/2, y+height/2+fontSize/3+2);
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
			if(value >= 0.0 && value <= 1.0) {
				setValue(percent);
				if(type == BufferedSound.MUSIC || type == BufferedSound.MUSIC) {
					BufferedSound.setVolumeType(type, value);
					Assets.adjustVolume();
				}
				else
					GameWindow.setFields(type, percent);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (visible && event.getX() >= x && event.getX() <= x + width && event.getY() >= y && event.getY() <= y + height) {
			int x = event.getX()-50;
			float value = (float)x/(float)(width);
			int percent = (int) (value*100);
			if(value >= 0.0 && value <= 1.0) {
				setValue(percent);
				if(type == BufferedSound.MUSIC || type == BufferedSound.MUSIC) {
					BufferedSound.setVolumeType(type, value);
					Assets.adjustVolume();
				}
				else
					GameWindow.setFields(type, percent);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(editing) {
			if(type == BufferedSound.MUSIC || type == BufferedSound.MUSIC) {
				BufferedSound.setVolumeType(type, (float) value/100);
				Assets.adjustVolume();
			}
			else
				GameWindow.setFields(type, value);
		}
		editing = false;
	}

}