package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.gui.GameWindow;
import fr.bomberman.gui.GuiIngame;
import fr.bomberman.gui.GuiMainMenu;
import fr.bomberman.utils.Vec2D;

public class Bomb extends Entity {

	private static final int MAX_FRAME = 6;
	public static final int SPRITE_WIDTH = 26;
	public static final int SPRITE_HEIGHT = 32;
	public static final int BOMB_TIMING = 0;
	public static final int BOMB_EXPLOSION = 1;
	public static final int BOMB_EXPLODED = 2;

	private static BufferedSound SFX_Trail = Assets.getSound("sounds/spark.wav");
	private static BufferedSound SFX_Explosion = Assets.getSound("sounds/pokeball_explosion.wav");
	private static BufferedSound SFX_BombDrop = Assets.getSound("sounds/pokeball.wav");
	private static BufferedSound SFX_EntityDie = Assets.getSound("sounds/fainted.wav");
	
	private Set<Effect> effectToAdd;
	private Set<EnumDirection> blockedDir;


	private int x, y;
	private CopyOnWriteArrayList<Effect> effects;

	private EntityLiving player;

	private int frame;
	private int state;
	private int skin_id;
	private Map map;
	private boolean master_bomb;
	
	private final long clockExplosion = 2000;
	
	private Timer animClock;
	private Timer exploClock;

	public Bomb(EntityLiving player, boolean master, Map map, CopyOnWriteArrayList<Effect> effects) {
		super(player.getPosition(), map);
		master_bomb = master;
		SFX_BombDrop.setVolume(0.01F);
		SFX_BombDrop.play();
		this.player = player;
		this.effects = effects;
		animClock = new Timer();
		animClock.schedule(this.animBomb(), 0, 50);
		exploClock = new Timer();
		exploClock.schedule(this.removeBomb(), clockExplosion);
		this.frame = 3;
		this.state = BOMB_TIMING;
		this.skin_id = 0;
		this.map = map;
		this.position = player.getPosition();
		this.x = (int) position.getX();
		this.y = (int) position.getY();
		this.position = new Vec2D(x, y);
		this.next_position = new Vec2D(x, y);
		this.speed = 5.0;
		map.setTileTypeAt(x, y, Map.BOMB_TILE);
	}

	public void updatePosition() {
		this.x = (int) position.getX();
		this.y = (int) position.getY();
	}

	private boolean isMoving() {
		return !position.equals(next_position, 0.1F);
	}
	
	@Override
	public void update() {
		if (isMoving()) {
			if (position.getX() < next_position.getX()) {
				if((position.getX() + 0.1F*speed) > next_position.getX())
					position.setX(next_position.getX());
				else
					position.addX(0.1F*(float)speed);
			} else if (position.getX() > next_position.getX()) {
				if((position.getX() - 0.1F*speed) < next_position.getX())
					position.setX(next_position.getX());
				else
					position.addX(-(0.1F*(float)speed));
			}
			if (position.getY() < next_position.getY()) {
				if((position.getY() + 0.1F*speed) > next_position.getY())
					position.setY(next_position.getY());
				else
					position.addY(0.1F*(float)speed);
			} else if (position.getY() > next_position.getY()) {
				if((position.getY() + 0.1F*speed) < next_position.getY())
					position.setY(next_position.getY());
				else
					position.addY(-(0.1F*(float)speed));
			}
		} else {
			position.setX(next_position.getX());
			position.setY(next_position.getY());
		}
		updatePosition();
	}
	
	@Override
	public void move(EnumDirection direction) {
		map.setTileTypeAt(next_position.getX(), next_position.getY(), Map.TILE_FREE);
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
		map.setTileTypeAt(next_position.getX(), next_position.getY(), Map.BOMB_TILE);
	}

	public void cancelExplosion() {
		exploClock.cancel();
		exploClock.purge();
	}
	
	public void instantExplode() {
		cancelExplosion();

		state = BOMB_EXPLOSION;
		frame = 0;
		map.setTileTypeAt(x, y, Map.TILE_FREE);
		if(!GuiIngame.instance.isPaused())
			SFX_Explosion.play();
		explode();
		player.removeBombPlaced();
		state = BOMB_EXPLODED;
	}
	
	
	@Override
	public int getDisplayX() {
		return (int) (position.getX() * Map.TILE_SCALE);
	}

