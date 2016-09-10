package main.appdata;

import processing.data.JSONObject;
import shared.GameBaseApp;
import shared.PreGameInfo;
import shared.VersionControle;
import shared.Updater.GameState;

public class ProfileHandler implements appdataInfos {

	static JSONObject profile;
	private static boolean newGame = false;

	static public void loadProfile() {
		newGame = true;
		JSONObject oldProfile;
		try {
			oldProfile = GameBaseApp.app
					.loadJSONObject(appdataInfos.path + "info.json");
		} catch (Exception e) {
			GameBaseApp.app.saveJSONObject(new JSONObject(), appdataInfos.path
					+ "info.json");
			oldProfile = GameBaseApp.app
					.loadJSONObject(appdataInfos.path + "info.json");
		}
		try {
			VersionControle.rateChange(oldProfile);
			if (!oldProfile.hasKey("name")) {
				oldProfile.setString("name", "unknown");
			}
			if (!oldProfile.hasKey("plays")) {
				oldProfile.setInt("plays", 0);
			}
			if (!oldProfile.hasKey("wins")) {
				oldProfile.setInt("wins", 0);
			}
			if (!oldProfile.hasKey("aliens-wins")) {
				oldProfile.setInt("aliens-wins", 0);
			}
			if (!oldProfile.hasKey("ahnen-wins")) {
				oldProfile.setInt("ahnen-wins", 0);
			}
			if (!oldProfile.hasKey("robots-wins")) {
				oldProfile.setInt("robots-wins", 0);
			}
			if (!oldProfile.hasKey("humans-wins")) {
				oldProfile.setInt("humans-wins", 0);
			}
			if (!oldProfile.hasKey("scientists-wins")) {
				oldProfile.setInt("scientists-wins", 0);
			}
			if (!oldProfile.hasKey("rate")) {
				oldProfile.setFloat("rate", 1000);
			}
			if (!oldProfile.hasKey("version")) {
				oldProfile.setString("version", VersionControle.version);
			}
			VersionControle.versionSystemChange(oldProfile);
			profile = oldProfile;
			GameBaseApp.app.saveJSONObject(profile, appdataInfos.path + "info.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void gameEndCalculations(float enemyRate) {
		if (newGame && (!PreGameInfo.isSinglePlayer())) {
			newGame = false;

			float rate = profile.getFloat("rate");
			rate = rate
					+ (GameApplet.GameBaseApp.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate);
			System.out.println("InfoDocHandler.gameEndCalculations()"
					+ (GameApplet.GameBaseApp.gameState == GameState.WON ? enemyRate
							/ rate : -rate / enemyRate) + " " + rate);
			profile.setFloat("rate", rate);

			int plays = profile.getInt("plays");
			plays++;
			profile.setInt("plays", plays);

			if (GameApplet.GameBaseApp.gameState == GameState.WON) {
				int wins = profile.getInt("wins");
				int nationWins = profile.getInt(GameBaseApp.getPlayer().getNation().toString()
						+ "-wins");
				wins++;
				nationWins++;

				profile.setInt("wins", wins);
				profile.setInt(GameBaseApp.getPlayer().getNation().toString() + "-wins",
						nationWins);
			}
		}

	}

	public static void saveName(String name) {
		profile.setString("name", name);
		GameBaseApp.app.saveJSONObject(profile, appdataInfos.path + "info.json");
	}

	static public String getName() {
		return profile.getString("name");
	}

	public static float getRate() {
		try {
			return profile.getFloat("rate");
		} catch (Exception e) {
			return 999;
		}
	}

	public static void dispose() {
		app.saveJSONObject(profile, appdataInfos.path + "info.json");
		// info = null;
	}

	public static boolean isDeveloper() {
		if (!profile.hasKey("isDeveloper"))
			return false;
		return profile.getBoolean("isDeveloper");

	}
}
