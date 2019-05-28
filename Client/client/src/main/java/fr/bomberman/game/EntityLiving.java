package fr.bomberman.game;

import fr.bomberman.utils.Vec2D;

public abstract class EntityLiving extends Entity {

	private int power;
	private int bombCount;
	private int bombPlaced;

	public EntityLiving(int power, Map map, int x, int y) {
		super(new Vec2D(x, y), map);
		setPower(power);
		setBombCount(2);
	}

	public void addBombPlaced() {
		this.bombPlaced++;
	}
	
	public void removeBombPlaced() {
		this.bombPlaced--;
	}
	
	private void setPower(int power) {
		this.power = power <= 0 || power > 8 ? 1 : power;
	}
	
	private void setBombCount(int count) {
		this.bombCount = count <= 0 || count > 8 ? 1 : count;
	}
	
	public int getBombPlaced() {
		return bombPlaced;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getBombCount() {
		return bombCount;
	}

}
