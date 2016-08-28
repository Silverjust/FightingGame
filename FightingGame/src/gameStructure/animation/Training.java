package gameStructure.animation;

import game.GameBaseApp;
import gameStructure.GameObject;
import gameStructure.Trainer;
import gameStructure.Unit;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListManager;

public class Training extends Ability {
	GameObject toTrain;

	public Training(PImage[][] IMG, int duration) {
		super(IMG, duration);
	}

	public Training(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Training(PImage IMG, int duration) {
		super(IMG, duration);
	}

	@Override
	public void updateAbility(GameObject e, boolean isServer) {
		if (isSetup() && isNotOnCooldown()) {
			if (isServer) {
				float xt = ((Trainer) e).getXTarget();
				float yt = ((Trainer) e).getYTarget();
				GameBaseApp.updater.send("<spawn "
						+ toTrain.getClass().getSimpleName()
						+ " "
						+ e.player.getUser().ip
						+ " "
						+ (e.getX() + (xt - e.getX()) / PApplet.dist(e.getX(), e.getY(), xt, yt)
								* (e.getRadius() + toTrain.getRadius()))
						+ " "
						+ (e.getY() + (yt - e.getY()) / PApplet.dist(e.getX(), e.getY(), xt, yt)
								* (e.getRadius() + toTrain.getRadius()))//
						+ " " //
						+ xt //
						+ " " //
						+ yt);
			}
			toTrain = null;
		}
	}

	/*
	 * @Override public void update(Entity e) { if (isFinished()) { setup(e); }
	 * if (isNotOnCooldown()) { System.out.println("Training.update()end");
	 * e.sendDefaultAnimation(this); } }
	 */

	public void setEntity(GameObject toTrain) {
		startCooldown();
		this.toTrain = toTrain;
	}

	@Override
	public boolean isSetup() {
		return toTrain != null;
	}

	@Override
	public boolean doRepeat(GameObject e) {
		return !isNotOnCooldown();
	}

	@Override
	public boolean isInterruptable() {
		return isNotOnCooldown();
	}

	public static void updateExecTraining(String[] c, GameObject trainer) {
		if (c[2].equals("train") && trainer instanceof Trainer) {
			GameObject toTrain = null;
			Training a = (Training) ((Trainer) trainer).getTraining();
			try {
				String name = ContentListManager.getEntityContent().getString(
						c[3]);
				toTrain = (GameObject) Class.forName(name)
						.getConstructor(String[].class)
						.newInstance(new GameObject[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (toTrain != null && a.isNotOnCooldown()
					&& trainer.getAnimation().isInterruptable()) {
				a.cooldown = ((Unit) toTrain).trainTime;
				a.setEntity(toTrain);
				trainer.setAnimation(a);
			}

		} else if (c[2].equals("setTarget") && trainer instanceof Trainer) {
			float x = Float.parseFloat(c[3]);
			float y = Float.parseFloat(c[4]);
			((Trainer) trainer).setTarget(x, y);
		}
	}
}
