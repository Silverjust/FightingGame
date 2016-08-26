package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Commander;
import gameStructure.MainBuilding;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class AlienMainBuilding extends MainBuilding implements Commander {
	private static PImage standImg;
	private static PImage previewImg;
	static PImage groundImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = ImageHandler.load(path, "AlienMainBuilding");
		groundImg = ImageHandler.load(path, "AlienGround");
	}

	public AlienMainBuilding(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = new Death(standImg, 1000);

		setAnimation(stand);
		
		// ************************************
		setxSize(85);
		setySize(85);

		setSight(50);

		setHp(hp_max = 1500);
		setRadius(RADIUS);

		commandingRange = 250;

		descr = "Alien Main Building§when it dies, you loose";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(GameApplet.app, groundImg, xToGrid(getX()), yToGrid(getY()),
				commandingRange * 2, commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, getX(), getY(), commandingRange * 2,
				commandingRange * 2);
	}

	@Override
	public void renderUnder() {
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	public PImage preview() {
		return previewImg;
	}

	static PImage getGround() {
		return groundImg;
	}

}
