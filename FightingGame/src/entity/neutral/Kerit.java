package entity.neutral;

import processing.core.PImage;
import shared.ref;
import gameStructure.Building;
import gameStructure.animation.Animation;

public class Kerit extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "Kerit");
	}

	public Kerit(String[] c) {
		super(c);
		player = ref.updater.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);

		// ************************************
		setxSize(30);
		setySize(30);

		setRadius(15);

		descr = "Kerit";
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
