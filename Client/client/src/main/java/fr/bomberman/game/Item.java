package fr.bomberman.game;

import java.awt.image.BufferedImage;

import fr.bomberman.assets.Assets;
import fr.bomberman.utils.Vec2D;

public class Item extends Entity {

	public final static int SPRITE_HEIGHT = 64;
	public final static int SPRITE_WIDTH = 64;
	public final static int WAITING = 0;
	public final static int PICKED = 1;
	
	private int state;
	
	public Item(Vec2D position, Map map) {
		super(position, map);
	}
	
	protected void boostPlayer(EntityPlayer player) {
		
	}
	
	public void pick() {
		this.state = PICKED;
	}
	
	public int getState() {
		return state;
	}
	
	@Override
	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/item_power_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, 0);
	}
}
