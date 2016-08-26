package game.aim;

import shared.ref;
import game.AimHandler.Cursor;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.GameObject.GroundPosition;
import game.ImageHandler;

public class BuildAim extends Aim {
	protected Building buildable;
	protected GameObject builder;

	public BuildAim(GameObject builder, GameObject building) {
		try {
			this.builder = builder;
			this.buildable = (Building) building;
		} catch (ClassCastException e) {
			System.err.println(building + " is not buildable \n");
		}

	}
	public  Class<? extends Building> getToBuild() {
		return buildable.getClass();
	}

	@Override
	public Cursor getCursor() {
		return Cursor.BUILD;
	}

	@Override
	public void update() {
		float x, y;
		x = Building.xToGrid(Building.gridToX());
		y = Building.xToGrid(Building.gridToY());
		if (canPlaceAt(x, y)) {
			ref.app.tint(255, 150);
		} else {
			ref.app.tint(255, 100, 100, 150);
		}
		ImageHandler.drawImage(ref.app, buildable.preview(), x, y / 2, buildable.getxSize(), buildable.getySize());
		ref.app.tint(255);
	}

	protected boolean canPlaceAt(float x, float y) {
		boolean placeFree = true;
		boolean inCommanderRange = false;
		for (GameObject e : ref.updater.gameObjects) {
			if (e.isInRange(x, y, buildable.getRadius() + e.getRadius())
					&& e.groundPosition == GroundPosition.GROUND)
				placeFree = false;
			if (isInCommandingRange(e, x, y))
				inCommanderRange = true;
		}

		return placeFree && inCommanderRange
				&& buildable.canBeBought(builder.player);
	}

	public boolean isInCommandingRange(GameObject e, float x, float y) {
		if (e instanceof Commander && e.player == builder.player
				&& e.isInRange(x, y, ((Commander) e).commandRange())) {
			return true;
		}
		return false;
	}

	@Override
	public void execute(float x, float y) {
		/*
		 * float x, y; x = Entity.xToGrid(Entity.gridToX()); y =
		 * Entity.xToGrid(Entity.gridToY());
		 */
		if (canPlaceAt(x, y)) {
			ref.updater.send("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.getUser().ip + " " + x + " " + y);
			buildable.buyFrom(builder.player);
		}
	}
}
