package gameStructure.animation;

import game.GameApplet;
import gameStructure.GameObject;
import processing.core.PImage;

public class Death extends Animation {

	public Death(PImage[][] IMG, int duration) {
		super(IMG, duration);

	}

	public Death(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Death(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void update(GameObject e) {
		if (isFinished()) {
			// setup(e);
			GameApplet.updater.toRemove.add(e);
		}

	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
