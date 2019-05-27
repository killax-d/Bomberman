package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public class Bomb extends Entity {

	private static final int MAX_FRAME = 6;
	public static final int SPRITE_WIDTH = 26;
	public static final int SPRITE_HEIGHT = 32;
	public static final int BOMB_TIMING = 0;
	public static final int BOMB_EXPLOSION = 1;
	public static final int BOMB_EXPLODED = 2;

	private int x, y;
	private Set<Effect> effects;

	private EntityLiving player;
	private String type; // Enum ?

	private int frame;
	private int state;
	private int skin_id;
	private Map map;
	
	private Timer animClock;

	public Bomb(EntityLiving player, Map map, Set<Effect> effects) {
		super(player.getPosition());
		this.player = player;
		this.effects = effects;
		animClock = new Timer();
		animClock.schedule(this.animBomb(), 0, 50);
		new Timer().schedule(this.removeBomb(), 3000);
		this.frame = 3;
		this.state = BOMB_TIMING;
		this.skin_id = 0;
		this.position = player.getPosition();
		setPosition((int) position.getX(), (int) position.getY());
		setMap(map);
		map.setTileTypeAt(x, y, Map.BOMB_TILE);
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		position.setXY(x, y);
	}
	
	@Override
	public int getDisplayX() {
		return (int) (x * Map.TILE_SCALE);
	}
	
	@Override
	public int getDisplayY() {
		return (int) (y * Map.TILE_SCALE - (Entity.SPRITE_HEIGHT - Map.TILE_SCALE));
	}

	@Override
	public void move(EnumDirection direction) {}

	@Override
	public void update() {}
	
	@Override
	public void setMap(Map map) {
		this.map = map;
	}

	@Override
	public void setFrame(int frame) {
		this.frame = frame % MAX_FRAME;
	}

	@Override
	public int getFrame() {
		return frame;
	}

	@Override
	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/bomb_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, this.state);
	}

	private void explode() {
		int power = player.getPower() + 1;

		Set<EnumDirection> blockedDir = new HashSet<EnumDirection>();
		Set<Effect> effectToAdd = new HashSet<Effect>();

		for (int i = 0; i < power; i++) {
			if(!blockedDir.contains(EnumDirection.EST) && x+i < Map.MAP_WIDTH
						&& (map.getTileTypeAt(x+i, y) == Map.TILE_FREE || map.getTileTypeAt(x+i, y) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x+i, y, Map.TILE_FREE);
				effectToAdd.add(new EffectTrail(x+i, y));
			} else {
				blockedDir.add(EnumDirection.EST);
			}
			if(!blockedDir.contains(EnumDirection.WEST) && x-i > 0
						&& (map.getTileTypeAt(x-i, y) == Map.TILE_FREE || map.getTileTypeAt(x-i, y) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x-i, y, Map.TILE_FREE);
				effectToAdd.add(new EffectTrail(x-i, y));
			} else {
				blockedDir.add(EnumDirection.WEST);
			}
			if(!blockedDir.contains(EnumDirection.SOUTH) && y+i < Map.MAP_HEIGHT
						&& (map.getTileTypeAt(x, y+i) == Map.TILE_FREE || map.getTileTypeAt(x, y+i) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x, y+i, Map.TILE_FREE);
				effectToAdd.add(new EffectTrail(x, y+i));
			} else {
				blockedDir.add(EnumDirection.SOUTH);
			}
			if(!blockedDir.contains(EnumDirection.NORTH) && y-i > 0
						&& (map.getTileTypeAt(x, y-i) == Map.TILE_FREE || map.getTileTypeAt(x, y-i) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x, y-i, Map.TILE_FREE);
				effectToAdd.add(new EffectTrail(x, y-i));
			} else {
				blockedDir.add(EnumDirection.NORTH);
			}
		}
		for (Effect effect : effectToAdd) {
			effects.add(effect);
		}

	}
	
	public int getState() {
		return state;
	}
	
	
	public TimerTask animBomb() {
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				if(state == BOMB_TIMING) {
					if (frame < MAX_FRAME) {
						++frame;
					} else {
						frame = 0;
					}
				}
			}
		};
		return task;
	}

	private TimerTask removeBomb() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				state = BOMB_EXPLOSION;
				frame = 0;
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						map.setTileTypeAt(x, y, Map.TILE_FREE);
						explode();
						state = BOMB_EXPLODED;
					}

				}, 150);
			}

		};
		return task;
	}

}
