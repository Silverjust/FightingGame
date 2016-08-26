package entity.humans;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Build;
import gameStructure.animation.Death;
import gameStructure.animation.Explosion;
import gameStructure.animation.ShootAttack;

public class HumanDepot extends Building implements Commander, Shooter {
	private int commandingRange;
	ShootAttack basicAttack;
	private byte splashrange;
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
		standImg = ImageHandler.load(path, "HumanDepot");
	}

	public HumanDepot(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		basicAttack = new ShootAttack(standImg, 1000);
		basicAttack.explosion = new Explosion(standImg, 800);

		setAnimation(build);

		// ************************************
		setxSize(20);
		setySize(20);

		kerit = 500;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(5000);

		setSight(50);

		setHp(hp_max = 1000);
		setRadius(13);

		splashrange = 10;
		basicAttack.range = 70;
		basicAttack.damage = 15;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);// eventtime is defined by target distance
		basicAttack.speed = 0.5f;

		commandingRange = 250;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		float importance = 0;
		GameObject importantEntity = null;
		if (isServer && (getAnimation() == stand)) {// ****************************************************
			for (GameObject e : player.visibleEntities) {
				if (e.isEnemyTo(this)) {
					if (e.isInRange(getX(), getY(), basicAttack.range + e.getRadius())
							&& basicAttack.canTargetable(e)
							&& !(e instanceof Building)) {
						float newImportance = calcImportanceOf(e);
						if (newImportance > importance) {
							importance = newImportance;
							importantEntity = e;
						}
					}
				}
			}
			if (importantEntity != null && getBasicAttack().isNotOnCooldown()) {
				sendAnimation("basicAttack " + importantEntity.number);
			}
		}
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Attack.updateExecAttack(c, this);
	}

	@Override
	public void calculateDamage(Attack a) {
		GameObject target = ((ShootAttack) a).getTarget();
		for (GameObject e : ref.updater.gameObjects) {
			if (e != null & e.isEnemyTo(this)
					&& e.isInRange(target.getX(), target.getY(), e.getRadius() + splashrange)) {
				ref.updater.send("<hit " + e.number + " " + a.damage + " "
						+ a.pirce);
			}
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
		basicAttack.drawAbility(this, (byte) 0);
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(),
				progress);
		ref.app.fill(255, 100, 0);
		ref.app.strokeWeight(0);
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
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
