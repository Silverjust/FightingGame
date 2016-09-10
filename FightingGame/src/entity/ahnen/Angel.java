package entity.ahnen;

import processing.core.PApplet;
import processing.core.PImage;
import shared.Coms;
import shared.GameBaseApp;
import shared.Nation;
import shared.Player;
import game.ImageHandler;
import gameStructure.Spell;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Shooter;
import gameStructure.Unit;
import gameStructure.animation.Ability;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.ShootAttack;

public class Angel extends Unit implements Attacker, Shooter {

	private static PImage standingImg;

	private static PImage cloakSym;

	byte aggroRange;
	boolean isCloaked;

	ShootAttack basicAttack;
	Ability cloak;

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
		standingImg = game.ImageHandler.load(path, "Angel");
		cloakSym = ImageHandler.load(Nation.AHNEN.toString() + "/symbols/", "cloak");
	}

	public Angel(String[] c) {
		super(c);
		iconImg = standingImg;

		stand = new Animation(standingImg, 1000);
		walk = new Animation(standingImg, 800);
		death = new Death(standingImg, 500);
		basicAttack = new ShootAttack(standingImg, 800);
		cloak = new Ability(standingImg, 1000);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);
		setHeight(30);

		kerit = 300;
		pax = 0;
		arcanum = 0;
		prunam = 10;
		trainTime = 4000;

		setHp(hp_max = 100);
		armor = 1;
		setSpeed(0.7f);
		setRadius(7);
		setSight(90);
		groundPosition = GameObject.GroundPosition.AIR;

		aggroRange = (byte) (getRadius() + 60);
		basicAttack.range = (byte) (getRadius() + 20);
		basicAttack.damage = 80;
		basicAttack.pirce = 1;
		basicAttack.cooldown = 3000;
		basicAttack.range = 25;
		basicAttack.setCastTime(100);// eventtime is defined by target distance
		basicAttack.speed = 0.6f;

		cloak.cooldown = 40000;
		cloak.doRepeat = true;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		if (isServer && (getAnimation() == walk && isAggro || getAnimation() == stand)) {// ****************************************************
			boolean isEnemyInHitRange = false;
			float importance = 0;
			GameObject importantEntity = null;
			for (GameObject e : player.visibleEntities) {
				if (e != this) {
					if (e.isEnemyTo(this)) {
						if (e.isInRange(getX(), getY(), aggroRange + e.getRadius()) && basicAttack.canTargetable(e)) {
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
				sendAnimation("basicAttack " + importantEntity.getNumber(), this);
			} else if (importantEntity != null) {
				Attack.sendWalkToEnemy(this, importantEntity, basicAttack.range);
			}
		}
		basicAttack.updateAbility(this, isServer);
		if (isServer && isCloaked && cloak.isNotOnCooldown()) {
			sendDefaultAnimation(getAnimation());
		}
	}

	@Override
	public boolean isCollidable(GameObject entity) {
		return !isCloaked;
	}

	@Override
	public boolean isVisibleTo(Player p) {
		return super.isVisibleTo(p) && (!isCloaked || player == p);
	}

	@Override
	public void hit(int damage, byte pirce) {
		if (!isCloaked)
			super.hit(damage, pirce);
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		if (c[2].equals("cloak")) {
			if (getAnimation() != cloak) {
				isCloaked = true;
				setHeight(5);
				setRadius(5);
				setMoving(false);
				groundPosition = GroundPosition.GROUND;
				cloak.startCooldown();
				setAnimation(cloak);
			}
		} else if (c[2].equals("stand") || c[2].equals("walk")) {
			if (getAnimation() == cloak) {
				isCloaked = false;
				setHeight(30);
				setRadius(7);
				groundPosition = GroundPosition.AIR;
			}
		}
	}

	@Override
	public void calculateDamage(Attack a) {
		GameBaseApp.getUpdater().sendDirect(Coms.DAMAGE+" " + basicAttack.getTarget().getNumber() + " " + a.damage + " " + a.pirce);
		// SoundHandler.startIngameSound(HUD.hm, x, y);
	}

	@Override
	public void renderAir() {
		drawSelected();
		if (isCloaked) {
			GameBaseApp.app.tint(255, 150);
		}
		getAnimation().draw(this, direction, getCurrentFrame());
		GameBaseApp.app.tint(255);
		basicAttack.drawAbility(this, direction);
		drawTaged();
	}

	@Override
	public void display() {
		super.display();
		if (isCloaked)
			drawBar(1 - cloak.getCooldownPercent());
	}

	@Override
	public void drawShot(GameObject target, float progress) {
		float x = PApplet.lerp(this.getX(), target.getX(), progress);
		float y = PApplet.lerp(this.getY() - getHeight(), target.getY() - target.getHeight(), progress);
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

	public static class CloakActive extends Spell {
		public CloakActive(int x, int y, char n) {
			super(x, y, n, cloakSym);
			setClazz(Angel.class);
		}

		@Override
		public String getDescription() {
			return "cloak";
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.GameBaseApp.selected) {
				if (e instanceof Angel) {
					e.sendAnimation("cloak", this);
				}
			}
		}
	}

}