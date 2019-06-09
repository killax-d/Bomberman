package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import fr.bomberman.game.Map;
import fr.bomberman.utils.Vec2D;

public class GameWindow extends JFrame implements KeyListener, MouseListener, MouseMotionListener {

	private static GameWindow window;
	private static boolean demo = false;
	private static int lives = 3;
	private static int AIPlayer = 2;
	private static int team = GuiSpinner.FALSE;
	private static int plantChance = 70;
	private static int itemChance = 50;
	private static String playerName = "Player";
	public static int WIDTH;
	public static int HEIGHT;
	
	public static enum Fields{
		LIVES, AIPLAYER, TEAM, PLANT_CHANCE, ITEM_CHANCE, PLAYER_NAME;
	}
	
	private GameWindow(String title, int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setTitle(title);
		setCurrentGui(new GuiMainMenu());
		setSize(width, height);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
		registerClock();
		addMouseListener(this);
		addKeyListener(this);
		addMouseMotionListener(this);
	}
	
	public static Vec2D getCenter() {
		return new Vec2D(WIDTH/2, HEIGHT/2);
	}
	
	public static void setFields(int type, Object value){
	    switch (Fields.values()[type]) {
	    	case LIVES:
	    		lives = (int) value >= 1 && (int) value <= 5 ? (int) value : 1;
	    		break;
	    	case AIPLAYER:
	    		AIPlayer = (int) value >= 2 && (int) value <= 4 ? (int) value : 2;
	    		break;
	    	case TEAM:
	    		team = (int) value == GuiSpinner.TRUE || (int) value == GuiSpinner.FALSE ? (int) value : GuiSpinner.FALSE;
	    		break;
	    	case PLANT_CHANCE:
	    		plantChance = (int) value >= 0 && (int) value <= 100 ? (int) value : 70;
	    		break;
	    	case ITEM_CHANCE:
	    		itemChance = (int) value >= 0 && (int) value <= 100 ? (int) value : 50;
	    		break;
	    	case PLAYER_NAME:
	    		playerName = (String) value;
	    		break;
	    	default:
	    		break;
	    }
	}
	
	public static Object getFields(int type){
	    switch (Fields.values()[type]) {
	    	case LIVES:
	    		return (int) lives;
	    	case AIPLAYER:
	    		return (int) AIPlayer;
	    	case TEAM:
	    		return (int) team;
	    	case PLANT_CHANCE:
	    		return (int) plantChance;
	    	case ITEM_CHANCE:
	    		return (int) itemChance;
	    	case PLAYER_NAME:
	    		return (String) playerName;
	    	default:
	    		return 0;
	    }
		
	}
	
	public int getTotalLives() {
		return (int) getFields(GameWindow.Fields.LIVES.ordinal());
	}
	
	public int getTotalAIPlayer() {
		return (int) getFields(GameWindow.Fields.AIPLAYER.ordinal());
	}
	
	public boolean isTeamMode() {
		return (int) getFields(GameWindow.Fields.TEAM.ordinal()) == GuiSpinner.TRUE ? true : false;
	}
	
	public int getPlantChance() {
		return (int) getFields(GameWindow.Fields.PLANT_CHANCE.ordinal());
	}
	
	public int getItemChance() {
		return (int) getFields(GameWindow.Fields.ITEM_CHANCE.ordinal());
	}
	
	public String getPlayerName() {
		return (String) playerName;
	}
	
	// DEMO
	public void switchDemo() {
		if (demo)
			demo = false;
		else
			demo = true;
	}

	// DEMO
	public boolean isInDemoMode() {
		return demo;
	}
	
	public void setCurrentGui(Container gui) {
		setContentPane(gui);
		revalidate();
	}

	public Container getCurrentGui() {
		return getContentPane();
	}

	private void registerClock() {
		Timer clock = new Timer();
		clock.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				window.getCurrentGui().repaint();
			}
		}, 1000, 17);
	}

	public static GameWindow instance() {
		if (window == null) {
			window = new GameWindow("PokeBomber", Map.MAP_WIDTH * Map.TILE_SCALE,
					(Map.MAP_HEIGHT) * Map.TILE_SCALE - 2);
		}
		return window;
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
