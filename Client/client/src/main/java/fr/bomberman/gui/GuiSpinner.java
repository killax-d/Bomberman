package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class GuiSpinner implements MouseListener, MouseMotionListener {

	private static Set<GuiSpinner> spinners = new HashSet<GuiSpinner>();
	private final static int fontSize = 26;
	public static int TRUE = -1;
	public static int FALSE = -2;
	
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
	private boolean teamModePossible;
	
	private static int teamModeId = GameWindow.Fields.TEAM.ordinal();
	
	public GuiSpinner(String text, int x, int y, int width, int height, int min, int max, int value, int type) {
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
		spinners.add(this);
		if (type == teamModeId)
			teamModePossible = true;
	}
	
	public int getType() {
		return type;
	}
	
	public static GuiSpinner getSpinner(int type) {
		for (GuiSpinner spinner : spinners) {
			if(spinner != null && spinner.getType() == type)
				return spinner;
		}
		return null;
	}
	
	public void setTeamModePossible(boolean teamMode) {
		teamModePossible = teamMode;
		setValue(FALSE);
	}

	public void setValue(int value) {
		if (value < min)
			this.value = min;
		else if (value > max)
			this.value = max;
		else
			this.value = value;
		
		GameWindow.setFields(type, value);
		if (type == GameWindow.Fields.AIPLAYER.ordinal()) {
			GuiSpinner spinner = null;
			if ((spinner = getSpinner(GameWindow.Fields.TEAM.ordinal())) != null) {
				spinner.setTeamModePossible(value % 2 == 0);
			}
		}
	}
	
	public void paint(Graphics g) {
		visible = true;

		g.setClip(0, 0, GameWindow.WIDTH, GameWindow.HEIGHT);

		g.setFont(new Font("Arial", Font.BOLD, fontSize));
		
		int arrowWidth = g.getFontMetrics().stringWidth("<");
		g.setColor(value > min ? Color.LIGHT_GRAY : Color.BLACK);
		g.fillRect(x, y, 50, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 50, height);
		g.setColor(value > min ? Color.WHITE : Color.LIGHT_GRAY);
		g.drawString("<" , x+25-arrowWidth/2, y+height/2+fontSize/3+2);
		
		
		if (type == teamModeId & !teamModePossible)
			g.setColor(Color.BLACK);
		else
			g.setColor(value < max ? Color.LIGHT_GRAY : Color.BLACK);
		g.fillRect(x+width-50, y, 50, height);
		g.setColor(Color.BLACK);
		g.drawRect(x+width-50, y, 50, height);
		if (type == teamModeId & !teamModePossible)
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(value < max ? Color.WHITE : Color.LIGHT_GRAY);
		g.drawString(">" , x+25+(width-50)-arrowWidth/2, y+height/2+fontSize/3+2);
		
		String label = "";
		
		if(value != TRUE && value != FALSE)
			label = String.format(text, value).concat(value > 1 ? "s" : "");
		else
			label = String.format(text, (value == FALSE ? "no" : "yes"));
		g.setColor(Color.BLACK);
		g.fillRect(x+50, y, width-100, height);
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(x+50, y, width-100, height);

		int textWidth = g.getFontMetrics().stringWidth(label);
		g.setColor(Color.WHITE);
		g.drawString(label, x+50+(width-100)/2-textWidth/2, y+height/2+fontSize/3+2);
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
		if(event.getY() >= y && event.getY() <= y+height)
			if(event.getX() >= x && event.getX() <= x+50 && value > min)
				setValue(--value);
			else if(event.getX() >= x+width-50 && event.getX() <= x+width && value < max)
				if(type != teamModeId | (type == teamModeId && teamModePossible))
					setValue(++value);
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

}