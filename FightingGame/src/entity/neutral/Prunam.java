package entity.neutral;

import processing.core.PImage;
import game.GameBaseApp;
import gameStructure.Building;
import gameStructure.animation.Animation;

public class Prunam extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "Prunam");
	}

	public Prunam(String[] c) {
		super(c);
		player = GameApplet.GameBaseApp.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);

		// ************************************
		setxSize(30);
		setySize(30);

		setRadius(15);
		// ************************************
	}

	@Override
	public void renderGround() {
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	@Override
	public PImage preview() {
		return standImg;
	}
}
