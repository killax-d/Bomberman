package fr.bomberman.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public class EntityAIPlayer extends EntityLiving {

	private String name;
	private GuiIngame game;
	private DefaultDirectedGraph<String, DefaultEdge> graph;
	private Set<Vec2D> path;
	
	public EntityAIPlayer(String name, Map map, int x, int y) {
		super(8, map, x, y);
		game = GuiIngame.instance;
		this.setName(name);
		this.graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		initGraph();
		this.path = new HashSet<Vec2D>();
		new Timer().scheduleAtFixedRate(calculatePath(), 0, 500);
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
	
	private void initGraph() {
		graph.addVertex("1-1");
		for(int y = 1; y < map.MAP_HEIGHT-1; y++) {
			for (int x = 1; x < map.MAP_WIDTH-1; x++) {
				String currentPoint = x + "-" + y;
				Vec2D rightTile = new Vec2D(x,  y+1);
				Vec2D bottomTile = new Vec2D(x+1,  y);
				System.out.println(map);
				if (map.getTileTypeAt(bottomTile.getX(), bottomTile.getY()) != Map.ROCK_TILE) {
					String point = bottomTile.getX() + "-" + bottomTile.getY();
					graph.addVertex(point);
					graph.addEdge(currentPoint, point);
				}
				if (map.getTileTypeAt(rightTile.getX(), rightTile.getY()) != Map.ROCK_TILE) {
					String point = rightTile.getX() + "-" + rightTile.getY();
					graph.addVertex(point);
					graph.addEdge(currentPoint, point);
				}
			}
		}
	}
	
	public TimerTask calculatePath() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				Vec2D dest = getNearestEnnemy();
				DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
				SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths(dest.getX() + "-" + dest.getY());
		        System.out.println(iPaths.getPath(dest.getX() + "-" + dest.getY()) + "\n");
			}
			
		};
		return task;
	}

}
