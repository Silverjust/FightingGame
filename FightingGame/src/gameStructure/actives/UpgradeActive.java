package gameStructure.actives;

import shared.ref;
import game.AimHandler;
import game.aim.UpgradeAim;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Spell;

public class UpgradeActive extends Spell {
	protected Class<? extends Building> newBuilding;
	protected Class<? extends Building> oldBuilding;
	String descr = " ", stats = " ";

	public UpgradeActive(int x, int y, char n, GameObject b,
			Class<? extends Building> oldBuilding, Class<?> builder) {
		super(x, y, n, b.iconImg);
		this.newBuilding = ((Building) b).getClass();
		this.oldBuilding = oldBuilding;
		descr = b.getDescription();
		stats = b.getStatistics();
		setClazz(builder);
	}

	@Override
	public void onActivation() {
		GameObject builder = null;
		for (GameObject e : ref.updater.selected) {
			if (getClazz().isAssignableFrom(e.getClass())) {
				builder = e;
			}
		}
		if (builder != null) {
			try {
				Building b = newBuilding.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				AimHandler.setAim(new UpgradeAim(builder, b, oldBuilding));
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