package game.aim;

import game.AimHandler.Cursor;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.GameObject.GroundPosition;
import shared.GameBaseApp;
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
		x = Building.xToGrid(Building.gridToX(app.mouseX));
		y = Building.xToGrid(Building.gridToY(player.app.mouseY));
		if (canPlaceAt(x, y)) {
			GameBaseApp.app.tint(255, 150);
		} else {
			GameBaseApp.app.tint(255, 100, 100, 150);
		}
		ImageHandler.drawImage(GameBaseApp.app, buildable.preview(), x, y / 2, buildable.getxSize(), buildable.getySize());
		GameBaseApp.app.tint(255);
	}

	protected boolean canPlaceAt(float x, float y) {
		boolean placeFree = true;
		boolean inCommanderRange = false;
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e.isInRange(x, y, buildable.getStats().getRadius().getTotalAmount() + e.getStats().getRadius().getTotalAmount())
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
			GameBaseApp.getUpdater().sendDirect("<spawn " + buildable.getClass().getSimpleName()
					+ " " + builder.player.getUser().getIp() + " " + x + " " + y);
			buildable.buyFrom(builder.player);
		}
	}
}
