package fr.bomberman.assets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.bomberman.game.Controls;
import fr.bomberman.gui.GameWindow;
import fr.bomberman.gui.GuiInput;
import fr.bomberman.gui.GuiSlider;
import fr.bomberman.gui.GuiSpinner;

public class Init {
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String path;
	private static File file;
	
	public Init() {
		path = System.getProperty("user.home").concat("/.pokebomber/");
		if(OS.indexOf("win") >= 0)
			path.replace("/", "\\");
		file = new File(path.concat("pokebomber.ini"));
		checkFile();
	}
	
	private static void checkFile() {
		if(!file.exists()) {
			new File(path).mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Controls.initDefaultControl();
			writeIniFile();
		}
		else
			readIniFile();
	}
	
	public static void writeIniFile() {
		try(BufferedWriter bf = new BufferedWriter(new FileWriter(file))){
			if(bf != null) {
				
				bf.write(String.format("%s : %s", "MUSIC", BufferedSound.MUSIC_VOLUME));
				bf.newLine();
				bf.write(String.format("%s : %s", "SFX", BufferedSound.SFX_VOLUME));
				bf.newLine();
				bf.write(String.format("%s : %s", "LIVES", GameWindow.getFields(GameWindow.Fields.LIVES.ordinal())));
				bf.newLine();
				bf.write(String.format("%s : %s", "AIPLAYER", GameWindow.getFields(GameWindow.Fields.AIPLAYER.ordinal())));
				bf.newLine();
				bf.write(String.format("%s : %s", "TEAM", GameWindow.getFields(GameWindow.Fields.TEAM.ordinal())));
				bf.newLine();
				bf.write(String.format("%s : %s", "PLANT", GameWindow.getFields(GameWindow.Fields.PLANT_CHANCE.ordinal())));
				bf.newLine();
				bf.write(String.format("%s : %s", "ITEM", GameWindow.getFields(GameWindow.Fields.ITEM_CHANCE.ordinal())));
				bf.newLine();
				bf.write(String.format("%s : %s", "NAME", GameWindow.getFields(GameWindow.Fields.PLAYER_NAME.ordinal())));
				
				for (String control : Controls.getControls().keySet()) {
					Controls c = Controls.getControl(control);
					if (c != null) {
						bf.newLine();
						bf.write(String.format("%s : %s // %s", control, c.getKeyCode(), c.getControlName()));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void readIniFile() {
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			if(br != null) {
				String line = "";
				while((line = br.readLine()) != null) {
					String[] args = line.split(" : ");
					if(args.length >= 2)
						if (line.startsWith("MUSIC"))
							BufferedSound.setVolumeType(BufferedSound.MUSIC, Float.parseFloat(args[1]));
						else if (line.startsWith("SFX"))
							BufferedSound.setVolumeType(BufferedSound.SFX, Float.parseFloat(args[1]));
						else if (line.startsWith("LIVES"))
							GuiSpinner.getSpinner(GameWindow.Fields.LIVES.ordinal()).setValue(Integer.valueOf(args[1]));
						else if (line.startsWith("AIPLAYER"))
							GuiSpinner.getSpinner(GameWindow.Fields.AIPLAYER.ordinal()).setValue(Integer.valueOf(args[1]));
						else if (line.startsWith("TEAM"))
							GuiSpinner.getSpinner(GameWindow.Fields.TEAM.ordinal()).setValue(Integer.valueOf(args[1]));
						else if (line.startsWith("PLANT"))
							GuiSlider.getSlider(GameWindow.Fields.PLANT_CHANCE.ordinal()).setValue(Integer.valueOf(args[1]));
						else if (line.startsWith("ITEM"))
							GuiSlider.getSlider(GameWindow.Fields.ITEM_CHANCE.ordinal()).setValue(Integer.valueOf(args[1]));
						else if (line.startsWith("NAME"))
							GuiInput.getInput(GameWindow.Fields.PLAYER_NAME.ordinal()).setValue(args[1]);
						else
							new Controls(args[0], args[1].split(" // ")[1], Integer.parseInt(args[1].split(" // ")[0]));
					else {
						System.err.println(String.format("Error while parsing .ini file"));
					}
						
				}
				
				Assets.adjustVolume();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Controls.initDefaultControl();
			writeIniFile();
		}
	}
}
