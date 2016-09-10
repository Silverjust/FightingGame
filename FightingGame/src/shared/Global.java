package shared;

import java.util.ArrayList;

public class Global {
	private static ArrayList<GameBaseApp> apps = new ArrayList<GameBaseApp>();

	public static ArrayList<GameBaseApp> getApps() {
		return apps;
	}

	public static void addApp(GameBaseApp app) {
		app.setAppNumber(apps.size());
		Global.apps.add(app);
	}
}
