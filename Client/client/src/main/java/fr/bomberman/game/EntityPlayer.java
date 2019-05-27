package fr.bomberman.game;

public class EntityPlayer extends EntityLiving {

	private String name;

	public EntityPlayer(String name, int x, int y) {
		super(8, x, y);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
