package champs;

import game.GameApplet;
import game.ImageHandler;
import game.PlayerInterface;
import gameStructure.Attacker;
import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Spell;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PImage;
import shared.Helper;

public class Ticul extends Champion implements Attacker {
	// TODO animations are displayed wrong

	private static PImage[][] standingImg;
	private static PImage[][] walkingImg;
	private static PImage[][] attackImg;
	private static PImage smiteImg;

	byte aggroRange;

	MeleeAttack basicAttack;

	public static void loadImages(GameApplet app, ImageHandler imageHandler) {
		String path = path(new GameObject(null) {
		});
		standingImg = imageHandler.load(path, "Ticul", 's', (byte) 8, (byte) 1);
		walkingImg = imageHandler.load(path, "Ticul", 'w', (byte) 8, (byte) 8);
		attackImg = imageHandler.load(path, "Ticul", 'b', (byte) 8, (byte) 8);
	}

	public Ticul(String[] c) {
		super(c);
		System.out.println("Ticul.Ticul()");
		if (walkingImg != null)
			iconImg = walkingImg[0][0];

		stand = new Animation(standingImg, 100);
		walk = new Animation(walkingImg, 800);
		death = new Death(attackImg, 500);
		basicAttack = new MeleeAttack(attackImg, 600);

		setAnimation(walk);

		// ************************************
		setxSize(50);
		setySize(50);

		initHp(500);
		setArmor(30);
		setMagicResist(30);

		setSpeed(1.2f);
		setRadius(15);
		setSight(100);

		aggroRange = (byte) (getRadius() + 50);
		basicAttack.range = 9;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);

		descr = " ";
		stats = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		super.updateDecisions(isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		player.app.updater.send("<hit " + basicAttack.getTarget().number + " " + a.damage + " " + a.pirce);
	}

	@Override
	public void renderGround() {
		getAnimation().draw(this, direction, getCurrentFrame());
		drawTaged();
	}

	@Override
	public Attack getBasicAttack() {
		return basicAttack;
	}

	@Override
	public void setupSpells(PlayerInterface inter) {
		inter.addSpell(new Shot(inter, 1));
		inter.addSpell(new TargetedShot(inter, 2));
	}

	public class Shot extends Spell {// ******************************************************
		private int range = 100;

		public Shot(PlayerInterface inter, int pos) {
			super(inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public void onActivation() {

			System.out.println("Ticul.Shot.onActivation()");
			player.app.updater.send(SPAWN + " TestProjectile " + player.getUser().ip + " " + getX() + " " + getY() + " "
					+ Helper.gridToX(player.app.mouseX) + " " + Helper.gridToY(player.app.mouseY) + " "
					+ getInternName());
			startCooldown();
		}

		@Override
		public String getDescription() {
			return null;
		}

	}

	public class TargetedShot extends Spell {// ******************************************************

		private int range = 100;

		public TargetedShot(PlayerInterface inter, int pos) {
			super(inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public void onActivation() {

			System.out.println("Ticul.Shot.onActivation()");
			player.app.updater.send(SPAWN + " TestProjectile " + player.getUser().ip + " " + getX() + " " + getY() + " "
					+ HOMING + " " + 2 + " " + getInternName());
			startCooldown();
		}

		@Override
		public String getDescription() {
			return null;
		}

	}

}
