package gameStructure.animation;

import game.GameBaseApp;
import gameStructure.GameObject;
import processing.core.PImage;

public class Death extends Animation {

	public Death(GameBaseApp app, PImage[][] IMG, int duration) {
		super(app, IMG, duration);

	}

	public Death(GameBaseApp app, PImage[] IMG, int duration) {
		super(app, IMG, duration);
	}

	public Death(GameBaseApp app, PImage IMG, int duration) {
		super(app, IMG, duration);
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
