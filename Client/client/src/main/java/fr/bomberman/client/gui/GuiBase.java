package fr.bomberman.client.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class GuiBase extends Container implements KeyListener, MouseListener, MouseMotionListener {

	private Set<IGuiElement> uiElements;

	public GuiBase() {
		uiElements = new HashSet<IGuiElement>();
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paint(Graphics g) {
		for (IGuiElement element : uiElements) {
			element.paint(g);
		}
		super.paint(g);
	}

	public void mouseClicked(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseClicked(event);
		}
	}

	public void mouseEntered(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseEntered(event);
		}
	}

	public void mouseExited(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseExited(event);
		}
	}

	public void mousePressed(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mousePressed(event);
		}
	}

	public void mouseReleased(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseReleased(event);
		}
	}

	public void mouseDragged(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseDragged(event);
		}
	}

	public void mouseMoved(MouseEvent event) {
		for (IGuiElement element : uiElements) {
			element.mouseMoved(event);
		}
	}

	public void keyPressed(KeyEvent event) {
		for (IGuiElement element : uiElements) {
			element.keyPressed(event);
		}
	}

	public void keyReleased(KeyEvent event) {
		for (IGuiElement element : uiElements) {
			element.keyReleased(event);
		}
	}

	public void keyTyped(KeyEvent event) {
		for (IGuiElement element : uiElements) {
			element.keyTyped(event);
		}
	}

	public void addElement(IGuiElement element) {
		uiElements.add(element);
	}

	public Set<IGuiElement> getUiElements() {
		return uiElements;
	}

}
