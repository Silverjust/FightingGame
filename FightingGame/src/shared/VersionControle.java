package shared;

import java.util.Set;

import main.appdata.ProfileHandler;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public abstract class VersionControle {
	public final static String version = "1.0.0.0";

	public static boolean isNewerVersion(String s1, String s2) {
		String[] sa1 = s1.split("\\.");
		String[] sa2 = s2.split("\\.");
		System.out.println(s1 + " " + s2);
		for (int i = 0; i < (sa1.length > sa2.length ? sa1.length : sa2.length); i++) {
			int i1 = (sa1.length > i ? Integer.parseInt(sa1[i]) : 0);
			int i2 = (sa2.length > i ? Integer.parseInt(sa2[i]) : 0);
			System.out.println(i1 + " " + i2);
			if (i1 > i2)
				return true;
		}
		return false;
	}

	public static void versionControle() {
		String chanegelogVersion = PApplet.splitTokens(ref.app.loadStrings("data/changelog.txt")[0], "-")[0];
		if (!chanegelogVersion.equals(version))
			System.err.println("changelog has different version than VC " + chanegelogVersion + " " + version
					+ " (VersionControle.java:11)");
	}

	// **********************************************************************
	public static void objToArray(JSONObject mapData, String mapName) {
		try {
			mapData.getJSONArray("entities");
		} catch (Exception e) {
			System.out.println("convert to json array");
			@SuppressWarnings("unchecked")
			Set<String> entitySet = mapData.getJSONObject("entities").keys();
			JSONArray entitys = new JSONArray();
			for (String string : entitySet) {
				JSONObject entity = mapData.getJSONObject("entities").getJSONObject(string);
				entitys.append(entity);
			}
			mapData.remove("entities");
			mapData.setJSONArray("entities", entitys);
			if (ProfileHandler.isDeveloper())
				ref.app.saveJSONObject(mapData,
						System.getProperty("user.home").replace("\\", "/") + "/Desktop/" + mapName + ".json");
		}
	}

	public static void rateChange(JSONObject oldProfile) {
		if (oldProfile.hasKey("rate") && !oldProfile.hasKey("version")) {
			float f = oldProfile.getFloat("rate");
			oldProfile.setFloat("rate", f + 1000 - 10);
		}
	}

	public static void versionSystemChange(JSONObject oldProfile) {
		if (oldProfile.hasKey("version")) {
			try {
				oldProfile.getString("version");
			} catch (Exception e) {
				oldProfile.remove("version");
				oldProfile.setString("version", version);
			}

		}
	}

	public static void setToBaseShortcuts(JSONObject o) {
		if (o.hasKey("unitsShortcuts")) {
			o.setJSONArray("baseShortcuts", o.getJSONArray("unitsShortcuts"));
			o.remove("unitsShortcuts");
		}
		if (o.hasKey("baseShortcuts")) {
			o.remove("baseShortcuts");//TODO remove
		}

	}
}