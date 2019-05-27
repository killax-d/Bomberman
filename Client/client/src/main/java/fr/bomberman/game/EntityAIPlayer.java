package fr.bomberman.game;

import java.util.Timer;
import java.util.TimerTask;

import fr.bomberman.gui.GameWindow;
import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public class EntityAIPlayer extends EntityLiving {

	private String name;
	private GuiIngame game;
	
	public EntityAIPlayer(String name, int x, int y) {
		super(8, x, y);
		this.setName(name);
		new Timer().scheduleAtFixedRate(play(), 0, 50);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Vec2D getNearestEnnemy() {
		game = GuiIngame.instance;
		Vec2D nearest = null;
		Double near = null;
		for (Entity entity : game.getEntities()) {
			if (entity != this && (near == null || near > position.dist(entity.getPosition()))) {
				near = position.dist(entity.getPosition());
				nearest = entity.getPosition();
			}
		}
		return nearest;
	}
	
	public void moveToPoint(Vec2D point) {
		if (position.getX() < point.getX()) {
			move(EnumDirection.EST);
		}
		if (position.getX() > point.getX()) {
			move(EnumDirection.WEST);
		}
		if (position.getY() < point.getY()) {
			move(EnumDirection.SOUTH);
		}
		if (position.getY() > point.getY()) {
			move(EnumDirection.NORTH);
		}
	}
	
	public TimerTask play() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Vec2D dest = getNearestEnnemy();
				moveToPoint(dest);
			}
			
		};
		return task;
	}

}
