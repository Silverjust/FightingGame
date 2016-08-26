package gameStructure.animation;

import processing.core.PImage;
import shared.Updater;
import game.GameApplet;
import game.ImageHandler;
import gameStructure.GameObject;

public class Explosion extends Animation {

	private float xSize = 50;
	private float ySize = 50;

	public Explosion(PImage[] IMG, int duration) {
		super(IMG, duration);
	}

	public Explosion(PImage IMG, int duration) {
		super(IMG, duration);
	}

	public void setup(GameObject e) {
		start = Updater.Time.getMillis();
	}

	@Override
	@Deprecated
	public void update(GameObject e) {
		if (isFinished()) {
			setup(e);
		}
	}

	@Override
	@Deprecated
	public void draw(GameObject e, byte d, byte f) {
	}

	public void draw(float x, float y) {
		int currentFrame = 0;
		if (Updater.Time.getMillis() - start >= speed() * currentFrame) {
			currentFrame = (byte) ((Updater.Time.getMillis() - start) / speed());
			if (currentFrame > img.length - 1) {
				currentFrame = (byte) (img.length - 1);
			}
			if (currentFrame < 0) {
				currentFrame = 0;
			}
		}

		if (img != null && currentFrame < img.length) {
			ImageHandler.drawImage(GameApplet.app, img[currentFrame],
					GameObject.xToGrid(x), GameObject.yToGrid(y), xSize, ySize);
		}
	}

	@Override
	public boolean doRepeat(GameObject e) {
		return false;
	}
}
