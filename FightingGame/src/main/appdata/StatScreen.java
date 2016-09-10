package main.appdata;

import processing.core.PConstants;
import processing.data.JSONObject;
import shared.GameBaseApp;
import g4p_controls.G4P;
import g4p_controls.GTextArea;
import g4p_controls.GWindow;

public class StatScreen {

	static GTextArea stats;
	private static GWindow window;

	static public void setup() {
		window = new GWindow(GameBaseApp.app, "Stats", 0, 0, 200, 300, false,
				PConstants.JAVA2D);
		window.setActionOnClose(G4P.CLOSE_WINDOW);

		JSONObject info = ProfileHandler.profile;
		stats = new GTextArea(window.papplet, 0, 0, 200, 300);
		stats.setTextEditEnabled(false);

		stats.appendText("name: " + info.getString("name"));
		stats.appendText(" ");
		stats.appendText("plays: " + info.getInt("plays"));
		stats.appendText("wins: " + info.getInt("wins"));
		stats.appendText("ranking: " + info.getFloat("rate"));
		stats.appendText(" ");
		stats.appendText("aliens-wins: 		" + info.getInt("aliens-wins"));
		stats.appendText("ahnen-wins: 		" + info.getInt("ahnen-wins"));
		stats.appendText("robots-wins: 		" + info.getInt("robots-wins"));
		stats.appendText("humans-wins: 		" + info.getInt("humans-wins"));
		stats.appendText("scientists-wins: 	" + info.getInt("scientists-wins"));
	}

	static public void dispose() {
		stats.dispose();
	}

}
