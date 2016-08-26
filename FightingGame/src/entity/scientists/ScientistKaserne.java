package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.ImageHandler;
import gameStructure.Spell;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.TrainActive;
import gameStructure.Trainer;
import gameStructure.Unit;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Training;

public class ScientistKaserne extends Building implements Commander, Trainer,
		Equiping {
	private int commandingRange;
	protected float xTarget;
	protected float yTarget;

	private Ability training;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standImg = ImageHandler.load(path, "ScientistKaserne");
	}

	public ScientistKaserne(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
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

		setSight(50);

		setHp(hp_max = 1000);
		setRadius(15);

		commandingRange = 250;

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
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
		for (GameObject e : ref.player.visibleEntities) {
			if (e instanceof Lab && e.isAllyTo(e) && e.isAlive()
					&& e.isInRange(getX(), getY(), commandingRange)) {
				ref.app.stroke(100);
				ref.app.line(xToGrid(getX()), yToGrid(getY() - getySize()), e.getX(),
						(e.getY() - e.flyHeight()) / 2);
				ref.app.stroke(0);
			}
		}
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
		return commandingRange;
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

	public void hit(int damage, byte pirce) {
	
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);
	
			setHp((int) (getHp() - damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.1)));
			/** check if it was lasthit */
			if (getHp() <= 0 && getHp() != Integer.MAX_VALUE) {// marker
				setHp(-32768);
				onDeath();
			}
	
		}
	}

	public void heal(int heal) {
		setHp(getHp() + heal);
		/** check if it was overheal */
		if (getHp() > hp_max) {
			setHp(hp_max);
		}
	}

	protected void onDeath() {
		sendAnimation("death");
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			ref.app.fill(0, 150);
			ref.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			ref.app.tint(player.color);
			ImageHandler.drawImage(ref.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getHp() / hp_max, h);
			ref.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - getRadius() - e.getRadius()));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public boolean isAlive() {
		if (isMortal())
			return (getAnimation().getClass() != death.getClass()) && getHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void initHp(int hp) {
		this.hp = hp;
		this.hp_max = hp;
	}

	public static class EquipActive extends Spell {
		Class<? extends Unit> unit;
		Class<?> lab;
		String descr = " ", stats = " ";

		public EquipActive(int x, int y, char n, GameObject u,
				Class<? extends GameObject> trainerHeper) {

			super(x, y, n, u.iconImg);
			setClazz(Equiping.class);
			lab = trainerHeper;
			unit = ((Unit) u).getClass();
			descr = u.getDescription();
			stats = u.getStatistics();
		}

		@Override
		public void onActivation() {
			ScientistKaserne trainer = null;
			GameObject trained = null;
			for (GameObject e : ref.updater.selected) {
				if (e instanceof ScientistKaserne && e.isAlive()) {
					for (GameObject e2 : ref.player.visibleEntities) {
						if (e2 instanceof Lab
								&& e2.getClass().equals(lab)
								&& e2.isAllyTo(e)
								&& e2.isAlive()
								&& e2.isInRange(e.getX(), e.getY(),
										((ScientistKaserne) e).commandingRange)) {
							trainer = (ScientistKaserne) e;

						}
					}
				}
			}
			if (trainer != null) {
				for (GameObject e : ref.player.visibleEntities) {
					if (e instanceof GuineaPig
							&& e.isAlive()
							&& e.isAllyTo(trainer)
							&& e.isInRange(trainer.getX(), trainer.getY(),
									trainer.commandingRange)) {
						trained = e;
					}
				}
			}
			Unit newUnit = null;
			try {
				newUnit = unit.getConstructor(String[].class).newInstance(
						new GameObject[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (trained != null && newUnit != null
					&& newUnit.canBeBought(trained.player)) {
				newUnit.buyFrom(trained.player);
				trained.sendAnimation("equip " + unit.getSimpleName());
			}
		}

		@Override
		public String getDescription() {
			return descr;
		}

		@Override
		public String getStatistics() {
			return stats;
		}
	}

	@Deprecated
	public static class ScientistTrainActive extends TrainActive {

		private Class<? extends GameObject> lab;

		public ScientistTrainActive(int x, int y, char n, GameObject u,
				Class<? extends GameObject> trainerHeper) {
			super(x, y, n, (Unit) u, ScientistKaserne.class);
			lab = trainerHeper;
		}

		public void onActivation() {
			GameObject trainer = null;
			for (GameObject e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& e.getAnimation() == e.stand) {
					for (GameObject e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.getX(), e2.getY(), e.getRadius()
										+ ((Lab) e2).equipRange)) {
							trainer = e;
						}
					}
				}
			}

			GameObject toTrain = null;
			try {
				toTrain = unit.getConstructor(String[].class).newInstance(
						new GameObject[] { null });
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (trainer != null && toTrain != null
					&& toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				trainer.sendAnimation("train " + unit.getSimpleName());

			}
		}

		@Override
		public boolean isActivateable() {
			boolean isActivateable = false;
			for (GameObject e : ref.updater.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					for (GameObject e2 : ref.player.visibleEntities) {
						if (e2.player == e.player
								&& e2.getClass().equals(lab)
								&& e.isInRange(e2.getX(), e2.getY(), e.getRadius()
										+ ((Lab) e2).equipRange)) {
							isActivateable = true;
						}
					}
				}
			}
			return isActivateable;
		}
	}
}
