package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;

public class Leuchte extends Building implements Attacker {

	private static PImage standingImg;
	static PImage healImg;
	static PImage buffImg;

	MeleeAttack heal;
	MeleeAttack buff;
	Ability timer;
	Upgrade upgrade = Upgrade.STANDARD;
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
		standingImg = game.ImageHandler.load(path, "Leuchte");
		healImg = game.ImageHandler.load(path, "HeilLeuchte");
		buffImg = game.ImageHandler.load(path, "DestruktorLeuchte");
	}

	public Leuchte(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		heal = new MeleeAttack(healImg, 500);
		buff = new MeleeAttack(buffImg, 500);
		death = new Death(standingImg, 500);
		timer = new Ability(standingImg, 100);

		setAnimation(stand);

		// ************************************
		setxSize(10);
		setySize(10);
		setHeight(5);

		kerit = 0;
		pax = 0;
		arcanum = 0;
		prunam = 0;

		timer.cooldown = 30000;
		timer.startCooldown();
		setHp(hp_max = 50);
		setRadius(3);
		setSight(127);
		groundPosition = GameObject.GroundPosition.GROUND;

		heal.range = getSight();
		heal.damage = 2;// heal
		heal.pirce = -1;// heal
		heal.cooldown = 2400;
		heal.setCastTime(300);

		buff.range = getSight();
		buff.damage = 3;// heal
		buff.pirce = -1;// heal
		buff.cooldown = 2400;
		buff.setCastTime(300);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (upgrade == Upgrade.HEAL) {
			heal.setTargetFrom(this, this);
			heal.updateAbility(this, isServer);
		} else if (upgrade == Upgrade.BUFF) {
			buff.setTargetFrom(this, this);
			buff.updateAbility(this, isServer);
		}
		timer.updateAbility(this, isServer);
		if (timer.isNotOnCooldown()) {
			if (upgrade == Upgrade.STANDARD) {
				sendAnimation("death");
			} else if (upgrade == Upgrade.HEAL) {
				sendAnimation("stand");
			} else if (upgrade == Upgrade.BUFF) {
				sendAnimation("heal");
			}
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("stand")) {
			if (upgrade != Upgrade.STANDARD) {
				iconImg = standingImg;
				timer.startCooldown();
				hp_max = 50;
				setHp(hp_max);
			}
			upgrade = Upgrade.STANDARD;
			setAnimation(stand);
		} else if (c[2].equals("heal")) {
			if (upgrade != Upgrade.HEAL) {
				iconImg = healImg;
				timer.startCooldown();
				hp_max = 150;
				setHp(hp_max);
			}
			upgrade = Upgrade.HEAL;
			setAnimation(heal);
		} else if (c[2].equals("buff")) {
			if (upgrade != Upgrade.BUFF) {
				iconImg = buffImg;
				timer.startCooldown();
				hp_max = 300;
				setHp(hp_max);
			}
			upgrade = Upgrade.BUFF;
			setAnimation(buff);
		}
	}

	@Override
	public boolean isCollidable(GameObject entity) {
		return !entity.isAllyTo(this);
	}

	@Override
	public void calculateDamage(Attack a) {
		for (GameObject e : GameApplet.updater.gameObjects) {
			if (e != null && e.isAllyTo(this)
					&& e.isInRange(getX(), getY(), e.getRadius() + a.range)) {
				GameApplet.updater.send("<heal " + e.number + " " + heal.damage);
			}
		}
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (upgrade == Upgrade.HEAL) {
			sendAnimation("heal");
		} else if (upgrade == Upgrade.BUFF) {
			sendAnimation("buff");
		} else
			sendAnimation("stand");
	}

	@Override
	public Attack getBasicAttack() {
		if (upgrade == Upgrade.HEAL) {
			return heal;
		} else if (upgrade == Upgrade.BUFF) {
			return buff;
		}
		return null;
	}

	@Override
	public void renderUnder() {
		drawShadow();
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
		drawTaged();
	}

	@Override
	public void display() {
		super.display();
		drawBar(1 - timer.getCooldownPercent());
	}

	@Override
	public PImage preview() {
		return standingImg;
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
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getHp() / hp_max, h);
			GameApplet.app.tint(255);
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

	enum Upgrade {
		STANDARD, HEAL, BUFF
	}
}
