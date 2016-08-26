package champs;

import game.GameApplet;
import game.ImageHandler;
import gameStructure.Damage;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Projectile;
import gameStructure.animation.Animation;
import processing.core.PImage;

public class TestProjectile extends Projectile {
	protected static PImage[] anim;

	public static void loadImages(GameApplet app, ImageHandler imageHandler) {
		String path = path(new GameObject(null) {
		});
		anim = imageHandler.load(path, "test", 's', 1);
		System.out.println("TestProjectile.loadImages()");
	}

	public TestProjectile(String[] c) {
		super(c);

		stand = new Animation(anim, 100);
		setAnimation(stand);

		setxSize(20);
		setySize(20);

		setSight(20);
		setSpeed(0.9f);
		setRadius(10);
	}

	@Override
	protected void onHit(GameObject o) {
		if (o.isEnemyTo(player) && o instanceof Entity) {
			((Entity) o).sendDamage(this, new Damage((Entity) o, player.champion, 50, true), player, origin);
			player.app.updater.send(BUFF + " ArmorShred " + o.number + " " + player.champion.number + " 6000");
			player.app.updater.send(BUFF + " Slow " + player.champion.number + " " + player.champion.number + " "
					+ (int) player.app.random(1000, 3000) + " " + (int) player.app.random(100));
		}
	}
}