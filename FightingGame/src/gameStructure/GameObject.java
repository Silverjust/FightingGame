package gameStructure;

import game.GameApplet;
import game.GameDrawer;
import game.ImageHandler;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Coms;
import shared.Nation;
import shared.Player;
import shared.Updater;

public abstract class GameObject implements Coms {
	// TODO upgrades für einheiten
	public static int entityCounter;
	public int number;
	public Player player;

	public boolean isTaged;

	private float x, y;
	private int radius = 1;
	private int sight = 10;
	private int height;
	private int currentFrame;
	private int xSize, ySize;

	private static PImage shadowImg;
	protected static PImage selectedImg;
	protected static PImage hpImg;
	public Animation stand;

	private Animation animation;
	private Animation nextAnimation;
	protected String descr = " ";
	protected String stats = " ";

	public static void loadImages() {
		String path = path(new GameObject(null) {
		});
		shadowImg = ImageHandler.load(path, "shadow");
		selectedImg = ImageHandler.load(path, "selected");
		hpImg = ImageHandler.load(path, "hp");

		// hit = ref.minim.loadSnippet("test.mp3");
	}

	public GameObject(String[] c) {
		if (c != null) {
			player = GameApplet.updater.getPlayer(c[2]);
			setX(Float.parseFloat(c[3]));
			setY(Float.parseFloat(c[4]));
		}
	}

	public void updateAnimation() {
		animation = getNextAnimation();
		if (Animation.observe.isAssignableFrom(this.getClass())) {
			System.out.println("Entity.updateAnimation()" + animation.getName(this));
		}
		animation.update(this);
	}

	public void updateDecisions(boolean isServer) {
	}

	public void updateMovement() {
	}

	public void renderTerrain() {
	}

	public void renderUnder() {
	}

	public void renderGround() {
	}

	public void renderAir() {
	}

	public void display() {
	}

	public void renderRange() {
		if (this instanceof Attacker && ((Attacker) this).getBasicAttack() != null) {
			Attack a = ((Attacker) this).getBasicAttack();
			drawCircle(a.range);
			drawCircle((int) (a.range * a.getCooldownPercent()));
		}
		if (this instanceof Unit) {
			GameApplet.app.line(getX(), getY() / 2, ((Unit) this).xTarget, ((Unit) this).yTarget / 2);
		}
	}

	public void exec(String[] c) {
		try {
			String string = c[2];
			if ("stand".equals(string)) {
				if (this instanceof Unit)
					((Unit) this).setMoving(false);
				setAnimation(stand);
			}
		} catch (Exception e) {
			System.err.println(getAnimation() + " " + getNextAnimation());
			PApplet.printArray(c);
			e.printStackTrace();
		}
	}

	public void onSpawn(boolean isServer) {
	}

	protected void drawShadow() {
		ImageHandler.drawImage(GameApplet.app, shadowImg, xToGrid(getX()), yToGrid(getY()), getRadius() * 2, getRadius());
	}

