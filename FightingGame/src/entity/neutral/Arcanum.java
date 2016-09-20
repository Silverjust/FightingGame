package entity.neutral;

import processing.core.PImage;
import shared.GameBaseApp;
import gameStructure.Building;
import gameStructure.animation.Animation;

public class Arcanum extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path( new Object() {
		});
		standImg = game.ImageHandler.load(path, "Arcanum");
	}

	public Arcanum(String[] c) {
		super(c);
		player = GameApplet.GameBaseApp.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);
		
		// ************************************
		setxSize(50);
		setySize(50);

		getStats().setRadius(15);

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
