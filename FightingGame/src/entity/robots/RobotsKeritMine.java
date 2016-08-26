package entity.robots;

import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Extract;
import processing.core.PImage;

public class RobotsKeritMine extends entity.neutral.KeritMine {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = game.ImageHandler.load(path, "RobotsKeritMine");
	}

	public RobotsKeritMine(String[] c) {
		super(c);
		iconImg = standImg;
		stand = new Extract(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);

		setAnimation(build);
		
		// ************************************
		build.setBuildTime(buildTime);
		((Extract) stand).cooldown = cooldown;
		((Extract) stand).ressource = ressource;
		((Extract) stand).efficenty = efficenty;
		descr = " ";
		stats = "ressource/s: "
				+ (((Extract) stand).efficenty / ((Extract) stand).cooldown * 1000);
		// ************************************
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	public PImage preview() {
		return standImg;
	}

}
