package game;

import java.util.Collections;

import gameStructure.EntityHeightComparator;
import gameStructure.GameObject;
import shared.Player;
import shared.Updater;
import shared.User;

public class GameUpdater extends Updater {
	// FIXME einheiten vibrieren

	public Input input;

	public GameUpdater(GameBaseApp app) {
		super(app);
		this.app = app;
		neutral = Player.createNeutralPlayer(app);
		input = new Input(app);
		map = new Map(app, app.preGameInfo.map);

		for (String key : app.getPreGameInfo().users.keySet()) {
			User user = app.getPreGameInfo().users.get(key);
			Player p = Player.createPlayer(app, user);
			if (p.getUser().ip == app.clientHandler.identification) {
				p.color = app.color(0, 255, 100);
				app.player = p;
			} else
				p.color = app.color(200, 0, 0);
			players.put(key, p);
		}
	}

	public void update() {
		input.update();
		updateAIs();

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
			for (String key : players.keySet()) {
				players.get(key).visibleEntities.clear();
				for (GameObject e : gameObjects) {
					/*
					 * if (player.get(key) == ref.player) { selected.remove(e);
					 * if (e.isSelected) { selected.add(e); } }
					 */
					if (GameDrawer.godeye || e.isVisibleTo(players.get(key))) {
						players.get(key).visibleEntities.add(e);
					}
				}
			}
			// sortierfunktion
			Collections.sort(app.player.visibleEntities, new EntityHeightComparator());
			map.mapCodeUpdate();
			for (GameObject e : gameObjects) {
				e.updateAnimation();
				e.updateDecisions(PreGameInfo.isSinglePlayer());
				e.updateMovement();
			}
			
		}

	}

	private void updateAIs() {
	}

	@Override
	public void send(String string) {
		app.clientHandler.send(string);
	}

	@Override
	public void startPause() {
		app.getDrawer().getHud().menue = new IngameMenu(app);
	}

	@Override
	public void endPause() {
		app.getDrawer().getHud().menue.dispose();
		app.getDrawer().getHud().menue = null;
	}

}
