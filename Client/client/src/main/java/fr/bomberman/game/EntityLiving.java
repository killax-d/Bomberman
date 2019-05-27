package fr.bomberman.game;

import fr.bomberman.utils.Vec2D;

public abstract class EntityLiving extends Entity {

	private int power;

	public EntityLiving(int power, int x, int y) {
		super(new Vec2D(x, y));
		setPower(power);
	}

	private void setPower(int power) {
		this.power = power <= 0 || power > 8 ? 1 : power;
	}

	public int getPower() {
		return power;
	}

}
