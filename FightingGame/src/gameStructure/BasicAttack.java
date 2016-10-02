package gameStructure;

import gameStructure.animation.Animation;
import processing.core.PImage;
import shared.GameBaseApp;

public class BasicAttack extends Projectile {
	protected static PImage[] anim;

	public BasicAttack(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null) {
			System.out.println("BasicAttack.BasicAttack()"+origin);
			anim = ((Entity) origin).getBasicAttackAnim();
			stand = new Animation(app, anim, 100);
			setAnimation(stand);
		}

		setxSize(10);
		setySize(10);

		getStats().setSight(0);
		setSpeed(3f);
		getStats().setRadius(1);
	}

	@Override
	protected void onHit(GameObject o) {
		if (o.isEnemyTo(player) && o instanceof Entity && o == getTargetObject()) {
			((Entity) o).sendDamage(new Damage((Entity) o, player.getChampion(), 50, true), player, originInfo);
		}
	}
}
