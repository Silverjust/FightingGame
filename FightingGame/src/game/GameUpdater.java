package game;

import java.util.Collections;

import gameStructure.EntityHeightComparator;
import gameStructure.GameObject;
import main.ClientHandler;
import main.preGame.MainPreGame.GameSettings;
import shared.Player;
import shared.Updater;
import shared.User;

public class GameUpdater extends Updater {
	// FIXME einheiten vibrieren

	public Input input;
	private GameApplet app;

	public GameUpdater(GameApplet app) {
		this.app = app;
		neutral = Player.createNeutralPlayer(app);
		input = new Input(app);
		map = new Map(app, app.preGameInfo.map);

		for (String key : app.preGame.users.keySet()) {
			User user = app.preGame.users.get(key);
			Player p = Player.createPlayer(user, app);
			if (p.getUser().ip == ClientHandler.identification) {
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
				toAdd.get(i).onSpawn(GameSettings.singlePlayer);
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
				e.updateDecisions(GameSettings.singlePlayer);
				e.updateMovement();
			}
			if (selectionChanged) {
				selectionChanged = false;
				keepGrid = false;
			}
		}

	}

	private void updateAIs() {
	}

	@Override
	public void send(String string) {
		ClientHandler.send(string);
	}

	@Override
	public void startPause() {
		HUD.menue = new IngameMenu(app);
	}

	@Override
	public void endPause() {
		HUD.menue.dispose();
		HUD.menue = null;
	}

}
