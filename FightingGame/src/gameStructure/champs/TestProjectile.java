package gameStructure.champs;

import game.ImageHandler;
import gameStructure.Damage;
import gameStructure.Damage.DmgType;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.animation.Animation;
import gameStructure.baseBuffs.Stunn;
import gameStructure.baseBuffs.events.Event;
import gameStructure.baseBuffs.events.Event.HitTypes;
import gameStructure.baseBuffs.events.GettingHitEvent;
import gameStructure.baseBuffs.events.HitEvent;
import gameStructure.champs.Ticul.ArmorShred;
import processing.core.PImage;
import shared.GameBaseApp;

public class TestProjectile extends Projectile {
	protected static PImage[] anim;

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		String path = path(new GameObject(app, null) {
		});
		anim = imageHandler.load(path, "test", 's', 1);
		System.out.println("TestProjectile.loadImages()");
	}

	public TestProjectile(GameBaseApp app, String[] c) {
		super(app, c);

		stand = new Animation(app, this, anim, 100);
		setAnimation(stand);

		setxSize(20);
		setySize(20);

		getStats().getSight().setBrutoBaseAmount(20);
		setSpeed(100);
		getStats().getRadius().setBrutoBaseAmount(10);
		System.out.println("TestProjectile.TestProjectile()");
	}

	@Override
	protected void onHit(GameObject o, boolean isServer) {
		System.out.println("TestProjectile.onHit()" + o + " " + o.isEnemyTo(player));
		if (o.isEnemyTo(origin) && o instanceof Entity) {
			Damage damage = new Damage((Entity) o, origin, 40, DmgType.MAGIC_DMG);
			damage.doDamage(o, origin, this, originInfo, HitTypes.SPELL, isServer);
			player.app.getUpdater().sendBuff(ArmorShred.class, (Entity) o, player.getChampion(), 3000, "");
			// player.app.getUpdater().sendBuff(Slow.class,
			// player.getChampion(), player.getChampion(),
			// (int) player.app.random(1000, 3000), (int) player.app.random(100)
			// + "");

		}
	}
}