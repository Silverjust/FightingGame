package server;

import shared.Coms;
import shared.GameBaseApp;
import shared.Loader;
import shared.Mode;

public class ServerLoader extends Loader {

	ServerApp app;

	public ServerLoader(GameBaseApp app) {
		this.app = (ServerApp) app;
	}

	@Override
	public void update() {
		switch (state) {

		case NEWGAME:
			app.setUpdater(new ServerUpdater(app, null));
			state = State.MAP;// map
			break;
		case MAP:
			app.getUpdater().map.setup();
			state = State.WAIT;
			break;
		case WAIT:
			break;
		case ENTITIES:// spawn entity-setup
			app.getUpdater().mapHandler.setupEntities(app.getUpdater().map.mapData);
			app.getUpdater().update();
			state = State.END;
			break;
		case END:
			// ServerDisplay.main(new String[] {});
			state = State.NEWGAME;
			app.setMode(Mode.GAME);
			app.getServerHandler().sendDirect(Coms.START_GAME + "");
			System.out.println("Game Start");
			break;
		case ERROR:
			System.out.println("error");
			app.stop();
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
		if (state == State.WAIT && app.getUpdater().arePlayerReady()) {
			state = State.ENTITIES;
		} else if (app.getMode() == Mode.GAME) {
			app.getServerHandler().sendDirect(Coms.START_GAME + "");
		}
	}

}
