package main.preGame;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import game.GameBaseApp;

public class PreGameCampainDisplay extends PreGameDisplay {
	public PreGameCampainDisplay() {
		super();
		champSelect = new ChampSelect(this);
	}

	@Override
	public void handleSelectNation(GGameButton button, GEvent event) {
		champSelect.handleSelectNation(button, event);
		GameBaseApp.getPreGameInfo().getUser("").nation = champSelect.nation;
		mapSelect.setupMapSelection();
	}

	public void dispose() {
		super.dispose();
		champSelect.dispose();
	}

	public void setActive(boolean b) {
		super.setActive(b);
		champSelect.setEnabled(b);
	}
}
