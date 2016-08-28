package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import game.AimHandler;
import game.ImageHandler;
import game.AimHandler.Cursor;
import game.GameBaseApp;
import game.aim.Aim;
import gameStructure.Spell;
import gameStructure.actives.MultiCDActive;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class BioLab extends Lab {

	private static PImage standingImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standingImg = ImageHandler.load(path, "BioLab");
	}

	public Ability swampify;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	public BioLab(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		swampify = new Ability(standingImg, 1000);

		setAnimation(walk);

		// ************************************
		kerit = 600;
		pax = 50;
		arcanum = 0;
		prunam = 0;
		trainTime = TRAINTIME;

		swampify.cooldown = 20000;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void renderUnder() {
		super.renderUnder();
		if (isAlive() && AimHandler.getAim() instanceof SwampAim
				&& swampify.isNotOnCooldown()) {
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, selectedImg, xToGrid(getX()),
					yToGrid(getY()), equipRange * 2, equipRange);
			GameBaseApp.app.tint(255);
		}
	}

	public Ability getSwampify() {
		return swampify;
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
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getCurrentHp() / hp_max, h);
			GameBaseApp.app.tint(255);
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

	public static class CreateSwampActive extends MultiCDActive {
		GameObject builder = null;
		private int acooldown, adamage;

		public CreateSwampActive(int x, int y, char n) {
			super(x, y, n, new Swamp(null).iconImg);
			Swamp swamp = new Swamp(null);
			acooldown = swamp.getBasicAttack().cooldown;
			adamage = swamp.getBasicAttack().damage;
			setClazz(BioLab.class);
			setAbilityGetter("getSwampify");
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (getClazz().isAssignableFrom(e.getClass())) {
					builder = e;
				}
			}
			if (builder != null) {
				AimHandler.setAim(new SwampAim(this, builder));
			}
		}

		@Override
		public String getDescription() {
			return "creates a swamp§damaging ground units";
		}

		@Override
		public String getStatistics() {
			return super.getStatistics() + "dps: " + adamage + "/"
					+ (acooldown / 1000.0) + " ="
					+ PApplet.nfc(adamage / (acooldown / 1000.0f), 2) + " (0)";
		}
	}

	public static class SwampAim extends Aim {

		private GameObject builder;
		private Spell active;

		public SwampAim(Spell active, GameObject builder) {
			this.active = active;
			this.builder = builder;
		}

		@Override
		public Cursor getCursor() {
			return Cursor.SHOOT;
		}

		@Override
		public void execute(float x, float y) {
			if (canPlaceAt(x, y)) {
				GameBaseApp.updater.send("<spawn " + Swamp.class.getSimpleName() + " "
						+ builder.player.getUser().ip + " " + x + " " + y);
				active.startCooldown();
				((BioLab) builder).getSwampify().startCooldown();
			}
		}

		protected boolean canPlaceAt(float x, float y) {
			boolean inLabRange = false;
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e instanceof BioLab && e.player == builder.player
						&& e.isInRange(x, y, ((BioLab) e).equipRange)
						&& ((BioLab) e).swampify.isNotOnCooldown()) {
					inLabRange = true;
				}
			}
			return inLabRange;
		}
	}
}