package champs;

import game.ImageHandler;
import gameStructure.Damage;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.animation.Animation;
import gameStructure.baseBuffs.Slow;
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

		stand = new Animation(app, anim, 100);
		setAnimation(stand);

		setxSize(20);
		setySize(20);

		getStats().setSight(20);
		setSpeed(3f);
		getStats().setRadius(10);
	}

	@Override
	protected void onHit(GameObject o) {
		if (o.isEnemyTo(player) && o instanceof Entity) {
			((Entity) o).sendDamage(this, new Damage((Entity) o, player.getChampion(), 50, true), player, origin);
			player.app.getUpdater().sendBuff(ArmorShred.class, (Entity) o, player.getChampion(), 6000, "");
			player.app.getUpdater().sendBuff(Slow.class, player.getChampion(), player.getChampion(),
					(int) player.app.random(1000, 3000), (int) player.app.random(100) + "");
		}
	}
}