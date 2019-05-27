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
		game = GuiIngame.instance;
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
			if(!canMove(EnumDirection.EST)) {
				if(getBombCount() != getBombPlaced()) {
					game.getEntities().add(new Bomb(this, map, game.getEffects()));
					addBombPlaced();
				}
			}
			else
				move(EnumDirection.EST);
		}
		else if (position.getX() > point.getX()) {
			if(!canMove(EnumDirection.WEST)) {
				if(getBombCount() != getBombPlaced()) {
					game.getEntities().add(new Bomb(this, map, game.getEffects()));
					addBombPlaced();
				}
			}
			else
				move(EnumDirection.WEST);
		}
		else if (position.getY() < point.getY()) {
			if(!canMove(EnumDirection.SOUTH)) {
				if(getBombCount() != getBombPlaced()) {
					game.getEntities().add(new Bomb(this, map, game.getEffects()));
					addBombPlaced();
				}
			}
			else
				move(EnumDirection.SOUTH);
		}
		else if (position.getY() > point.getY()) {
			if(!canMove(EnumDirection.NORTH)) {
				if(getBombCount() != getBombPlaced()) {
					game.getEntities().add(new Bomb(this, map, game.getEffects()));
					addBombPlaced();
				}
			}
			else
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
