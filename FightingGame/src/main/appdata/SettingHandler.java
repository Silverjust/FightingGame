package main.appdata;

import g4p_controls.GDropList;
import game.GameBaseApp;
import main.Setting;

public class SettingHandler {
	private Setting setting;

	GDropList settingList;
	Setting[] settings;

	private GameBaseApp app;

	public void setup() {
		loadSettings();
	}

	private void loadSettings() {
		Setting oldSettings = new Setting(app);
		try {
			oldSettings.fromJSON(appdataInfos.path + "settings.json");
			setSetting(oldSettings);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SettingHandler(GameBaseApp app) {
		this.app = app;
		setup();
		// settingList = new GDropList(app, 200, 200, 100, 400, 5);
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

}
