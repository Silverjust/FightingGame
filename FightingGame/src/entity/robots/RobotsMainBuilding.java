package entity.robots;

import processing.core.PGraphics;
import processing.core.PImage;
import entity.MainBuilding;
import game.ImageHandler;
import gameStructure.Commander;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class RobotsMainBuilding extends MainBuilding implements Commander {

	private static PImage standImg;
	private static PImage previewImg;

	// static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "RobotsMainBuilding");
		// groundImg = ImageHandler.load(path, "AlienGround");
	}

	public RobotsMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = new Death(standImg, 1000);

		setAnimation(stand);
		
		// ************************************
		setxSize(60);
		setySize(60);

		setSight(50);

		setHp(hp_max = 1500);
		setRadius(RADIUS);

		commandingRange = 100;

		descr = "United Robotss Main Building�when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderTerrain() {
		// ref.app.image(groundImg, xToGrid(x), yToGrid(y), commandingRange * 2,
		// commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		// graphics.image(RobotsMainBuilding.groundImg, x, y, commandingRange *
		// 2,
		// commandingRange * 2);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	public PImage preview() {
		return previewImg;
	}

}
