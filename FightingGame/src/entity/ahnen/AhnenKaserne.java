package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Nation;
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

public class AhnenKaserne extends Building implements Trainer, Commander {
	protected float xTarget;
	protected float yTarget;

	byte level = 0;

	private Training training;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	private static PImage standImg;
	private static PImage levelSym;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standImg = ImageHandler.load(path, "AhnenKaserne");
		levelSym = ImageHandler.load(Nation.AHNEN.toString() + "/symbols/",
				"levelSym");
	}

	public AhnenKaserne(String[] c) {
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
		build.setBuildTime(3000);

		animation.setSight(50);

		setHp(hp_max = 1000);
		stats.setRadius(15);

		descr = " ";
		stats = "level 1";
		// ************************************
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("level")) {
			level = (byte) Integer.parseInt(c[3]);
			stats = "level " + (level + 1);
			GameApplet.GameBaseApp.selectionChanged = true;
			GameApplet.GameBaseApp.keepGrid = true;
			setAnimation(build);
		} else if (c[2].equals("train")) {
			boolean b = false;
			if (level >= 0 && c[3].equals("Berserker"))
				b = true;
			if (level >= 1 && c[3].equals("Witcher"))
				b = true;
			if (level >= 2 && c[3].equals("Warrior"))
				b = true;
			if (level >= 3 && c[3].equals("Angel"))
				b = true;
			if (level >= 4
					&& (c[3].equals("Astrator") || c[3].equals("Destructor")))
				b = true;
			if (b) {
				Training.updateExecTraining(c, this);
			}
		} else if (c[2].equals("setTarget")) {
			Training.updateExecTraining(c, this);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
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

	@Override
	// TODO remove this and add depot
	public int commandRange() {
		return 250;
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
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getHp() / hp_max, h);
			GameBaseApp.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - stats.getRadius() - e.getStats().getRadius()));
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

	public static class LevelActive extends Spell {
		public LevelActive(int x, int y, char n) {
			super(x, y, n, levelSym);
			setClazz(AhnenKaserne.class);
		}

		@Override
		public String getDescription() {
			AhnenKaserne target = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof AhnenKaserne) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null) {
				if (target.level == 4)
					return "fully upgraded";
				return "upgrade to level " + (target.level + 2);
			}
			return "upgrade level";
		}

		@Override
		public String getStatistics() {
			AhnenKaserne target = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof AhnenKaserne) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null) {
				return "kerit: " + getCosts(target);
			}
			return " ";
		}

		@Override
		public void onActivation() {
			AhnenKaserne target = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof AhnenKaserne && e.getAnimation() == e.stand) {
					target = (AhnenKaserne) e;
				}
			}
			if (target != null && target.level < 4
					&& target.player.canBy(getCosts(target), 0, 0, 0)) {
				target.buyFrom(target.player, getCosts(target), 0, 0, 0);
				target.sendAnimation("level " + (target.level + 1), this);
			}
		}

		@Override
		public boolean isActivateable() {
			AhnenKaserne target = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof AhnenKaserne) {
					target = (AhnenKaserne) e;
				}
			}
			return target != null && target.level < 4;
		}

		private int getCosts(AhnenKaserne target) {
			if (target.level == 4)
				return 0;
			return 100 + target.level * 20;
		}
	}

	public static class AhnenTrainActive extends TrainActive {

		public AhnenTrainActive(int x, int y, char n, GameObject u,
				Class<? extends GameObject> trainer) {
			super(x, y, n, (Unit) u, trainer);
		}

		public void onActivation() {
			AhnenKaserne trainer = null;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (clazz.isAssignableFrom(e.getClass())
						&& e.getAnimation() == e.stand) {
					boolean b = false;
					AhnenKaserne t = (AhnenKaserne) e;
					if (t.level >= 0 && unit.equals(Berserker.class))
						b = true;
					if (t.level >= 1 && unit.equals(Witcher.class))
						b = true;
					if (t.level >= 2 && unit.equals(Warrior.class))
						b = true;
					if (t.level >= 3 && unit.equals(Angel.class))
						b = true;
					if (t.level >= 4
							&& (unit.equals(Astrator.class) || unit
									.equals(Destructor.class)))
						b = true;
					if (b)
						trainer = t;
				}
			}

			GameObject toTrain = null;
			try {
				toTrain = unit.getConstructor(String[].class).newInstance(
						new GameObject[] { null });
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (trainer != null && toTrain != null
					&& toTrain.canBeBought(trainer.player)) {
				toTrain.buyFrom(trainer.player);
				trainer.sendAnimation("train " + unit.getSimpleName(), this);

			}
		}

		@Override
		public boolean isActivateable() {
			boolean isActivateable = false;
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (clazz.isAssignableFrom(e.getClass())) {
					boolean b = false;
					AhnenKaserne t = (AhnenKaserne) e;
					if (t.level >= 0 && unit.equals(Berserker.class))
						b = true;
					if (t.level >= 1 && unit.equals(Witcher.class))
						b = true;
					if (t.level >= 2 && unit.equals(Warrior.class))
						b = true;
					if (t.level >= 3 && unit.equals(Angel.class))
						b = true;
					if (t.level >= 4
							&& (unit.equals(Astrator.class) || unit
									.equals(Destructor.class)))
						b = true;
					if (b)
						isActivateable = true;
				}
			}
			return isActivateable;
		}
	}
}
