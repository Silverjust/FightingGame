package shared;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import champs.ArmorShred;
import champs.TestProjectile;
import champs.Ticul;
import game.GameApplet;
import game.PreGameInfo;
import gameStructure.GameObject;
import gameStructure.baseBuffs.Buff;
import gameStructure.baseBuffs.Root;
import gameStructure.baseBuffs.Slow;
import processing.data.JSONObject;

public class ContentListManager {
	public static String path = "data/content.json";
	static JSONObject contentList;
	static JSONObject entityList;
	static JSONObject campainEntityList;

	/*
	 * public static void load() { contentList = ref.app.loadJSONObject(path);
	 * entityList = contentList.getJSONObject("entities"); campainEntityList =
	 * contentList.getJSONObject("campainEntities"); for (Object o :
	 * campainEntityList.keys()) { entityList.setString((String) o,
	 * campainEntityList.getString((String) o)); } }
	 */

	/*
	 * public static JSONObject getEntityContent() { return entityList; }
	 */

	private HashMap<String, Class<? extends GameObject>> gameObjectMap = new HashMap<String, Class<? extends GameObject>>();
	private HashMap<String, Class<? extends Buff>> buffMap = new HashMap<String, Class<? extends Buff>>();

	public ContentListManager() {
	}

	public void load() {
		ArrayList<Class<? extends GameObject>> objectList = new ArrayList<Class<? extends GameObject>>();
		// ********************************
		objectList.add(Ticul.class);
		objectList.add(TestProjectile.class);
		// ************************************
		for (Class<? extends GameObject> c : objectList) {
			GameObject o = createEntity(c);
			if (o != null)
				gameObjectMap.put(o.getInternName(), o.getClass());
		}

		ArrayList<Class<? extends Buff>> buffList = new ArrayList<Class<? extends Buff>>();
		// ********************************
		buffList.add(Root.class);
		buffList.add(Slow.class);

		buffList.add(ArmorShred.class);
		// ************************************
		for (Class<? extends Buff> c : buffList) {
			Buff b = createBuff(c);
			if (b != null)
				buffMap.put(b.getInternName(), b.getClass());
		}

		contentList = GameApplet.app.loadJSONObject(path);
		campainEntityList = contentList.getJSONObject("campainEntities");
		for (Object o : campainEntityList.keys()) {
			entityList.setString((String) o, campainEntityList.getString((String) o));
		}
	}

	private Buff createBuff(Class<? extends Buff> c) {
		Constructor<?> ctor;
		try {
			ctor = c.getConstructor(String[].class);
			Buff b = (Buff) ctor.newInstance(new Object[] { null });
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private GameObject createEntity(Class<? extends GameObject> c) {
		Constructor<?> ctor;
		try {
			ctor = c.getConstructor(String[].class);
			GameObject o = (GameObject) ctor.newInstance(new Object[] { null });
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/*
	 * public static void load() { contentList = ref.app.loadJSONObject(path);
	 * entityList = contentList.getJSONObject("entities"); campainEntityList =
	 * contentList.getJSONObject("campainEntities"); for (Object o :
	 * campainEntityList.keys()) { entityList.setString((String) o,
	 * campainEntityList.getString((String) o)); } }
	 */

	/*
	 * public static JSONObject getEntityContent() { return entityList; }
	 */

	public static JSONObject getModeMaps() {
		if (PreGameInfo.isCampain()) {
			if (GameApplet.getPreGameInfo().getUser("").nation != null && GameApplet.getPreGameInfo().getUser("").nation != Nation.NEUTRAL) {
				return contentList.getJSONObject("maps").getJSONObject("campain")
						.getJSONObject(GameApplet.getPreGameInfo().getUser("").nation.toString());
			} else {
				return new JSONObject();
			}
		}
		return contentList.getJSONObject("maps").getJSONObject("standard");
	}

	public Class<? extends GameObject> getGObjectClass(String name) {
		Class<? extends GameObject> c = gameObjectMap.get(name);
		if (c == null) {
			System.err.println("ERROR: " + name + " is not in gameObjectMap (ContentListManager.java:40)");
		}
		return c;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends GameObject>[] getGameObjectArray() {
		return (Class<? extends GameObject>[]) gameObjectMap.values().toArray(new Class<?>[gameObjectMap.size()]);
	}

	public Class<? extends Buff> getBuffClass(String name) {
		Class<? extends Buff> c = buffMap.get(name);
		if (c == null) {
			System.err.println("ERROR: " + name + " is not in buffMap (ContentListManager.java:51)");
		}
		return c;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends GameObject>[] getBuffArray() {
		return (Class<? extends GameObject>[]) buffMap.values().toArray(new Class<?>[gameObjectMap.size()]);
	}
}
