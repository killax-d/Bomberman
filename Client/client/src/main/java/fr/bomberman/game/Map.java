package fr.bomberman.game;

import java.util.Random;

import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public class Map {

	public static final int TILE_FREE = 0;
	public static final int ROCK_TILE = 1;
	public static final int PLANT_TILE = 2;
	public static final int FLOWER_TILE = 3;
	public static final int BOMB_TILE = 4;

	public static final int MAP_WIDTH = 25;
	public static final int MAP_HEIGHT = 15;
	public static final int TILE_SCALE = 64;
	
	private int plantChance;

	private int[][] map;

	public Map() {
		this.plantChance = GuiIngame.instance().getPlantChance();
		this.map = new int[MAP_WIDTH][MAP_HEIGHT];
		generateMap();
	}

	private void generateMap() {
		Random rand = new Random();
		// Generate the flowers and rocks on the map
		boolean rockTile;
		for (int i = 0; i < MAP_WIDTH; i++) {
			for (int j = 0; j < MAP_HEIGHT; j++) {
				rockTile = (i == 0 || j == 0 || i + 1 == MAP_WIDTH || j + 1 == MAP_HEIGHT
						|| (j % 2 == 0 && i % 2 == 0));
				if (rockTile) {
					map[i][j] = ROCK_TILE;
				} else {
					int r = rand.nextInt(100);
					if (r < 5) {
						map[i][j] = FLOWER_TILE;
					} else if (r < plantChance) {
						map[i][j] = PLANT_TILE;
					}
				}
			}
		}
	}

	public int getTileTypeAt(int x, int y) {
		return map[x][y];
	}

	public int getTileTypeAt(float x, float y) {
		return getTileTypeAt((int) x, (int) y);
	}
	
	public int getTileTypeAt(Vec2D point) {
		return getTileTypeAt(point.getX(), point.getY());
	}

	public void setTileTypeAt(int x, int y, int type) {
		map[x][y] = type;
	}
	
	public void setTileTypeAt(float x, float y, int type) {
		map[(int) x][(int) y] = type;
	}

}
