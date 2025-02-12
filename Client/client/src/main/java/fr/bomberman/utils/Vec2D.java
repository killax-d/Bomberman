package fr.bomberman.utils;

public class Vec2D {

	private float x, y;

	public Vec2D() {
		setXY(0F, 0F);
	}

	public Vec2D(float x, float y) {
		setXY(x, y);
	}
	
	public double dist(Vec2D location) {
		return Math.sqrt(Math.pow(location.getX() - getX(), 2) + Math.pow(location.getY() - getY(), 2));
	}
	
	public static double dist(Vec2D A, Vec2D B) {
		return Math.sqrt(Math.pow(B.getX() - A.getX(), 2) + Math.pow(B.getY() - A.getY(), 2));
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Vec2D other, float delta) {
		return Math.abs(x - other.getX()) < delta && Math.abs(y - other.getY()) < delta;
	}

	public void addX(float x) {
		this.x += x;
	}

	public void addY(float y) {
		this.y += y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
