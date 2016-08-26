package entity.humans;

import processing.core.PImage;
import game.ImageHandler;
import gameStructure.Commander;
import gameStructure.MainBuilding;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class HumanMainBuilding extends MainBuilding implements Commander {

	private static PImage standImg;
	private static PImage previewImg;

	// static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "HumanMainBuilding");
		// groundImg = ImageHandler.load(path, "AlienGround");
	}

	public HumanMainBuilding(String[] c) {
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

		commandingRange = 250;

		descr = "United Humans Main Building§when it dies, you loose";
		stats = " ";
		// ************************************
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
