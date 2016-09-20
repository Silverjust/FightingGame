package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Nation;
import game.ImageHandler;
import gameStructure.Spell;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.ShootAttack;

public class SN41L10N extends Unit implements Shooter {

	private static PImage standingImg;
	private static PImage anchoredImg;
	private static PImage anchorSym;

	byte aggroRange;
	boolean isAnchored;

	ShootAttack basicAttack;
	private Animation anchored;
	private byte splashrange;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standingImg = game.ImageHandler.load(path, "SN41L10N");
		anchoredImg = game.ImageHandler.load(path, "SN41L10N_c");
		anchorSym = ImageHandler.load(Nation.ROBOTS.toString() + "/symbols/",
				"anchor");
	}

	public SN41L10N(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		anchored = new Animation(anchoredImg, 1000);
		basicAttack = new ShootAttack(anchoredImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(30);
		setySize(30);
		setHeight(5);

		kerit = 800;
		pax = 0;
		arcanum = 25;
		prunam = 0;
		trainTime = 4000;

		setHp(hp_max = 700);
		armor = 3;
		getStats.setSpeed(0.7f);
		stats.setRadius(8);
		animation.setSight(90);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = 120;
		splashrange = 10;
		basicAttack.damage = 40;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 2000;
		basicAttack.range = 40;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		descr = "~Shellion";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isAnchored && isServer) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getStats().getRadius())
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getStats().getRadius())
								&& basicAttack.canTargetable(e)) {
							isEnemyInHitRange = true;
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.getNumber(), this);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (isAnchored)
			sendAnimation("anchored", this);
		else
			super.sendDefaultAnimation(oldAnimation);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			getStats.setMoving(this, false);
			armor = 5;
			setAnimation(anchored);// later anchor?
		} else if (c[2].equals("anchored")) {
			getStats.setMoving(this, false);
			setAnimation(anchored);
		} else if (c[2].equals("walk")) {
			System.out.println("ANT10N.exec()");
			isAnchored = false;
			armor = 3;
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		GameObject target = ((ShootAttack) a).getTarget();
		for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.getX(), target.getY(), e.getStats().getRadius() + splashrange)
					&& a.canTargetable(e)) {
				GameBaseApp.getUpdater().sendDirect("<hit " + e.getNumber() + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		GameBaseApp.app.fill(0, 200, 255);
		GameBaseApp.app.strokeWeight(0);
		GameBaseApp.app.ellipse(xToGrid(x), yToGrid(y), 3, 3);
		GameBaseApp.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
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
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getCurrentHp() / hp_max, h);
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

	public static class AnchorActive extends Spell {
		public AnchorActive(int x, int y, char n) {
			super(x, y, n, anchorSym);
			setClazz(SN41L10N.class);
		}

		@Override
		public String getDescription() {
			return "anchor to heal";
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof SN41L10N) {
					e.sendAnimation("anchor", this);
				}
			}
		}
	}

}