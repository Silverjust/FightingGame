package game;

import java.util.ArrayList;

import entity.neutral.KeritMine;
import entity.neutral.SandboxBuilding;
import gameStructure.GameObject;
import gameStructure.MainBuilding;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class MapHandler {

	private GameApplet app;

	public MapHandler(GameApplet app) {
		this.app = app;
	}

	public void setupEntities(JSONObject map) {
		try {
			JSONArray entitys = map.getJSONArray("entities");
			System.out.println("MapHandler.setupEntities() " + entitys.size() + " Entities to spawn");
			for (int i = 0; i < entitys.size(); i++) {
				JSONObject entity = entitys.getJSONObject(i);
				int playerNumber = entity.getInt("player");
				if (app.updater.players.keySet().size() > playerNumber) {
					String player;
					if (playerNumber >= 0) {
						player = new ArrayList<String>(app.updater.players.keySet()).get(playerNumber);
					} else {
						player = "0";
					}
					String type = entity.getString("type");
					if (type.equals("MainBuilding")) {
						type = app.updater.players.get(player).getNation().getNationInfo().getMainBuilding()
								.getSimpleName();
					} else if (type.equals("KeritMine")) {
						type = app.updater.players.get(player).getNation().getNationInfo().getKeritMine()
								.getSimpleName();
					}
					/*
					 * if (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); } if
					 * (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); } if
					 * (type.equals("MainBuilding")) { type =
					 * ref.updater.player.get(player).nation
					 * .getMainBuilding().getSimpleName(); }
					 */
					float x = entity.getFloat("x");
					float y = entity.getFloat("y");
					app.updater.send("<spawn " + type + " " + player + " " + x + " " + y);
				}

			}
		} catch (Exception e) {
			System.err.println("there is something wrong with this map");
			e.printStackTrace();
		}
	}

	public void saveMap(String intName, String name) {
		JSONObject oldMap = app.loadJSONObject("data/" + app.preGame.map + ".json");
		JSONObject map = new JSONObject();
		map.setString("name", name);
		map.setString("descr", " ");
		map.setString("texture", oldMap.getString("texture"));
		map.setString("coll", oldMap.getString("coll"));
		map.setInt("w", app.updater.map.width);
		map.setInt("h", app.updater.map.height);

		JSONArray entities = new JSONArray();
		for (GameObject e : app.updater.getGameObjects()) {
			if (e.getClass() != SandboxBuilding.class) {
				JSONObject atributes = new JSONObject();
				String type = e.getClass().getSimpleName().toString();
				if (e instanceof MainBuilding)
					type = "MainBuilding";
				else if (e instanceof KeritMine)
					type = "KeritMine";

				atributes.setString("type", type);
				int playerNumber = new ArrayList<String>(app.updater.players.keySet()).indexOf(e.player.getUser().ip);
				atributes.setInt("player", playerNumber);
				atributes.setFloat("x", e.getX());
				atributes.setFloat("y", e.getY());
				entities.append(atributes);
			}
		}
		map.setJSONArray("entities", entities);

		System.out.println("\"" + intName + "\" : \"maps/" + intName + "/" + intName + "\"");

		app.saveJSONObject(map, System.getProperty("user.home").replace("\\", "/") + "/Desktop/" + intName + ".json");
	}
}
