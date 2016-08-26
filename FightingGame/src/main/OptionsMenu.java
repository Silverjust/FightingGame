package main;

import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import shared.Menu;
import shared.ref;

public class OptionsMenu extends Menu {
	int x, y, wh, h;
	GButton returnToGame;

	public OptionsMenu() {
		if (((MainApp) ref.app).startPage != null)
			((MainApp) ref.app).startPage.setActive(false);
		if (ref.preGame != null)
			ref.preGame.setActive(false);
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		returnToGame = new GButton(ref.app, x - wh, y + 100, wh * 2, 40,
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
		if (((MainApp) ref.app).startPage != null)
			((MainApp) ref.app).startPage.setActive(true);
		if (ref.preGame != null)
			ref.preGame.setActive(true);
		returnToGame.dispose();
	}

}
