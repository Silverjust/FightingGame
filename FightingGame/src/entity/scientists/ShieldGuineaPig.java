package entity.scientists;

import processing.core.PApplet;
import processing.core.PImage;
import shared.ref;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import gameStructure.animation.ShootAttack;

public class ShieldGuineaPig extends Unit implements Attacker, Shooter {

	private static PImage standingImg;
	private static PImage shieldImg;

	byte aggroRange;
	byte shield;
	byte shield_max;

	ShootAttack basicAttack;
	MeleeAttack regenerate;
	MeleeAttack explode;
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
		standingImg = game.ImageHandler.load(path, "ShieldGuineaPig");
		shieldImg = game.ImageHandler.load(path, "Shield");
	}

	public ShieldGuineaPig(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);

		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		regenerate = new MeleeAttack(shieldImg, 800);// shield regeneration
		explode = new MeleeAttack(shieldImg, 1500);// shield break

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);

		kerit = 120;
		pax = 0;
		arcanum = 50;
		prunam = 0;
		trainTime = 1500;

		shield_max = 60;
		setHp(hp_max = 100);
		armor = 1;
		setSpeed(0.9f);
		setRadius(7);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.damage = 7;
		basicAttack.pirce = 0;
		basicAttack.cooldown = 1500;
		basicAttack.range = 40;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		explode.pirce = 0;
		explode.cooldown = 0;
		explode.range = 15;
		explode.setCastTime(100);

		regenerate.damage = 6;
		regenerate.pirce = -2;
		regenerate.cooldown = 1500;
		regenerate.range = 1;
		regenerate.setCastTime(0);
		regenerate.setTargetFrom(this, this);

		descr = " unit with shield";
		stats = "shield: " + shield_max + " +" + regenerate.damage + "/"
				+ (regenerate.cooldown / 1000.0) 
				+ " ="
				+ PApplet.nfc(regenerate.damage / (regenerate.cooldown / 1000.0f), 2);
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer
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
		}
		if (regenerate.isNotOnCooldown()) {
			regenerate.setTargetFrom(this, this);
			regenerate.setup(this);
		}
		if (getCurrentHp() == hp_max)
			regenerate.updateAbility(this, isServer);
		explode.updateAbility(this, isServer);
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		if (a == basicAttack || a == regenerate) {
			ref.updater.send("<hit " + ((MeleeAttack) a).getTarget().number
					+ " " + a.damage + " " + a.pirce);
			// SoundHandler.startIngameSound(HUD.hm, x, y);
		} else if (a == explode) {
			for (GameObject e : ref.updater.gameObjects) {
				if (e != null & e.isEnemyTo(this)
						&& e.isInRange(getX(), getY(), e.getRadius() + a.range)
						&& e.groundPosition == GroundPosition.GROUND) {
					ref.updater.send("<hit " + e.number + " " + a.damage + " "
							+ a.pirce);
				}
			}
		}
	}

	@Override
	public void hit(int damage, byte pirce) {
		if (isMortal()) {
			if (pirce == -2) {
				shield += damage;
				if (shield > shield_max)
					shield = shield_max;
			} else if (shield > 0) {
				shield -= damage;
				if (shield < 0) {
					setHp(getCurrentHp() + shield * 2);
					sendAnimation("explode " + -shield * 2);
					shield = 0;
				}
			} else {
				setHp(getCurrentHp() - damage
						* (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.05));
				/** check if it was lasthit */
				if (getCurrentHp() <= 0 && getCurrentHp() != Integer.MIN_VALUE) {// marker
					setHp(Integer.MIN_VALUE);
					onDeath();
				}
			}

		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if ("explode".equals(c[2])) {
			byte n = Byte.parseByte(c[3]);
			explode.damage = n;
			explode.setTargetFrom(this, this);
			explode.setup(this);
		}
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		if (shield > 0) {
			ref.app.tint(255, ((float) shield / shield_max * 255f));
			regenerate.draw(this, direction, getCurrentFrame());
			ref.app.tint(255);
		}
		if (explode.isSetup() && !explode.isEvent()) {
			explode.draw(this, direction, getCurrentFrame());
		}
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
		ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
		ref.app.strokeWeight(1);
	}

	@Override
	public void display() {
		super.display();
		drawBar((float) shield / shield_max, ref.app.color(255, 50, 0));
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