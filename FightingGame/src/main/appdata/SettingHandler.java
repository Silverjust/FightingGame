package main.appdata;

import g4p_controls.GDropList;
import game.GameApplet;
import main.Setting;

public class SettingHandler {
	public static Setting setting;

	GDropList settingList;
	Setting[] settings;

	private GameApplet app;

	public void setup() {
		loadSettings();
	}

	private void loadSettings() {
		Setting oldSettings = new Setting(app);
		try {
			oldSettings.fromJSON(appdataInfos.path + "settings.json");
			setting = oldSettings;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SettingHandler(GameApplet app) {
		this.app = app;
		//settingList = new GDropList(app, 200, 200, 100, 400, 5);
	}

}
