package fr.bomberman.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.bomberman.assets.Assets;
import fr.bomberman.assets.BufferedSound;
import fr.bomberman.game.Bomb;
import fr.bomberman.game.Controls;
import fr.bomberman.game.Effect;
import fr.bomberman.game.EffectTrail;
import fr.bomberman.game.Entity;
import fr.bomberman.game.EntityAIPlayer;
import fr.bomberman.game.EntityLiving;
import fr.bomberman.game.EntityPlayer;
import fr.bomberman.game.EnumDirection;
import fr.bomberman.game.Item;
import fr.bomberman.game.Map;

public class GuiIngame extends Container implements KeyListener {

	private int[][] spawns = new int[][] {{1,1}, {Map.MAP_WIDTH - 2, 1}, {1, Map.MAP_HEIGHT - 2}, {Map.MAP_WIDTH - 2, Map.MAP_HEIGHT - 2}};
	
	// Parameters for party
	private boolean demo;
	private int lives;
	private int AIPlayer;
	private boolean teamMode;
	private int plantChance;
	private int itemChance;
	
	// Tile images
	private BufferedImage rock = Assets.getTile("map_tileset.png", 16, 16, 5, 3);
	private BufferedImage plant = Assets.getTile("map_tileset.png", 16, 16, 6, 3);
	private BufferedImage grass = Assets.getTile("map_tileset.png", 16, 16, 7, 3);
	private BufferedImage flower = Assets.getTile("map_tileset.png", 16, 16, 5, 2);
	// Entities on the map
	private CopyOnWriteArrayList<Entity> entities;
	private CopyOnWriteArrayList<Effect> effects;
	private CopyOnWriteArrayList<Item> powerups;
	// Sound
	private static final BufferedSound SFX_BackgroundMusic = Assets.getSound("sounds/background_music.wav", BufferedSound.MUSIC);
	private static final BufferedSound SFX_ImpossibleAction = Assets.getSound("sounds/impossible_action.wav", BufferedSound.SFX);

	private static GuiIngame instance;
	
	private Map map;
	private EntityPlayer player;
	private boolean gamePause;

	// ENDSCREEN
	public static final int UNKNOW = -1;
	public static final int VICTORY = 0;
	public static final int DEFEAT = 1;
	private int typeWin;
	private BufferedImage victory_screen = Assets.getImage("victory.png");
	private BufferedImage defeat_screen = Assets.getImage("defeat.png");

	public GuiIngame() {
		if (instance != null)
			for (Bomb bomb : GuiIngame.instance.getBombs())
				if(!bomb.isDead())
					bomb.cancelExplosion();
		SFX_BackgroundMusic.setLoop(true);
		SFX_BackgroundMusic.play();
		instance = this;
		// parameters
		teamMode = GameWindow.instance().isTeamMode();
		lives = GameWindow.instance().getTotalLives();
		AIPlayer = GameWindow.instance().getTotalAIPlayer();
		demo = GameWindow.instance().isInDemoMode();
		plantChance = GameWindow.instance().getPlantChance();
		itemChance = GameWindow.instance().getItemChance();
		
		gamePause = false;
		this.map = new Map();
		this.entities = new CopyOnWriteArrayList<Entity>();
		this.effects = new CopyOnWriteArrayList<Effect>();
		this.powerups = new CopyOnWriteArrayList<Item>();
		int players = GameWindow.instance().getTotalAIPlayer();
		this.player = new EntityPlayer(GameWindow.instance().getPlayerName(), map, spawns[0][0], spawns[0][1], 1);
		for(int i = 1; i < players; i++) {
			int[] spawn = spawns[i];
			this.entities.add(new EntityAIPlayer("AIplayer".concat(String.valueOf(i)), map, spawn[0], spawn[1], (teamMode ? (players == 4 && i == 1 ? 1 : 0) : 0)));
		}
		this.entities.add(player);
		typeWin = UNKNOW;
	}
	
	public void checkEnd() {
		if (playerIsAlive() & !isInDemoMode())
			if (GuiIngame.instance().getAlivePlayerCount() <= 1)
				setWinScreen(VICTORY);
			else if (teamMode && getTeamLeft() <= 1)
				setWinScreen(VICTORY);
			else
				setWinScreen(UNKNOW);
		else if (!playerIsAlive())
			setWinScreen(DEFEAT);
		else
			setWinScreen(UNKNOW);

		if(getWinScreen() != GuiIngame.UNKNOW) {
			for(Bomb bomb : GuiIngame.instance().getBombs())
				if(bomb != null)
					bomb.cancelExplosion();
			for(Item item : GuiIngame.instance().getItems())
				if(item != null) {
					item.die();
					item.setState(Item.DISPAWNED);
				}
			new Timer().schedule(new TimerTask() {
		
				@Override
				public void run() {
					stopMusic();
					GameWindow.instance().setCurrentGui(new GuiMainMenu());
					clearInstance();
				}
					
			}, 3000);
		}
	}
	
