package game;

import java.util.ArrayList;

import entity.MainBuilding;
import entity.neutral.KeritMine;
import entity.neutral.SandboxBuilding;
import gameStructure.Champion;
import gameStructure.GameObject;
import processing.data.JSONArray;
import processing.data.JSONObject;
import shared.GameBaseApp;
import shared.Player;
import shared.Team;

public class MapHandler {

	private GameBaseApp app;

	public MapHandler(GameBaseApp app) {
		this.app = app;
	}

	public void setupEntities(JSONObject map) {
		for (String ip : app.getUpdater().players.keySet()) {
			Player player = app.getUpdater().getPlayer(ip);
			System.out.println("MapHandler.setupEntities()" + player.getUser().championName);
			if (player.getUser().championName != null && !player.getUser().championName.equals("")
					&& !player.getUser().championName.equals("null")) {
				Class<? extends Champion> c = app.getContentListManager().getChampClass(player.getUser().championName);
				player.app.getUpdater().sendSpawn(c, player,
						(player.getUser().team == Team.LEFTSIDE ? app.random(100, 200) : app.random(600, 700)) + " "
								+ app.random(200, 600));
				System.out.println("MapHandler.setupEntities() champs");
			}
		}
		try {
			JSONArray entitys = map.getJSONArray("entities");
			System.out.println("MapHandler.setupEntities() " + entitys.size() + " Entities to spawn");
			for (int i = 0; i < entitys.size(); i++) {
				JSONObject entity = entitys.getJSONObject(i);
				int playerNumber = entity.getInt("player");
				if (app.getUpdater().players.keySet().size() > playerNumber) {
					String player;
					if (playerNumber >= 0) {
						player = new ArrayList<String>(app.getUpdater().players.keySet()).get(playerNumber);
					} else {
						player = "0";
					}
					String type = entity.getString("type");

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
					Class<? extends Champion> c = app.getContentListManager().getChampClass(type);
					if (c != null)
						app.getUpdater().sendSpawn(c, app.getUpdater().neutral, x + " " + y);
				}
			}
		} catch (Exception e) {
			System.err.println("there is something wrong with this map");
			e.printStackTrace();
		}

	}

	public void saveMap(String intName, String name) {
		JSONObject oldMap = app.loadJSONObject("data/" + app.getPreGameInfo().map + ".json");
		JSONObject map = new JSONObject();
		map.setString("name", name);
		map.setString("descr", " ");
		map.setString("texture", oldMap.getString("texture"));
		map.setString("coll", oldMap.getString("coll"));
		map.setInt("w", app.getUpdater().map.width);
		map.setInt("h", app.getUpdater().map.height);

		JSONArray entities = new JSONArray();
		for (GameObject e : app.getUpdater().getGameObjects()) {
			if (e.getClass() != SandboxBuilding.class) {
				JSONObject atributes = new JSONObject();
				String type = e.getClass().getSimpleName().toString();
				if (e instanceof MainBuilding)
					type = "MainBuilding";
				else if (e instanceof KeritMine)
					type = "KeritMine";

				atributes.setString("type", type);
				int playerNumber = new ArrayList<String>(app.getUpdater().players.keySet())
						.indexOf(e.player.getUser().getIp());
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
