package server;

import game.GameBaseApp;
import game.Map;
import gameStructure.GameObject;
import gameStructure.Unit;

import java.util.ArrayList;

import shared.ComHandler;
import shared.Coms;
import shared.Player;
import shared.Updater;

public class ServerUpdater extends Updater {
	public ServerUpdater() {
		for (String key : GameApplet.GameBaseApp.users.keySet()) {
			players.put(key, Player.createPlayer(GameApplet.GameBaseApp.users.get(key)));
		}
		neutral = Player.createNeutralPlayer();

		map = new Map(GameApplet.GameBaseApp.map);
	}

	@Override
	public void update() {
		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				GameObject.entityCounter += 1;
				gameObjects.add(toAdd.get(i));
				getNamedObjects().put(GameObject.entityCounter, toAdd.get(i));
				toAdd.get(i).number = GameObject.entityCounter;
				toAdd.get(i).onSpawn(true);
				toAdd.remove(i);
			}
			for (int i = 0; i < toRemove.size(); i++) {
				if (toRemove.get(i) != null) {
					int n = toRemove.get(i).number;
					getNamedObjects().remove(n);
					selected.remove(toRemove.get(i));
					gameObjects.remove(toRemove.get(i));
					toRemove.remove(i);
					// System.out.println("removed " + n);
				}
			}
			if (GameApplet.GameBaseApp.frameCount % 1000 == 0) {
				GameBaseApp.updater.send("<say SERVER " + "sync");
				for (GameObject e : gameObjects) {
					if (e instanceof Unit) {
						GameBaseApp.updater.send("<tp " + e.number + " " + e.getX() + " " + e.getY() + " false");
					}
				}
			}

			for (String key : players.keySet()) {
				players.get(key).visibleEntities.clear();
				for (GameObject e : gameObjects) {
					if (e.isVisibleTo(players.get(key))) {
						players.get(key).visibleEntities.add(e);
					}
				}
			}
			map.mapCodeUpdate();
			for (GameObject e : gameObjects) {
				e.updateAnimation();
				e.updateDecisions(true);
				e.updateMovement();
			}
		}
	}

	@Override
	public void send(String string) {
		ComHandler.executeCom(string);
		((ServerApp) GameBaseApp.app).serverHandler.send(string);
	}

	/** pauses the game, clears all entities, respawns every entity and unpauses*/
	public void reconnect() {
		gameState = GameState.PAUSE;
		GameBaseApp.updater.send(Coms.PAUSE + " true");
		ArrayList<String> spawns = new ArrayList<String>();
		for (GameObject entity : gameObjects) {
			spawns.add("<spawn " + entity.getClass().getSimpleName() + " " + entity.player.getUser().ip + " " + entity.getX()
					+ " " + entity.getY());
		}
		for (GameObject entity : gameObjects) {
			GameBaseApp.updater.send("<remove " + entity.number);
		}
		for (String com : spawns) {
			GameBaseApp.updater.send(com);
		}
		System.out.println("finished reconnect, restart game");
		GameBaseApp.updater.send(Coms.PAUSE + " false");
		gameState = GameState.PLAY;
	}
}
