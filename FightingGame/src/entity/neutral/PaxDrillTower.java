package entity.neutral;

import gameStructure.Building;
import gameStructure.animation.Extract;
import shared.GameBaseApp;

public abstract class PaxDrillTower extends Building {

	protected static final int efficenty = 14;
	protected static final String ressource = "pax";
	protected static final int cooldown = 1000;
	protected static final int buildTime = 7000;

	public PaxDrillTower(String[] c) {
		super(c);
		// ************************************
		setxSize(40);
		setySize(40);

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;

		setHp(hp_max = 500);
		getStats().setRadius(15);
		animation.setSight(50);

		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (getAnimation() == stand)
			((Extract) stand).updateAbility(this, isServer);
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		GameBaseApp.getUpdater().sendDirect("<spawn Pax 0 " + getX() + " " + getY());
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

}
