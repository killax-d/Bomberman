package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import fr.bomberman.assets.Assets;
import fr.bomberman.game.Bomb;
import fr.bomberman.game.Entity;
import fr.bomberman.game.EntityPlayer;
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
	private EntityPlayer player;

	public GuiIngame() {
		this.map = new Map();
		this.entities = new HashSet<Entity>();
		this.player = new EntityPlayer("Player");
		this.entities.add(player);
		player.setPosition(1, 1);
		map.setTileTypeAt(1, 1, Map.TILE_FREE);
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
		movePlayer(event.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent event) {
//		System.out.println(event.getKeyCode() + " " + KeyEvent.VK_SPACE);
		movePlayer(event.getKeyCode());
//		if(event.getKeyCode() == KeyEvent.VK_F) {
//			System.out.println("ché");
//			entities.add(new Bomb(player, map));
//		}
	}

	private void movePlayer(int keyCode) {
		switch (keyCode) {
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
		case KeyEvent.VK_SPACE:
			int mapTile = map.getTileTypeAt(player.getPosition().getX(), player.getPosition().getY());
			if(mapTile == Map.TILE_FREE || mapTile == Map.FLOWER_TILE)
				entities.add(new Bomb(player, map));
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	private void drawEntities(Graphics g) {
		Set<Bomb> bombToRemove = new HashSet<Bomb>();
		for (Entity entity : entities) {
			if (entity instanceof Bomb && ((Bomb) entity).getState() == Bomb.BOMB_EXPLODED) {
				bombToRemove.add((Bomb) entity);
			}
			else
				g.drawImage(entity.getSprite(), entity.getDisplayX(), entity.getDisplayY(), Entity.SPRITE_WIDTH,
						Entity.SPRITE_HEIGHT, null);
		}
		for (Bomb bomb : bombToRemove) {
			entities.remove(bomb);
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
