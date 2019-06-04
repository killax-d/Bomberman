package fr.bomberman.game;

import java.awt.event.KeyEvent;
import java.util.HashMap;

public class Controls {

	public static String FORWARD = "UP";
	public static String BACKWARD = "DOWN";
	public static String STRAFE_RIGHT = "RIGHT";
	public static String STRAFE_LEFT = "LEFT";
	public static String BOMB = "BOMB";
	
	private String name;
	private int keyId;
	
	private static HashMap<String, Controls> controls = new HashMap<String, Controls>();
	
	public static Controls getControl(String control) {
		if (controls.containsKey(control)) {
			return controls.get(control);
		}
		Controls c = new Controls(control, 0);
		controls.put(control, c);
		return c;
	}
	
	public static HashMap<String, Controls> getControls(){
		return controls;
	}
	
	public static void initDefaultControl(){
		controls.putIfAbsent("UP", new Controls("Forward", 90)); // Forward
		controls.putIfAbsent("DOWN", new Controls("Backward", 83)); // Backward
		controls.putIfAbsent("RIGHT", new Controls("Strafe Right", 68)); // Strafe Right
		controls.putIfAbsent("LEFT", new Controls("Strafe Left", 81)); // Strafe Left
		controls.putIfAbsent("BOMB", new Controls("Drop Bomb", 32)); // Bomb
	}
	
	public Controls(String control, int keyId){
		this.name = control;
		this.keyId = keyId;
		controls.put(control, this);
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
			for (String control : controls.keySet()) {
				if (getControl(control) != this && getControl(control).getKeyCode() == id) {
					controls.get(control).setKeyCode(0);
				}
			}
		keyId = id;
	}
	
	public static boolean hasInvalidKey() {
		for (String control : controls.keySet()) {
			if (getControl(control) != null && getControl(control).getKeyCode() <= 0)
				return true;
		}
		return false;
	}
}
