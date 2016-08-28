package main.preGame;

import main.FrameInfo;
import main.Listener;
import main.MainApp;
import main.OptionsMenu;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameBaseApp;
import shared.Menu;

public class PreGameMenu extends Menu {
	int x, y, wh, h;
	GButton options, exit, returnToGame;

	public PreGameMenu() {
		if (((MainApp) GameBaseApp.app).startPage != null)
			((MainApp) GameBaseApp.app).startPage.setActive(false);
		if (GameBaseApp.getPreGameInfo() != null)
			GameBaseApp.getPreGameInfo().setActive(false);
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		exit = new GButton(GameBaseApp.app, x - wh, y, wh * 2, 40, "exit");
		exit.setLocalColorScheme(G4P.BLUE_SCHEME);
		exit.addEventHandler(this, "handleButtonEvents");

		options = new GButton(GameBaseApp.app, x - wh, y + 50, wh * 2, 40, "options");
		options.setLocalColorScheme(G4P.BLUE_SCHEME);
		options.addEventHandler(this, "handleButtonEvents");

		returnToGame = new GButton(GameBaseApp.app, x - wh, y + 100, wh * 2, 40,
				"return");
		returnToGame.setLocalColorScheme(G4P.BLUE_SCHEME);
		returnToGame.addEventHandler(this, "handleButtonEvents");

	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (event == GEvent.CLICKED) {
			if (button == exit) {
				new Listener().windowClosing(null);
			} else if (button == options) {
				dispose();
				((MainApp) GameBaseApp.app).menu = new OptionsMenu();
			} else if (button == returnToGame) {
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
		options.dispose();
		exit.dispose();
		returnToGame.dispose();
	}

}
