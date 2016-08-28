package server;

import java.util.HashMap;

import game.ClientHandler;
import game.GameBaseApp;
import gameStructure.GameObject;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import shared.Coms;
import shared.Mode;
import shared.Player;
import shared.Updater;
import shared.VersionControle;
import shared.Updater.GameState;

public class Protocol {
	static int protocollNumber;
	static String protocollText = "";

	static Class<?> clazz;

	public static void collectInfos() {
		try {
			if (((ServerApp) GameBaseApp.app).mode == Mode.GAME) {
				for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
					if (e.getClass().equals(clazz)) {
						addInfo("0 " + e.number + " :" + e.getAnimation().toString());
						addInfo("0 " + e.number + " :" + e.player.kerit);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void filterComs(String info, String s) {
		try {
			String[] c = PApplet.splitTokens(s, " " + ClientHandler.endSymbol);
			if (c[0].equals(Coms.EXECUTE)
					&& GameBaseApp.updater.getNamedObjects().get(Integer.parseInt(c[1])).getClass().equals(clazz))
				addInfo(info + s);

			if (c[0].equals("<give") && Integer.parseInt(c[3]) < 0)
				addInfo(info + s);

			if (!c[0].equals("<execute") && !c[0].equals("<give"))
				addInfo(info + s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addInfo(String s) {
		protocollText += getTime() + s + "\n";
		// System.out.println(getTime() + s);
	}

	static public void createFile() {
		String name = "statistics_" + VersionControle.version.replace(".", "_");
		String file = System.getProperty("user.home").replace("\\", "/") + "/Desktop/EE_stats/" + name + ".json";
		JSONArray array;
		try {
			array = GameBaseApp.app.loadJSONArray(file);
		} catch (Exception e) {
			System.out.println("creating new statistics");
			array = new JSONArray();
		}
		JSONObject game = new JSONObject();
		if (Updater.resfreeze != null)
			game.setFloat("time", Updater.resfreeze.cooldown / 1000.0f / 60.0f);
		int i = 0;
		for (String key : GameApplet.GameBaseApp.players.keySet()) {
			i++;
			Player p = GameApplet.GameBaseApp.players.get(key);
			JSONObject player = new JSONObject();
			player.setString("name", p.getUser().name);
			player.setString("nation", p.getUser().nation.toString());
			System.out.println("Protocol.createFile()" + p.gameState);
			player.setBoolean("win", p.gameState == GameState.WON);

			String[] lines = PApplet.splitTokens(protocollText, "\n");
			HashMap<String, Integer> countedUnits = new HashMap<>();
			for (String line : lines) {
				String[] c = PApplet.splitTokens(line, " ");
				int n = 4;
				// PApplet.printArray(line);
				if (c[n].equals("<spawn") && GameApplet.GameBaseApp.players.get(c[2 + n]) == p) {
					if (countedUnits.get(c[1 + n]) == null)
						countedUnits.put(c[1 + n], 1);
					else
						countedUnits.put(c[1 + n], countedUnits.get(c[1 + n]) + 1);
				}
			}
			String units = "";
			// System.out.println("Protocol.createFile()" +
			// countedUnits.size());
			for (String unitName : countedUnits.keySet()) {
				int n = countedUnits.get(unitName);
				units = units + n + " " + unitName + "\n";
			}
			System.out.println("Protocol.createFile()" + units);
			player.setString("units", units);
			game.setJSONObject(i + "", player);
		}
		array.append(game);
		GameBaseApp.app.saveJSONArray(array, file);
		System.out.println("saved as " + file);
	}

	static String getTime() {
		// System.out.println("Protocol.getTime()");
		return " m" + min() + " s" + (min() == 0 ? sec() : (sec() % (min() * 60))) + " ms"
				+ (sec() == 0 ? Updater.Time.getMillis() : (Updater.Time.getMillis() % (sec() * 1000)) + " :");
	}

	static int sec() {
		return PApplet.floor(Updater.Time.getMillis() / 1000);
	}

	static int min() {
		return PApplet.floor(Updater.Time.getMillis() / 60000);
	}

	public static void dispose() {

	}
}
