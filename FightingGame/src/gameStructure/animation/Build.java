package gameStructure.animation;

import gameStructure.GameObject;
import processing.core.PImage;

public class Build extends Ability {

	public Build(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Build(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Build(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setBuildTime(int buildTime) {
		cooldown = buildTime;
		startCooldown();//why not started with setup
	}

	@Override
	public boolean doRepeat(GameObject e) {
		return !isNotOnCooldown();
	}
	@Override
	public boolean isInterruptable() {
		return isNotOnCooldown();
	}
}
