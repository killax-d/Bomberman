package fr.bomberman.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jgrapht.GraphPath;
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
	private GraphPath path;
	private ArrayList<Vec2D> pathVector;
	private boolean customAction;
	
	public EntityAIPlayer(String name, Map map, int x, int y) {
		super(8, 1, map, x, y);
		game = GuiIngame.instance;
		this.customAction = false;
		this.setName(name);
		this.pathVector = new ArrayList<Vec2D>();
		this.graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		initGraph();
		new Timer().scheduleAtFixedRate(calculatePath(), 0, 500);
		new Timer().scheduleAtFixedRate(moveWithPath(), 0, 150);
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
	
	private void cleanPath() {
		if (path != null && path.getLength() > 0) {
			pathVector = new ArrayList<Vec2D>();
			for (Object point : path.getEdgeList()) {
				String[] args = point.toString().replace("(", "").replace(")", "").split(" : ");
				String[] coord = args[1].split("-");
				int x = Integer.parseInt(coord[0]);
				int y = Integer.parseInt(coord[1]);
				pathVector.add(new Vec2D(x, y));
			}
		}
	}
	
	private TimerTask moveWithPath() {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (dead) {
					this.cancel();
				}
				boolean moved = false;
				if (pathVector.size() > 1) {
					for(int i = 0; i < pathVector.size() && !moved; i++) {
						if (pathVector.get(i).getX() == position.getX() 
								&& pathVector.get(i).getY() == position.getY()
								&& i != pathVector.size()-1) {
							moveToPoint(pathVector.get(i+1));
							moved = true;
						}
					}
					if (!moved)
						moveToPoint(pathVector.get(0));
				}
			}
		};
		return task;
	}
	
	private void initGraph() {
		graph.addVertex("1-1");
		for(int y = 1; y < map.MAP_HEIGHT-1; y++) {
			for (int x = 1; x < map.MAP_WIDTH-1; x++) {
				String currentPoint = x + "-" + y;
				if(map.getTileTypeAt(x, y) != map.ROCK_TILE) {
					Vec2D rightTile = new Vec2D(x+1,  y);
					Vec2D bottomTile = new Vec2D(x,  y+1);
					if (map.getTileTypeAt(bottomTile.getX(), bottomTile.getY()) != Map.ROCK_TILE) {
						String point = (bottomTile.getX() + "-" + bottomTile.getY()).replace(".0",  "");
						graph.addVertex(point);
						graph.addEdge(currentPoint, point);
						graph.addEdge(point, currentPoint);
					}
					if (map.getTileTypeAt(rightTile.getX(), rightTile.getY()) != Map.ROCK_TILE) {
						String point = (rightTile.getX() + "-" + rightTile.getY()).replace(".0",  "");
						graph.addVertex(point);
						graph.addEdge(currentPoint, point);
						graph.addEdge(point, currentPoint);
					}
				}
			}
		}
	}
	
	public void generateShortPathToPoint(Vec2D point) {
		DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(graph);
		String start = String.format("%d-%d", (int) position.getX(), (int) position.getY());
		if(!graph.containsVertex(start)) {
			start = String.format("%d-%d", (int) position.getY(), (int) position.getX());
		}
		SingleSourcePaths<String, DefaultEdge> iPaths = dijkstraAlg.getPaths(start);
		if (point != null) {
			path = (iPaths.getPath((point.getX() + "-" + point.getY()).replace(".0", "")));
			cleanPath();
		}
	}
	
	public TimerTask calculatePath() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (dead) {
					this.cancel();
				}
				if (!customAction) {
					Vec2D dest = getNearestEnnemy();
					generateShortPathToPoint(dest);
				}
			}
		};
		return task;
	}

}
