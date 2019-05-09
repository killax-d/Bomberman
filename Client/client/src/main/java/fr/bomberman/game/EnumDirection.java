package fr.bomberman.game;

public enum EnumDirection {

	SOUTH(0), WEST(1), EST(2), NORTH(3);

	private int id;

	EnumDirection(int id) {
		this.id = id;
	}

	public int getID() {
		return this.id;
	}

}
