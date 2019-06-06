package fr.bomberman.game;

public class EntityPlayer extends EntityLiving {

	private String name;

	public EntityPlayer(String name, Map map, int x, int y, int team) {
		super(1, 1, map, x, y, team);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
