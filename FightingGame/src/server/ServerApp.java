package server;

import g4p_controls.G4P;
import processing.core.PApplet;
import processing.core.PConstants;
import shared.Client;
import shared.Server;
import shared.Updater;
import shared.Mode;
import shared.ref;
import shared.Helper.Timer;

@SuppressWarnings("serial")
public class ServerApp extends PApplet {
	public static void main(String args[]) {
		System.out.println("starting Server...");
		PApplet.main(new String[] { "server.ServerApp" });
	}

	public ServerHandler serverHandler;

	public GUI gui;

	Mode mode;

	public void setup() {
		size(500, 500, PConstants.P2D);
		frame.setTitle("Server EliteEngine");
		mode = Mode.PREGAME;
		G4P.messagesEnabled(false);

		ref.setTextScale(1.4F);// so ungefär

		ref.setApp(this);

		ref.setPreGame(new ServerPreGame());

		gui = new GUI();
		serverHandler = new ServerHandler();

		startRF();
	}

	public void draw() {
		background(240);
		gui.update();
		serverHandler.update();
		switch (mode) {
		case PREGAME:
			ref.preGame.update();
			break;
		case LADESCREEN:
			ref.loader.update();
			break;
		case GAME:
			// if (frameCount % 2 == 0)
			thread("serverUpdate");
			break;
		default:
			break;
		}

	}

	public void serverUpdate() {
		ref.updater.update();
	}

	public void disconnectEvent(Client client) {
		serverHandler.disconnectEvent(client);
	}

	public void serverEvent(Server server, Client someClient) {
		serverHandler.serverEvent(server, someClient);
	}

	private void startRF() {
		float time = (float) (((int) (Math.random() * 500.0)) / 100.0);
		int cooldown = (int) (time * 60 * 1000);
		ref.preGame.write("GAME", "resfreeze in " + (cooldown / 60.0 / 1000.0));
		Updater.resfreeze = new Timer(cooldown);
		if (ref.app instanceof ServerApp)
			((ServerApp) ref.app).serverHandler.doProtocol = true;
	}

	@Override
	public void dispose() {
		serverHandler.dispose();
		super.dispose();
	}
}
