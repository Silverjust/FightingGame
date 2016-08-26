package entity.aliens;

import main.ClientHandler;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.Spell;
import gameStructure.Attacker;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Helper;
import shared.Nation;

public class Ticul extends Unit implements Attacker {
	// TODO animations are displayed wrong

	private static PImage[][] standingImg;
	private static PImage[][] walkingImg;
	private static PImage[][] attackImg;
	private static PImage smiteImg;

	byte aggroRange;

	MeleeAttack basicAttack;
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
		standingImg = game.ImageHandler.load(path, "Ticul", 's', (byte) 8,
				(byte) 1);
		walkingImg = game.ImageHandler.load(path, "Ticul", 'w', (byte) 8,
				(byte) 8);
		attackImg = game.ImageHandler.load(path, "Ticul", 'b', (byte) 8,
				(byte) 8);

		smiteImg = ImageHandler.load(Nation.ALIENS.toString() + "/symbols/",
				"smite");
	}

	public Ticul(String[] c) {
		super(c);
		if (walkingImg != null)
			iconImg = walkingImg[0][0];

		stand = new Animation(standingImg, 100);
		walk = new Animation(walkingImg, 800);
		death = new Death(attackImg, 500);
		basicAttack = new MeleeAttack(attackImg, 600);

		setAnimation(walk);

		// ************************************
		setxSize(15);
		setySize(15);

		kerit = 56;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		trainTime = 3000;

		setHp(hp_max = 50);
		setSpeed(1.2f);
		setRadius(5);
		setSight(70);
		groundPosition = GameObject.GroundPosition.GROUND;

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.range = 9;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);
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
	public void calculateDamage(Attack a) {
		GameApplet.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
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
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getCurrentHp() / hp_max, h);
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

	static public class Flash extends Spell {// ******************************************************
		private int range = 100;

		public Flash(int x, int y, char n) {
			super(x, y, n, smiteImg);
			setClazz(Ticul.class);
			setCooldown(1000);
		}

		@Override
		public void onActivation() {
			for (GameObject e : GameApplet.updater.selected) {
				float tx = Helper.gridToX(GameApplet.app.mouseX);
				float ty = Helper.gridToY(GameApplet.app.mouseY);
				float x = e.getX() + (tx - e.getX()) / PApplet.dist(e.getX(), e.getY(), tx, ty)
						* range;
				float y = e.getY() + (ty - e.getY()) / PApplet.dist(e.getX(), e.getY(), tx, ty)
						* range;
				ClientHandler.send("<tp " + e.number + " " + x + " " + y);
			}
			startCooldown();
		}

		@Override
		public String getDescription() {
			return "short range teleport§work in progress";
		}

	}

}
