package entity.aliens;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import game.GameApplet;
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
import gameStructure.animation.ShootAttack;

public class ThornTower extends Building implements Shooter, Commander {
	private int commandingRange;

	ShootAttack basicAttack;

	private int hp;

	private int hp_max;

	private int armor;

	public PImage iconImg;

	protected String descr = " ";

	protected String stats = " ";

	public Death death;

	private static PImage standImg;
	private static PImage previewImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		previewImg = standImg = ImageHandler.load(path, "ThornTower");
	}

	public ThornTower(String[] c) {
		super(c);

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 1000);
		death = new Death(standImg, 100);
		basicAttack = new ShootAttack(standImg, 800);

		setAnimation(build);

		// ************************************
		setxSize(30);
		setySize(30);

		kerit = 450;
		pax = 100;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(10000);

		setSight(70);

		setHp(hp_max = 800);
		setRadius(15);

		basicAttack.range = 70;
		basicAttack.damage = 55;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);// eventtime is defined by target distance
		basicAttack.speed = 0.1f;

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
		GameApplet.updater.send("<hit " + basicAttack.getTarget().number + " "
				+ a.damage + " " + a.pirce);

	}

	@Override
	public void renderTerrain() {
		ImageHandler.drawImage(GameApplet.app, AlienMainBuilding.groundImg, xToGrid(getX()),
				yToGrid(getY()), commandingRange * 2, commandingRange);
	}

	@Override
	public void drawOnMinimapUnder(PGraphics graphics) {
		graphics.image(AlienMainBuilding.groundImg, getX(), getY(), commandingRange * 2,
				commandingRange * 2);
	}

	@Override
	public void renderUnder() {
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
		float y = PApplet.lerp(this.getY(), target.getY(), progress);
		GameApplet.app.stroke(100, 100, 0);
		GameApplet.app.line(xToGrid(this.getX()), yToGrid(this.getY()), x, y / 2);
		GameApplet.app.stroke(0);
	}

	public PImage preview() {
		return previewImg;
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public int commandRange() {
		return commandingRange;
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

}
