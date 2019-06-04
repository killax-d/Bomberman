package fr.bomberman.game;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Controls {

	public static Object[] FORWARD = new Object[] {"Forward", 90}; // UP
	public static Object[] BACKWARD = new Object[] {"Backward", 83}; // DOWN
	public static Object[] STRAFE_RIGHT = new Object[] {"Strafe Right", 68}; // RIGHT
	public static Object[] STRAFE_LEFT = new Object[] {"Strafe Left", 81}; // LEFT
	public static Object[] BOMB = new Object[] {"Drop Bomb", 32}; // BOMB
	
	private String name;
	private int keyId;
	
	private static HashMap<Object[], Controls> controls = new HashMap<Object[], Controls>();
	
	public static Controls getControl(Object[] control) {
		if (controls.containsKey(control)) {
			return controls.get(control);
		}
		Controls c = new Controls(control);
		controls.put(control, c);
		return c;
	}
	
	Controls(Object[] control){
		this.name = (String) control[0];
		this.keyId = (int) control[1];
	}
	
	public int getKeyCode() {
		return keyId;
	}
	
	public String getControlName() {
		return name;
	}
	
	public String getKeyText() {
		if (keyId == -1)
			return "...";
		if (keyId == 0)
			return "?";
		return KeyEvent.getKeyText(keyId);
	}
	
	public void setKeyCode(int id) {
		if (id != 0)
			for (Object[] control : controls.keySet()) {
				if (controls.get(control) != this && controls.get(control).getKeyCode() == id) {
					controls.get(control).setKeyCode(0);
				}
			}
		keyId = id;
	}
}
