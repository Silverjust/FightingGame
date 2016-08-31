package champs;

import game.GameBaseApp;
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

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		String path = path(new GameObject(app, null) {
		});
		standingImg = imageHandler.load(path, "Ticul", 's', (byte) 8, (byte) 1);
		walkingImg = imageHandler.load(path, "Ticul", 'w', (byte) 8, (byte) 8);
		attackImg = imageHandler.load(path, "Ticul", 'b', (byte) 8, (byte) 8);
	}

	public Ticul(GameBaseApp app, String[] c) {
		super(app, c);
		System.out.println("Ticul.Ticul()");
		if (walkingImg != null)
			iconImg = walkingImg[0][0];

		stand = new Animation(app, standingImg, 100);
		walk = new Animation(app, walkingImg, 800);
		death = new Death(app, attackImg, 500);
		basicAttack = new MeleeAttack(app, attackImg, 600);

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
		player.app.getUpdater().send("<hit " + basicAttack.getTarget().number + " " + a.damage + " " + a.pirce);
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
		inter.addSpell(new Consume(player.app, inter, 0));
		inter.addSpell(new Shot(player.app, inter, 1));
		inter.addSpell(new TargetedShot(player.app, inter, 2));
	}

	public class Consume extends Spell {// ******************************************************	
		public Consume(GameBaseApp app, PlayerInterface inter, int pos) {
			super(app, inter, pos, smiteImg);
			setCooldown(1000);
		}
	
		@Override
		public String getDescription() {
			return null;
		}

		@Override
		protected void onActivation() {
			// TODO Auto-generated method stub
			
		}
	
	}

	public class Shot extends Spell {// ******************************************************
		private int range = 100;

		public Shot(GameBaseApp app, PlayerInterface inter, int pos) {
			super(app, inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public void onActivation() {

			System.out.println("Ticul.Shot.onActivation()");
			player.app.getUpdater()
					.send(SPAWN + " TestProjectile " + player.getUser().ip + " " + getX() + " " + getY() + " "
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

		public TargetedShot(GameBaseApp app, PlayerInterface inter, int pos) {
			super(app, inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public void onActivation() {

			System.out.println("Ticul.Shot.onActivation()");
			player.app.getUpdater().send(SPAWN + " TestProjectile " + player.getUser().ip + " " + getX() + " " + getY()
					+ " " + HOMING + " " + 2 + " " + getInternName());
			startCooldown();
		}

		@Override
		public String getDescription() {
			return null;
		}

	}

}
