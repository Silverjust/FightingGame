package server;

import g4p_controls.G4P;
import game.GameBaseApp;
import preGame.PreGameApp;
import processing.core.PApplet;
import processing.core.PConstants;
import shared.Client;
import shared.Global;
import shared.Mode;
import shared.Server;

@SuppressWarnings("serial")
public class ServerApp extends GameBaseApp {
	public static void main(String args[]) {
		boolean b = false;
		// b=true;
		if (b) {
			System.out.println("starting Server...");
			PApplet.main(new String[] { "server.ServerApp", "1" });
		} else {
			PApplet.main(new String[] { "preGame.PreGameApp" });
		}
	}

	public ServerHandler serverHandler;

	public ServerInterface gui;

	public PreGameManager preGame;

	PreGameApp adminApp;

	public void setup() {
		size(500, 500, PConstants.P2D);
		frame.setTitle("Server FightingGame");
		Global.addApp(this);

		G4P.messagesEnabled(false);
		setTextScale(1.4F);// so ungefär

		registerServer();

		setMode(Mode.PREGAME);
		preGame = new PreGameManager(this);
		serverHandler = new ServerHandler(this);
		gui = new ServerInterface(this);

	}

	public void draw() {
		background(240);
		gui.update();
		serverHandler.update();
		switch (mode) {
		case PREGAME:
			// getPreGameInfo().update();
			break;
		case LADESCREEN:
			// GameBaseApp.loader.update();
			break;
		case GAME:
			// if (frameCount % 2 == 0)
			thread("serverUpdate");
			break;
		default:
			break;
		}

	}

	private void registerServer() {
		if (args != null) {
			int i = Integer.parseInt(args[0]);
			if (i < Global.getApps().size()) {
				GameBaseApp gameBaseApp = Global.getApps().get(i);
				if (gameBaseApp instanceof PreGameApp) {
					adminApp = (PreGameApp) gameBaseApp;
				}
			}
			for (String arg : args) {
				System.out.println("ServerApp.setup() arg: " + arg);
			}
		}
		if (adminApp == null) {
			System.err.println("no admin app found");
		} else {
			adminApp.registerServer(this);
		}
	}

	public void serverUpdate() {
		getUpdater().update();
	}

	public void disconnectEvent(Client client) {
		serverHandler.disconnectEvent(client);
	}

	public void serverEvent(Server server, Client someClient) {
		serverHandler.serverEvent(server, someClient);
	}

	@Override
	public void dispose() {
		serverHandler.dispose();
		super.dispose();
	}

	public boolean hadError() {
		return false;
	}
}
