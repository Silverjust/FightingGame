package preGame;

import g4p_controls.G4P;
import main.Listener;
import processing.core.PApplet;
import server.ServerApp;
import shared.Client;
import shared.GameBaseApp;
import shared.Global;
import shared.Mode;
import shared.VersionControle;

@SuppressWarnings("serial")
public class PreGameApp extends GameBaseApp {

	String gameName = "FigthingGame";

	public static void main(String[] args) {
		PApplet.main(new String[] { "preGame.PreGameApp" });
	}

	private StartScreenInterface startScreen;
	private PreGameInterface preGameInterface;
	private ServerApp serverApp;

	public void setup() {
		size(1000, 600, JAVA2D);
		background(50);
		frame.setTitle(gameName);
		frame.addWindowListener(new Listener(this));

		Global.addApp(this);
		System.out.println("\n\tFightingGame v" + VersionControle.version + "\n");
		G4P.messagesEnabled(false);

		setMode(Mode.STARTSCREEN);
		if (args != null && args.length > 0 && args[0].equals("dummy")) {
			startScreen.initDummy();
		}
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
			setPreGameIntaerface(new PreGameInterface(this));
			break;

		default:
			break;
		}
		this.mode = mode;
	}

	public void connectToServer(String ip) {
		startScreen.setWaiting(true);
		setClientHandler(new PreGameClientHandler(this, ip));
		startScreen.dispose();
		System.out.println("PreGameApp.connectToServer()");
		setMode(Mode.PREGAME);
	}

	public void registerServerApp(ServerApp serverApp) {
		gameName = "admin-" + gameName;
		this.setServerApp(serverApp);
	}

	public void setServerApp(ServerApp serverApp) {
		this.serverApp = serverApp;
	}

	public void releaseConnectionAttempt(ServerApp serverApp) {
		startScreen.setWaiting(false);
	}

	public PreGameInterface getPreGameInterface() {
		return preGameInterface;
	}

	public void setPreGameIntaerface(PreGameInterface preGame) {
		this.preGameInterface = preGame;
	}

	public ServerApp getServerApp() {
		return serverApp;
	}

	public void clientEvent(Client someClient) {
		getClientHandler().clientEvent(someClient);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public boolean isServer() {
		return false;
	}
}
