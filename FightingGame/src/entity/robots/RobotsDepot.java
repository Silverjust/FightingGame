package entity.robots;

import processing.core.PImage;
import game.ImageHandler;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Build;
import gameStructure.animation.Death;

public class RobotsDepot extends Building implements Commander {
	private int commandingRange;
	private static PImage standImg;

	public static void loadImages() {
		String path = path(new Object() {
		});
		standImg = ImageHandler.load(path, "RobotsDepot");
	}

	public RobotsDepot(String[] c) {
		super(c);
		if (c != null && c.length >= 5 && c[5] != null && c[5].equals("select")) 
			select();

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = new Build(standImg, 5000);
		death = new Death(standImg, 1000);
		// basicAttack = new ShootAttack(standImg, 1000);
		// basicAttack.explosion = new Explosion(standImg, 800);

		setAnimation(build);

		// ************************************
		setxSize(20);
		setySize(20);

		kerit = 200;
		pax = 0;
		arcanum = 0;
		prunam = 0;
		build.setBuildTime(2000);

		setSight(50);

		setHp(hp_max = 1000);
		setRadius(13);

		/*
		 * splashrange = 10; basicAttack.range = 70; basicAttack.damage = 13;
		 * basicAttack.cooldown = 1500; basicAttack.setCastTime(500);//
		 * eventtime is defined by target distance basicAttack.speed = 0.5f;
		 */

		commandingRange = 100;

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		/*
		 * float importance = 0; Entity importantEntity = null; if
		 * (getAnimation() == stand) { for (Entity e : player.visibleEntities) {
		 * if (e.isEnemyTo(this)) { if (e.isInRange(x, y, basicAttack.range +
		 * e.radius) && basicAttack.canTargetable(e) && !(e instanceof
		 * Building)) { float newImportance = calcImportanceOf(e); if
		 * (newImportance > importance) { importance = newImportance;
		 * importantEntity = e; } } } } if (importantEntity != null &&
		 * getBasicAttack().isNotOnCooldown()) { sendAnimation("basicAttack " +
		 * importantEntity.number); } } basicAttack.updateAbility(this,
		 * isServer);
		 */
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		Attack.updateExecAttack(c, this);
	}

	/*
	 * @Override public void calculateDamage(Attack a) { Entity target =
	 * ((ShootAttack) a).getTarget(); for (Entity e : ref.updater.entities) { if
	 * (e != null & e.isEnemyTo(this) && e.isInRange(target.x, target.y,
	 * e.radius + splashrange)) { ref.updater.send("<hit " + e.number + " " +
	 * a.damage + " " + a.pirce); } } }
	 */

	@Override
	public void renderGround() {
		drawSelected();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
		// basicAttack.drawAbility(this, (byte) 0);
	}

	/*
	 * @Override public void drawShot(Entity target, float progress) { float x =
	 * PApplet.lerp(this.x, target.x, progress); float y = PApplet.lerp(this.y -
	 * height, target.y - target.height, progress); ref.app.fill(255, 100, 0);
	 * ref.app.strokeWeight(0); ref.app.ellipse(xToGrid(x), yToGrid(y), 1, 1);
	 * ref.app.strokeWeight(1); }
	 */

	public PImage preview() {
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandingRange;
	}

	/*
	 * @Override public Attack getBasicAttack() { return basicAttack; }
	 */

}
