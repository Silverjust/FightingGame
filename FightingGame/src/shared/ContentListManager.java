package shared;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import champs.ArmorShred;
import champs.Mage;
import champs.TestProjectile;
import champs.Ticul;
import game.GameBaseApp;
import gameStructure.Champion;
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

	private HashMap<String, Class<? extends Champion>> champMap = new HashMap<String, Class<? extends Champion>>();
	private HashMap<String, Class<? extends GameObject>> gameObjectMap = new HashMap<String, Class<? extends GameObject>>();
	private HashMap<String, Class<? extends Buff>> buffMap = new HashMap<String, Class<? extends Buff>>();
	private GameBaseApp app;

	public ContentListManager(GameBaseApp app) {
		this.app = app;
		Updater.Time.setup(app);
		load();
	}

	public void load() {
		ArrayList<Class<? extends Champion>> champList = new ArrayList<Class<? extends Champion>>();
		/** List of champions to select */
		champList.add(Ticul.class);
		champList.add(Mage.class);
		/***********************************/

		ArrayList<Class<? extends GameObject>> objectList = new ArrayList<Class<? extends GameObject>>();
		/** List of GameObjects to load/spawn */
		objectList.add(Ticul.class);
		objectList.add(TestProjectile.class);
		/***********************************/

		ArrayList<Class<? extends Buff>> buffList = new ArrayList<Class<? extends Buff>>();
		/** List of Buffs to buff/debuff */
		buffList.add(Root.class);
		buffList.add(Slow.class);

		buffList.add(ArmorShred.class);
		/***********************************/
		for (Class<? extends Champion> c : champList) {
			Champion o = (Champion) createGObj(c);
			if (o != null)
				champMap.put(o.getInternName(), o.getClass());
		}
		for (Class<? extends GameObject> c : objectList) {
			GameObject o = createGObj(c);
			if (o != null)
				gameObjectMap.put(o.getInternName(), o.getClass());
		}

		for (Class<? extends Buff> c : buffList) {
			Buff b = createBuff(c);
			if (b != null)
				buffMap.put(b.getInternName(), b.getClass());
		}

		contentList = app.loadJSONObject(path);
		campainEntityList = contentList.getJSONObject("campainEntities");
		for (Object o : campainEntityList.keys()) {
			entityList.setString((String) o, campainEntityList.getString((String) o));
		}
	}

	private Buff createBuff(Class<? extends Buff> c) {
		Constructor<?> ctor;
		try {
			ctor = c.getConstructor(GameBaseApp.class, String[].class);
			Buff b = (Buff) ctor.newInstance(new Object[] { app, null });
			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public GameObject createGObj(Class<? extends GameObject> c) {
		Constructor<?> ctor;
		try {
			ctor = c.getConstructor(GameBaseApp.class, String[].class);
			GameObject o = (GameObject) ctor.newInstance(new Object[] { app, null });
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
	@Deprecated
	public JSONObject getModeMaps() {
		return contentList.getJSONObject("maps").getJSONObject("standard");
	}

	public Class<? extends Champion> getChampClass(String name) {
		Class<? extends Champion> c = champMap.get(name);
		if (c == null) {
			System.err.println("ERROR: " + name + " is not in champMap (ContentListManager.java:36)");
		}
		return c;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Champion>[] getChampsArray() {
		return (Class<? extends Champion>[]) champMap.values().toArray(new Class<?>[champMap.size()]);
	}

	public Class<? extends GameObject> getGObjectClass(String name) {
		Class<? extends GameObject> c = gameObjectMap.get(name);
		if (c == null) {
			System.err.println("ERROR: " + name + " is not in gameObjectMap (ContentListManager.java:37)");
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
			System.err.println("ERROR: " + name + " is not in buffMap (ContentListManager.java:38)");
		}
		return c;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends GameObject>[] getBuffArray() {
		return (Class<? extends GameObject>[]) buffMap.values().toArray(new Class<?>[gameObjectMap.size()]);
	}
}
