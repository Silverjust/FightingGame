package shared;

import java.util.ArrayList;
import java.util.HashMap;

import game.GameApplet;
import game.Map;
import gameStructure.GameObject;
import shared.Helper.Timer;

public abstract class Updater {
	protected HashMap<Integer, GameObject> namedObjects = new HashMap<Integer, GameObject>();
	protected ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	protected ArrayList<GameObject> toAdd = new ArrayList<GameObject>();
	public ArrayList<GameObject> toRemove = new ArrayList<GameObject>();

	public Updater() {
		if (resfreeze != null)
			resfreeze.startCooldown();

		ComHandler.addUpdater(this);
	}

	public Map map;

	public HashMap<String, Player> players = new HashMap<String, Player>();
	public Player neutral;

	public GameState gameState = GameState.PLAY;
	public boolean selectionChanged;
	public boolean keepGrid;

	static public Timer resfreeze;

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

		public static void startPause() {
			if (GameApplet.updater.gameState == GameState.PLAY) {
				GameApplet.updater.gameState = GameState.PAUSE;
				pauseStart = GameApplet.app.millis();
			}
		}

		public static void endPause() {
			if (GameApplet.updater.gameState != GameState.PLAY) {
				GameApplet.updater.gameState = GameState.PLAY;
				pauseTime += GameApplet.app.millis() - pauseStart;
				pauseStart = 0;
			}
		}

		public static int getMillis() {
			if (pauseStart == 0)
				return GameApplet.app.millis() - pauseTime;
			else
				return GameApplet.app.millis() - (pauseTime + GameApplet.app.millis() - pauseStart);
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
