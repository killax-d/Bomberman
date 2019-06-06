package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.utils.Vec2D;

public class EffectTrail extends Effect {

	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 32;
	public static final int EFFECT_WAITING = 0;
	public static final int EFFECT_ENDED = 1;

	private int x, y;

	private int state;
	private int frame;
	private int skin_id = 0;
	private Vec2D position;
	private EntityLiving player;

	public EffectTrail(EntityLiving player, int x, int y) {
		this.x = x;
		this.y = y;
		this.position = new Vec2D(x, y);
		setPosition(x, y);
		this.player = player;
		this.frame = 0;
		this.skin_id = 0;
		this.state = EFFECT_WAITING;
		new Timer().schedule(this.remove(), 1000);
	}
	
	public EntityLiving getOwnerPlayer() {
		return player;
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
	public int getState() {
		return state;
	}

	public void setPosition(int x, int y) {
		
	}

	public void setMap(Map map) {
		this.map = map;
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
	
	public TimerTask remove() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				state = EFFECT_ENDED;
			}
			
		};
		return task;
	}
	
}
