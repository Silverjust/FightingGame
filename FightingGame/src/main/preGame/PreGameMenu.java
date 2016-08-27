package main.preGame;

import main.FrameInfo;
import main.Listener;
import main.MainApp;
import main.OptionsMenu;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import game.GameApplet;
import shared.Menu;

public class PreGameMenu extends Menu {
	int x, y, wh, h;
	GButton options, exit, returnToGame;

	public PreGameMenu() {
		if (((MainApp) GameApplet.app).startPage != null)
			((MainApp) GameApplet.app).startPage.setActive(false);
		if (GameApplet.getPreGameInfo() != null)
			GameApplet.getPreGameInfo().setActive(false);
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		exit = new GButton(GameApplet.app, x - wh, y, wh * 2, 40, "exit");
		exit.setLocalColorScheme(G4P.BLUE_SCHEME);
		exit.addEventHandler(this, "handleButtonEvents");

		options = new GButton(GameApplet.app, x - wh, y + 50, wh * 2, 40, "options");
		options.setLocalColorScheme(G4P.BLUE_SCHEME);
		options.addEventHandler(this, "handleButtonEvents");

		returnToGame = new GButton(GameApplet.app, x - wh, y + 100, wh * 2, 40,
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
				((MainApp) GameApplet.app).menu = new OptionsMenu();
			} else if (button == returnToGame) {
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
		options.dispose();
		exit.dispose();
		returnToGame.dispose();
	}

}
