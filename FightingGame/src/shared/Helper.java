package shared;

import java.util.ArrayList;

import game.GameApplet;
import game.GameDrawer;
import gameStructure.GameObject;

public class Helper {

	public static byte getDirection(float x, float y, float tx, float ty) {
		float a = (float) Math.toDegrees(Math.atan2(y - ty, x - tx));
		a += 112.5;
		if (a < 0) {
			a += 360;
		}
		byte b = (byte) (a * 8 / 360);
		return (0 <= b && b <= 8) ? b : 0;
	}

	@Deprecated
	public static boolean StringToBoolean(String S) {
		boolean b = false;
		if (S.equals("true")) {
			b = true;
		} else if (S.equals("false")) {
			b = false;
		} else {
			throw new IllegalArgumentException("String is no boolean");
		}
		return b;
	}

	public static boolean isOver(float x, float y, float x1, float y1, float x2, float y2) {
		boolean b = x1 <= x && x <= x2 && y1 <= y && y <= y2;
		return b;
	}

	public static boolean isBetween(float x, float y, float x1, float y1, float x2, float y2) {
		boolean b = (x1 < x2 ? (x1 <= x && x <= x2) : (x2 <= x && x <= x1))
				&& (y1 < y2 ? (y1 <= y && y <= y2) : (y2 <= y && y <= y1));
		return b;
	}

	public static boolean isMouseOver(float x1, float y1, float x2, float y2) {
		boolean b = x1 <= GameApplet.app.mouseX && GameApplet.app.mouseX <= x2 && y1 <= GameApplet.app.mouseY && GameApplet.app.mouseY <= y2;
		return b;
	}

	public static float gridToX(float x) {
		return ((x - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY(float y) {
		return ((y - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public static String nameToIP(String name) {
		Player p;
		p = GameApplet.updater.players.get(name);
		if (p != null)
			return p.getUser().ip;// ip from ip
		for (String key : GameApplet.updater.players.keySet()) {
			if (GameApplet.updater.players.get(key).getUser().name.equalsIgnoreCase(name))
				return GameApplet.updater.players.get(key).getUser().ip;// ip from name
		}
		try {
			String[] a = GameApplet.updater.players.keySet().toArray(new String[GameApplet.updater.players.keySet().size()]);
			return GameApplet.updater.players.get(a[Integer.parseInt(name)]).getUser().ip;
			// ip from number
		} catch (Exception e) { // not a number
		}
		return GameApplet.player.getUser().ip; // ip from this player
	}

	public static String ipToName(String ip) {
		User u = GameApplet.getPreGameInfo().getUser(ip);
		String name = null;
		if (u != null)
			name = u.name;
		if (name != null)
			return name;
		return ip;
	}

	public static String secureInput(String in) {
		in.replace('<', ' ');// gegen command injektion
		in.replace('>', ' ');
		if (in != "" && in.charAt(0) == ' ')
			in = in.substring(1);
		return in;
	}

	public static boolean listContainsInstanceOf(Class<?> c, ArrayList<?> arrlist) {
		if (c == null) {
			return true;
		}
		for (Object o : arrlist) {
			if (c.isAssignableFrom(o.getClass())) {
				return true;
			}

		}
		return false;
	}

	public static int listContainsInstancesOf(Class<?> c, ArrayList<GameObject> arrlist) {
		if (c == null) {
			return 0;
		}
		int i = 0;
		for (GameObject e : arrlist) {
			if (c.isAssignableFrom(e.getClass())) {
				i++;
			}

		}
		return i;
	}

	public float fontHeight() {
		return GameApplet.app.textAscent() * GameApplet.textScale;
	}

	static public class Timer {
		public int cooldown;
		private int cooldownTimer;

		public Timer() {
		}

		public Timer(int cooldown) {
			this.cooldown = cooldown;
		}

		public void startCooldown() {
			cooldownTimer = Updater.Time.getMillis() + cooldown;
		}

		public boolean isNotOnCooldown() {
			return cooldownTimer <= Updater.Time.getMillis();
		}

		public float getCooldownPercent() {
			float f = 1 - (float) (cooldownTimer - Updater.Time.getMillis()) / cooldown;
			return f > 1 || f < 0 ? 1 : f;
		}

		public float getTimeLeft() {
			return (cooldownTimer - Updater.Time.getMillis());
		}

	}
}
