package gameStructure.champs;

import game.ImageHandler;
import gameStructure.Damage;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.animation.Animation;
import gameStructure.baseBuffs.Stunn;
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

		System.out.println("Stats.setSight()" + 20);
		getStats().getSight().setBrutoBaseAmount(20);
		setSpeed(100);
		getStats().getRadius().setBrutoBaseAmount(10);
	}

	@Override
	protected void onHit(GameObject o) {
		System.out.println("TestProjectile.onHit()" + o + " " + o.isEnemyTo(player));
		if (o.isEnemyTo(player) && o instanceof Entity) {
			((Entity) o).sendDamage(new Damage((Entity) o, player.getChampion(), 50, true), player, originInfo);
			player.app.getUpdater().sendBuff(Stunn.class, (Entity) o, player.getChampion(), 1000, "");
			// player.app.getUpdater().sendBuff(Slow.class,
			// player.getChampion(), player.getChampion(),
			// (int) player.app.random(1000, 3000), (int) player.app.random(100)
			// + "");
		}
	}
}