package game;

import gameStructure.GameObject;
import pathfinder.Graph;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.JSONObject;
import shared.Player;
import shared.VersionControle;
import shared.ref;

public class Map {
	public Graph graph;

	public int width, height;
	public final int fogScale = 7;

	public PImage textur;
	public PImage collision;
	public PGraphics fogOfWar;

	public JSONObject mapData;

	public MapCode mapCode;

	public Map(String map) {

		try {
			mapData = ref.app.loadJSONObject("data/" + map + ".json");
			VersionControle.objToArray(mapData, map);
			width = mapData.getInt("w");
			height = mapData.getInt("h");
			if (mapData.hasKey("MapCode")) {
				System.out.println("Map.Map() creating mapcode");
				mapCode = (MapCode) Class.forName(mapData.getString("MapCode")).getConstructor(Map.class)
						.newInstance(new Object[] { this });
			}
		} catch (Exception e) {
			System.err.println(map + " could not be loaded");
			e.printStackTrace();
		}
		if (mapCode == null) {
			mapCode = new MapCode(this) {
			};
		}
		mapCode.setup();
	}

	public void loadImages() {
		try {
			textur = ImageHandler.load("", mapData.getString("texture"));
			collision = ImageHandler.load("", mapData.getString("coll"));
		} catch (Exception e) {
			System.err.println(mapData.getString("texture") + " " + mapData.getString("coll"));
			e.printStackTrace();
		}
		fogOfWar = ref.app.createGraphics(width / fogScale, height / 2 / fogScale, PConstants.P2D);
	}

	public void setup() {
		// PathHandler.makeGraph(graph, collision, 20, 20);
	}

	public void updateFogofWar(Player player) {
		fogOfWar.beginDraw();
		fogOfWar.clear();
		fogOfWar.background(80);
		fogOfWar.noStroke();
		fogOfWar.fill(200);
		for (GameObject e : ref.updater.getGameObjects()) {
			if (e.player == player) {
				e.drawSight(ref.updater);
			}
		}
		fogOfWar.endDraw();
	}

	public void mapCodeUpdate() {
		if (mapCode != null)
			mapCode.update();
	}
}
