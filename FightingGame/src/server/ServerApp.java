package server;

import g4p_controls.G4P;
import main.Listener;
import preGame.PreGameApp;
import processing.core.PApplet;
import shared.Client;
import shared.GameBaseApp;
import shared.Global;
import shared.Mode;
import shared.Server;

@SuppressWarnings("serial")
public class ServerApp extends GameBaseApp {
	public static void main(String args[]) {
		boolean b = false;
		// b=true;
		if (b) {
			PApplet.main(new String[] { "server.ServerApp", "1" });
		} else {
			PApplet.main(new String[] { "preGame.PreGameApp" });
		}
	}

	public ServerInterface serverInterface;
	public ServerPreGameManager serverPreGameManager;

	private PreGameApp adminApp;

	public void setup() {
		System.out.println("starting Server...");
		size(500, 500, JAVA2D);
		frame.setTitle("Server FightingGame");
		frame.addWindowListener(new Listener(this));
		Global.addApp(this);
		G4P.messagesEnabled(false);
		setTextScale(1.4F);// so ungefär

		registerServer(args);

		setMode(Mode.PREGAME);
		serverPreGameManager = new ServerPreGameManager(this);
		serverInterface = new ServerInterface(this);
		setServerHandler(new ServerHandler(this));

	}

	public void draw() {
		background(240);
		serverInterface.update();
		getServerHandler().update();
		switch (mode) {
		case PREGAME:
			// getPreGameInfo().update();
			break;
		case LADESCREEN:
			getLoader().update();
			break;
		case GAME:
			// if (frameCount % 2 == 0)
			thread("serverUpdate");
			break;
		default:
			break;
		}

	}

	@Override
	public void write(String name, String text) {
		if (!name.equals(""))
			serverInterface.addChatText(name + " :" + text);
		else
			serverInterface.addChatText(text);
	}

	private void registerServer(String[] args) {
		if (args != null) {
			int i = Integer.parseInt(args[0]);
			if (i < Global.getApps().size()) {
				GameBaseApp gameBaseApp = Global.getApps().get(i);
				if (gameBaseApp instanceof PreGameApp) {
					setAdminApp((PreGameApp) gameBaseApp);
				}
			}
		}
		if (getAdminApp() == null) {
			System.err.println("no admin app found");
		} else {
			getAdminApp().registerServerApp(this);
		}
	}

	public void serverUpdate() {
		getUpdater().update();
	}

	public void disconnectEvent(Client client) {
		getServerHandler().disconnectEvent(client);
	}

	public void serverEvent(Server server, Client someClient) {
		getServerHandler().serverEvent(server, someClient);
	}

	@Override
	public void dispose() {
		getServerHandler().dispose();
		super.dispose();
	}

	public PreGameApp getAdminApp() {
		return adminApp;
	}

	public void setAdminApp(PreGameApp adminApp) {
		this.adminApp = adminApp;
	}

	@Override
	public boolean isServer() {
		return true;
	}

}
