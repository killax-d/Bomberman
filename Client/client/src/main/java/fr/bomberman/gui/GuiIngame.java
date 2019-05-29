package fr.bomberman.gui;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.game.Bomb;
import fr.bomberman.game.Effect;
import fr.bomberman.game.EffectTrail;
import fr.bomberman.game.Entity;
import fr.bomberman.game.EntityAIPlayer;
import fr.bomberman.game.EntityPlayer;
import fr.bomberman.game.EnumDirection;
import fr.bomberman.game.Item;
import fr.bomberman.game.Map;

public class GuiIngame extends Container implements KeyListener {

	// Tile images
	private BufferedImage rock = Assets.getTile("map_tileset.png", 16, 16, 5, 3);
	private BufferedImage plant = Assets.getTile("map_tileset.png", 16, 16, 6, 3);
	private BufferedImage grass = Assets.getTile("map_tileset.png", 16, 16, 7, 3);
	private BufferedImage flower = Assets.getTile("map_tileset.png", 16, 16, 5, 2);
	// Entities on the map
	private Set<Entity> entities;
	private Set<Effect> effects;
	private Set<Item> powerups;
	// Sound
	private static final BufferedSound SFX_BackgroundMusic = Assets.getSound("sounds/background_music.wav");
	private static final BufferedSound SFX_ImpossibleAction = Assets.getSound("sounds/impossible_action.wav");

	public static GuiIngame instance;
	
	private Map map;
	private EntityPlayer player;
	private EntityAIPlayer AIplayer;

	public GuiIngame() {
		SFX_BackgroundMusic.setLoop(true);
		SFX_BackgroundMusic.setVolume(0.025F);
		SFX_BackgroundMusic.play();
		instance = this;
		this.map = new Map();
		this.entities = new HashSet<Entity>();
		this.effects = new HashSet<Effect>();
		this.powerups = new HashSet<Item>();
		this.player = new EntityPlayer("Player", map, 1, 1);
		this.AIplayer = new EntityAIPlayer("AI", map, Map.MAP_WIDTH - 2, 1);
		this.entities.add(player);
		this.entities.add(AIplayer);
		map.setTileTypeAt(1, 1, Map.TILE_FREE);
		map.setTileTypeAt(Map.MAP_WIDTH - 2, 1, Map.TILE_FREE);
	}

	@Override
	public void paint(Graphics g) {
		drawGrass(g);
		drawMap(g);
		drawEntities(g);
		drawEffects(g);
		drawPowerups(g);
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
	
	public Set<Entity> getEntities(){
		return entities;
	}
	
	public Set<Effect> getEffects(){
		return effects;
	}
	
	public Set<Item> getPowerups(){
		return powerups;
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
			if((mapTile == Map.TILE_FREE || mapTile == Map.FLOWER_TILE) && !player.isDead())
				if(player.getBombPlaced() >= player.getBombCount()) {
					SFX_ImpossibleAction.play();
				}
				else {
					entities.add(new Bomb(player, map, effects));
					player.addBombPlaced();
				}
			break;
		case KeyEvent.VK_ESCAPE:
			stopMusic();
			GameWindow.instance().setCurrentGui(new GuiMainMenu());
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	private void drawEntities(Graphics g) {
		Set<Entity> entityToDisplay = new HashSet<Entity>();
		Set<Entity> entityToRemove = new HashSet<Entity>();
		for (Entity entity : entities) {
			if (entity instanceof Bomb && ((Bomb) entity).getState() == Bomb.BOMB_EXPLODED) {
				entityToRemove.add((Bomb) entity);
			}
			else if(!(entity instanceof Bomb) && entity.isDead()) {
				entityToRemove.add((Bomb) entity);
			}
			else
				entityToDisplay.add(entity);
		}
		for (Entity entity : entityToDisplay) {
			g.drawImage(entity.getSprite(), entity.getDisplayX(), entity.getDisplayY(), Entity.SPRITE_WIDTH,
					Entity.SPRITE_HEIGHT, null);
		}
		for (Entity entity : entityToRemove) {
			entities.remove(entity);
		}
	}
	
	public void stopMusic() {
		SFX_BackgroundMusic.stop();
	}
	
	private void drawPowerups(Graphics g) {
		Set<Item> itemToDisplay = new HashSet<Item>();
		Set<Item> itemToRemove = new HashSet<Item>();
		for (Item item : powerups) {
			if(item != null) {
				if (item.getState() == Item.PICKED) {
					itemToRemove.add(item);
				}
				else
					itemToDisplay.add(item);
			}
		}
		for (Item item : itemToDisplay) {
			if(item != null) {
				g.drawImage(item.getSprite(), item.getDisplayX(), item.getDisplayY(), Item.SPRITE_WIDTH,
						Item.SPRITE_HEIGHT, null);
			}
		}
		for (Item item : itemToRemove) {
			powerups.remove(item);
		}
	}
	private void drawEffects(Graphics g) {
		Set<Effect> effectToDisplay = new HashSet<Effect>();
		Set<Effect> effectToRemove = new HashSet<Effect>();
		for (Effect effect : effects) {
			if(effect != null) {
				if (effect instanceof EffectTrail && ((EffectTrail) effect).getState() == EffectTrail.EFFECT_ENDED) {
					effectToRemove.add(effect);
				}
				else
					effectToDisplay.add(effect);
			}
		}
		for (Effect effect : effectToDisplay) {
			g.drawImage(effect.getSprite(), effect.getDisplayX(), effect.getDisplayY(), Effect.SPRITE_WIDTH,
					Effect.SPRITE_HEIGHT, null);
		}
		for (Effect effect : effectToRemove) {
			effects.remove(effect);
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
