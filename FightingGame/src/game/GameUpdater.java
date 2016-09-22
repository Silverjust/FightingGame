package game;

import java.util.Collections;

import gameStructure.Champion;
import gameStructure.EntityHeightComparator;
import gameStructure.GameObject;
import shared.GameBaseApp;
import shared.Player;
import shared.PreGameInfo;
import shared.Team;
import shared.Updater;
import shared.User;

public class GameUpdater extends Updater {
	// FIXME einheiten vibrieren

	public Input input;

	public GameUpdater(GameBaseApp app) {
		super(app);
		neutral = Player.createNeutralPlayer(app, null);
		leftsideNeutral = Player.createNeutralPlayer(app, Team.LEFTSIDE);
		rightsideNeutral = Player.createNeutralPlayer(app, Team.RIGHTSIDE);
		input = new Input(app);
		map = new Map(app, app.getPreGameInfo().map);

		for (String key : app.getPreGameInfo().users.keySet()) {
			User user = app.getPreGameInfo().users.get(key);
			Player p = Player.createPlayer(app, user);
			if (p.getUser().getIp() == app.getClientHandler().getIdentification()) {
				p.color = app.color(0, 255, 100);
				app.setPlayer(p);
			} else
				p.color = app.color(200, 0, 0);

			players.put(key, p);
		}
		app.setPlayer(players.get(app.getClientHandler().getIdentification()));
	}

	public void update() {
		input.update();
		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				gameObjects.add(toAdd.get(i));
				namedObjects.put(toAdd.get(i).getNumber(), toAdd.get(i));
				toAdd.get(i).onSpawn(PreGameInfo.isSinglePlayer());
				map.mapCode.onEntitySpawn(toAdd.get(i));
				toAdd.remove(i);
			}
			for (int i = 0; i < toRemove.size(); i++) {
				GameObject entity = toRemove.get(i);
				if (entity != null) {
					int n = entity.getNumber();
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
			Collections.sort(app.getPlayer().visibleEntities, new EntityHeightComparator());
			map.mapCodeUpdate();
			for (GameObject e : gameObjects) {
				e.updateAnimation();
				e.updateDecisions(false);
				e.updateMovement();
			}
		}
	}

	@Override
	public void send(String string) {
		app.getClientHandler().send(string);
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

	@Override
	public void handleChampionInit(Champion champion) {
		app.getDrawer().getHud().playerInterface.registerChampion(champion);
	}

}
