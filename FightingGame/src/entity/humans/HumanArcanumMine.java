package entity.humans;

import processing.core.PImage;
import entity.neutral.ArcanumMine;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Extract;

public class HumanArcanumMine extends ArcanumMine {

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		previewImg = standImg = game.ImageHandler
				.load(path, "HumanArcanumMine");
	}

	public HumanArcanumMine(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Extract(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 1000);

		setAnimation(build);
		
		// ************************************
		build.setBuildTime(buildTime);

		((Extract) stand).cooldown = cooldown;
		((Extract) stand).ressource = ressource;
		((Extract) stand).efficenty = efficenty;

		descr = " ";
		setStats("ressource/s: "
				+ (((Extract) stand).efficenty / ((Extract) stand).cooldown * 1000));
		// ************************************
	}

	public PImage preview() {
		return previewImg;
	}
}