	protected void drawTaged() {
		/** just for debug */
		if (isTaged) {
			GameApplet.app.fill(0, 0);
			GameApplet.app.stroke(player.color);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 0.3f, getRadius() * 2, getRadius() * 1.5f);
			GameApplet.app.stroke(0);
		}
		isTaged = false;
	}

	public void drawBar(float f) {
		int h = 1;
		if (true) {//
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY() - h * 3) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(200);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY() - h * 3) - getRadius() * 1.5f,
					getRadius() * 2 * f, h);
			GameApplet.app.tint(255);
		}
	}

	public void drawBar(float f, int c) {
		int h = 1;
		if (true) {//
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY() - h * 3) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(c);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY() - h * 3) - getRadius() * 1.5f,
					getRadius() * 2 * f, h);
			GameApplet.app.tint(255);
		}
	}

	protected void drawCircle(int r) {
		ImageHandler.drawImage(GameApplet.app, selectedImg, xToGrid(getX()), yToGrid(getY()), r * 2, r);
	}

	protected void drawCircle(float x, float y, byte range) {
		ImageHandler.drawImage(GameApplet.app, selectedImg, xToGrid(x), yToGrid(y), range * 2, range);
	}

	void drawLine(float tx, float ty) {
		GameApplet.app.line(xToGrid(getX()), yToGrid(getY()), tx, ty / 2);
	}

	public void drawOnMinimap(PGraphics graphics) {
	}

	public void drawOnMinimapUnder(PGraphics graphics) {
	}

	public void drawSight(Updater updater) {
		int scale = updater.map.fogScale;
		updater.map.fogOfWar.ellipse(getX() / scale, getY() / scale / 2, getSight() * 2 / scale, getSight() / scale);
	}

	public void sendDefaultAnimation(Animation oldAnimation) {
		sendAnimation("stand");
	}

	public void sendAnimation(String s) {
		if (s != "") {
			GameApplet.updater.send("<execute " + number + " " + s);
		}
	}

	public void setAnimation(Animation a) {
		if ((a != null && animation == null) || (a != null && animation.isInterruptable() && animation != a)) {
			if (Animation.observe.isAssignableFrom(this.getClass())) {
				System.out.println("Entity.setAnimation()" + a.getName(this));
			}
			if (animation == null)
				animation = a;
			nextAnimation = a;
			a.setup(this);
		}
	}

	public Animation getAnimation() {
		return animation;
	}

	public Animation getNextAnimation() {
		return nextAnimation;
	}

	public static float xToGrid(float x) {
		return x;
	}

	public static float yToGrid(float y) {
		return y / 2;
	}

	public static float gridToX() {
		return ((GameApplet.app.mouseX - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY() {
		return ((GameApplet.app.mouseY - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public boolean isCollision(GameObject e) {
		if (getRadius() == 0 || e.getRadius() == 0)
			return false;
		float f = PApplet.dist(getX(), getY(), e.getX(), e.getY());
		boolean b = f < getRadius() + e.getRadius() && e.isCollidable(this);
		// if(b)System.out.println(number + "/" + e.number + ":" + f + b);
		return b;
	}

	public boolean isCollidable(GameObject entity) {
		return true;
	}

	public boolean isInRange(float X, float Y, int R) {
		float f = PApplet.dist(getX(), getY(), X, Y);
		boolean b = f < R;
		// System.out.println(number + "/" + f + "/" + R + " " + b);
		return b;
	}

	public boolean isEnemyTo(Entity e) {
		return (e != null) && (this.player != null) && (e.player != null) && (this.player != e.player)
				&& (this.player != GameApplet.updater.neutral) && (e.player != GameApplet.updater.neutral) //
				&& isAlive() && e.isAlive();
	}

	protected boolean isAlive() {
		return true;
	}

	public boolean isEnemyTo(Player p) {
		return isEnemyTo(p.champion);
	}

	public boolean isAllyTo(GameObject e) {
		return (this.player == e.player);
	}

	public boolean isAllyTo(Player p) {
		return (this.player == p);
	}

	public boolean isVisibleTo(Player p) {
		boolean isVisible = false;
		for (GameObject spotter : GameApplet.updater.getGameObjects()) { 
			if (spotter.player == p && spotter.getSight() > 0
					&& spotter.isInRange(getX(), getY(), spotter.getSight() + getRadius()))
				isVisible = true;
			if (player == GameApplet.updater.neutral)
				isVisible = true;
		}
		return isVisible;
	}

	protected static String path(GameObject object) {
		String pack = object.getClass().getEnclosingClass().getPackage().getName();
		pack = pack.substring(pack.lastIndexOf('.') + 1, pack.length());

		String path = pack + "/" + object.getClass().getEnclosingClass().getSimpleName() + "/";
		return path;
	}

	protected static String path(Nation nation, GameObject object) {
		String path = nation.toString() + "/" + object.getClass().getEnclosingClass().getSimpleName() + "/";
		return path;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int i) {
		this.currentFrame = i;
	}

	public int getxSize() {
		return xSize;
	}

	/** set width of texture */
	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	/** sets height of texture */
	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	/** sets the colission-circle-size */
	public void setRadius(int i) {
		this.radius = i;
	}

	public int getSight() {
		return sight;
	}

	/** sets the sight-radius */
	public void setSight(int i) {
		this.sight = i;
	}

	public float calcImportanceOf(Entity e) {
		float importance = PApplet.abs(10000
				/ (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - getRadius() - e.getRadius()));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public String getInternName() {
		return this.getClass().getSimpleName();
	}

}
