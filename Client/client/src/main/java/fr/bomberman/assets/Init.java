package fr.bomberman.assets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.bomberman.game.Controls;

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
				
				bf.write(String.format("%s:%s", "MUSIC", BufferedSound.MUSIC_VOLUME));
				bf.newLine();
				bf.write(String.format("%s:%s", "SFX", BufferedSound.SFX_VOLUME));
				
				for (String control : Controls.getControls().keySet()) {
					Controls c = Controls.getControl(control);
					if (c != null) {
						bf.newLine();
						bf.write(String.format("%s:%s", control, c.getKeyCode()));
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
					String[] args = line.split(":");
					if(args.length == 2)
						if (line.startsWith("MUSIC"))
							BufferedSound.setVolumeType(BufferedSound.MUSIC, Float.parseFloat(args[1]));
						else if (line.startsWith("SFX"))
							BufferedSound.setVolumeType(BufferedSound.SFX, Float.parseFloat(args[1]));
						else
							new Controls(args[0], Integer.parseInt(args[1]));
					else
						throw new Exception();
						
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Controls.initDefaultControl();
			writeIniFile();
		}
	}
}
