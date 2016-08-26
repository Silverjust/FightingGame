package game;

import shared.Coms;
import shared.Menu;
import main.FrameInfo;
import main.OptionsMenu;
import main.appdata.ProfileHandler;
import shared.ref;
import g4p_controls.G4P;
import g4p_controls.GButton;
import g4p_controls.GEvent;

public class IngameMenu extends Menu {
	int x, y, wh, h;
	GButton options, surrender, returnToGame;

	public IngameMenu() {
		x = (int) FrameInfo.xCenter;
		y = 200;
		wh = 50;
		surrender = new GButton(ref.app, x - wh, y, wh * 2, 40, "surrender");
		surrender.setLocalColorScheme(G4P.BLUE_SCHEME);
		surrender.addEventHandler(this, "handleButtonEvents");

		options = new GButton(ref.app, x - wh, y + 50, wh * 2, 40, "options");
		options.setLocalColorScheme(G4P.BLUE_SCHEME);
		options.addEventHandler(this, "handleButtonEvents");

		returnToGame = new GButton(ref.app, x - wh, y + 100, wh * 2, 40,
				"return");
		returnToGame.setLocalColorScheme(G4P.BLUE_SCHEME);
		returnToGame.addEventHandler(this, "handleButtonEvents");

	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (event == GEvent.CLICKED) {
			if (button == surrender) {
				ref.updater.send("<gameend lost " + ref.player.getUser().ip + " "
						+ ProfileHandler.getRate());
				dispose();
			} else if (button == options) {
				dispose();
				HUD.menue = new OptionsMenu();
			} else if (button == returnToGame) {
				ref.updater.send(Coms.PAUSE+" false");
			}
		}
	}

	@Override
	public void dispose() {
		options.dispose();
		surrender.dispose();
		returnToGame.dispose();
	}
}
