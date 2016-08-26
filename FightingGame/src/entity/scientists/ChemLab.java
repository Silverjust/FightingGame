package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.AimHandler;
import game.ImageHandler;
import game.aim.BuildWallAim;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.actives.BuildWallActive;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class ChemLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standingImg = ImageHandler.load(path, "ChemLab");
	}

	public int buildRange;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	public ChemLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);

		setAnimation(walk);

		// ************************************
		kerit = 600;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = TRAINTIME;

		buildRange = 200;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof ScientistWallAim) {
			ref.app.tint(player.color);
			ImageHandler.drawImage(ref.app, selectedImg, xToGrid(getX()),
					yToGrid(getY()), buildRange * 2, buildRange);
			ref.app.tint(255);
		}
	}

	public void hit(int damage, byte pirce) {
	
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);
	
			setHp((int) (getCurrentHp() - damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.1)));
			/** check if it was lasthit */
			if (getCurrentHp() <= 0 && getCurrentHp() != Integer.MAX_VALUE) {// marker
				setHp(-32768);
				onDeath();
			}
	
		}
	}

	public void heal(int heal) {
		setHp(getCurrentHp() + heal);
		/** check if it was overheal */
		if (getCurrentHp() > hp_max) {
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
					getRadius() * 2 * getCurrentHp() / hp_max, h);
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
			return (getAnimation().getClass() != death.getClass()) && getCurrentHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public int getCurrentHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void initHp(int hp) {
		this.hp = hp;
		this.hp_max = hp;
	}

	public static class ScientistWallActive extends BuildWallActive {

		public ScientistWallActive(int x, int y, char n) {
			super(x, y, n, new ScientistWall(null), ChemLab.class);
		}

		@Override
		public void onActivation() {
			GameObject builder = null;
			for (GameObject e : ref.updater.selected) {
				if (getClazz().isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}

			if (builder != null) {
				try {
					Building b = building.getConstructor(String[].class)
							.newInstance(new GameObject[] { null });
					AimHandler.setAim(new ScientistWallAim(builder, b));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class ScientistWallAim extends BuildWallAim {

		public ScientistWallAim(GameObject builder, GameObject building) {
			super(builder, building);
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean placeFree = true;
			boolean inLabRange = false;
			boolean inCommanderRange = false;

			for (GameObject e : ref.updater.gameObjects) {
				if (e.isInRange(x, y, buildable.getRadius() + e.getRadius())
						&& e.groundPosition == GroundPosition.GROUND)
					placeFree = false;
				if (e instanceof ChemLab && e.player == builder.player
						&& e.isInRange(x, y, ((ChemLab) e).buildRange)) {
					inLabRange = true;
				}
				if (isInCommandingRange(e, x, y))
					inCommanderRange = true;
			}
			return placeFree && (inLabRange || inCommanderRange);
		}
	}

}