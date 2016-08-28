package game.aim;

import shared.NationInfo;
import entity.neutral.Arcanum;
import entity.neutral.Kerit;
import entity.neutral.Pax;
import entity.neutral.Prunam;
import game.AimHandler;
import game.GameBaseApp;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.actives.UpgradeActive;

public class MineAim extends UpgradeAim {

	public MineAim(GameObject builder, GameObject newBuildable,
			Class<? extends GameObject> oldBuildable) {
		super(builder, newBuildable, oldBuildable);
	}

	@Override
	protected boolean canPlaceAt(float x, float y) {
		boolean rightPlace = false;
		boolean inCommanderRange = false;
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e.getX() == x && e.getY() == y) {
				if (e.getClass().equals(oldBuildable)) {
					replaced = (Building) e;
					rightPlace = true;
				} else
					changeMine(e);
			}
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}
		return rightPlace && inCommanderRange
				&& ((GameObject) buildable).canBeBought(builder.player);
	}

	private void changeMine(GameObject e) {
		try {
			if (e.getClass().equals(Kerit.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getKeritMine().getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				oldBuildable = Kerit.class;
			} else if (e.getClass().equals(Pax.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getPaxDrillTower().getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				oldBuildable = Pax.class;
			} else if (e.getClass().equals(Arcanum.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getArcanumMine().getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				oldBuildable = Arcanum.class;

			} else if (e.getClass().equals(Prunam.class)) {
				NationInfo n = builder.player.getNation().getNationInfo();
				buildable = n.getPrunamHarvester()
						.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
				oldBuildable = Prunam.class;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public static class BuildMineActive extends UpgradeActive {

		public BuildMineActive(int x, int y, char n, GameObject b, Class<?> builder) {
			super(x, y, n, b, Kerit.class, builder);
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
					Building b = newBuilding.getConstructor(String[].class)
							.newInstance(new GameObject[] { null });
					AimHandler.setAim(new MineAim(builder, b, oldBuilding));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
