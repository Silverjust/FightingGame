package entity;

import gameStructure.Building;
import gameStructure.Commander;
import main.appdata.ProfileHandler;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper.Timer;

public abstract class MainBuilding extends Building implements Commander, Coms {
	protected static final int RADIUS = 27;
	protected int commandingRange;
	public Timer progress;

	public MainBuilding(String[] c) {
		super(c);
		try {
			player.mainBuilding = this;
		} catch (Exception e) {
		}
	}

	@Override
	protected void onDeath() {
		super.onDeath();
		GameBaseApp.getUpdater().sendDirect(GAMEEND + " lost " + player.getUser().getIp() + " " + ProfileHandler.getRate());
	}

	@Override
	public void display() {
		if (progress != null && !progress.isNotOnCooldown())
			drawBar(progress.getCooldownPercent());
		super.display();
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

}
