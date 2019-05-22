package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.utils.Vec2D;

public class Effect extends TimerTask {

	private static final int MAX_FRAME = 1;
	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 48;

	protected int x, y;
	
	protected int state;
	protected Map map;
	protected int frame;
	protected int skin_id = 0;
	protected Vec2D position;

	public Effect() {
		this.position = new Vec2D(1F, 1F);
		this.frame = 0;
		this.skin_id = 0;
	}

	public void runAnimation() {
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
	
	public int getState() {
		return state;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setFrame(int frame) {
		this.frame = frame % MAX_FRAME;
	}

	public int getFrame() {
		return frame;
	}

	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/trail_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame,
				0);
	
	}

	public Vec2D getPosition() {
		return position;
	}

	@Override
	public void run() {
		frame = (frame + 1) % MAX_FRAME;
	}

}
