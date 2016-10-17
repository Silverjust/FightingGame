package gameStructure.animation;

import gameStructure.GameObject;
import processing.core.PImage;
import shared.GameBaseApp;

public class Death extends Animation {

	public Death(GameBaseApp app, GameObject animated, PImage[][] IMG, int duration) {
		super(app, animated, IMG, duration);

	}

	public Death(GameBaseApp app, GameObject animated, PImage[] IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	public Death(GameBaseApp app, GameObject animated, PImage IMG, int duration) {
		super(app, animated, IMG, duration);
	}

	@Override
	public void update(GameObject e) {
		if (isFinished()) {
			// setup(e);
			app.getUpdater().toRemove.add(e);
		}

	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

}
