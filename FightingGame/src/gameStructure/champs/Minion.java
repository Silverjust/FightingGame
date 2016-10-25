package gameStructure.champs;

import game.ImageHandler;
import gameStructure.BasicAttackProjectile.BaAtInfo;
import gameStructure.GameObject;
import gameStructure.Unit;
import gameStructure.UnitStats;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.animation.BasicAttackAnim;
import gameStructure.animation.Death;
import processing.core.PImage;
import shared.ContentListManager;
import shared.GameBaseApp;

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
		stats.getHp().initAmount(5000);
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


}
