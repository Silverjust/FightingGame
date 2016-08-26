package main.appdata;

import g4p_controls.GDropList;
import shared.ref;
import main.Setting;

public class SettingHandler {
	public static Setting setting;

	GDropList settingList;
	Setting[] settings;

	public static void setup() {
		loadSettings();
	}

	private static void loadSettings() {
		Setting oldSettings = new Setting();
		try {
			oldSettings.fromJSON(appdataInfos.path + "settings.json");
			setting = oldSettings;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public SettingHandler() {
		settingList = new GDropList(ref.app, 200, 200, 100, 400, 5);
	}

}
