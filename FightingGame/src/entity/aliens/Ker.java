package entity.aliens;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import gameStructure.animation.ShootAttack;

public class Ker extends Unit implements Shooter {

	private static PImage standingImg;

	byte aggroRange;

	MeleeAttack basicAttack;
	ShootAttack shoot;

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
		standingImg = game.ImageHandler.load(path, "Ker");
	}

	public Ker(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new MeleeAttack(standingImg, 800);
		shoot = new ShootAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 335;
		pax = 0;
		arcanum = 0;
		prunam = 60;
		trainTime = 10000;

		setHp(hp_max = 300);
		setSpeed(0.9f);
		setRadius(8);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = 100;
		basicAttack.range = (byte) (getRadius() + 10);
		basicAttack.damage = 112;
		basicAttack.pirce = 5;
		basicAttack.cooldown = 700;
		basicAttack.setCastTime(500);
		basicAttack.targetable = groundPosition;

		shoot.range = 90;
		shoot.damage = 120;
		shoot.pirce = 3;
		shoot.cooldown = 2000;
		shoot.setCastTime(500);
		shoot.speed = 1;

		descr = " ";
		stats = "dps: " + shoot.damage + "/" + shoot.cooldown / 1000.0 + " ("
				+ shoot.pirce + ")" + " ="
				+ PApplet.nfc(shoot.damage / (shoot.cooldown / 1000.0f), 2) + " _°§";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
				&& (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			boolean isEnemyInShootRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius())) {
							float newImportance = calcImportanceOf(e);
							if (newImportance > importance) {
								importance = newImportance;
								importantEntity = e;
							}
							if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())
									&& basicAttack.canTargetable(e))
								isEnemyInHitRange = true;
							if (e.isInRange(getX(), getY(), shoot.range + e.getRadius())
									&& shoot.canTargetable(e))
								isEnemyInShootRange = true;
						}

					}
				}
			}
			if (isEnemyInHitRange && basicAttack.isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			} else if (isEnemyInShootRange && shoot.isNotOnCooldown()) {
				sendAnimation("shoot " + importantEntity.number);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(
						this,
						importantEntity,
						importantEntity.groundPosition == GroundPosition.AIR ? shoot.range
								: basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		shoot.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("shoot") && shoot.isNotOnCooldown()) {
			int n = Integer.parseInt(c[3]);
			GameObject e = ref.updater.getNamedObjects().get(n);
			shoot.setTargetFrom(this, e);
			setAnimation(shoot);
			setMoving(false);
		}

	}

	@Override
	public void calculateDamage(Attack a) {
		ref.updater.send("<hit " + a.getTarget().number + " "
				+ (a.getTarget() instanceof Building ? a.damage / 2 : a.damage)
				+ " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		basicAttack.drawAbility(this, direction);

		drawTaged();
	}

	@Override
	public void renderRange() {
		super.renderRange();
		drawCircle(shoot.range);
		drawCircle((int) (shoot.range * shoot.getCooldownPercent()));
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		ref.app.fill(200, 255, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 2, 2);
		ref.app.strokeWeight(1);
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

}