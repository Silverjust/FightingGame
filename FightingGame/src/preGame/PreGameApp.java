package preGame;

import game.GameBaseApp;
import processing.core.PApplet;
import server.ServerApp;
import shared.Global;
import shared.Mode;

@SuppressWarnings("serial")
public class PreGameApp extends GameBaseApp {

	public static void main(String[] args) {
		PApplet.main(new String[] { "preGame.PreGameApp" });
	}

	private StartScreenInterface startScreen;
	private PreGameInterface preGameInterface;
	public ClientHandler clientHandler;
	private ServerApp serverApp;

	public void setup() {
		size(1000, 600, JAVA2D);
		background(50);
		frame.setTitle("FigthingGame");
		Global.addApp(this);

		// G4P.messagesEnabled(false);
		setMode(Mode.STARTSCREEN);
	}

	public void draw() {
		switch (getMode()) {
		case STARTSCREEN:
			startScreen.update();
			break;
		case PREGAME:
			preGameInterface.update();
			break;
		default:
			break;
		}
	}

	public StartScreenInterface getStartscreen() {
		return startScreen;
	}

	public void setStartscreen(StartScreenInterface startscreen) {
		this.startScreen = startscreen;
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		switch (mode) {
		case STARTSCREEN:
			setStartscreen(new StartScreenInterface(this));
			break;
		case PREGAME:
			setPreGame(new PreGameInterface(this));
			break;

		default:
			break;
		}
		this.mode = mode;
	}

	public void connectToServer(String ip) {
		clientHandler = new ClientHandler(this, ip);
		startScreen.setWaiting(true);
		startScreen.dispose();
		setMode(Mode.PREGAME);
	}

	public void registerServer(ServerApp serverApp) {
		this.setServerApp(serverApp);
	}

	public PreGameInterface getPreGame() {
		return preGameInterface;
	}

	public void setPreGame(PreGameInterface preGame) {
		this.preGameInterface = preGame;
	}

	public ServerApp getServerApp() {
		return serverApp;
	}

	public void setServerApp(ServerApp serverApp) {
		this.serverApp = serverApp;
	}

}
