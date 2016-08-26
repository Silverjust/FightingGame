package game.aim;

import gameStructure.Building;
import gameStructure.GameObject;
import shared.ref;

public class UpgradeAim extends BuildAim {
	Class<? extends GameObject> oldBuildable;

	Building replaced;

	public UpgradeAim(GameObject builder, GameObject newBuildable,
			Class<? extends GameObject> oldBuildable) {
		super(builder, newBuildable);
		this.oldBuildable = oldBuildable;
	}

	@Override
	public void execute(float x, float y) {
		/*
		 * float x, y; x = Building.xToGrid(Building.gridToX()); y =
		 * Building.xToGrid(Building.gridToY());
		 */
		x = Building.xToGrid(x);
		y = Building.xToGrid(y);
		if (canPlaceAt(x, y)) {
			ref.updater.send("<remove " + replaced.number);
			ref.updater.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.getUser().ip + " " + x + " " + y);
			((GameObject) buildable).buyFrom(builder.player);
		}
	}

	@Override
	protected boolean canPlaceAt(float x, float y) {
		boolean rightPlace = false;
		boolean inCommanderRange = false;
		for (GameObject e : ref.updater.gameObjects) {
			if (e.getClass().equals(oldBuildable) && e.getX() == x && e.getY() == y) {
				replaced = (Building) e;
				rightPlace = true;
			}
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}
		return rightPlace && inCommanderRange
				&& ((GameObject) buildable).canBeBought(builder.player);
	}
}
