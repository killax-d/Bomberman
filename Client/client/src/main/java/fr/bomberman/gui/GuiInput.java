package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class GuiInput implements MouseListener, MouseMotionListener, KeyListener{

	private static Set<GuiInput> inputs = new HashSet<GuiInput>();
	private final static int fontSize = 26;
	
	private boolean visible;
	private boolean hovered;
	private boolean focused;
	private String text;
	private int x;
	private int y;
	private int width;
	private int height;
	private int min;
	private int max;
	private String value;
	private int type;
	
	private float typeBar;
	
	public GuiInput(String text, int x, int y, int width, int height, int min, int max, String value, int type) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.min = min;
		this.max = max;
		this.value = value;
		this.type = type;
		visible = false;
		inputs.add(this);
		focused = false;
		typeBar = 0.0F;
	}
	
	public int getType() {
		return type;
	}
	
	public static GuiInput getInput(int type) {
		for (GuiInput input : inputs) {
			if(input != null && input.getType() == type)
				return input;
		}
		return null;
	}

	public void setValue(String value) {
		if (value.length() >= min && value.length() <= max) {
			this.value = value;
			GameWindow.setFields(type, value);
		}
	}
	
	public String getValue() {
		return value;
	}
	
	public void paint(Graphics g) {
		visible = true;
		
		String label = String.format(text, value);
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width, height);
		g.drawRect(x, y, width, height);
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		if(focused) {
			g.setColor(new Color(1F, 1F, 1F, typeBar/100));
			int labelWidth = g.getFontMetrics().stringWidth(label);
			typeBar = (typeBar+2) % 50;
			g.drawLine(x+labelWidth+10+3, y+15, x+labelWidth+10+3, y+height-10);
		}
		g.setColor(Color.WHITE);
		g.drawString(label, x+10, y+height/2+fontSize/3+2);
	}
	
	public float f(float x) {
		return (float) (-(Math.pow(x, 2))+x);
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
		
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if(visible && event.getX() >= x && event.getX() <= x+width && event.getY() >= y && event.getY() <= y+height) {
			focused = true;
		}
		else {
			focused = false;
			if(getValue() == "")
				setValue("Player");
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
	public void keyPressed(KeyEvent event) {
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (visible && focused) {
			if((int) event.getKeyChar() == 8)
				if (value.length() > 1)
					setValue(value.substring(0, value.length()-1));
				else
					setValue("");
			else if((int) event.getKeyChar() == 10) {
				if(getValue() == "")
					setValue("Player");
				focused = false;
			}
			else if(Character.isAlphabetic(event.getKeyChar()))
				setValue(value+event.getKeyChar());
		}
	}

}