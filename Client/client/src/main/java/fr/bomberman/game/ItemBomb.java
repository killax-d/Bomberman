package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.utils.Vec2D;

public class ItemBomb extends Item {

	private final int SPRITE_HEIGHT = 32;
	private final int SPRITE_WIDTH = 32;
	private final int MAX_FRAME = 3;
	private Timer animClock;
	
	public ItemBomb(Vec2D position, Map map) {
		super(position, map);
		this.frame = 0;
		this.skin_id = 0;
		this.position = position;
		animClock = new Timer();
		animClock.scheduleAtFixedRate(this.animItem(), 0, 125);
		setPosition((int) position.getX(), (int) position.getY());
	}

	@Override
	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/item_bomb_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, 0);
	}
	
	public TimerTask animItem() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (frame < MAX_FRAME) {
					++frame;
				} else {
					frame = 0;
				}
			}
		};
		return task;
	}

}
