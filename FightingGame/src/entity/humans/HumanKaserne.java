package entity.humans;

import processing.core.PImage;
import game.ImageHandler;
import gameStructure.Building;
import gameStructure.Trainer;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Training;

public class HumanKaserne extends Building implements  Trainer {
	protected float xTarget;
	protected float yTarget;

	private Ability training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "HumanKaserne");
	}

	public HumanKaserne(String[] c) {
		super(c);
 
		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		animation.setSight(50);

		setHp(hp_max = 1000);
		getStats().setRadius(15);

		descr = " ";
		setStats(" ");
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Training.updateExecTraining(c, this);
	}	

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	@Override
	public void display() {
		super.display();
		if (getAnimation() == training)
			drawBar(training.getCooldownPercent());
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public Ability getTraining() {
		return training;
	}

	@Override
	public float getXTarget() {
		return xTarget;
	}

	@Override
	public float getYTarget() {
		return yTarget;
	}

	@Override
	public void setTarget(float x, float y) {
		xTarget = x;
		yTarget = y;

	}
}
