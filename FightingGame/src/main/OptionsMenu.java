package main;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameBaseApp;
import shared.Menu;

public class OptionsMenu extends Menu {
	int x, y, wh, h;
	GButton returnToGame;

	public OptionsMenu() {
		if (((MainApp) GameBaseApp.app).startPage != null)
			((MainApp) GameBaseApp.app).startPage.setActive(false);
		if (GameBaseApp.getPreGameInfo() != null)
			GameBaseApp.getPreGameInfo().setActive(false);
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		returnToGame = new GButton(GameBaseApp.app, x - wh, y + 100, wh * 2, 40,
				"return");
		returnToGame.setLocalColorScheme(G4P.BLUE_SCHEME);
		returnToGame.addEventHandler(this, "handleButtonEvents");

	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (event == GEvent.CLICKED) {
			if (button == returnToGame) {
				dispose();
			}
		}
	}

	@Override
	public void dispose() {
		if (((MainApp) GameBaseApp.app).startPage != null)
			((MainApp) GameBaseApp.app).startPage.setActive(true);
		if (GameBaseApp.getPreGameInfo() != null)
			GameBaseApp.getPreGameInfo().setActive(true);
		returnToGame.dispose();
	}

}
