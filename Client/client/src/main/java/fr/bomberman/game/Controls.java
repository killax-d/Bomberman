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
		initDefaultControl();
		return getControl(control);
	}
	
	public static HashMap<String, Controls> getControls(){
		return controls;
	}
	
	public static void initDefaultControl(){
		new Controls("UP", "Forward", 90); // Forward
		new Controls("DOWN", "Backward", 83); // Backward
		new Controls("RIGHT", "Strafe Right", 68); // Strafe Right
		new Controls("LEFT", "Strafe Left", 81); // Strafe Left
		new Controls("BOMB", "Drop Bomb", 32); // Bomb
	}
	
	public Controls(String controlName, String control, int keyId) {
		this.name = control;
		this.keyId = keyId;
		controls.put(controlName, this);
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
		if (id > 0)
			for (String control : controls.keySet()) {
				if (getControl(control) != this && getControl(control).getKeyCode() == id) {
					controls.get(control).setKeyCode(0);
				}
			}
		keyId = id;
	}
	
	public static boolean hasInvalidKey() {
		for (String control : controls.keySet()) {
			if (getControl(control) != null && getControl(control).getKeyCode() <= 0) {
				System.err.println(String.format("Invalid key : %s with %s | code : %d", getControl(control).getControlName(), getControl(control).getKeyText(), getControl(control).getKeyCode()));
				return true;
			}
		}
		return false;
	}
}
