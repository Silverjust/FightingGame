package server;

import game.ImageHandler;
import game.MapHandler;
import shared.Coms;
import shared.Loader;
import shared.Mode;
import shared.Nation;
import shared.Updater;
import shared.ref;

public class MultiplayerLoader extends Loader {

	ServerApp app;

	public MultiplayerLoader() {
		app = (ServerApp) ref.app;
	}

	@Override
	public void update() {
		switch (state) {

		case NEWGAME:
			Nation.setNationsToPlayableNations();
			ref.updater = new ServerUpdater();
			state = State.MAP;// map
			break;
		case STARTIMAGES:
			boolean b = ImageHandler.requestAllImages();
			if (b) {
				state = State.IMAGES;
			} else {
				state = State.ERROR;
			}
			break;
		case IMAGES:
			float f = game.ImageHandler.stateOfLoading();
			if (f < 0) {
				state = State.ERROR;
			} else if (f < 1) {
			} else {
				state = State.MAP;
			}
			break;
		case MAP:
			ref.updater.map.setup();
			state = State.WAIT;
			break;
		case WAIT:
			break;
		case ENTITIES:// spawn entity-setup
			MapHandler.setupEntities(ref.updater.map.mapData);
			state = State.END;
			break;
		case END:
			// ServerDisplay.main(new String[] {});
			state = State.NEWGAME;
			app.mode = Mode.GAME;
			app.serverHandler.send(Coms.START_GAME+"");
			System.out.println("Game Start");
			sendRFInfo();
			break;
		case ERROR:
			System.out.println("error");
			ref.app.stop();
			break;
		default:
			break;
		}
	}

	@Override
	public void startGame() {

	}

	@Override
	public void tryStartGame() {
		if (state == State.WAIT && ref.updater.arePlayerReady()) {
			state = State.ENTITIES;
		} else if (app.mode == Mode.GAME) {
			app.serverHandler.send(Coms.START_GAME+"");
		}
	}

	private void sendRFInfo() {
		if (Updater.resfreeze != null) {
			ref.preGame.write("GAME", "resfreeze in " + (Updater.resfreeze.cooldown / 60.0 / 1000.0));
		}
	}
}
