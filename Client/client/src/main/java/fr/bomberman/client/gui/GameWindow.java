package fr.bomberman.client.gui;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

	private GameWindow window;
	private GuiBase currentGui;

	public GameWindow(String title, int width, int height) {
		this.window = this;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setTitle(title);
		setCurrentGui(new GuiMainMenu());
		setSize(width, height);
		setLocationRelativeTo(null);
		setVisible(true);
		registerClock();
	}

	public void setCurrentGui(GuiBase gui) {
		setContentPane(gui);
		this.currentGui = gui;
	}

	public GuiBase getCurrentGui() {
		return currentGui;
	}

	private void registerClock() {
		Timer clock = new Timer();
		clock.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				window.getContentPane().repaint();
			}
		}, 0, 17);
	}

}
