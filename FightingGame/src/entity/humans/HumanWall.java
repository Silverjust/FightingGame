package entity.humans;

import processing.core.PImage;
import game.ImageHandler;
import game.aim.BuildWallAim;
import gameStructure.Building;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;

public class HumanWall extends Building {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanWall");
	}

	public HumanWall(String[] c) {
		super(c);
		BuildWallAim.setupWall(this, c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);

		setAnimation(build);
		
		// ************************************
		setxSize(20);
		setySize(25);

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		animation.setSight(20);

		setHp(hp_max = 500);
		armor = 2;
		getStats().setRadius(10);

		descr = "wall ";
		setStats(" ");
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
