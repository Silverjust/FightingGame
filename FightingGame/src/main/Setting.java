package main;

import java.awt.im.InputContext;

import game.GameApplet;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;
import shared.VersionControle;

public class Setting {

	public int mouseCommand;
	public int mouseMove;
	public int toggleChat;
	public int strg;
	public int shift;
	public int changeAbilityMode;
	public int togglePause;
	public char centerView;
	public char[] baseShortcuts = new char[7];
	public char[] hotKeys = new char[10];
	private JSONObject shortcutsJson;
	private GameApplet app;

	public Setting(GameApplet app) {
		this.app = app;
	}

	public void fromJSON(String s) {
		JSONObject o = null;
		try {
			o = app.loadJSONObject(s);
		} catch (Exception e) {
			o = new JSONObject();
		}
		VersionControle.setToBaseShortcuts(o);
		fillEmptySettings(o);
		mouseCommand = o.getInt("mouseCommand");
		mouseMove = o.getInt("mouseSelect");
		toggleChat = o.getInt("toggleChat");
		strg = o.getInt("strg");
		shift = o.getInt("shift");
		changeAbilityMode = o.getInt("changeAbilityMode");
		togglePause = o.getInt("togglePause");
		centerView = o.getString("centerView").charAt(0);
		{
			JSONArray jarr = o.getJSONArray("baseShortcuts");
			for (int i = 0; i < jarr.size(); i++) {
				baseShortcuts[i] = jarr.getString(i).charAt(0);

			}
		}
		{
			JSONArray jarr = o.getJSONArray("hotKeys");
			for (int i = 0; i < jarr.size(); i++) {
				hotKeys[i] = jarr.getString(i).charAt(0);
			}
		}

		app.saveJSONObject(o, s);
	}

	private void fillEmptySettings(JSONObject o) {

		if (!o.hasKey("mouseCommand"))
			o.setInt("mouseCommand", mouseCommand = PConstants.LEFT);
		if (!o.hasKey("mouseSelect"))
			o.setInt("mouseSelect", PConstants.RIGHT);
		if (!o.hasKey("toggleChat"))
			o.setInt("toggleChat", PConstants.ENTER);
		if (!o.hasKey("strg"))
			o.setInt("strg", PConstants.CONTROL);
		if (!o.hasKey("shift"))
			o.setInt("shift", PConstants.SHIFT);
		if (!o.hasKey("changeAbilityMode"))
			o.setInt("changeAbilityMode", PConstants.TAB);

		if (!o.hasKey("togglePause"))
			o.setInt("togglePause", PConstants.ESC);

		if (!o.hasKey("centerView"))
			o.setString("centerView", " ");

		String standardGrid = "[ \"q\", \"w\", \"e\", \"r\", \"t\", \"y\", \"u\" ]";
		InputContext context = InputContext.getInstance();
		if (context.getLocale().toString().equals("de_DE"))
			standardGrid = "[ \"q\", \"w\", \"e\", \"r\", \"t\", \"z\", \"u\" ]";

		if (!o.hasKey("baseShortcuts"))
			o.setJSONArray("baseShortcuts", JSONArray.parse(standardGrid));
		if (!o.hasKey("hotKeys"))
			o.setJSONArray("hotKeys",
					JSONArray.parse("[ \"1\", \"2\", \"3\", \"4\", \"5\", \"6\", \"7\", \"8\", \"9\", \"0\" ]"));
	}

	public char[] getShortcut(String nation, String name) {
		char[] shortcuts = new char[7];
		try {
			JSONArray jarr = shortcutsJson.getJSONObject(nation).getJSONArray(name);
			for (int i = 0; i < jarr.size(); i++) {
				shortcuts[i] = jarr.getString(i).charAt(0);

			}
		} catch (Exception e) {
			return null;
		}
		return shortcuts;
	}

	public static String getKeyName(int i) {
		if (i == 39)
			return "right-click";
		if (i == 37)
			return "left-click";
		return i + "";
	}
}
