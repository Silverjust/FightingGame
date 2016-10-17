package gameStructure.champs;

import game.ImageHandler;
import gameStructure.BasicAttackProjectile.BaAtInfo;
import gameStructure.EntityStats;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.Spell;
import gameStructure.Unit;
import gameStructure.UnitStats;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.BasicAttackAnim;
import gameStructure.animation.Death;
import gameStructure.baseBuffs.Buff;
import gameStructure.baseBuffs.SpeedBuff;
import processing.core.PImage;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Helper;
import shared.Player;
import shared.SpellHandler;

public class Minion extends Unit {
	// TODO animations are displayed wrong

	private static PImage[][] standingImg;
	private static PImage[][] walkingImg;
	private static PImage[][] attackImg;
	private static PImage[] basicAttackProjAnim;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		String path = path(new GameObject(app, null) {
		});
		standingImg = imageHandler.load(path, "Ticul", 's', (byte) 8, (byte) 1);
		walkingImg = imageHandler.load(path, "Ticul", 'w', (byte) 8, (byte) 8);
		attackImg = imageHandler.load(path, "Ticul", 'b', (byte) 8, (byte) 8);
		basicAttackProjAnim = imageHandler.load(path, "basicAttack/test", 's', 1);
	}

	public Minion(GameBaseApp app, String[] c) {
		super(app, c);
		System.out.println("Minion.Minion()+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		if (c != null) {
			if (walkingImg != null)
				iconImg = walkingImg[0][0];

			stand = new Animation(app, this, standingImg, 100);
			walk = new Animation(app, this, walkingImg, 800);
			death = new Death(app, this, attackImg, 500);
			basicAttack = new BasicAttackAnim(app, this, attackImg, 600);

			setAnimation(walk);

			basicAttack.setRange(30);
			basicAttack.setDamage(40);
			basicAttack.setCooldown(2000);
			basicAttack.setCastTime(500);
		}
		setxSize(30);
		setySize(30);
		setHpBarLength(20);

		UnitStats stats = getStats();
		stats.getHp().initAmount(500);
		stats.getArmor().setBrutoBaseAmount(30);
		stats.getMagicResist().setBrutoBaseAmount(30);

		stats.getMovementSpeed().setBrutoBaseAmount(120);
		stats.getRadius().setBrutoBaseAmount(15);
		stats.getSight().setBrutoBaseAmount(200);

	}

	@Override
	public void updateDecisions(boolean isServer) {
		super.updateDecisions(isServer);
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
	public BaAtInfo getBasicAttackAnim() {
		return new BaAtInfo(basicAttackProjAnim, 10, 10, 300);

	}

	@Override
	public void onRegistration(ContentListManager contentListManager) {
	}

	static public class PassiveSpell extends Spell {// ******************************************************
		private static PImage symbolImg;

		public PassiveSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, symbolImg);
			setPassive(true);
		}

		public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
			String path = path(new GameObject(app, null) {
			});
			symbolImg = imageHandler.load(path, "symbol");
			System.out.println("Ticul.PassiveSpell.loadImages()" + symbolImg);
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

	static public class QSpell extends Spell {// ******************************************************
		private int maxRange = 120;
		private static PImage symbolImg;

		public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
			String path = path(new GameObject(app, null) {
			});
			symbolImg = imageHandler.load(path, "symbol");
		}

		public QSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, symbolImg);
			setCooldown(2000);
		}

		@Override
		protected void onActivation() {
			float xo = app.getPlayer().getChampion().getX();
			float yo = app.getPlayer().getChampion().getY();
			float xt = Projectile.cutProjectileRangeX(Helper.gridToX(app.mouseX) - xo, Helper.gridToY(app.mouseY) - yo,
					maxRange, maxRange);
			float yt = Projectile.cutProjectileRangeY(Helper.gridToX(app.mouseX) - xo, Helper.gridToY(app.mouseY) - yo,
					maxRange, maxRange);
			app.getUpdater().sendInput(app.getPlayer(), getPos(), 1, +(xt + xo) + " " + (yt + yo));
			startCooldown();
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			app.getUpdater().sendSpawn(TestProjectile.class, player,
					player.getChampion().getX() + " " + player.getChampion().getY() + " "
							+ player.getChampion().getNumber() + " " + c[4] + " " + c[5] + " " + getInternName());
			startCooldown();
		}

		@Override
		public String getDescription() {
			return "shoots a projectile that stunns the first target hit for 1 sec";
		}

	}

	static public class WSpell extends Spell {// ******************************************************
		private static PImage symbolImg;

		public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
			String path = path(new GameObject(app, null) {
			});
			symbolImg = imageHandler.load(path, "symbol");
		}

		public WSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, symbolImg);
			setCooldown(5000);
		}

		@Override
		public String getDescription() {
			return "gives Ticul a 70%§img ms§ buff for 1,5 sec";
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			player.app.getUpdater().sendBuff(SpeedBuff.class, player.getChampion(), player.getChampion(), 1500,
					70 + "");
			startCooldown();
		}

	}

	static public class RSpell extends Spell {// ******************************************************
		private static PImage symbolImg;

		public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
			String path = path(new GameObject(app, null) {
			});
			symbolImg = imageHandler.load(path, "symbol");
		}

		public RSpell(GameBaseApp app, SpellHandler inter, int pos) {
			super(app, inter, pos, symbolImg);
			setCooldown(5000);
		}

		@Override
		public String getDescription() {
			return "gives Ticul 30§img armor§ and  30§img mr§ for 4 sec";
		}

		@Override
		public void recieveInput(String[] c, Player player) {
			player.app.getUpdater().sendBuff(RBuff.class, player.getChampion(), player.getChampion(), 4000, 70 + "");
			startCooldown();
		}

	}

	static public class RBuff extends Buff {
		private int amount = 30;

		public RBuff(GameBaseApp app, String[] c) {
			super(app, c);
		}

		@Override
		public void onStart() {
			super.onStart();
			EntityStats stats = owner.getStats();
			stats.getArmor().setBrutoBonusAmount(stats.getArmor().getBrutoBonusAmount() + amount);
			stats.getMagicResist().setBrutoBonusAmount(stats.getMagicResist().getBrutoBonusAmount() + amount);
		}

		@Override
		public void onEnd() {
			super.onEnd();
			EntityStats stats = owner.getStats();
			stats.getArmor().setBrutoBonusAmount(stats.getArmor().getBrutoBonusAmount() - amount);
			stats.getMagicResist().setBrutoBonusAmount(stats.getMagicResist().getBrutoBonusAmount() - amount);

		}
	}

	static public class ArmorShred extends Buff {
		private int amount = 30;
		private int totalAmount = 0;

		public ArmorShred(GameBaseApp app, String[] c) {
			super(app, c);
			maxStacks = 3;
		}

		@Override
		public void onStart() {
			super.onStart();
			EntityStats stats = owner.getStats();
			stats.getArmor().setBrutoBonusAmount(stats.getArmor().getBrutoBonusAmount() - amount);
			totalAmount += amount;
		}

		@Override
		public void onEnd() {
			super.onEnd();
			EntityStats stats = owner.getStats();
			stats.getArmor().setBrutoBonusAmount(stats.getArmor().getBrutoBonusAmount() - totalAmount);
		}

		@Override
		public boolean doesStack() {
			return stacks < maxStacks;
		}

		/** only apply 1 at a time */
		@Override
		protected void onStackApply(int i) {
			super.onStackApply(i);
			EntityStats stats = owner.getStats();
			stats.getArmor().setBrutoBonusAmount(stats.getArmor().getBrutoBonusAmount() - amount);
			totalAmount += amount;
		}
	}
}
