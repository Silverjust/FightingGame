package game;

import shared.Menu;
import main.FrameInfo;
import main.MainApp;
import shared.ref;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;

public class endGameMenu extends Menu {
	int x, y, wh, h;
	GButton returnToStart;

	public endGameMenu() {
		x = (int) FrameInfo.xCenter;
		y = (int) FrameInfo.yCenter + 100;
		wh = 50;
		returnToStart = new GButton(ref.app, x - wh, y, wh * 2, 40,
				"return to start");
		returnToStart.setLocalColorScheme(G4P.BLUE_SCHEME);
		returnToStart.addEventHandler(this, "handleButtonEvents");
	}

	public void handleButtonEvents(GButton button, GEvent event) {
		System.out.println(event);
		if (event == GEvent.CLICKED) {
			/*
			 * ref.app.clear(); ref.app.background(200); dispose();
			 * ref.updater.dispose(); ImageHandler.dispose(); HUD.dispose();
			 * Chat.dispose(); ((MainApp) ref.app).hauptmenue = new
			 * Hauptmenue(); ((MainApp) ref.app).mode = Mode.HAUPTMENUE;
			 */
			G4P.setGlobalColorScheme(G4P.BLUE_SCHEME);
			MainApp.main(new String[] { "main.MainApp" });
			ref.app.dispose();
		}
	}

	@Override
	public void dispose() {
		returnToStart.dispose();
	}
}
