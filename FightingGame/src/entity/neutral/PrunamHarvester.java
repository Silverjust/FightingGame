package entity.neutral;

import game.GameApplet;
import gameStructure.Building;
import gameStructure.animation.Extract;

public abstract class PrunamHarvester extends Building {

	protected static final int efficenty = 10;
	protected static final String ressource = "prunam";
	protected static final int cooldown = 10000;
	protected static final int buildTime = 8000;

	public PrunamHarvester(String[] c) {
		super(c);
		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;

		setHp(hp_max = 500);
		setRadius(15);
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
		GameApplet.updater.send("<spawn Prunam 0 " + getX() + " " + getY());
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}
}
