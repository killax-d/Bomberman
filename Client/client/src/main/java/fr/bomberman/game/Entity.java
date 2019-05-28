package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public abstract class Entity extends TimerTask {

	private static final int MAX_FRAME = 4;
	public static final int SPRITE_WIDTH = 64;
	public static final int SPRITE_HEIGHT = 96;

	protected Map map;
	protected Timer clock;
	protected EnumDirection direction;
	protected int frame;
	protected int skin_id = 0;
	protected Vec2D position;
	protected Vec2D next_position;
	protected boolean dead;

	public Entity(Vec2D position, Map map) {
		this.position = position;
		this.next_position = new Vec2D(position.getX(), position.getY());
		this.direction = EnumDirection.SOUTH;
		this.frame = 0;
		this.skin_id = 1;
		setMap(map);
		this.dead = false;
		new Timer().scheduleAtFixedRate(this, 0, 20);
	}

	public int getDisplayX() {
		return (int) (position.getX() * Map.TILE_SCALE);
	}

	public int getDisplayY() {
		return (int) (position.getY() * Map.TILE_SCALE - (Entity.SPRITE_HEIGHT - Map.TILE_SCALE));
	}

	public void setPosition(int x, int y) {
		position.setXY(x, y);
	}

	private boolean isMoving() {
		return !position.equals(next_position, 0.1F);
	}

	protected boolean canMove(EnumDirection direction) {
		int tileType = Map.TILE_FREE;
		switch (direction) {
		case NORTH:
			tileType = map.getTileTypeAt(next_position.getX(), next_position.getY() - 1);
			break;
		case SOUTH:
			tileType = map.getTileTypeAt(next_position.getX(), next_position.getY() + 1);
			break;
		case EST:
			tileType = map.getTileTypeAt(next_position.getX() + 1, next_position.getY());
			break;
		case WEST:
			tileType = map.getTileTypeAt(next_position.getX() - 1, next_position.getY());
			break;
		}
		return (tileType == Map.TILE_FREE || tileType == Map.FLOWER_TILE);
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setDirection(EnumDirection direction) {
		this.direction = direction;
	}

	public EnumDirection getDirection() {
		return direction;
	}

	public void setFrame(int frame) {
		this.frame = frame % MAX_FRAME;
	}

	public int getFrame() {
		return frame;
	}

	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/player_%d.png", skin_id), SPRITE_WIDTH/2, SPRITE_HEIGHT/2, this.frame,
				direction.getID());
	}

	public void update() {
		if (isMoving()) {
			if (position.getX() < next_position.getX()) {
				position.addX(0.1F);
			} else if (position.getX() > next_position.getX()) {
				position.addX(-0.1F);
			}
			if (position.getY() < next_position.getY()) {
				position.addY(0.1F);
			} else if (position.getY() > next_position.getY()) {
				position.addY(-0.1F);
			}
		} else {
			position.setX(next_position.getX());
			position.setY(next_position.getY());
		}
	}

	public void move(EnumDirection direction) {
		if (isMoving()) {
			return;
		}
		this.direction = direction;
		if (!canMove(direction)) {
			return;
		}
		switch (direction) {
		case NORTH:
			next_position.addY(-1F);
			break;
		case SOUTH:
			next_position.addY(+1F);
			break;
		case WEST:
			next_position.addX(-1F);
			break;
		case EST:
			next_position.addX(+1F);
			break;
		}
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public void die() {
		this.dead = true;
		GuiIngame.instance.getEntities().remove(this);
	}

	public Vec2D getPosition() {
		return position;
	}

	public Vec2D getNextPosition() {
		return next_position;
	}

	@Override
	public void run() {
		update();
		if (isMoving()) {
			frame = (frame + 1) % MAX_FRAME;
		} else {
			frame = 0;
		}
	}

}
