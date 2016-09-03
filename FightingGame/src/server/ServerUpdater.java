package server;

import game.GameBaseApp;
import game.Map;
import game.PreGameInfo;
import gameStructure.GameObject;
import gameStructure.Unit;
import shared.Player;
import shared.Updater;

public class ServerUpdater extends Updater {
	public ServerUpdater(GameBaseApp app) {
		super(app);
		for (String key : app.getPreGameInfo().users.keySet()) {
			players.put(key, Player.createPlayer(app, app.getPreGameInfo().users.get(key)));
		}
		neutral = Player.createNeutralPlayer(app);

		map = new Map(app, app.getPreGameInfo().map);
	}

	@Override
	public void update() {
		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				GameObject.entityCounter += 1;
				gameObjects.add(toAdd.get(i));
				namedObjects.put(GameObject.entityCounter, toAdd.get(i));
				toAdd.get(i).number = GameObject.entityCounter;
				toAdd.get(i).onSpawn(PreGameInfo.isSinglePlayer());
				map.mapCode.onEntitySpawn(toAdd.get(i));
				toAdd.remove(i);
			}
			for (int i = 0; i < toRemove.size(); i++) {
				GameObject entity = toRemove.get(i);
				if (entity != null) {
					int n = entity.number;
					namedObjects.remove(n);
					gameObjects.remove(entity);
					toRemove.remove(i);
					// System.out.println("removed " + n);
				}
			}
			if (app.frameCount % 1000 == 0) {
				send("<say SERVER " + "sync");
				for (GameObject e : gameObjects) {
					if (e instanceof Unit) {
						send("<tp " + e.number + " " + e.getX() + " " + e.getY() + " false");
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

	@SuppressWarnings("deprecation")
	@Override
	public void send(String string) {
		app.getComHandler().executeCom(string);
		((ServerApp) app).getServerHandler().sendDirect(string);
	}

	/**
	 */
	public void reconnect() {
		
	}
}
