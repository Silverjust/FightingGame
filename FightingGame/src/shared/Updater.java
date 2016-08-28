package shared;

import java.util.ArrayList;
import java.util.HashMap;

import game.GameBaseApp;
import game.Map;
import gameStructure.GameObject;
import shared.Helper.Timer;

public abstract class Updater {
	protected HashMap<Integer, GameObject> namedObjects = new HashMap<Integer, GameObject>();
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
	public ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	public Map map;

	public HashMap<String, Player> players = new HashMap<String, Player>();
	public Player neutral;

	public GameState gameState = GameState.PLAY;
	public boolean selectionChanged;
	public boolean keepGrid;
	protected GameBaseApp app;

	static public Timer resfreeze;

	public Updater(GameBaseApp app) {
		this.app = app;
		if (resfreeze != null)
			resfreeze.startCooldown();

		app.getComHandler().addUpdater(this);
	}

	public abstract void update();

	public boolean arePlayerReady() {
		boolean b = true;
		for (String key : players.keySet()) {
			if (!players.get(key).getUser().isReady)
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
			System.out.println("Updater.Time.setup()" + Time.app);
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
		GameObject.entityCounter = 0;
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
		return namedObjects.get(n);
	}

	public void addGameObject(GameObject o, String string) {
		toAdd.add(o);
		System.out.println("Updater.addGameObject() " + string + " " + o);
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}
}
