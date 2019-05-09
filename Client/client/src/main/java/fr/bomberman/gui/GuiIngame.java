package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import fr.bomberman.assets.Assets;
import fr.bomberman.game.Entity;
import fr.bomberman.game.EnumDirection;
import fr.bomberman.game.Map;

public class GuiIngame extends Container implements KeyListener {

	// Tile images
	private BufferedImage rock = Assets.getTile("map_tileset.png", 16, 16, 5, 3);
	private BufferedImage plant = Assets.getTile("map_tileset.png", 16, 16, 6, 3);
	private BufferedImage grass = Assets.getTile("map_tileset.png", 16, 16, 7, 3);
	private BufferedImage flower = Assets.getTile("map_tileset.png", 16, 16, 5, 2);
	// Entities on the map
	private Set<Entity> entities;

	private Map map;
	private Entity player;

	public GuiIngame() {
		this.map = new Map();
		this.entities = new HashSet<Entity>();
		this.player = new Entity();
		this.entities.add(player);
		player.setPosition(1, 1);
		player.setMap(map);
	}

	@Override
	public void paint(Graphics g) {
		drawGrass(g);
		drawMap(g);
		drawEntities(g);
		super.paint(g);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.VK_S:
			player.move(EnumDirection.SOUTH);
			break;
		case KeyEvent.VK_Q:
			player.move(EnumDirection.WEST);
			break;
		case KeyEvent.VK_D:
			player.move(EnumDirection.EST);
			break;
		case KeyEvent.VK_Z:
			player.move(EnumDirection.NORTH);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	private void drawEntities(Graphics g) {
		for (Entity entity : entities) {
			g.drawImage(entity.getSprite(), entity.getDisplayX(),
					entity.getDisplayY(), Entity.SPRITE_WIDTH,
					Entity.SPRITE_HEIGHT, null);
		}
	}

	private void drawMap(Graphics g) {
		for (int x = 0; x < Map.MAP_WIDTH; x++) {
			for (int y = 0; y < Map.MAP_HEIGHT; y++) {
				switch (map.getTileTypeAt(x, y)) {
				case Map.ROCK_TILE:
					g.drawImage(rock, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;
				case Map.PLANT_TILE:
					g.drawImage(plant, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;
				case Map.FLOWER_TILE:
					g.drawImage(flower, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;
				}
			}
		}
	}

	private void drawGrass(Graphics g) {
		for (int x = 0; x < Map.MAP_WIDTH; x++) {
			for (int y = 0; y < Map.MAP_HEIGHT; y++) {
				g.drawImage(grass, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);

			}
		}
	}

}