	public static void clearInstance() {
		instance = null;
	}
	
	public static GuiIngame instance() {
		return instance;
	}
	
	private int getWinScreen() {
		return typeWin;
	}

	private void setWinScreen(int type) {
		typeWin = type;
	}
	
	public void displayEndScreen(Graphics g) {
		if (getWinScreen() != UNKNOW)
			if (getWinScreen() == VICTORY)
				g.drawImage(victory_screen, 0, 0, getWidth(), getHeight(), null);
			else
				g.drawImage(defeat_screen, 0, 0, getWidth(), getHeight(), null);
			
	}
	
	public boolean isInDemoMode() {
		return demo;
	}
	
	public int getTotalLives() {
		return lives;
	}
	
	public int getTotalAIPlayer() {
		return AIPlayer;
	}
	
	public boolean isTeamMode() {
		return teamMode;
	}
	
	public int getPlantChance() {
		return plantChance;
	}
	
	public int getItemChance() {
		return itemChance;
	}
	
	public boolean playerIsAlive() {
		return !((Entity) player).isDead();
	}
	
	public int getTeamLeft() {
		int team = 0;
		Set<Integer> tmp = new HashSet<Integer>();
		for(EntityLiving entity : getEntitiesLiving()) {
			if(entity != null && !entity.isDead() && !tmp.contains(entity.getTeam())) {
				tmp.add(entity.getTeam());
				team++;
			}
		}
		return team;
	}
	
	public void pause() {
		for (Bomb bomb : getBombs())
			if(bomb != null  && !bomb.isDead())
				bomb.pause();

		for (Item item : getPowerups())
			if(item != null  && !item.isDead())
				item.pause();
		
		gamePause = true;
		stopMusic();
		GameWindow.instance().setCurrentGui(new GuiMainMenu());
	}
	
	public boolean isPaused() {
		return gamePause;
	}
	
	public void resume() {
		gamePause = false;
		for (Bomb bomb : getBombs())
			if(bomb != null  && !bomb.isDead())
				bomb.resume();

		for (Item item : getPowerups())
			if(item != null  && !item.isDead())
				item.resume();
		SFX_BackgroundMusic.play();
	}
	
