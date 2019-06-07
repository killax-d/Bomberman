package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.utils.Vec2D;

public class Item extends Entity {

	public final static int SPRITE_HEIGHT = 64;
	public final static int SPRITE_WIDTH = 64;
	public final static int WAITING = 0;
	public final static int PICKED = 1;
	public final static int DISPAWNED = 2;
	
	private Timer dispawnClock;
	private long dispawnStart;
	private long dispawnLeft;
	private final long dispawnTime = 20000;
	
	private static BufferedSound SFX_Pick = Assets.getSound("sounds/item_picked.wav", BufferedSound.SFX);
	
	private int state;
	
	public Item(Vec2D position, Map map) {
		super(position, map, -1);
		dispawnClock = new Timer();
		dispawnClock.schedule(dispawn(), dispawnTime);
	}

	public void cancelDispawn() {
		dispawnClock.cancel();
		dispawnClock.purge();
	}
	
	public void pause() {
		long time = System.nanoTime();
		cancelDispawn();
		dispawnLeft = (time - dispawnStart)/1000000;
	}
	
	public void resume() {
		dispawnClock = new Timer();
		dispawnClock.schedule(dispawn(), dispawnLeft);
	}
	
	public void pick(Entity entity) {
		dispawnClock.cancel();
		dispawnClock.purge();
		this.state = PICKED;
		if(entity instanceof EntityPlayer)
			SFX_Pick.play();
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/item_power_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, 0);
	}
	
	public TimerTask dispawn() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				state = DISPAWNED;
			}
			
		};
		return task;
	}
}
