package fr.bomberman.game;

public class EntityPlayer extends EntityLiving {

	private String name;

	public EntityPlayer(String name, Map map, int x, int y, int team) {
		super(name, 1, 1, map, x, y, team);
		this.setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
