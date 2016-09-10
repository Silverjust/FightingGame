package entity.neutral;

import gameStructure.Building;
import gameStructure.animation.Extract;
import shared.GameBaseApp;

public abstract class KeritMine extends Building {

	protected static final int efficenty = 17;
	protected static final String ressource = "kerit";
	protected static final int cooldown = 1000;
	protected static final int buildTime = 5000;

	public KeritMine(String[] c) {
		super(c);

		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 250;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		// buildtime in child

		setHp(hp_max = 500);
		setRadius(10);
		setSight(50);
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
		GameBaseApp.getUpdater().sendDirect("<spawn Kerit 0 " + getX() + " " + getY());
	}

}
