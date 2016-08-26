package gameStructure.actives;

import game.AimHandler;
import game.GameApplet;
import game.aim.BuildAim;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Spell;

public class BuildActive extends Spell {
	Class<? extends Building> building;
	String descr = " ", stats = " ";

	public BuildActive(int x, int y, char n, Building b,
			Class<?> builder) {
		super(x, y, n, b.iconImg);
		building = b.getClass();
		descr = b.getDescription();
		stats = b.getStatistics();
		setClazz(builder);
	}

	@Override
	public void onActivation() {
		GameObject builder = null;
		for (GameObject e : GameApplet.updater.selected) {
			if (getClazz().isAssignableFrom(e.getClass())) {
				builder = e;
			}
		}

		if (builder != null) {
			try {
				Building b = building.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				AimHandler.setAim(new BuildAim(builder, b));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getDescription() {
		return descr;
	}

	@Override
	public String getStatistics() {
		return stats;
	}
}
