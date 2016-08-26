package gameStructure.animation;

import gameStructure.GameObject;
import processing.core.PImage;
import shared.ref;

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
			ref.updater.toRemove.add(e);
		}

	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
