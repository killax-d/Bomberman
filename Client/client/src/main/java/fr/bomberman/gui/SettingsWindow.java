package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import fr.bomberman.App;
import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.assets.Init;
import fr.bomberman.game.Controls;

public class SettingsWindow extends JFrame implements KeyListener, MouseListener, MouseMotionListener {
	
	public static SettingsWindow window;
	public final static int WIDTH = 600;
	public final static int HEIGHT = 600;
	
	// Sound
	private static final BufferedSound SFX_ImpossibleAction = Assets.getSound("sounds/impossible_action.wav", BufferedSound.SFX);
	
	private Timer clock;
	

	public SettingsWindow(String title) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		if (App.dg.isWindowTranslucencySupported(WindowTranslucency.TRANSLUCENT))
			setBackground(new Color(0, 0, 0, 0.75F));
		else {
	    	System.err.println("Transparent background not supported, set BLACK background color instead");
			setBackground(Color.BLACK);
		}
		setForeground(Color.WHITE);
		getRootPane().setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0.75F, 0.75F, 0.75F, 0.75F)));
		setTitle(title);
		setCurrentGui(new GuiSettings());
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
	}
	
	public void setCurrentGui(Container gui) {
		setContentPane(gui);
		revalidate();
	}

	public Container getCurrentGui() {
		return getContentPane();
	}
	
	public void close() {
		if(!Controls.hasInvalidKey()) {
			Init.writeIniFile();
			GameWindow.instance().setEnabled(true);
			stopClock();
			setVisible(false);
		}
		else {
			SFX_ImpossibleAction.play();
	    	System.err.println("Settings has invalid key");
		}
	}
	
	public static SettingsWindow instance() {
		if (window == null) {
			window = new SettingsWindow("Settings");
		}
		GameWindow.instance().setEnabled(false);
		window.setVisible(true);
		window.clock = new Timer();
		window.registerClock();
		return window;
	}
	
	private void stopClock() {
		clock.cancel();
		clock.purge();
	}
	
	private void registerClock() {
		clock.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				window.getCurrentGui().repaint();
			}
		}, 1000, 34);
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (getCurrentGui() instanceof MouseMotionListener) {
			((MouseMotionListener) getCurrentGui()).mouseDragged(event);
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		if (getCurrentGui() instanceof MouseMotionListener) {
			((MouseMotionListener) getCurrentGui()).mouseMoved(event);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		if (getCurrentGui() instanceof MouseListener) {
			((MouseListener) getCurrentGui()).mouseClicked(event);
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		if (getCurrentGui() instanceof MouseListener) {
			((MouseListener) getCurrentGui()).mouseEntered(event);
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		if (getCurrentGui() instanceof MouseListener) {
			((MouseListener) getCurrentGui()).mouseExited(event);
		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (getCurrentGui() instanceof MouseListener) {
			((MouseListener) getCurrentGui()).mousePressed(event);
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (getCurrentGui() instanceof MouseListener) {
			((MouseListener) getCurrentGui()).mouseReleased(event);
		}
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (getCurrentGui() instanceof KeyListener) {
			((KeyListener) getCurrentGui()).keyPressed(event);
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (getCurrentGui() instanceof KeyListener) {
			((KeyListener) getCurrentGui()).keyReleased(event);
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		if (getCurrentGui() instanceof KeyListener) {
			((KeyListener) getCurrentGui()).keyTyped(event);
		}
	}
	
}
