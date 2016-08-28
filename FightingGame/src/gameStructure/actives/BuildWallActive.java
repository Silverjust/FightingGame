package gameStructure.actives;

import game.AimHandler;
import game.GameBaseApp;
import game.aim.BuildWallAim;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Spell;

public class BuildWallActive extends Spell {
	protected Class<? extends Building> building;
	String descr = " ", stats = " ";

	public BuildWallActive(int x, int y, char n, GameObject b, Class<?> builder) {
		super(x, y, n, b.iconImg);
		building = ((Building) b).getClass();
		descr = b.getDescription();
		stats = b.getStatistics();
		setClazz(builder);
	}

	@Override
	public void onActivation() {
		GameObject builder = null;
		for (GameObject e : GameApplet.GameBaseApp.selected) {
			if (getClazz().isAssignableFrom(e.getClass())) {
				builder = e;
			}
		}

		if (builder != null) {
			try {
				Building b = building.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				AimHandler.setAim(new BuildWallAim(builder, b));
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
