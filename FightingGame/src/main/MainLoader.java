package main;

import game.GameDrawer;
import game.GameUpdater;
import game.ImageHandler;
import main.preGame.MainPreGame.GameSettings;
import shared.Coms;
import shared.Loader;
import shared.Mode;
import shared.ref;

public class MainLoader extends Loader {
	public boolean isReconnectLoad;

	public void update() {
		switch (state) {

		case NEWGAME:// create players
			LoadingScreen.setup();
			ref.updater = new GameUpdater();
			state = State.STARTIMAGES;
			break;
		case STARTIMAGES:
			ImageHandler.setup();
			boolean b = ImageHandler.requestAllImages();
			if (b) {
				state = State.IMAGES;
			} else {
				state = State.ERROR;
			}
			break;
		case IMAGES:
			float f = ImageHandler.stateOfLoading();
			if (f < 0) {
				state = State.ERROR;
			} else if (f < 1) {
				LoadingScreen.setPercent(f);
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			ref.updater.map.setup();
			state = State.ENTITIES;
			break;
		case ENTITIES:// spawn entity-setup

			if (GameSettings.singlePlayer) {
				if (GameSettings.sandbox)
					ref.updater.send("<spawn SandboxBuilding 0 20 20");
				/*
				 * if (GameSettings.tutorial)
				 * ref.updater.send("<spawn Tutorial 0 20 20");
				 */
				// MapHandler.setupEntities(ref.updater.map.mapData);
				/*
				 * int i = 0; for (String key : ref.updater.player.keySet()) {
				 * i++; Player p = ref.updater.player.get(key); if (i == 1) {
				 * ref.updater.send("<spawn AlienMainBuilding " + p.ip + " " +
				 * 100 + " " + 800); ref.updater.send("<spawn KeritMine " + p.ip
				 * + " " + 35 + " " + 850); } else if (i == 2) {
				 * ref.updater.send("<spawn AlienMainBuilding " + p.ip + " " +
				 * 700 + " " + 100); ref.updater.send("<spawn KeritMine " + p.ip
				 * + " " + 750 + " " + 35); }
				 * ref.updater.send("<spawn KeritMine " + p.ip + " " +
				 * ref.app.random(200, 600) + " " + ref.app.random(200, 600));
				 * ref.updater.send("<spawn Kerit " + p.ip + " " +
				 * ref.app.random(200, 600) + " " + ref.app.random(200, 600));
				 * ref.updater.send("<spawn Pax " + p.ip + " " +
				 * ref.app.random(200, 600) + " " + ref.app.random(200, 600));
				 * ref.updater.send("<spawn Arcanum " + p.ip + " " +
				 * ref.app.random(200, 600) + " " + ref.app.random(200, 600));
				 * ref.updater.send("<spawn Prunam " + p.ip + " " +
				 * ref.app.random(200, 600) + " " + ref.app.random(200, 600)); }
				 */
				ref.updater.send("<spawn Ticul " + 1 + " " + ref.app.random(200, 600) + " " + ref.app.random(200, 600));
				ref.updater.send("<spawn Ticul " + 2 + " " + ref.app.random(200, 600) + " " + ref.app.random(200, 600));
			}
			GameDrawer.setup();
			if (GameSettings.sandbox) {
				GameDrawer.godeye = true;
				GameDrawer.godhand = true;
				GameDrawer.nocosts = true;
				GameDrawer.showRanges = true;
			}
			if (isReconnectLoad)
				ref.updater.send(Coms.RECONNECT + "");
			state = State.WAIT;
			break;
		case WAIT:
			ref.updater.send(Coms.READY + " " + ClientHandler.identification);
			break;
		case END:
			state = State.NEWGAME;
			ref.updater.onGameStart();
			((MainApp) ref.app).mode = Mode.GAME;
			// System.out.println(ref.updater);
			System.out.println("Game Start");
			break;
		case ERROR:
			System.out.println("error");
			ref.app.dispose();
			break;
		default:
			break;
		}
		LoadingScreen.update();
	}

	@Override
	public void startGame() {
		if (state == State.WAIT) {
			state = State.END;
		} else {
			System.out.println("game started to early: " + state);
		}
	}

	@Override
	public void tryStartGame() {
		// do nothing when multiplayer
		if (GameSettings.singlePlayer)
			startGame();
	}
}
