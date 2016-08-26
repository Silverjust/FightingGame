package entity.robots;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.actives.MultiCDActive;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.Explosion;
import gameStructure.animation.MeleeAttack;
import gameStructure.animation.ShootAttack;

public class W4SP extends Unit implements Attacker, Shooter {

	float speedingSpeed;

	private static PImage standingImg;

	byte aggroRange;

	ShootAttack basicAttack, basicAttack2;

	private byte splashrange;

	private MeleeAttack speeding;

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
		standingImg = game.ImageHandler.load(path, "W4SP");
	}

	public W4SP(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = walk = new Animation(standingImg, 1000);
		// walk = new Animation(standingImg, 1000);
		speeding = new MeleeAttack(standingImg, 1000);// for 2 cooldowns
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		basicAttack2 = new ShootAttack(standingImg, 800);
		basicAttack.explosion = new Explosion(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(40);
		setySize(40);
		setHeight(30);

		kerit = 200;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 5000;

		setHp(hp_max = 100);
		armor = 3;
		setSpeed(0.8f);
		setRadius(5);
		setSight(70);
		groundPosition = GameObject.GroundPosition.AIR;

		aggroRange = 60;
		splashrange = 10;
		basicAttack.damage = 60;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 3000;
		basicAttack.range = 45;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;
		basicAttack.targetable = GroundPosition.GROUND;

		speedingSpeed = 1.5f;
		speeding.setCastTime(1800);
		speeding.cooldown = 20000;

		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& ((getAnimation() == walk && isMoving()) && isAggro || (getAnimation() == stand && !isMoving()))) {
			// ****************************************************
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
							if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius()))
								isEnemyInHitRange = true;
						}

					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		if (speeding.isSetup() && speeding.isEvent()) {
			System.out.println("W4SP.updateDecisions()");
			speeding.setTargetFrom(null, null);
			sendAnimation("speeddown");
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("speed")) {
			if (speeding.isNotOnCooldown()) {
				armor = 5;
				setSpeed(speedingSpeed);
				speeding.setTargetFrom(this, this);
				speeding.setup(this);
			}
		} else if (c[2].equals("speeddown")) {
			armor = 3;
			setSpeed(speedingSpeed);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		// ref.updater.send("<hit " + basicAttack.getTarget().number + " "
		// + a.damage + " " + a.pirce);
		GameObject target = ((ShootAttack) a).getTarget();
		for (GameObject e : ref.updater.gameObjects) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.getX(), target.getY(), e.getRadius() + splashrange)
					&& e.groundPosition == GroundPosition.GROUND) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void display() {
		super.display();
		if (speeding.isSetup() && !speeding.isEvent())
			drawBar(speeding.getProgressPercent());
	}

	@Override
	public void renderAir() {
		drawSelected();
		if (speeding.isSetup() && !speeding.isEvent()) {
			if (getAnimation() == stand)
				speeding.draw(this, direction, getCurrentFrame());
			else if (getAnimation() == basicAttack)
				basicAttack2.draw(this, direction, getCurrentFrame());
		} else
			getAnimation().draw(this, direction, getCurrentFrame());
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		ref.app.fill(255, 100, 0);
		ref.app.strokeWeight(0);
		// if (progress < 0.9) {
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		// } else {
		// ref.app.ellipse(xToGrid(x), yToGrid(y), splashrange * 2,
		// splashrange);
		// }
		ref.app.strokeWeight(1);
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	public Attack getAbility() {
		return speeding;
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

	public static class SpeedActive extends MultiCDActive {
		public SpeedActive(int x, int y, char n) {
			super(x, y, n, standingImg);
			setClazz(W4SP.class);
			setAbilityGetter("getAbility");
		}

		@Override
		public String getDescription() {
			return "speeds up and§gives armor";
		}

		@Override
		public void onActivation() {
			for (GameObject e : ref.updater.selected) {
				if (e instanceof W4SP) {
					e.sendAnimation("speed");
				}
			}
		}
	}
}