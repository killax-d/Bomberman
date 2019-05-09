package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;

public class Entity extends TimerTask {

	private static final int MAX_FRAME = 3;
	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 48;

	private int x, y;
	private int next_x, next_y;
	private long last_move;

	private EnumDirection direction;
	private int frame;
	private int skin_id = 0;
	private Map map;

	public Entity() {
		new Timer().schedule(this, 0, 50);
		this.direction = EnumDirection.SOUTH;
		this.last_move = System.currentTimeMillis();
		this.frame = 0;
		this.skin_id = 0;
	}

	public int getDisplayX() {
		float offset_x = frame * (((next_x  - x) * Map.TILE_SCALE) / 4);
		return (int) (x * Map.TILE_SCALE + offset_x);
	}

	public int getDisplayY() {
		float offset_y = frame * (((next_y  - y) * Map.TILE_SCALE) / 4);
		return (int) (y * Map.TILE_SCALE - (Entity.SPRITE_HEIGHT - Map.TILE_SCALE) + offset_y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
		this.next_x = x;
		this.next_y = y;
	}

	public void move(EnumDirection direction) {
		if (map != null) {
			if (canMove(direction)) {
				this.last_move = System.currentTimeMillis();
				setDirection(direction);
				switch (direction) {
				case NORTH:
					next_y = y - 1;
					break;
				case WEST:
					next_x = x - 1;
					break;
				case EST:
					next_x = x + 1;
					break;
				case SOUTH:
					next_y = y + 1;
					break;
				}
			}
		}

	}

	private boolean isMoving() {
		return x != next_x || y != next_y;
	}

	private boolean canMove(EnumDirection direction) {
		boolean canMove = false;
		switch (direction) {
		case NORTH:
			canMove = (map.getTileTypeAt(x, y - 1) == Map.TILE_FREE);
			break;
		case WEST:
			canMove = (map.getTileTypeAt(x - 1, y) == Map.TILE_FREE);
			break;
		case EST:
			canMove = (map.getTileTypeAt(x + 1, y) == Map.TILE_FREE);
			break;
		case SOUTH:
			canMove = (map.getTileTypeAt(x, y + 1) == Map.TILE_FREE);
			break;
		}

		return !isMoving() && canMove;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setDirection(EnumDirection direction) {
		this.direction = direction;
	}

	public void setFrame(int frame) {
		this.frame = frame % MAX_FRAME;
	}

	public EnumDirection getDirection() {
		return direction;
	}

	public int getFrame() {
		return frame;
	}

	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/player_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame,
				direction.getID());
	}

	public void update() {
		System.out.println(System.currentTimeMillis());
	}

	@Override
	public void run() {
		if (isMoving()) {
			if (frame < MAX_FRAME) {
				++frame;
			} else {
				frame = 0;
				x = next_x;
				y = next_y;
			}
		}
	}

}
