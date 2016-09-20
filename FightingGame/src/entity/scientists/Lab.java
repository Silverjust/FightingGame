package entity.scientists;

import gameStructure.GameObject;
import gameStructure.Unit;
import shared.GameBaseApp;

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
		getStats.setSpeed(0.7f);
		getStats().setRadius(12);
		setHeight(30);
		animation.setSight(70);
		groundPosition = GameObject.GroundPosition.AIR;

		equipRange = 120;

		descr = " ";
		setStats(" ");
		// ************************************
	}

	@Override
	public void onSpawn(boolean isServer) {
		GameApplet.GameBaseApp.selectionChanged = true;
		GameApplet.GameBaseApp.keepGrid = true;
	}

	@Override
	public void renderAir() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

}