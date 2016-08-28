package game;

import main.appdata.SettingHandler;

public class GroupHandler {
	public static Group[] groups;
	public static Group recentGroup;

	static void setup() {
		groups = new Group[10];
		for (int i = 0; i < groups.length; i++) {
			groups[i] = new Group(300 + (Group.w + 30) * i, GameApplet.GameBaseApp.height
					- HUD.height + 5, SettingHandler.getSetting().hotKeys[i]);
		}
		recentGroup = groups[0];
	}

	static void update() {
		GameBaseApp.app.stroke(0);
		GameBaseApp.app.fill(255);
		for (int i = 0; i < groups.length; i++) {
			groups[i].update();
		}
	}

	public static void fireGroup(int i) {
		groups[i].button.pressManually();
	}

	public static void dispose() {
		if (groups != null) {
			for (int i = 0; i < groups.length; i++) {
				groups[i].button.dispose();
				groups[i].groupEntities.clear();
			}
			groups = null;
		}
	}
}
