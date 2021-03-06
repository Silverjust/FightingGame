package main.preGame;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.ClientHandler;
import main.MainApp;
import shared.GameBaseApp;
import shared.Mode;
import shared.Nation;

public class ChampSelect {
	public GGameButton[] nationButtons;
	public Nation nation;

	public ChampSelect(PreGameDisplay display) {
		this.nationButtons = new GGameButton[5];
		for (int i = 0; i < nationButtons.length; i++) {
			nationButtons[i] = new GGameButton(GameBaseApp.app, 200 + 210 * i, 100, 200, 500, getNationImages(i));// change
			// buttons
			nationButtons[i].addEventHandler(display, "handleSelectNation");
		}
	}

	public void handleSelectNation(GGameButton button, GEvent event) {
		if (event == GEvent.PRESSED && ((MainApp) GameBaseApp.app).mode == Mode.PREGAME) {
			// System.out.println(((MainApp) ref.app).mode);
			for (int i = 0; i < nationButtons.length; i++) {
				// System.out.println(nationButtons[i] == button);
				if (nationButtons[i] == button) {
					nationButtons[i].setSwitch(true);
					nation = Nation.fromNumber(i);
					PreGameClientHandler
							.sendDirect("<setNation " + GameBaseApp.getPreGameInfo().getUser("").getIp() + " " + Nation.fromNumber(i).toString());
				} else {
					nationButtons[i].setSwitch(false);
				}
			}
		}
	}

	private String[] getNationImages(int i) {
		String[] s = new String[3];
		Nation nation = Nation.fromNumber(i);
		for (int j = 0; j < s.length; j++) {
			s[j] = "preGame/" + nation.toString() + "_" + (j + 1) + ".jpg";
			// System.out.println(s[j]);
		}
		return s;
	}

	public void dispose() {
		if (nationButtons[0] != null) {
			for (int i = 0; i < nationButtons.length; i++) {
				nationButtons[i].dispose();
			}
		}
	}

	public void setEnabled(boolean b) {
		if (nationButtons[0] != null) {
			for (int i = 0; i < nationButtons.length; i++) {
				nationButtons[i].setVisible(b);
			}
		}
	}
}