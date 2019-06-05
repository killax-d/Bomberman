package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import fr.bomberman.game.Controls;

public class GuiControlLabel extends GuiButton implements MouseMotionListener {
	
	public final static int caseWidth = 120;
	public final static int caseHeight = 50;
	public final static int fontSize = 30;
	private Controls control;
	
	private int xTextLabel = -1;
	private int yTextLabel = -1;
	private int xText = -1;
	private int yText = -1;

	public GuiControlLabel(String control, int x, int y, int width, int height) {
		super(Controls.getControl(control).getControlName(), x, y, width, height, Color.WHITE, Color.GRAY);
		this.control = Controls.getControl(control);
		visible = false;
		setText(this.control.getKeyText());
	}
	
	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	private void textPosition(Graphics g) {
		if (xText == -1 && yText == -1) {
			int textWidth = g.getFontMetrics().stringWidth(control.getKeyText());
			xText = x+width/2-textWidth/2;
			yText = y+height/2+fontSize/3+2;
			xTextLabel = x+width+fontSize/2;
			yTextLabel = y+height/2+fontSize/3+2;
		}
	}

	@Override
	public void paint(Graphics g) {
		visible = true;
		textPosition(g);
		
		g.setColor((isHovered() ? colorHovered : color));
		g.drawRect(x, y, width, height);
		
		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		
		g.drawString(control.getKeyText(), xText, yText);
		g.drawString(control.getControlName(), xTextLabel, yTextLabel);
	}

	public Controls getControlCode() {
		return control;
	}
	
	public void setControleCode(int id) {
		control.setKeyCode(id);
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
