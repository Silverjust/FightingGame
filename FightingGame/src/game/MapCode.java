package game;

import gameStructure.GameObject;
import shared.GameBaseApp;
import shared.Player;
import shared.Updater.GameState;

public abstract class MapCode {
	protected Map map;
	private GameBaseApp app;

	public MapCode(GameBaseApp app, Map map) {
		this.app = app;
		this.map = map;
	}

	public void setup() {
	}

	public void onGameStart() {// gamestart
	}

	public void onEntitySpawn(GameObject e, boolean isServer) {// gamestart
	}

	public void update() {
	}

	public void onEnd() {// gameend
	}

	public void dispose() {
	}

	/** @return boolean finished game, everyone won or lost */
	public boolean handleGameEnd(String[] c) {
		boolean finished = false;
		Player looserP;
		looserP = app.getUpdater().players.get(c[2]);
		if (c[1].equals("lost") && looserP.gameState != GameState.LOST) {
			looserP.gameState = GameState.LOST;
			if (looserP == app.getPlayer()) {
				app.getUpdater().gameState = GameState.LOST;
				app.write("GAME", "you lost the game");
			} else {
				app.write("GAME", looserP.getUser().name + " lost the game");

				int nPlayersInGame = 0;
				for (String key : app.getUpdater().players.keySet()) {
					Player player = app.getUpdater().players.get(key);
					if (player.gameState != GameState.LOST) {
						nPlayersInGame++;
					}
				}

				if (nPlayersInGame == 1) {
					Player lastPlayingPlayer = null;
					for (String key : app.getUpdater().players.keySet()) {
						Player player = app.getUpdater().players.get(key);
						if (player.gameState != GameState.LOST) {
							lastPlayingPlayer = player;
						}
					}
					if (lastPlayingPlayer != null)
						lastPlayingPlayer.gameState = GameState.WON;
					app.getUpdater().gameState = GameState.WON;
					if (app.getPlayer() != null)
						app.getPlayer().gameState = GameState.WON;
					app.write("GAME", "you win");
					finished = true;
				}
			}

		}

		return finished;
	}
}
