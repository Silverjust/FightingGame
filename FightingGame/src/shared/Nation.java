package shared;

import entity.MainBuilding;
import entity.ahnen.AhnenInfo;
import entity.aliens.AlienInfo;
import entity.aliens.AlienMainBuilding;
import entity.humans.HumanInfo;
import entity.humans.HumanMainBuilding;
import entity.neutral.NeutralInfo;
import entity.robots.RobotsInfo;
import entity.scientists.ScientistInfo;
import game.GameBaseApp;

public enum Nation {

	ALIENS, AHNEN, ROBOTS, SCIENTISTS, HUMANS, NEUTRAL;

	public String toString() {
		String S = null;
		switch (this) {
		case AHNEN:
			S = "ahnen";
			break;
		case ALIENS:
			S = "aliens";
			break;
		case ROBOTS:
			S = "robots";
			break;
		case SCIENTISTS:
			S = "scientists";
			break;
		case HUMANS:
			S = "humans";
			break;
		case NEUTRAL:
			S = "neutral";
			break;
		}
		return S;
	}

	public static Nation fromString(String s) {
		Nation n = null;
		switch (s) {
		case "ahnen":
			n = AHNEN;
			break;
		case "aliens":
			n = ALIENS;
			break;
		case "robots":
			n = ROBOTS;
			break;
		case "scientists":
			n = SCIENTISTS;
			break;
		case "humans":
			n = HUMANS;
			break;
		case "neutral":
			n = NEUTRAL;
			break;
		}
		return n;
	}

	public int toNumber() {
		int n = -1;
		switch (this) {
		case AHNEN:
			n = 4;
			break;
		case ALIENS:
			n = 0;
			break;
		case ROBOTS:
			n = 1;
			break;
		case SCIENTISTS:
			n = 3;
			break;
		case HUMANS:
			n = 2;
			break;
		case NEUTRAL:
			n = 5;
			break;
		}
		return n;
	}

	public static Nation fromNumber(int i) {
		Nation nation = null;
		switch (i) {
		case 0:
			nation = Nation.ALIENS;
			break;
		case 1:
			nation = Nation.ROBOTS;
			break;
		case 2:
			nation = Nation.HUMANS;
			break;
		case 3:
			nation = Nation.SCIENTISTS;
			break;
		case 4:
			nation = Nation.AHNEN;
			break;
		default:
			break;
		}
		return nation;
	}

	@Deprecated
	public Class<? extends MainBuilding> getMainBuilding() {
		Class<? extends MainBuilding> t = null;

		switch (this) {
		case AHNEN:
			// t = AhnenMainBuilding.class;
			break;
		case ALIENS:
			t = AlienMainBuilding.class;
			break;
		case ROBOTS:
			// t = RobotsMainBuilding.class;
			break;
		case SCIENTISTS:
			// t = ScientistsMainBuilding.class;
			break;
		case HUMANS:
			t = HumanMainBuilding.class;
			break;
		case NEUTRAL:
			// t = NeutralMainBuilding.class;
			break;
		}
		return t;

	}

	// TODO remove, when all nations are stable
	public static void setNationsToPlayableNations() {
		for (String key : GameApplet.GameBaseApp.users.keySet()) {
			Nation n = GameApplet.GameBaseApp.users.get(key).nation;
			if (n != Nation.AHNEN && n != Nation.ALIENS && n != Nation.ROBOTS
					&& n != Nation.HUMANS && n != Nation.SCIENTISTS)
				GameApplet.GameBaseApp.users.get(key).nation = Nation.ALIENS;
		}
	}

	public NationInfo getNationInfo() {
		NationInfo info = null;

		switch (this) {
		case AHNEN:
			info = new AhnenInfo();
			break;
		case ALIENS:
			info = new AlienInfo();
			break;
		case ROBOTS:
			 info=new RobotsInfo();
			break;
		case SCIENTISTS:
			info = new ScientistInfo();
			break;
		case HUMANS:
			info = new HumanInfo();
			break;
		case NEUTRAL:
			info = new NeutralInfo();
			break;
		}
		return info;
	}

	public String officialName() {
		String S = null;
		switch (this) {
		case AHNEN:
			S = "ahnen";
			break;
		case ALIENS:
			S = "aliens";
			break;
		case ROBOTS:
			S = "A.W.I";
			break;
		case SCIENTISTS:
			S = "scientists";
			break;
		case HUMANS:
			S = "united humans";
			break;
		case NEUTRAL:
			S = "neutral";
			break;
		}
		return S;
	}

}
