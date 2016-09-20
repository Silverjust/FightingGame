package champs;

import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Champion;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.Spell;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.Death;
import gameStructure.animation.MeleeAttack;
import gameStructure.baseBuffs.SpeedBuff;
import processing.core.PImage;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;
import shared.Player;
import shared.SpellHandler;

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
		if (player != null)
			player.setChampion(this);
		// ************************************
		setxSize(50);
		setySize(50);

		getStats().initHp(500);
		getStats().setArmor(30);
		getStats().setMagicResist(30);

		getStats().setSpeed(1.2f);
		getStats().setRadius(15);
		getStats().setSight(200);
		getStats().setSight(200);

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
		inter.addSpell(new Consume(app, inter, 0));
		inter.addSpell(new ShotSpell(app, inter, 1));
		inter.addSpell(new SpeedBuffSpell(app, inter, 2));
	}

	static public class Consume extends Spell {// ******************************************************
		public Consume(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, smiteImg);
			setPassive(true);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		protected void onActivation() {
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			// TODO Auto-generated method stub

		}

	}

	static public class ShotSpell extends Spell {
		private int maxRange = 120;

		// ******************************************************
		public ShotSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, smiteImg);
			setCooldown(2000);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		protected void onActivation() {
			float xo = app.getPlayer().getChampion().getX();
			float yo = app.getPlayer().getChampion().getY();
			float xt = Projectile.cutProjectileRangeX(Helper.gridToX(app.mouseX) - xo, Helper.gridToY(app.mouseY) - yo,
					maxRange, maxRange);
			float yt = Projectile.cutProjectileRangeY(Helper.gridToX(app.mouseX) - xo, Helper.gridToY(app.mouseY) - yo,
					maxRange, maxRange);
			app.getUpdater().send(Coms.INPUT + " " + app.getPlayer().getUser().getIp() + " " + getPos() + " 1 "
					+ (xt + xo) + " " + (yt + yo));
			startCooldown();
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			app.getUpdater().sendSpawn(TestProjectile.class, player, player.getChampion().getX() + " "
					+ player.getChampion().getY() + " " + c[4] + " " + c[5] + " " + getInternName());
			startCooldown();
		}

	}

	static public class SpeedBuffSpell extends Spell {// ******************************************************
		public SpeedBuffSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, smiteImg);
			setCooldown(5000);
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			player.app.getUpdater().sendBuff(SpeedBuff.class, player.getChampion(), player.getChampion(), 1500,
					70 + "");
			startCooldown();
		}

	}

}
