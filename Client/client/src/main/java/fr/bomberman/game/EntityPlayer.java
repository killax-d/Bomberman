package fr.bomberman.game;

public class EntityPlayer extends EntityLiving {

	private String name;

	public EntityPlayer(String name) {
		super(1);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