	@Override
	public int getDisplayY() {
		return (int) (position.getY() * Map.TILE_SCALE - (Entity.SPRITE_HEIGHT - Map.TILE_SCALE));
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
		if (master_bomb)
			return Assets.getTile(String.format("skins/bomb_master_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, (this.state <= 1 ? this.state : 1));
		return Assets.getTile(String.format("skins/bomb_%d.png", skin_id), SPRITE_WIDTH, SPRITE_HEIGHT, this.frame, (this.state <= 1 ? this.state : 1));
	}

	private void killIfEntity(int x, int y) {
		if(GuiIngame.instance == null)
			return;
		Set<Entity> entities = new HashSet<Entity>();
		if(GuiIngame.instance.getEntities().size() > 1)
			for (Entity entity : GuiIngame.instance.getEntities()) {
				if(entity instanceof EntityLiving) {
					entities.add(entity);
				}
			}
		for (Item item : GuiIngame.instance.getPowerups()) {
			if (x == item.getPosition().getX() && y == item.getPosition().getY())
				item.die();
		}
		
		for (Bomb bomb : GuiIngame.instance.getBombs()) {
			if (x == bomb.getPosition().getX() && y == bomb.getPosition().getY())
				if (bomb != this && bomb.getState() < BOMB_EXPLOSION)
					bomb.instantExplode();
		}
		
		for (Entity entity : entities) {
			if (x == entity.getPosition().getX() && y == entity.getPosition().getY()) {
				if (entity != player)
					entity.die();
				else if(entity == player && entity instanceof EntityPlayer) {
					entity.die();
					end();
				}
				SFX_EntityDie.play();
				if (GameWindow.instance().getCurrentGui() instanceof GuiIngame)
					if (GuiIngame.instance.getAlivePlayerCount() == 1)
						end();
					
			}
				
		}
	}
	
	private void end() {
		if (!GameWindow.instance().isInDemoMode() || (GameWindow.instance().isInDemoMode() && player.isDead())) {
			for(Bomb bomb : GuiIngame.instance.getBombs())
				if(bomb != null)
					bomb.cancelExplosion();
			for(Item item : GuiIngame.instance.getItems())
				if(item != null) {
					item.die();
					item.setState(Item.DISPAWNED);
				}
			
			if (GuiIngame.instance.getAlivePlayerCount() == 1 && GuiIngame.instance.playerIsAlive())
				GuiIngame.instance.setWinScreen(GuiIngame.VICTORY);
			else
				GuiIngame.instance.setWinScreen(GuiIngame.DEFEAT);

			new Timer().schedule(new TimerTask() {
	
				@Override
				public void run() {
					if (GameWindow.instance().getCurrentGui() instanceof GuiIngame) {
						GuiIngame game = (GuiIngame) GameWindow.instance().getCurrentGui();
						game.stopMusic();
						GameWindow.instance().setCurrentGui(new GuiMainMenu());
						GuiIngame.instance = null;
					}
				}
				
			}, 3000);
		}
	}
	
	private void spawnItem(int x, int y) {
		if(GuiIngame.instance == null)
			return;
		Random rand = new Random();
		int r = rand.nextInt(100);
		if (r < 15) {
			GuiIngame.instance.getPowerups().add(new ItemPowerUp(new Vec2D(x, y), map));
		}
		else if (r < 30) {
			GuiIngame.instance.getPowerups().add(new ItemBombUp(new Vec2D(x, y), map));
		}
		else if (r < 45) {
			GuiIngame.instance.getPowerups().add(new ItemPowerDown(new Vec2D(x, y), map));
		}
		else if (r < 60) {
			GuiIngame.instance.getPowerups().add(new ItemBombDown(new Vec2D(x, y), map));
		}
		else if (r <= 75) {
			GuiIngame.instance.getPowerups().add(new ItemSpeedUp(new Vec2D(x, y), map));
		}
		else if (r <= 90) {
			GuiIngame.instance.getPowerups().add(new ItemSpeedDown(new Vec2D(x, y), map));
		}
		else if (r < 95) {
			GuiIngame.instance.getPowerups().add(new ItemBombMine(new Vec2D(x, y), map));
		}
		else {
			GuiIngame.instance.getPowerups().add(new ItemGloves(new Vec2D(x, y), map));
		}
	}
	
	private void bombCheck(EnumDirection direction, int x, int y) {
		if (x < 0 || y < 0 || x > Map.MAP_WIDTH || y > Map.MAP_WIDTH)
			return;
		
		if (direction == EnumDirection.EST || direction == EnumDirection.WEST)
			if(!blockedDir.contains(direction) && x < Map.MAP_WIDTH && isExplosableTile(x, y)) {
				effectToAdd.add(new EffectTrail(player, x, y));
				killIfEntity(x, y);
				if (map.getTileTypeAt(x, y) == Map.PLANT_TILE) {
					if(!master_bomb)
						blockedDir.add(direction);
					spawnItem(x, y);
				}
				map.setTileTypeAt(x, y, Map.TILE_FREE);
			} else {
				blockedDir.add(direction);
			}
		else
			if(!blockedDir.contains(direction) && y < Map.MAP_HEIGHT && isExplosableTile(x, y)) {
				effectToAdd.add(new EffectTrail(player, x, y));
				killIfEntity(x, y);
				if (map.getTileTypeAt(x, y) == Map.PLANT_TILE) {
					if(!master_bomb)
						blockedDir.add(direction);
					spawnItem(x, y);
				}
				map.setTileTypeAt(x, y, Map.TILE_FREE);
			} else {
				blockedDir.add(direction);
			}
	}
	
	private boolean isExplosableTile(int x, int y) {
		return (map.getTileTypeAt(x, y) == Map.BOMB_TILE || map.getTileTypeAt(x, y) == Map.TILE_FREE || map.getTileTypeAt(x, y) == Map.PLANT_TILE || map.getTileTypeAt(x, y) == Map.FLOWER_TILE);
	}
	
	private void explode() {
		if(GuiIngame.instance == null)
			return;
		int power = player.getPower() + 1;

		blockedDir = new HashSet<EnumDirection>();
		effectToAdd = new HashSet<Effect>();
		for (int i = 0; i < power; i++) {
			bombCheck(EnumDirection.EST, x+i, y);
			bombCheck(EnumDirection.WEST, x-i, y);
			bombCheck(EnumDirection.SOUTH, x, y+i);
			bombCheck(EnumDirection.NORTH, x, y-i);
		}
			
		if(GuiIngame.instance != null && !GuiIngame.instance.isPaused())
			SFX_Trail.play();
		
		effects.addAll(effectToAdd);

	}
	
	public int getState() {
		return state;
	}
	
	
	public TimerTask animBomb() {
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				update();
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
						map.setTileTypeAt(next_position.getX(), next_position.getY(), Map.TILE_FREE);
						if(GuiIngame.instance != null && !GuiIngame.instance.isPaused())
							SFX_Explosion.play();
						explode();
						player.removeBombPlaced();
						state = BOMB_EXPLODED;
					}

				}, 150);
			}

		};
		return task;
	}

}
