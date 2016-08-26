package game;

import java.util.ArrayList;

import g4p_controls.GEvent;
import g4p_controls.GGameButton;
import gameStructure.Building;
import gameStructure.GameObject;

public class Group {
	ArrayList<GameObject> groupEntities = new ArrayList<GameObject>();
	GGameButton button;
	int x, y;
	static int w = 50, h = 20;
	public boolean unitActives;

	public Group(int x, int y, char n) {
		this.x = x;
		this.y = y;
		button = new GGameButton(GameApplet.app, x, y, w, h, HUD.buttonImageFilename());
		button.setText(n + "");
		button.addEventHandler(this, "handleClickEvent");
	}

	void update() {
		if (!groupEntities.isEmpty())
			button.setSymbol(groupEntities.get(0).iconImg);
		else
			button.setSymbol(null);
	}

	void add(GameObject e) {
		groupEntities.add(e);
	}

	void remove(GameObject e) {
		groupEntities.remove(e);
	}

	public void handleClickEvent(GGameButton gamebutton, GEvent event) {
		if (event == GEvent.PRESSED) {
			if (((GameUpdater) GameApplet.updater).input.shiftMode) {

				for (GameObject entity : GameApplet.updater.selected) {
					if (!groupEntities.contains(entity))
						groupEntities.add(entity);
				}
			} else if (((GameUpdater) GameApplet.updater).input.strgMode) {

				boolean containsUnits = false;
				groupEntities.clear();
				for (GameObject entity : GameApplet.updater.selected) {
					groupEntities.add(entity);
					if (!(entity instanceof Building))
						containsUnits = true;
				}
				unitActives = containsUnits;

			}

			GroupHandler.recentGroup = this;
			HUD.activesGrid.selectionChange();
		}
	}
}
