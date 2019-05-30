package fr.bomberman.game;

import fr.bomberman.utils.Vec2D;

public abstract class EntityLiving extends Entity {

	private int power;
	private int bombCount;
	private int bombPlaced;
	private int bombMax;
	private int x;
	private int y;

	public EntityLiving(int power, int maxBomb, Map map, int x, int y) {
		super(new Vec2D(x, y), map);
		this.x = x;
		this.y = y;
		setPower(power);
		setBombCount(1);
		spawn();
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

}
