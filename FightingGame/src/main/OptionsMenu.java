package main;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameApplet;
import shared.Menu;

public class OptionsMenu extends Menu {
	int x, y, wh, h;
	GButton returnToGame;

	public OptionsMenu() {
		if (((MainApp) GameApplet.app).startPage != null)
			((MainApp) GameApplet.app).startPage.setActive(false);
		if (GameApplet.getPreGameInfo() != null)
			GameApplet.getPreGameInfo().setActive(false);
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		returnToGame = new GButton(GameApplet.app, x - wh, y + 100, wh * 2, 40,
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
		if (((MainApp) GameApplet.app).startPage != null)
			((MainApp) GameApplet.app).startPage.setActive(true);
		if (GameApplet.getPreGameInfo() != null)
			GameApplet.getPreGameInfo().setActive(true);
		returnToGame.dispose();
	}

}
