package main.preGame;

import main.MainApp;
import g4p_controls.GButton;
import g4p_controls.GDropList;
import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.ClientHandler;
import game.GameBaseApp;
import processing.data.JSONObject;
import shared.ContentListManager;
import shared.Mode;

public abstract class PreGameDisplay {
	public MainPreGame preGame;

	public GButton startButton;
	public int startMap = 0;

	MapSelect mapSelect;
	PlayerPanel playerPanel;
	ChampSelect champSelect;
	SandboxSelection sandboxSelection;
	PreGameChat chat;

	public PreGameDisplay() {
		preGame = (MainPreGame) GameBaseApp.getPreGameInfo();

		mapSelect = new MapSelect(this, startMap);
		chat = new PreGameChat();

		startButton = new GButton(GameBaseApp.app, GameApplet.GameBaseApp.width - 320, GameApplet.GameBaseApp.height - 200, 300, 175);
		startButton.setText("START");
		startButton.addEventHandler(this, "handleStartEvents");
	}

	public void update() {
		GameBaseApp.app.background(50);
	}

	public abstract void handleSelectNation(GGameButton button, GEvent event);

	public void handleSelectMap(GDropList list, GEvent event) {
		if (event == GEvent.SELECTED && ((MainApp) GameBaseApp.app).mode == Mode.PREGAME) {
			String file = "";
			try {
				file = "data/" + ContentListManager.getModeMaps().getString(mapSelect.intNames[list.getSelectedIndex()])
						+ ".json";
				@SuppressWarnings("unused")
				JSONObject mapData = GameBaseApp.app.loadJSONObject(file);
				ClientHandler.sendDirect(
						"<setMap " + GameBaseApp.getPreGameInfo().getUser("").ip + " " + mapSelect.intNames[list.getSelectedIndex()]);
				mapSelect.previousMap = list.getSelectedIndex();
			} catch (Exception e) {
				System.err.println(file + " does not exist or could not be read");
				mapSelect.mapSelector.setSelected(mapSelect.previousMap);
			}
		}
	}

	public void handleStartEvents(GButton button, GEvent event) {
		// System.out.println(event);
		if (event == GEvent.CLICKED && ((MainApp) GameBaseApp.app).mode == Mode.PREGAME) {
			preGame.tryStart();
		}
	}

	public void dispose() {
		mapSelect.dispose(this);
		startButton.dispose();
		chat.dispose();
	}

	public void setActive(boolean b) {
		mapSelect.setActive(this, b);
		startButton.setEnabled(b);
	}
}
