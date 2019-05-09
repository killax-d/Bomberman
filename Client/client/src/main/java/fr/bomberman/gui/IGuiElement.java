package fr.bomberman.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface IGuiElement {

	public void paint(Graphics g);
	
	public void mouseClicked(MouseEvent event);

	public void mouseEntered(MouseEvent event);

	public void mouseExited(MouseEvent event);

	public void mousePressed(MouseEvent event);

	public void mouseReleased(MouseEvent event);

	public void mouseDragged(MouseEvent event);

	public void mouseMoved(MouseEvent event);

	public void keyPressed(KeyEvent event);

	public void keyReleased(KeyEvent event);

	public void keyTyped(KeyEvent event);

}
