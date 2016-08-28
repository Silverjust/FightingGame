package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Nation;
import game.GameBaseApp;
import game.ImageHandler;
import gameStructure.Spell;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import gameStructure.animation.ShootAttack;

public class ANT10N extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	private static PImage anchorSym;

	byte aggroRange;
	boolean isAnchored;

	MeleeAttack heal;

	private ShootAttack basicAttack;

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
		standingImg = game.ImageHandler.load(path, "ANT10N");
		anchorSym = ImageHandler.load(Nation.ROBOTS.toString() + "/symbols/",
				"anchor");
	}

	public ANT10N(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		heal = new MeleeAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(30);
		setySize(30);
		setHeight(5);

		kerit = 300;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		trainTime = 4000;

		setHp(hp_max = 200);
		armor = 1;
		setSpeed(0.7f);
		setRadius(10);
		setSight(90);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = 110;
		basicAttack.damage = 20;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 900;
		basicAttack.range = 30;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;

		heal.range = (byte) (getRadius() + 25);
		heal.damage = 25;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 5000;
		heal.setCastTime(100);
		heal.doRepeat = true;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& !isAnchored
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())
								&& basicAttack.canTargetable(e)) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
						}
						if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())
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
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		} else if (isServer && isAnchored) {// ****************************************************
			if (heal.isNotOnCooldown()) {
				sendAnimation("heal");
			}
		}
		basicAttack.updateAbility(this, isServer);
		heal.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			setMoving(false);
			setHeight(0);
		} else if (c[2].equals("walk")) {
			isAnchored = false;
			setHeight(5);
		} else if (c[2].equals("heal")) {
			if (heal.isNotOnCooldown() && !heal.isSetup()) {
				heal.setTargetFrom(this, this);
				setAnimation(heal);
			}
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		if (a == heal) {
			for (GameObject e : GameApplet.GameBaseApp.gameObjects)
				if (e != null && e.isAllyTo(this)
						&& e.isInRange(getX(), getY(), e.getRadius() + a.range))
					GameBaseApp.updater.send("<heal " + e.number + " " + heal.damage);
		} else
			GameBaseApp.updater.send("<hit " + basicAttack.getTarget().number + " "
					+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		heal.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		GameBaseApp.app.fill(0, 255, 0);
		GameBaseApp.app.strokeWeight(0);
		GameBaseApp.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		GameBaseApp.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(heal.range);
		drawCircle((int) (heal.range * heal.getCooldownPercent()));
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

	public static class AnchorActive extends Spell {
		public AnchorActive(int x, int y, char n) {
			super(x, y, n, anchorSym);
			setClazz(ANT10N.class);
		}

		@Override
		public String getDescription() {
			return "anchor to heal";
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof ANT10N) {
					e.sendAnimation("anchor");
				}
			}
		}
	}

}