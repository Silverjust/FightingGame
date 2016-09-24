package game;

import main.LoadingScreen;
import shared.Coms;
import shared.GameBaseApp;
import shared.Loader;
import shared.Mode;
import shared.PreGameInfo;

public class MainLoader extends Loader {
	public boolean isReconnectLoad;
	private GameBaseApp app;
	private ImageHandler imageHandler;
	private LoadingScreen loadingScreen;

	public MainLoader(GameBaseApp app) {
		this.app = app;
	}

	public void update() {
		switch (state) {

		case NEWGAME:// create players
			loadingScreen = new LoadingScreen(app);
			app.setUpdater(new GameUpdater(app));
			app.setDrawer(new GameDrawer(app));
			state = State.STARTIMAGES;
			break;
		case STARTIMAGES:
			imageHandler = new ImageHandler(app);
			app.getDrawer().setImageHandler(imageHandler);

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
				loadingScreen.setPercent(f);
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			app.getUpdater().map.setup();
			state = State.ENTITIES;
			break;
		case ENTITIES:// spawn entity-setup
			app.getDrawer().getHud().playerInterface.setup();
			if (PreGameInfo.isSandbox()||true) {
				GameDrawer.godeye = true;
				GameDrawer.godhand = true;
				GameDrawer.nocosts = true;
				GameDrawer.showRanges = true;
			}
			if (isReconnectLoad)
				app.getUpdater().send(Coms.RECONNECT + "");
			state = State.WAIT;
			break;
		case WAIT:
			app.getUpdater().send(Coms.READY + " " + app.getClientHandler().getIdentification());
			break;
		case END:
			state = State.NEWGAME;
			app.getUpdater().onGameStart();
			app.setMode(Mode.GAME);
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
		loadingScreen.update();
	}

	@Override
	public void startGame() {
		if (state == State.WAIT) {
			System.out.println("MainLoader.startGame() update updater");
			app.getUpdater().update();
			state = State.END;
		} else {
			System.err.println("game started to early: " + state);
		}
	}

	@Override
	public void tryStartGame() {
		// do nothing when multiplayer
		if (PreGameInfo.isSinglePlayer())
			startGame();
	}
}
