package fr.bomberman.game;

public abstract class EntityLiving extends Entity {

	private int power;

	public EntityLiving(int power) {
		setPower(power);
	}

	private void setPower(int power) {
		this.power = power <= 0 || power > 8 ? 1 : power;
	}

	public int getPower() {
		return power;
	}

}
