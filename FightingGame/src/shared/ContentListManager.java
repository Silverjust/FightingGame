package shared;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import champs.Mage;
import champs.TestProjectile;
import champs.Ticul;
import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Spell;
import gameStructure.baseBuffs.Buff;
import gameStructure.baseBuffs.Root;
import gameStructure.baseBuffs.Slow;
import gameStructure.baseBuffs.SpeedBuff;
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
	private GameBaseApp app;

	private HashMap<String, Class<? extends Champion>> champMap = new HashMap<String, Class<? extends Champion>>();
	private HashMap<String, Class<? extends GameObject>> gameObjectMap = new HashMap<String, Class<? extends GameObject>>();
	private HashMap<String, Class<? extends Buff>> buffMap = new HashMap<String, Class<? extends Buff>>();
	private HashMap<String, Class<? extends Spell>> spellMap = new HashMap<String, Class<? extends Spell>>();

	public ContentListManager(GameBaseApp app) {
		this.app = app;
		Updater.Time.setup(app);
		load();
	}

	public void load() {
		/** List of champions to select */
		addClassToChamps(Mage.class);
		/***********************************/

		/** List of GameObjects to load/spawn */
		addClassToGObjects(Ticul.class);
		addClassToGObjects(TestProjectile.class);
		/***********************************/

		/** List of Buffs to buff/debuff */
		addClassToBuffs(Root.class);
		addClassToBuffs(Slow.class);
		addClassToBuffs(SpeedBuff.class);
		/***********************************/

		/** List of Spells to load */
		/***********************************/

		contentList = app.loadJSONObject(path);
		campainEntityList = contentList.getJSONObject("campainEntities");
		for (Object o : campainEntityList.keys()) {
			entityList.setString((String) o, campainEntityList.getString((String) o));
		}
	}

	public void addClassToChamps(Class<? extends Champion> c) {
		String key = null;
		Champion o = (Champion) createGObj(c);
		if (o != null)
			key = o.getInternName();
		champMap.put(key, c);
	}

	public void addClassToGObjects(Class<? extends GameObject> c) {
		String key = null;
		GameObject o = createGObj(c);
		if (o != null) {
			o.onRegistration(this);
			key = o.getInternName();
		}
		gameObjectMap.put(key, c);
	}

	public void addClassToBuffs(Class<? extends Buff> c) {
		String key = null;
		Buff o = createBuff(c);
		if (o != null)
			key = o.getInternName();
		buffMap.put(key, c);
	}

	public void addClassToSpells(Class<? extends Spell> c) {
		String key = null;
		key = c.getName();
		spellMap.put(key, c);
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

	@SuppressWarnings("unchecked")
	public Class<? extends Spell>[] getSpellArray() {
		return (Class<? extends Spell>[]) spellMap.values().toArray(new Class<?>[spellMap.size()]);
	}
}
