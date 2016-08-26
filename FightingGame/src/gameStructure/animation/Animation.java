package gameStructure.animation;

import game.GameApplet;
import game.ImageHandler;
import gameStructure.Attacker;
import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.Trainer;
import gameStructure.Unit;
import processing.core.PImage;
import shared.Updater;

public class Animation {
	public static Class<?> observe = Animation.class;
	// not assignable class == off
	byte directions;
	private byte frames;
	PImage[][] imgWD;
	PImage[] img;
	public int duration;
	int start;

	public Animation(PImage[][] IMG, int duration) {
		imgWD = IMG;
		if (IMG != null) {
			directions = (byte) imgWD.length;
			frames = (byte) imgWD[0].length;
		}
		this.duration = duration;
	}

	public Animation(PImage[] IMG, int duration) {
		img = IMG;
		directions = 1;
		if (IMG != null)
			frames = (byte) img.length;
		this.duration = duration;

	}

	public Animation(PImage IMG, int duration) {
		img = new PImage[1];
		img[0] = IMG;
		directions = 1;
		frames = 1;
		this.duration = duration;

	}

	public int speed() {
		return duration / frames;
	}

	public void setup(GameObject e) {
		e.setCurrentFrame(0);
		start = Updater.Time.getMillis();
	}

	public void update(GameObject e) {
		if (isFinished()) {
			if (doRepeat(e)) {
				if (Animation.observe.isAssignableFrom(e.getClass())) {
					System.out.println("Animation.update()rep" + getName(e));
				}
				e.setAnimation(this);
				setup(e);
			} else {
				if (Animation.observe.isAssignableFrom(e.getClass())) {
					System.out.println("Animation.update()norep" + getName(e));
				}
				e.sendDefaultAnimation(this);
				setup(e);// continue until com sets default animation
			}
		}
	}

	public void draw(GameObject e, int j, int i) {
		if (Updater.Time.getMillis() - start >= speed() * e.getCurrentFrame()) {
			e.setCurrentFrame((byte) ((Updater.Time.getMillis() - start) / speed()));
			if (e.getCurrentFrame() > frames - 1) {
				e.setCurrentFrame((byte) (frames - 1));
			}
			if (e.getCurrentFrame() < 0) {
				e.setCurrentFrame(0);
			}
		}

		if (imgWD != null && j < directions && i < frames) {
			ImageHandler.drawImage(GameApplet.app, imgWD[j][i], GameObject.xToGrid(e.getX()), GameObject.yToGrid(e.getY()),
					e.getxSize(), e.getySize());
		} else if (img != null && i < frames) {
			ImageHandler.drawImage(GameApplet.app, img[i], GameObject.xToGrid(e.getX()),
					GameObject.yToGrid(e.getY() - e.getHeight()), e.getxSize(), e.getySize());
		}
		if (Animation.observe.isAssignableFrom(e.getClass())) {
			GameApplet.app.text(getName(e), GameObject.xToGrid(e.getX()), GameObject.yToGrid(e.getY() - e.getHeight()), 50,
					50);
		}
	}

	public void draw(GameObject e, float x, float y, byte d, byte f) {
		if (Updater.Time.getMillis() - start >= speed() * e.getCurrentFrame()) {
			e.setCurrentFrame((byte) ((Updater.Time.getMillis() - start) / speed()));
			if (e.getCurrentFrame() > frames - 1) {
				e.setCurrentFrame((byte) (frames - 1));
			}
			if (e.getCurrentFrame() < 0) {
				e.setCurrentFrame(0);
			}
		}

		if (imgWD != null && d < directions && f < frames) {
			ImageHandler.drawImage(GameApplet.app, imgWD[d][f], GameObject.xToGrid(x), GameObject.yToGrid(y), e.getxSize(),
					e.getySize());
		} else if (img != null && f < frames) {
			ImageHandler.drawImage(GameApplet.app, img[f], GameObject.xToGrid(x), GameObject.yToGrid(y - e.getHeight()),
					e.getxSize(), e.getySize());
		}
		if (Animation.observe.isAssignableFrom(e.getClass())) {
			GameApplet.app.text(getName(e), GameObject.xToGrid(e.getX()), GameObject.yToGrid(e.getY() - e.getHeight()), 50,
					50);
		}

	}

	public boolean isNotOnCooldown() {
		return true;
	}

	public boolean isFinished() {
		return Updater.Time.getMillis() - start >= duration;

	}

	public boolean isInterruptable() {
		return true;
	}

	public boolean doRepeat(GameObject e) {
		return true;
	}

	public String getName(GameObject o) {
		if (this == o.stand)
			return "stand";
		if (o instanceof Entity && ((Entity) o).death != null && this == ((Entity) o).death)
			return "death";

		if (o instanceof Trainer && this == ((Trainer) o).getTraining())
			return "train";
		if (o instanceof Unit && this == ((Unit) o).walk)
			return "walk";
		if (o instanceof Attacker && this == ((Attacker) o).getBasicAttack())
			return "basicAttack";
		if (this instanceof Ability)
			return "ability";
		return super.toString();
	}
}
