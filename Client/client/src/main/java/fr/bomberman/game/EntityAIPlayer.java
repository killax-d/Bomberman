package fr.bomberman.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm.SingleSourcePaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import fr.bomberman.assets.Assets;
import fr.bomberman.gui.GuiIngame;
import fr.bomberman.utils.Vec2D;

public class EntityAIPlayer extends EntityLiving {
	
	private String name;
	private GuiIngame game;
	private DefaultDirectedGraph<String, DefaultEdge> graph;
	private GraphPath<String, DefaultEdge> path;
	private ArrayList<Vec2D> pathVector;
	private boolean customAction;
	private boolean teamMode;
	
	public EntityAIPlayer(String name, Map map, int x, int y, int team) {
		super(name, 1, 1, map, x, y, team);
		game = GuiIngame.instance();
		teamMode = game.isTeamMode();
		this.customAction = false;
		this.setName(name);
		this.pathVector = new ArrayList<Vec2D>();
		this.graph = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		this.skin_id = 0;
		initGraph();
		new Timer().scheduleAtFixedRate(calculatePath(), 0, 500);
		new Timer().scheduleAtFixedRate(moveWithPath(), 0, 150);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getSprite() {
		return Assets.getTile(String.format("skins/player_%d.png", getTeam()), SPRITE_WIDTH/2, SPRITE_HEIGHT/2, this.frame,
				direction.getID());
	}
	
	public Vec2D getNearestEnnemy() {
		Vec2D nearest = null;
		Double near = null;
		for (Entity entity : game.getEntitiesLiving()) {
			if (entity != this && (near == null || near > position.dist(entity.getPosition())))
				if (!teamMode | (teamMode && entity.getTeam() != getTeam())) {
					near = position.dist(entity.getPosition());
					nearest = entity.getPosition();
				}
		}
		return nearest;
	}
	
	private boolean ennemyAround() {
		boolean ennemyPresent = false;
		for (int x = -1; x < 2; x++) {
			if (position.getX()+x >= 0 && position.getX()+x <= Map.MAP_WIDTH)
				for(EntityLiving entity : game.getEntitiesLiving()) {
					if ((entity != this && !teamMode) | (teamMode && entity != this && entity.getTeam() != getTeam()))
						if (position.getX()+x == entity.getPosition().getX())
							ennemyPresent = true;
				}
				
		}
		for (int y = -1; y < 2; y++) {
			if (position.getY()+y >= 0 && position.getY()+y <= Map.MAP_HEIGHT)
				for(EntityLiving entity : game.getEntitiesLiving()) {
					if ((entity != this && !teamMode) | (teamMode && entity != this && entity.getTeam() != getTeam()))
						if (position.getY()+y == entity.getPosition().getY() && entity != this)
							ennemyPresent = true;
				}
					
		}
		return ennemyPresent;
	}
	
	public void moveToPoint(Vec2D point) {
		if (ennemyAround())
			placeBomb(point);
		if (position.getX() < point.getX()) {
			if(!canMove(EnumDirection.EST))
				placeBomb(point);
			else
				move(EnumDirection.EST);
			move(EnumDirection.EST);
		}
		else if (position.getX() > point.getX()) {
			if(!canMove(EnumDirection.WEST))
				placeBomb(point);
			else
				move(EnumDirection.WEST);
			move(EnumDirection.WEST);
		}
		else if (position.getY() < point.getY()) {
			if(!canMove(EnumDirection.SOUTH))
				placeBomb(point);
			else
				move(EnumDirection.SOUTH);
			move(EnumDirection.SOUTH);
		}
		else if (position.getY() > point.getY()) {
			if(!canMove(EnumDirection.NORTH)) 
				placeBomb(point);
			else
				move(EnumDirection.NORTH);
			move(EnumDirection.NORTH);
		}
	}
	
	private boolean isFreeCell(Vec2D point) {
		return (map.getTileTypeAt(point.getX(), point.getY()) == Map.TILE_FREE || map.getTileTypeAt(point.getX(), point.getY()) == Map.FLOWER_TILE);
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
				if (GuiIngame.instance == null || isDead()) {
					this.cancel();
				}
				if(GuiIngame.instance != null && GuiIngame.instance.isPaused())
					return;
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
						if(pathVector.size() > 0 && pathVector.get(0) != null)
							moveToPoint(pathVector.get(0));
				}
			}
		};
		return task;
	}
	
	private void initGraph() {
		graph.addVertex("1-1");
		for(int y = 1; y < Map.MAP_HEIGHT-1; y++) {
			for (int x = 1; x < Map.MAP_WIDTH-1; x++) {
				String currentPoint = x + "-" + y;
				if(map.getTileTypeAt(x, y) != Map.ROCK_TILE) {
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
	
	private void placeBomb(Vec2D point) {
		if(canPlaceBomb() && isFreeCell(position) && isFreeCell(point)) {
			game.getEntities().add(new Bomb(this, this.hasMasterBomb(), map, getTeam(), game.getEffects()));
			addBombPlaced();
		}
		
	}
	
	public TimerTask calculatePath() {
		EntityLiving entity = this;
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				if (ennemyAround()) {
					placeBomb(entity.getPosition());
				}
				if (GuiIngame.instance == null || isDead()) {
					this.cancel();
				}
				if(GuiIngame.instance != null && GuiIngame.instance.isPaused())
					return;
				if (!customAction) {
					Vec2D dest = getNearestEnnemy();
					generateShortPathToPoint(dest);
				}
			}
		};
		return task;
	}

}
