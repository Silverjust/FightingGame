package game;

import main.LoadingScreen;
import shared.Coms;
import shared.Loader;
import shared.Mode;

public class MainLoader extends Loader {
	public boolean isReconnectLoad;
	private GameApplet app;
	private ImageHandler imageHandler;

	public MainLoader(GameApplet app) {
		this.app = app;
	}

	public void update() {
		switch (state) {

		case NEWGAME:// create players
			LoadingScreen.setup();
			app.updater = new GameUpdater(app);
			state = State.STARTIMAGES;
			break;
		case STARTIMAGES:
			imageHandler = new ImageHandler(app);
			boolean b = imageHandler.requestAllImages();
			if (b) {
				state = State.IMAGES;
			} else {
				state = State.ERROR;
			}
			break;
		case IMAGES:
			float f = imageHandler.stateOfLoading();
			if (f < 0) {
				state = State.ERROR;
			} else if (f < 1) {
				LoadingScreen.setPercent(f);
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			app.updater.map.setup();
			state = State.ENTITIES;
			break;
		case ENTITIES:// spawn entity-setup

			if (PreGameInfo.isSinglePlayer()) {
				if (PreGameInfo.isSandbox())
					app.updater.send("<spawn SandboxBuilding 0 20 20");
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
				app.updater.send("<spawn Ticul " + 1 + " " + app.random(200, 600) + " " + app.random(200, 600));
				app.updater.send("<spawn Ticul " + 2 + " " + app.random(200, 600) + " " + app.random(200, 600));
			}
			app.gameDrawer = new GameDrawer(app,imageHandler);
			if (PreGameInfo.isSandbox()) {
				GameDrawer.godeye = true;
				GameDrawer.godhand = true;
				GameDrawer.nocosts = true;
				GameDrawer.showRanges = true;
			}
			if (isReconnectLoad)
				app.updater.send(Coms.RECONNECT + "");
			state = State.WAIT;
			break;
		case WAIT:
			app.updater.send(Coms.READY + " " + app.clientHandler.identification);
			break;
		case END:
			state = State.NEWGAME;
			app.updater.onGameStart();
			app.mode = Mode.GAME;
			// System.out.println(ref.updater);
			System.out.println("Game Start");
			break;
		case ERROR:
			System.out.println("error");
			app.dispose();
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
		if (PreGameInfo.isSinglePlayer())
			startGame();
	}
}