	@Override
	public void paint(Graphics g) {
		drawGrass(g);
		drawMap(g);
		drawEntities(g);
		drawEffects(g);
		drawPowerups(g);
		displayEndScreen(g);
		if(GameWindow.instance().getTotalLives() > 1)
			displayLivesBar(g);
		displayPlayerName(g);
		super.paint(g);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		movePlayer(event.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent event) {
		movePlayer(event.getKeyCode());
	}
	
	public CopyOnWriteArrayList<Item> getItems(){
		return powerups;
	}
	
	public CopyOnWriteArrayList<Entity> getEntities(){
		return entities;
	}
	
	public Set<EntityLiving> getEntitiesLiving(){
		Set<EntityLiving> entitiesLiving = new HashSet<EntityLiving>();
		for (Entity entity : entities) {
			if(entity instanceof EntityLiving)
				entitiesLiving.add((EntityLiving) entity);
		}
		return entitiesLiving;
	}
	
	public Set<EntityLiving> getEntitiesLivingExceptAI(EntityAIPlayer e){
		Set<EntityLiving> entities = getEntitiesLiving();
		entities.remove(e);
		return entities;
	}
	
	private Set<EntityLiving> getAlivePlayer(){
		Set<EntityLiving> entitiesLiving = new HashSet<EntityLiving>();
		for (Entity entity : getEntitiesLiving()) {
			if(entity instanceof EntityLiving && !entity.isDead())
				entitiesLiving.add((EntityLiving) entity);
		}
		return entitiesLiving;
	}
	
	public int getAlivePlayerCount(){
		return getAlivePlayer().size();
	}
		
	public Set<Bomb> getBombs(){
		Set<Bomb> bombs = new HashSet<Bomb>();
		for (Entity entity : entities) {
			if(entity instanceof Bomb && ((Bomb) entity).getState() < Bomb.BOMB_EXPLOSION)
				bombs.add((Bomb) entity);
		}
		return bombs;
	}
	
	public Bomb getBombAt(int x, int y) {
		for(Bomb bomb : getBombs()) {
			if (bomb.getPosition().getX() == x && bomb.getPosition().getY() == y)
				return bomb;
		}
		return null;
	}
	
	public Bomb getBombAt(float x, float y) {
		return getBombAt((int) x, (int) y);
	}
	
	public CopyOnWriteArrayList<Effect> getEffects(){
		return effects;
	}
	
	public CopyOnWriteArrayList<Item> getPowerups(){
		return powerups;
	}

	private void movePlayer(int keyCode) {
		if(keyCode == Controls.getControl("DOWN").getKeyCode())
			if(!player.isDead())
				player.move(EnumDirection.SOUTH);
		if(keyCode == Controls.getControl("LEFT").getKeyCode())
			if(!player.isDead())
				player.move(EnumDirection.WEST);
		if(keyCode == Controls.getControl("RIGHT").getKeyCode())
			if(!player.isDead())
				player.move(EnumDirection.EST);
		if(keyCode == Controls.getControl("UP").getKeyCode())
			if(!player.isDead())
				player.move(EnumDirection.NORTH);
		if(keyCode == Controls.getControl("BOMB").getKeyCode()) {
			int mapTile = map.getTileTypeAt(player.getPosition().getX(), player.getPosition().getY());
			if((mapTile == Map.TILE_FREE || mapTile == Map.FLOWER_TILE) && !player.isDead())
				if(player.getBombPlaced() >= player.getBombCount()) {
					SFX_ImpossibleAction.play();
				}
				else {
					entities.add(new Bomb(player, player.hasMasterBomb(), map, player.getTeam(), effects));
					player.addBombPlaced();
				}
		}
		if(keyCode == KeyEvent.VK_ESCAPE)
			if(!player.isDead())
				pause();
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}
	
	public void displayPlayerName(Graphics g) {
		g.setFont(new Font("Arial", Font.BOLD, 20));
		for (EntityLiving entity : getAlivePlayer()) {
			g.setColor(Color.BLACK);
			g.drawString(entity.getName(), entity.getDisplayX()-2, entity.getDisplayY()-(lives > 1 ? 15 : 0)-2);
			g.setColor(entity == player ? Color.CYAN : Color.WHITE);
			g.drawString(entity.getName(), entity.getDisplayX(), entity.getDisplayY()-(lives > 1 ? 15 : 0));
		}
	}
	
	public void displayLivesBar(Graphics g) {
		for (EntityLiving entity : getAlivePlayer()) {
			g.setColor(Color.RED);
			g.fillRect(entity.getDisplayX(), entity.getDisplayY()-10, 64, 10);
			g.setColor(Color.GREEN);
			g.fillRect(entity.getDisplayX(), entity.getDisplayY()-10, (entity.getLives() == getTotalLives() ? 64 : (int) ((float) 64.0/ getTotalLives() * entity.getLives())), 10);
			g.setColor(Color.BLACK);
			g.drawRect(entity.getDisplayX(), entity.getDisplayY()-10, 64, 10);
		}
	}

	private void drawEntities(Graphics g) {
		for (Entity entity : entities) {
			if (entity instanceof Bomb && ((Bomb) entity).getState() == Bomb.BOMB_EXPLODED) {
			}
			else if(!(entity instanceof Bomb) && entity.isDead()) {
				entities.remove(entity);
			}
			else
				g.drawImage(entity.getSprite(), entity.getDisplayX(), entity.getDisplayY(), Entity.SPRITE_WIDTH,
						Entity.SPRITE_HEIGHT, null);
		}
	}
	
	public void stopMusic() {
		SFX_BackgroundMusic.stop();
	}
	
	private void drawPowerups(Graphics g) {
		for (Item item : powerups) {
			if(item != null) {
				if (item.getState() >= Item.PICKED || item.isDead()) {
					powerups.remove(item);
				}
				else
					g.drawImage(item.getSprite(), item.getDisplayX(), item.getDisplayY(), Item.SPRITE_WIDTH,
							Item.SPRITE_HEIGHT, null);
			}
		}
	}

	private void drawEffects(Graphics g) {
		for (Effect effect : effects) {
			if(effect != null) {
				if (effect instanceof EffectTrail && ((EffectTrail) effect).getState() == EffectTrail.EFFECT_ENDED) {
					effects.remove(effect);
				}
				else
					g.drawImage(effect.getSprite(), effect.getDisplayX(), effect.getDisplayY(), Effect.SPRITE_WIDTH,
							Effect.SPRITE_HEIGHT, null);
			}
		}
	}

	private void drawMap(Graphics g) {
		for (int x = 0; x < Map.MAP_WIDTH; x++) {
			for (int y = 0; y < Map.MAP_HEIGHT; y++) {
				switch (map.getTileTypeAt(x, y)) {
				case Map.ROCK_TILE:
					g.drawImage(rock, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;
				case Map.PLANT_TILE:
					g.drawImage(plant, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;
				case Map.FLOWER_TILE:
					g.drawImage(flower, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
					break;

				}
			}
		}
	}

	private void drawGrass(Graphics g) {
		for (int x = 0; x < Map.MAP_WIDTH; x++) {
			for (int y = 0; y < Map.MAP_HEIGHT; y++) {
				g.drawImage(grass, x * Map.TILE_SCALE, y * Map.TILE_SCALE, Map.TILE_SCALE, Map.TILE_SCALE, null);
			}
		}
	}

}
