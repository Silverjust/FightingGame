package entity.scientists;

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
import gameStructure.animation.ShootAttack;

public class AirshipGuineaPig extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	private static PImage anchorSym;

	byte aggroRange;
	boolean isAnchored;
	ShootAttack basicAttack;

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
		standingImg = game.ImageHandler.load(path, "AirshipGuineaPig");
		anchorSym = ImageHandler.load(Nation.SCIENTISTS.toString() + "/symbols/",
				"anchor");
	}

	public AirshipGuineaPig(String[] c) {
		super(c);
		GuineaPig.setupEquip(this, c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);

		setAnimation(walk);

		// ************************************
		setxSize(20);
		setySize(20);
		setHeight(25);

		kerit = 800;
		pax = 0;
		arcanum = 150;
		prunam = 1;
		trainTime = 1500;

		setHp(hp_max = 700);
		armor = 4;
		setSpeed(1.2f);
		setRadius(9);
		setSight(70);
		groundPosition = GameObject.GroundPosition.AIR;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.damage = 50;
		basicAttack.pirce = 3;
		basicAttack.cooldown = 5000;
		basicAttack.range = 120;
		basicAttack.setCastTime(200);// eventtime is defined by target distance
		basicAttack.speed = 1f;
		basicAttack.targetable = groundPosition;
		descr = " ";
		stats = " ";
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
		basicAttack.updateAbility(this, isServer);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("anchor")) {
			isAnchored = true;
			setHeight(5);
			setMoving(false);
			groundPosition = GroundPosition.GROUND;
			basicAttack.targetable = groundPosition;
			setAnimation(stand);
		} else if (c[2].equals("walk")) {
			isAnchored = false;
			setHeight(25);
			groundPosition = GroundPosition.AIR;
			basicAttack.targetable = groundPosition;
		}
	}

	@Override
	public void sendDefaultAnimation(Animation oldAnimation) {
		if (isAnchored) {
			sendAnimation("anchor");
		} else {
			sendAnimation("walk " + xTarget + " " + yTarget + " " + isAggro);
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		GameBaseApp.updater.sendDirect("<hit " + a.getTarget().number + " " + a.damage + " "
				+ a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderAir() {
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
		GameBaseApp.app.fill(255, 100, 0);
		GameBaseApp.app.strokeWeight(0);
		GameBaseApp.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
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
			setClazz(AirshipGuineaPig.class);
		}

		@Override
		public String getDescription() {
			return "anchor";
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof AirshipGuineaPig) {
					e.sendAnimation("anchor");
				}
			}
		}
	}
}