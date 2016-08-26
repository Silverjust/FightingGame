package main;

import main.preGame.MainPreGame;
import main.preGame.MainPreGame.GameSettings;
import main.appdata.ProfileHandler;
import main.appdata.StatScreen;
import shared.Helper;
import shared.Mode;
import shared.ref;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GPassword;
import g4p_controls.GTextArea;
import g4p_controls.GTextField;

public class StartPage {

	GTextField playerName, serverIp;
	GPassword password;
	GButton multiplayerButton, singleplayerButton, sandboxButton,
			campainButton, statisticsButton, localhost, startServer;
	GTextArea changes;

	public StartPage() {
		ProfileHandler.loadProfile();
		playerName = new GTextField(ref.app, 300, 300, 300, 20);
		playerName.setText(ProfileHandler.getName());
		playerName.setPromptText("your name");
		playerName.addEventHandler(this, "handleNameEvents");

		password = new GPassword(ref.app, 300, 330, 300, 20);
		// password.

		serverIp = new GTextField(ref.app, 300, 360, 215, 20);
		serverIp.setPromptText("server ip4 adress");

		localhost = new GButton(ref.app, 525, 360, 75, 20);
		localhost.setText("127.0.0.1");
		localhost.addEventHandler(this, "handleLocalhostEvents");

		multiplayerButton = new GButton(ref.app, 300, 390, 300, 40);
		multiplayerButton.setText("multiplayer");
		multiplayerButton.addEventHandler(this, "handleAcceptEvents");

		singleplayerButton = new GButton(ref.app, 300, 440, 300, 40);
		singleplayerButton.setText("singleplayer");
		singleplayerButton.addEventHandler(this, "handleAcceptEvents");

		sandboxButton = new GButton(ref.app, 300, 490, 300, 40);
		sandboxButton.setText("sandbox");
		sandboxButton.addEventHandler(this, "handleAcceptEvents");

		campainButton = new GButton(ref.app, 300, 540, 300, 40);
		campainButton.setText("campain");
		campainButton.addEventHandler(this, "handleAcceptEvents");

		statisticsButton = new GButton(ref.app, 300, 590, 300, 40);
		statisticsButton.setText("statistics");
		statisticsButton.addEventHandler(this, "handleAcceptEvents");

		changes = new GTextArea(ref.app, 800, 100, 600, 700,
				G4P.SCROLLBARS_VERTICAL_ONLY | G4P.SCROLLBARS_AUTOHIDE);
		changes.setTextEditEnabled(false);
		changes.setText(ref.app.loadStrings("data/changelog.txt"));

		startServer = new GButton(ref.app, 650, 300, 100, 60);
		startServer.setText("make to Server");
		startServer.addEventHandler(this, "handleStartServerEvents");

		MainPreGame.GameSettings.setupGameSettings();
		ref.app.clear();
		ref.app.background(200);
	}

	public void update() {
		ref.app.background(200);
	}

	public void handleAcceptEvents(GButton button, GEvent event) {
		String name = Helper.secureInput(playerName.getText());
		String ip = Helper.secureInput(serverIp.getText());

		if (event == GEvent.CLICKED) {
			if (name.equals("")) {
				playerName.setFocus(true);
				return;
			}
			if (button == multiplayerButton) {
				if (ip.equals("")) {
					serverIp.setFocus(true);
					return;
				}
			} else if (button == singleplayerButton) {
				GameSettings.singlePlayer = true;
				GameSettings.againstAI = true;// ai
			} else if (button == sandboxButton) {
				GameSettings.singlePlayer = true;
				GameSettings.sandbox = true;
			} else if (button == campainButton) {
				GameSettings.singlePlayer = true;
				GameSettings.campain = true;
			} else if (button == statisticsButton) {
				StatScreen.setup();
				return;
			}
			System.out.println(GameSettings.singlePlayer);

			ref.preGame = new MainPreGame(name);
			ClientHandler.setup(ip);
			if (!GameSettings.singlePlayer && ClientHandler.client == null) {
				serverIp.setFocus(true);
				((MainPreGame) ref.preGame).closeBecauseServer();
				return;
			}
			((MainPreGame) ref.preGame).setup();

			if (GameSettings.singlePlayer) {
				((MainApp) ref.app).mode = Mode.PREGAME;
			}
			dispose();

		}
	}

	void dispose() {
		try {
			ProfileHandler.saveName(Helper.secureInput(playerName.getText()));
			playerName.dispose();
			password.dispose();
			serverIp.dispose();
			localhost.dispose();
			multiplayerButton.dispose();
			singleplayerButton.dispose();
			sandboxButton.dispose();
			campainButton.dispose();
			statisticsButton.dispose();
			changes.dispose();
			startServer.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleStartServerEvents(GButton button, GEvent event) {
		server.ServerApp.main(new String[] { "server.ServerApp" });
		ref.app.dispose();
	}

	public void handleLocalhostEvents(GButton button, GEvent event) {
		serverIp.setText("127.0.0.1");
	}

	public void handleNameEvents(GTextField textfield, GEvent event) {
		if (event == GEvent.CHANGED) {

		}
	}

	public void setActive(boolean b) {
		try {
			playerName.setEnabled(b);
			password.setEnabled(b);
			serverIp.setEnabled(b);
			localhost.setEnabled(b);
			multiplayerButton.setEnabled(b);
			singleplayerButton.setEnabled(b);
			sandboxButton.setEnabled(b);
			campainButton.setEnabled(b);
			statisticsButton.setEnabled(b);
			changes.setEnabled(b);
			startServer.setEnabled(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
