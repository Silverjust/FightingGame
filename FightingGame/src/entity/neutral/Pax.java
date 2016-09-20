package entity.neutral;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.GameBaseApp;
import gameStructure.Building;
import gameStructure.animation.Animation;

public class Pax extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path( new Object() {
		});
		standImg = game.ImageHandler.load(path, "Pax");
	}

	public Pax(String[] c) {
		super(c);
		player = GameApplet.GameBaseApp.neutral;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);
		
		// ************************************
		setxSize(40);
		setySize(40);

		getStats().setRadius(0);

		descr = "pax";
		// ************************************
	}

	@Override
	public void renderUnder() {
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	@Override
	public void drawOnMinimap(PGraphics graphics) {
		graphics.fill(player.color);
		graphics.rect(getX(), getY(), 15 * 2, 15 * 2);
	}

	@Override
	public PImage preview() {
		return standImg;
	}
}
