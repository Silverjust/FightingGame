package server;

import java.util.HashMap;

import game.Map;
import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Unit;
import shared.GameBaseApp;
import shared.Player;
import shared.PreGameInfo;
import shared.SpellHandler;
import shared.Team;
import shared.Updater;
import shared.User;

public class ServerUpdater extends Updater {
	private int entityCounter = 1;
	public HashMap<String, ServerSpellHandler> spellHandlers = new HashMap<String, ServerSpellHandler>();

	public ServerUpdater(GameBaseApp app, Team team) {
		super(app);
		for (String ip : app.getPreGameInfo().users.keySet()) {
			User user = app.getPreGameInfo().users.get(ip);
			Player player = Player.createPlayer(app, user);
			players.put(ip, player);
			spellHandlers.put(ip, new ServerSpellHandler(app, player));
		}
		neutral = Player.createNeutralPlayer(app, null, players);
		leftsideNeutral = Player.createNeutralPlayer(app, Team.LEFTSIDE, players);
		rightsideNeutral = Player.createNeutralPlayer(app, Team.RIGHTSIDE, players);

		map = new Map(app, app.getPreGameInfo().map);

	}

	@Override
	public void update() {
		if (gameState == GameState.PLAY) {
			for (int i = 0; i < toAdd.size(); i++) {
				gameObjects.add(toAdd.get(i));
				namedObjects.put(toAdd.get(i).getNumber(), toAdd.get(i));
				toAdd.get(i).onSpawn(true);
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
			if (app.frameCount % 1000 == 0) {
				send("<say SERVER " + "sync");
				for (GameObject e : gameObjects) {
					if (e instanceof Unit) {
						send("<tp " + e.getNumber() + " " + e.getX() + " " + e.getY() + " false");
					}
				}
			}

			for (String key : players.keySet()) {
				players.get(key).visibleGObjects.clear();
				for (GameObject e : gameObjects) {
					if (e.isVisibleTo(players.get(key))) {
						players.get(key).visibleGObjects.add(e);
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
		app.getComHandler().executeCom(string, true, null);
		((ServerApp) app).getServerHandler().sendDirect(string);
	}

	public void reconnect() {

	}

	/**
	 * get current GObj number
	 */
	public int getCurrentGObjNumber() {
		return entityCounter;
	}

	/**
	 * create new GObj number
	 */
	public int getNextGObjNumber() {
		entityCounter++;
		return entityCounter - 1;
	}

	public SpellHandler getSpellHandler(String ip) {
		return spellHandlers.get(ip);
	}

	@Override
	public void handleChampionInit(Champion champion) {
		getSpellHandler(champion.player.getUser().getIp()).registerChampion(champion);
	}

}
