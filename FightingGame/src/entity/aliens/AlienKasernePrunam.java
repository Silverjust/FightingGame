package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import shared.GameBaseApp;
import game.ImageHandler;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.Trainer;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Training;

public class AlienKasernePrunam extends Building implements Commander, Trainer {
	private int commanderRange;
	protected float xTarget;
	protected float yTarget;

	private Training training;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "AlienKasernePrunam");
	}

	public AlienKasernePrunam(String[] c) {
		super(c);
		AlienMainBuilding.getGround();

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 6000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		setxSize(40);
		setySize(40);

		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		build.setBuildTime(10000);

		setSight(50);

		setHp(hp_max = 1000);
		setRadius(18);

		commanderRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Training.updateExecTraining(c, this);
	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(GameBaseApp.app, AlienMainBuilding.groundImg, xToGrid(getX()),
				yToGrid(getY()), commanderRange * 2, commanderRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, getX(), getY(), commanderRange * 2,
				commanderRange * 2);
	}

	@Override
	public void renderUnder() {
		if (isSelected && isAlive()) {
			GameBaseApp.app.stroke(player.color);
			GameBaseApp.app.line(xToGrid(getX()), yToGrid(getY()), xToGrid(xTarget),
					yToGrid(yTarget));
			GameBaseApp.app.stroke(0);
		}
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
	public int commandRange() {
		return commanderRange;
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
