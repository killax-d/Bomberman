package fr.bomberman.game;

import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public abstract class EntityLiving extends Entity {

	private String name;
	private int power;
	private int bombCount;
	private int bombPlaced;
	private int bombMax;
	private int x;
	private int y;
	private boolean gloves;
	private boolean master_bomb;
	private int team;
	private int lives;
	private boolean dead;
	
	private boolean invulnerable;
	private Timer protectionTimer;

	public EntityLiving(String name, int power, int maxBomb, Map map, int x, int y, int team) {
		super(new Vec2D(x, y), map, team);
		this.invulnerable = false;
		this.name = name;
		this.team = team;
		this.x = x;
		this.y = y;
		this.lives = GuiIngame.instance().getTotalLives();
		setPower(power);
		setBombCount(1);
		spawn();
		if (this instanceof EntityPlayer && GuiIngame.instance().isInDemoMode())
			gloves = true;
		else
			gloves = false;
		master_bomb = false;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isInvulnerable() {
		return invulnerable;
	}

	@Override
	public void die() {
		if(invulnerable)
			return;
		invulnerable = true;
		if(--lives <= 0)
			dead = true;
		
		
		protectionTimer = new Timer();
		protectionTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				invulnerable = false;
			}
			
		}, 1250);
		
		GuiIngame.instance().checkEnd();
	}
	
	@Override
	public boolean isDead() {
		return dead;
	}
	
	public int getLives() {
		return lives;
	}
	
	private void spawn() {
		for (int x = -1; x < 2; x++) {
			if (this.x+x >= 0 && this.x+x <= Map.MAP_WIDTH)
				if(map.getTileTypeAt(this.x+x, y) == Map.PLANT_TILE)
					map.setTileTypeAt(this.x+x, y, Map.TILE_FREE);
		}
		for (int y = -1; y < 2; y++) {
			if (this.y+y >= 0 && this.y+y <= Map.MAP_HEIGHT)
				if(map.getTileTypeAt(x, this.y+y) == Map.PLANT_TILE)
					map.setTileTypeAt(x, this.y+y, Map.TILE_FREE);
		}
	}
	
	@Override
	public int getTeam() {
		return team;
	}

	public boolean canPlaceBomb() {
		return bombPlaced < bombCount;
	}
	
	public void addGloves() {
		gloves = true;
	}

	public void addMasterBomb() {
		master_bomb = true;
	}
	
	public boolean hasGloves() {
		return gloves;
	}
	
	public boolean hasMasterBomb() {
		return master_bomb;
	}
	
	public void addSpeed() {
		setSpeed((speed >= 2.0 ? 2.0 : speed+0.2));
	}
	
	public void removeSpeed() {
		setSpeed((speed <= 1.0 ? 1.0 : speed-0.2));
	}
	
	private void setSpeed(double speed) {
		this.speed = (float) (speed <= 1.0 ? 1.0 : speed);
	}

	public void addBombPlaced() {
		this.bombPlaced++;
	}
	
	public void removeBombPlaced() {
		this.bombPlaced--;
	}
	
	public void addPower() {
		setPower((power == 8 ? 8 : ++power));
	}
	
	public void removePower() {
		setPower((power == 1 ? 1 : --power));
	}
	
	private void setPower(int power) {
		this.power = power <= 1 || power > 8 ? 1 : power;
	}

	public void addMaxBomb() {
		setBombCount((bombCount == 8 ? 8 : ++bombCount));
	}
	
	public void removeMaxBomb() {
		setBombCount((bombCount == 1 ? 1 : --bombCount));
	}
	
	private void setBombCount(int count) {
		this.bombCount = count <= 1 || count > 8 ? 1 : count;
	}
	
	public int getBombPlaced() {
		return bombPlaced;
	}

	public int getMaxBomb() {
		return bombMax;
	}
	
	
	public int getPower() {
		return power;
	}
	
	public int getBombCount() {
		return bombCount;
	}
	
	public boolean canMove(EnumDirection direction) {
		int tileType = Map.TILE_FREE;
		switch (direction) {
		case NORTH:
			tileType = map.getTileTypeAt(next_position.getX(), next_position.getY() - 1);
			if(GuiIngame.instance() != null && tileType == Map.BOMB_TILE && hasGloves()) {
				Bomb bomb = GuiIngame.instance().getBombAt(next_position.getX(), next_position.getY() - 1);
				if (bomb != null) {
					while(bomb.canMove(direction)) {
						bomb.move(direction);
					}
				}
			}
			break;
		case SOUTH:
			tileType = map.getTileTypeAt(next_position.getX(), next_position.getY() + 1);
			if(GuiIngame.instance() != null && tileType == Map.BOMB_TILE && hasGloves()) {
				Bomb bomb = GuiIngame.instance().getBombAt(next_position.getX(), next_position.getY() + 1);
				if (bomb != null) {
					while(bomb.canMove(direction)) {
						bomb.move(direction);
					}
				}
			}
			break;
		case EST:
			tileType = map.getTileTypeAt(next_position.getX() + 1, next_position.getY());
			if(GuiIngame.instance() != null && tileType == Map.BOMB_TILE && hasGloves()) {
				Bomb bomb = GuiIngame.instance().getBombAt(next_position.getX() + 1, next_position.getY());
				if (bomb != null) {
					while(bomb.canMove(direction)) {
						bomb.move(direction);
					}
				}
			}
			break;
		case WEST:
			tileType = map.getTileTypeAt(next_position.getX() - 1, next_position.getY());
			if(GuiIngame.instance() != null && tileType == Map.BOMB_TILE && hasGloves()) {
				Bomb bomb = GuiIngame.instance().getBombAt(next_position.getX() - 1, next_position.getY());
				if (bomb != null) {
					while(bomb.canMove(direction)) {
						bomb.move(direction);
					}
				}
			}
			break;
		}
		return (tileType == Map.TILE_FREE || tileType == Map.FLOWER_TILE);
	}

}
