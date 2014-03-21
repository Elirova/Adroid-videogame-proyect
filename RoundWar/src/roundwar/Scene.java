package roundwar;

import java.util.LinkedList;
import java.util.List;

import screenControl.Background;
import screenControl.GameScreen;
import Entities.LivingEntity;
import Entities.ReturnIntEntity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Scene {
	// Calcular el nivel maximo y minimo de los bichos que aparecen
	// Spawneo de bichos al comienzo del mapa
	// Spawneo dinámico de bichos durante el mapa
	// Tipos de bichos que pueden aparecer en este mapa
	// Bichos que hay spawneados?
	// Cantidad de bichos que has matado?
	// Numero de oleada en la que estas?
	private static GameScreen game;
	
	private int minLevel, maxLevel;
	private int totalEnemies;
	private String path, nameLevel;
	private List<Vector2> spawnPoints;
	private LinkedList<Wave> waves;
	private Wave currentWave;
	private Background bg;
	//private List<LivingEntity.Type> monstersSpawn;
	
	
	public Scene(String nameLevel) {
		this.nameLevel = nameLevel;
		this.path = "background/map" + nameLevel + ".tmx";
		this.totalEnemies = 0;
		bg = new Background(game, path);
		spawnPoints = bg.loadObstacles();
		game.getStage().addActor(bg);
		
		Wave.setScreen(game);
		Wave.setSpawns(spawnPoints);
		waves = new LinkedList<Wave>();
		
		if(this.nameLevel == "Prueba") {
			initializeLevel(1, 3, 300, 200);
			waves.add(new Wave(5, 6, minLevel, maxLevel));
			waves.add(new Wave(50, 6, 1, 2));
			currentWave = waves.pop();
		} else if(this.nameLevel == "2") {
			initializeLevel(1, 2, 100, 200);
		}
		
		//monstersSpawn = new LinkedList<LivingEntity.Type>();
		
	}
	
	public void update(float delta) {
		if(currentWave != null && game.getTime() > currentWave.getTime()) {
			//System.out.println("NUEVA OLEADA");
			if(!currentWave.spawnEnemies(delta)){
				currentWave = waves.isEmpty() ? null : waves.pop();
			}
		}
	}
	
	public void updateNumEnemies(int numEnemies) {
		totalEnemies += numEnemies;
	}
	
	public int getNumEnemies() {
		return totalEnemies;
	}
	
	public Vector2 calculateRandomSpawn() {
    	int random = (int)(Math.random()%spawnPoints.size());
    	return spawnPoints.get(random);
    }
	
	public List<Vector2> getSpawnPoints() {
		return spawnPoints;
	}
	
	public static void setScreen(GameScreen screen) {
		Scene.game = screen;
	}
	
	public void addSpawn(Vector2 point) {
		spawnPoints.add(point);
	}
	
	public void setMaxLevel(int level) {
		this.maxLevel = level;
	}
	
	public Background getBackground() {
		return bg;
	}
	
	public void setMinLevel(int level) {
		this.minLevel = level;
	}
	
	public String getPath() {
		return path;
	}
	
	private void initializeLevel(int minLevel, int maxLevel, int initialX, int initialY) {
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		game.getCharacter().setPosition(initialX, initialY);
	}
	
	public ReturnIntEntity isFree(LivingEntity entity, float deltaX, float deltaY, int cooldown) {
    	if(cooldown >= 0) {
    		return isFree(entity, deltaX, deltaY);
    	} else {
    		LivingEntity ent = game.collides(entity, deltaX, deltaY);
    		if(ent != null) {
    			return new ReturnIntEntity(3, ent);
    		}
    		return new ReturnIntEntity(0, null);
    	}
    }
    
    public ReturnIntEntity isFree(LivingEntity entity, float deltaX, float deltaY) {
    	Rectangle bounds = new Rectangle(entity.getBounds());
    	bounds.x += deltaX;
    	bounds.y += deltaY;
    	ReturnIntEntity result = new ReturnIntEntity(0, null);
    	LivingEntity ent = game.collides(entity, deltaX, deltaY);
    	
    	if(ent == null && bg.isFree(bounds)) { 			// Sin colision en ningún eje
    		return result;
    	} else {
    		result.setEntity(ent);
    		ent = game.collides(entity, deltaX, 0);
	    	bounds.y -= deltaY;
	    	if(ent == null && bg.isFree(bounds)) { 		// Sin colision en el eje x
	    		result.setInt(1);
	    	} else {
	    		result.setEntity(ent);
	    		ent = game.collides(entity, 0, deltaY);
	    		bounds.y += deltaY;
		    	bounds.x -= deltaX;
		    	if(ent == null && bg.isFree(bounds)) { 	// Sin colision en el eje y
		    		result.setInt(2);
		    	} else {								// Colisión en ambos ejes
		    		result.setInt(3);
		    	}
	    	}
    	}
    	return result;
    }
    
    public boolean isFree(float posX, float posY) {
    	return bg.isFree(posX, posY);
    }
    
    public void dispose() {
    	game.getStage().getRoot().removeActor(bg);
		bg.dispose();
    }
}