package fr.bomberman;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.Init;
import fr.bomberman.gui.GameWindow;

public class App {

	public static GraphicsEnvironment de = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public static GraphicsDevice dg = de.getDefaultScreenDevice();
	public static Init iniFile;
	public static final String version = "1.131-b";

	public static void main(String[] args) {
		GameWindow.instance();
		iniFile = new Init();
		Assets.adjustVolume();
	}

}
