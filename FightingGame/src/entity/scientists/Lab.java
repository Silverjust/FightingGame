package entity.scientists;

import game.GameApplet;
import gameStructure.GameObject;
import gameStructure.Unit;

public abstract class Lab extends Unit implements Equiping {
	protected static final int TRAINTIME = 3000;
	public byte equipRange;

	public Lab(String[] c) {
		super(c);
		// ************************************
		setxSize(20);
		setySize(20);

		setHp(hp_max = 700);
		armor = 1;
		setSpeed(0.7f);
		setRadius(12);
		setHeight(30);
		setSight(70);
		groundPosition = GameObject.GroundPosition.AIR;

		equipRange = 120;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		GameApplet.updater.selectionChanged = true;
		GameApplet.updater.keepGrid = true;
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

}