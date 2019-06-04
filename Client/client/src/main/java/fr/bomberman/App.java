package fr.bomberman;

import fr.bomberman.assets.Init;
import fr.bomberman.gui.GameWindow;

public class App {
	
	public static Init iniFile = new Init();

	public static void main(String[] args) {
		GameWindow.instance();
	}

}
