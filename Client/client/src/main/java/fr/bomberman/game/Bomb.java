package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;

public class Bomb extends Entity {

	private static Set<Bomb> bombs = new HashSet<Bomb>();

	private static final int MAX_FRAME = 6;
	public static final int SPRITE_WIDTH = 26;
	public static final int SPRITE_HEIGHT = 32;

	private int x, y;

	private EntityPlayer player;
	private String type; // Enum ?

	private int frame;
	private int state;
	private int skin_id;
	private Map map;

	public Bomb(EntityPlayer player, int x, int y, Map map) {
		this.player = player;
		new Timer().schedule(this, 0, 50);
		new Timer().schedule(this.removeBomb(), 3000);
		this.frame = 3;
		this.state = 0;
		this.skin_id = 0;
		setPosition(x, y);
		setMap(map);
		map.setTileTypeAt(x, y, Map.BOMB_TILE);
		bombs.add(this);
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

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
		return Assets.getTile(String.format("bombs/bomb_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, this.state);
	}

	public static Bomb getBombAt(int x, int y) {
		for (Bomb bomb : bombs) {
			if (bomb.x == x && bomb.y == y){
				return bomb;
			}
		}
		return null;
	}

	private void explode() {
		int power = player.getPower() + 1;

		Set<EnumDirection> blockedDir = new HashSet<EnumDirection>();

		for (int i = 0; i < power; i++) {
			if(!blockedDir.contains(EnumDirection.EST) && x+i < Map.MAP_WIDTH
						&& (map.getTileTypeAt(x+i, y) == Map.TILE_FREE || map.getTileTypeAt(x+i, y) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x+i, y, Map.TILE_FREE);
			} else {
				blockedDir.add(EnumDirection.EST);
			}
			if(!blockedDir.contains(EnumDirection.WEST) && x-i > 0
						&& (map.getTileTypeAt(x-i, y) == Map.TILE_FREE || map.getTileTypeAt(x-i, y) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x-i, y, Map.TILE_FREE);
			} else {
				blockedDir.add(EnumDirection.WEST);
			}
			if(!blockedDir.contains(EnumDirection.SOUTH) && y+i < Map.MAP_HEIGHT
						&& (map.getTileTypeAt(x, y+i) == Map.TILE_FREE || map.getTileTypeAt(x, y+i) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x, y+i, Map.TILE_FREE);
			} else {
				blockedDir.add(EnumDirection.SOUTH);
			}
			if(!blockedDir.contains(EnumDirection.NORTH) && y-i > 0
						&& (map.getTileTypeAt(x, y-i) == Map.TILE_FREE || map.getTileTypeAt(x, y-i) == Map.PLANT_TILE)) {
				map.setTileTypeAt(x, y-i, Map.TILE_FREE);
			} else {
				blockedDir.add(EnumDirection.NORTH);
			}
		}

	}

	@Override
	public void run() {
		if(state == 0) {
			if (frame < MAX_FRAME) {
				++frame;
			} else {
				frame = 0;
			}
		}
	}

	private TimerTask removeBomb() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				state = 1;
				frame = 0;
				new Timer().schedule(new TimerTask() {

					@Override
					public void run() {
						map.setTileTypeAt(x, y, Map.TILE_FREE);
						explode();
						bombs.remove(this);
					}

				}, 150);
			}

		};
		return task;
	}

}
