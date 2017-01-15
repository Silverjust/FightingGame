package gameStructure;

import game.ImageHandler;
import gameStructure.Damage.DmgType;
import gameStructure.animation.Animation;
import gameStructure.baseBuffs.events.Event;
import gameStructure.baseBuffs.events.Event.HitTypes;
import processing.core.PImage;
import shared.GameBaseApp;

public class BasicAttackProjectile extends Projectile {
	protected static PImage[] anim;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		/* code */ }

	public BasicAttackProjectile(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null) {
			System.out.println("BasicAttack.BasicAttack()" + origin);
			BaAtInfo baAtInfo = ((Entity) origin).getBasicAttackAnim();
			anim = baAtInfo.getBasicAttackAnim();
			stand = new Animation(app, this, anim, 100);
			setAnimation(stand);

			setxSize(baAtInfo.getSX());
			setySize(baAtInfo.getSY());
			setSpeed(baAtInfo.getSpeed());
		}

		System.out.println("Stats.setSight()" + 0);
		getStats().getSight().setBrutoBaseAmount(0);
		getStats().getRadius().setBrutoBaseAmount(1);
	}

	@Override
	protected void onHit(GameObject o, boolean isServer) {
		if (o.isEnemyTo(origin) && o instanceof Entity && o == getTargetObject()) {
			Damage damage = new Damage((Entity) o, origin,
					((Entity) origin).getStats().getAttackDamage().getTotalAmount() + //
							0.6f * ((Entity) origin).getStats().getAbilityPower().getTotalAmount(),
					DmgType.PHYSICAL_DMG).setKrit(true);
			damage.doDamage(o, origin, this, originInfo, HitTypes.BASIC_ATTACK, isServer);
		}
	}

	public static class BaAtInfo {

		private PImage[] basicAttackProjAnim;
		private int sx;
		private int sy;
		private int speed;

		public BaAtInfo(PImage[] basicAttackProjAnim, int sx, int sy, int speed) {
			this.basicAttackProjAnim = basicAttackProjAnim;
			this.sx = sx;
			this.sy = sy;
			this.speed = speed;
		}

		public int getSX() {
			return sx;
		}

		public int getSY() {
			return sy;
		}

		public PImage[] getBasicAttackAnim() {
			return basicAttackProjAnim;
		}

		public int getSpeed() {
			return speed;
		}

	}
}
