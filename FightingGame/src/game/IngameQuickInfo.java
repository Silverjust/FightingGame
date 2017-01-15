package game;

import gameStructure.GameObject;
import gameStructure.Damage.DmgType;
import processing.core.PApplet;
import processing.core.PConstants;
import shared.GameBaseApp;
import shared.Helper;
import shared.Helper.Timer;

/** class für dmg-werte und ähnliche informationen */
public class IngameQuickInfo {
	private int value;
	private GameBaseApp app;
	private float x;
	private float y;
	Timer timer;
	private boolean hasDecayed;
	private float direction;
	private int displace;
	private float textsize = 5;
	private String baseText;
	private DmgType dmgType;

	public IngameQuickInfo(GameBaseApp app, int cooldown, float x, float y) {
		this.app = app;
		this.x = x;
		this.y = y;
		if (cooldown > 0) {
			timer = new Timer(cooldown);
			timer.startCooldown();
		}
		direction = app.random(0, PConstants.TAU);
		displace = 0;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setDmgType(String s) {
		char c = s.charAt(0);
		for (DmgType type : DmgType.values()) {
			if (c == type.getName())
				dmgType = type;
		}

		if (dmgType == DmgType.PHYSICAL_DMG)
			baseText = "§color 255 100 0§";
		else if (dmgType == DmgType.MAGIC_DMG)
			baseText = "§color 255 0 255§";
		else if (dmgType == DmgType.TRUE_DMG)
			baseText = "§color 255 255 255§";
		else if (dmgType == DmgType.HEAL)
			baseText = "§color 150 255 50§";
		else if (dmgType == DmgType.BLOCK)
			baseText = "§color 180 180 180§";

		if (s.length() > 1 && s.charAt(1) == 'k') {
			baseText += "§img krit§";
		}
		//System.out.println("IngameQuickInfo.setDmgType()" + dmgType + " " + s);
	}

	public IngameQuickInfo setInfo(String string) {
		baseText = "§color 0 0 0§" + string;

		return this;
	}

	void display() {
		app.fill(255);
		app.textSize(textsize);
		app.rectMode(PConstants.CORNER);
		app.imageMode(PConstants.CORNER);

		String text = baseText;
		if (value != 0) {
			if (dmgType == DmgType.BLOCK)
				text += "(" + value + ")";
			else
				text += value;
		}
		Helper.text(app, text, xToGrid(x + PApplet.cos(direction) * displace),
				yToGrid(y + PApplet.sin(direction) * displace));

		if (timer != null) {
			displace = (int) (10 * timer.getCooldownPercent());
			//textsize = PApplet.map(timer.getCooldownPercent(), 0, 1, 5, 3);
			if (timer.isNotOnCooldown())
				setDecayed(true);
		}

	}

	public boolean hasDecayed() {
		return hasDecayed;
	}

	public void setDecayed(boolean hasDecayed) {
		this.hasDecayed = hasDecayed;
	}

	public static float xToGrid(float x) {
		return x;
	}

	public static float yToGrid(float y) {
		return y / 2;
	}

	public static float gridToX(int x) {
		return ((x - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY(int y) {
		return ((y - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public static void debugg(GameBaseApp app, GameObject owner, String text) {
		if (app.getDrawer() != null) {
			IngameQuickInfo info = new IngameQuickInfo(app, 2000, owner.getX(), owner.getY()).setInfo(text);
			app.getDrawer().addQuickInfo(info);
		}
	}
}
