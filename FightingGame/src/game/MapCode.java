package game;

import gameStructure.GameObject;
import shared.Player;
import shared.Updater.GameState;

public abstract class MapCode {
	protected Map map;
	private GameApplet app;

	public MapCode(GameApplet app, Map map) {
		this.app = app;
		this.map = map;
	}

	public void setup() {
	}

	public void onGameStart() {// gamestart
	}

	public void onEntitySpawn(GameObject e) {// gamestart
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
		looserP = app.updater.players.get(c[2]);
		if (c[1].equals("lost") && looserP.gameState != GameState.LOST) {
			looserP.gameState = GameState.LOST;
			if (looserP == app.player) {
				app.updater.gameState = GameState.LOST;
				app.preGame.write("GAME", "you lost the game");
			} else {
				app.preGame.write("GAME", looserP.getUser().name + " lost the game");

				int nPlayersInGame = 0;
				for (String key : app.updater.players.keySet()) {
					Player player = app.updater.players.get(key);
					if (player.gameState != GameState.LOST) {
						nPlayersInGame++;
					}
				}

				if (nPlayersInGame == 1) {
					Player lastPlayingPlayer = null;
					for (String key : app.updater.players.keySet()) {
						Player player = app.updater.players.get(key);
						if (player.gameState != GameState.LOST) {
							lastPlayingPlayer = player;
						}
					}
					if (lastPlayingPlayer != null)
						lastPlayingPlayer.gameState = GameState.WON;
					app.updater.gameState = GameState.WON;
					if (app.player != null)
						app.player.gameState = GameState.WON;
					app.preGame.write("GAME", "you win");
					finished = true;
				}
			}

		}

		return finished;
	}
}
