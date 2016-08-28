package game;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import main.MainApp;
import shared.Menu;

public class endGameMenu extends Menu {
	int x, y, wh, h;
	GButton returnToStart;

	public endGameMenu(GameBaseApp app) {
		super(app);
		x = (int) app.getxCenter();
		y = (int) app.getyCenter() + 100;
		wh = 50;
		returnToStart = new GButton(app, x - wh, y, wh * 2, 40, "return to start");
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
			app.dispose();
		}
	}

	@Override
	public void dispose() {
		returnToStart.dispose();
	}
}
