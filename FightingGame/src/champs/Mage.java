package champs;

import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Spell;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import processing.core.PImage;
import shared.GameBaseApp;
import shared.Player;
import shared.SpellHandler;

public class Mage extends Champion implements Attacker {
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

	public Mage(GameBaseApp app, String[] c) {
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

		getStats().initHp(500);
		getStats().setArmor(30);
		getStats().setMagicResist(30);

		getStats().setSpeed(1.2f);
		getStats().setRadius(15);
		getStats().setSight(100);

		aggroRange = (byte) (getStats().getRadius() + 50);
		basicAttack.range = 9;
		basicAttack.damage = 40;
		basicAttack.cooldown = 2000;
		basicAttack.setCastTime(500);

		descr = " ";
		// ************************************
	}

	@Override
	public void updateDecisions(boolean isServer) {
		super.updateDecisions(isServer);
	}

	@Override
	public void calculateDamage(Attack a) {
		player.app.getUpdater().send("<hit " + basicAttack.getTarget().getNumber() + " " + a.damage + " " + a.pirce);
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
	public void setupSpells(GameBaseApp app, SpellHandler inter) {
		inter.addSpell(new Consume(player.app, inter, 0));
		inter.addSpell(new Shot(player.app, inter, 1));
		inter.addSpell(new TargetedShot(player.app, inter, 2));
	}

	static public class Consume extends Spell {// ******************************************************
		public Consume(GameBaseApp app, SpellHandler inter, int pos) {
			super(champ, inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			// TODO Auto-generated method stub

		}

	}

	static public class Shot extends Spell {// ******************************************************

		public Shot(GameBaseApp app, SpellHandler inter, int pos) {
			super(champ, inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			// TODO Auto-generated method stub

		}

	}

	static public class TargetedShot extends Spell {// ******************************************************

		public TargetedShot(GameBaseApp app, SpellHandler inter, int pos) {
			super(champ, inter, pos, smiteImg);
			setCooldown(1000);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			// TODO Auto-generated method stub

		}

	}

}
