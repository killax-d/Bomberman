package fr.bomberman;

import fr.bomberman.assets.Init;
import fr.bomberman.gui.GameWindow;

public class App {
	
	public static Init iniFile = new Init();
	public static final String version = "1.109-b";

	public static void main(String[] args) {
		GameWindow.instance();
	}

}
