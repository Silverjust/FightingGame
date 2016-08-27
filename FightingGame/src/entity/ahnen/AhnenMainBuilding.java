package entity.ahnen;

import processing.core.PGraphics;
import processing.core.PImage;
import entity.MainBuilding;
import game.ImageHandler;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class AhnenMainBuilding extends MainBuilding {

	private static PImage standImg;
	private static PImage previewImg;

	// static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "AhnenMainBuilding");
		// groundImg = ImageHandler.load(path, "AlienGround");
	}

	public AhnenMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = new Death(standImg, 1000);

		setAnimation(stand);
		 
		// ************************************
		setxSize(65);
		setySize(65);

		setSight(50);

		setHp(hp_max = 1500);
		setRadius(RADIUS);

		commandingRange = 250;

		descr = "United Humans Main Building§when it dies, you loose";
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
		// graphics.image(HumanMainBuilding.groundImg, x, y, commandingRange *
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
