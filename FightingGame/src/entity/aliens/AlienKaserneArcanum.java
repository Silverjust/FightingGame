package entity.aliens;

import processing.core.PGraphics;
import processing.core.PImage;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.Trainer;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Training;

public class AlienKaserneArcanum extends Building implements Commander, Trainer {
	private int commandRange;
	protected float xTarget;
	protected float yTarget;

	private Training training;

	private static PImage standImg;
	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "AlienKaserneArcanum");

	}

	public AlienKaserneArcanum(String[] c) {
		super(c);
		AlienMainBuilding.getGround();

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 1000);
		training = new Training(standImg, 100);

		setAnimation(build);
		setupTarget();
		// ************************************
		setxSize(40);
		setySize(40);

		kerit = 600;
		pax = 0;
		arcanum = 40;
		prunam = 0;
		build.setBuildTime(10000);

		setSight(50);

		setHp(hp_max = 1000);
		setRadius(18);

		commandRange = 250;

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
		ImageHandler.drawImage(GameApplet.app, AlienMainBuilding.groundImg, xToGrid(getX()),
				yToGrid(getY()), commandRange * 2, commandRange);
	}

	@Override
	public void renderUnder() {
		if (isSelected&& isAlive()) {
			GameApplet.app.stroke(player.color);
			GameApplet.app.line(xToGrid(getX()), yToGrid(getY()), xToGrid(xTarget),
					yToGrid(yTarget));
			GameApplet.app.stroke(0);
		}
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, getX(), getY(), commandRange * 2,
				commandRange * 2);
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
		return commandRange;
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
