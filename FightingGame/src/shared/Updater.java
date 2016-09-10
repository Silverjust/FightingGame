package shared;

import java.util.ArrayList;
import java.util.HashMap;

import game.Map;
import game.MapHandler;
import gameStructure.GameObject;
import server.ServerUpdater;
import shared.Helper.Timer;

public abstract class Updater {
	protected HashMap<Integer, GameObject> namedObjects = new HashMap<Integer, GameObject>();
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
	public ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	public Map map;

	public HashMap<String, Player> players = new HashMap<String, Player>();
	public Player neutral;
	protected Player rightsideNeutral;
	protected Player leftsideNeutral;

	public GameState gameState = GameState.PLAY;
	protected GameBaseApp app;
	public MapHandler mapHandler;

	static public Timer resfreeze;

	public Updater(GameBaseApp app) {
		this.app = app;
		app.getComHandler().addUpdater(this);

		mapHandler = new MapHandler(app);
	}

	public abstract void update();

	public boolean arePlayerReady() {
		boolean b = true;
		for (String key : players.keySet()) {
			if (!players.get(key).getUser().isReady())
				b = false;
		}
		return b;
	}

	public abstract void send(String string);

	public enum GameState {
		PLAY, PAUSE, LOST, WON
	}

	public static class Time {
		// TODO pause not working
		private static int pauseStart;
		private static int pauseTime;
		private static GameBaseApp app;

		public static void setup(GameBaseApp app) {
			Time.app = app;
		}

		public static void startPause() {
			if (app.getUpdater().gameState == GameState.PLAY) {
				app.getUpdater().gameState = GameState.PAUSE;
				pauseStart = app.millis();
			}
		}

		public static void endPause() {
			if (app.getUpdater().gameState != GameState.PLAY) {
				app.getUpdater().gameState = GameState.PLAY;
				pauseTime += app.millis() - pauseStart;
				pauseStart = 0;
			}
		}

		public static int getMillis() {
			if (pauseStart == 0)
				return app.millis() - pauseTime;
			else
				return app.millis() - (pauseTime + app.millis() - pauseStart);
		}
	}

	public void startPause() {
	}

	public void endPause() {
	}

	public void dispose() {
		namedObjects.clear();
		gameObjects.clear();
		toAdd.clear();
		toRemove.clear();

		resfreeze = null;
	}

	public void onGameStart() {
		map.mapCode.onGameStart();
	}

	public Player getPlayer(String ip) {
		return players.get(ip);
	}

	public GameObject getGameObject(int n) {
		System.out.println("Updater.getGameObject()" + n + " " + namedObjects.size());
		return namedObjects.get(n);
	}

	public void addGameObject(GameObject o) {
		toAdd.add(o);
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void sendSpawn(Class<? extends GameObject> c, Player owner, String content) {
		send(Coms.SPAWN + " " + c.getSimpleName() + " " + ((ServerUpdater) this).getNextGObjNumber() + " "
				+ owner.getUser().getIp() + " " + content);
	}
}
