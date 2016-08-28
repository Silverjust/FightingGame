package entity.neutral;

import game.GameBaseApp;
import gameStructure.Building;
import gameStructure.animation.Extract;

public abstract class ArcanumMine extends Building {

	protected static final int efficenty = 20;
	protected static final String ressource = "arcanum";
	protected static final int cooldown = 5000;
	protected static final int buildTime = 8000;

	public ArcanumMine(String[] c) {
		super(c);
		// ************************************
		setxSize(50);
		setySize(50);

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
		GameBaseApp.updater.send("<spawn Arcanum 0 " + getX() + " " + getY());
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

}
