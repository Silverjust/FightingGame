package gameStructure;

import game.GameDrawer;
import game.ImageHandler;
import gameStructure.animation.Animation;
import gameStructure.animation.Attack;
import gameStructure.baseBuffs.events.Event;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import shared.Coms;
import shared.ContentListManager;
import shared.GameBaseApp;
import shared.Player;
import shared.Updater;

public abstract class GameObject implements Coms {
	// TODO upgrades für einheiten
	private int number;
	public Player player;

	public boolean isTaged;

	private float x, y;
	protected Stats stats;
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

	public static void loadImages(GameBaseApp app, ImageHandler imageHandler) {
		String path = path(new GameObject(null, null) {
		});
		shadowImg = imageHandler.load(path, "shadow");
		selectedImg = imageHandler.load(path, "selected");
		hpImg = imageHandler.load(path, "hp");

		// hit = ref.minim.loadSnippet("test.mp3");
	}

	public GameObject(GameBaseApp app, String[] c) {
		initStats(app);
		if (c != null) {
			player = app.getUpdater().getPlayer(c[3]);
			setX(Float.parseFloat(c[4]));
			setY(Float.parseFloat(c[5]));
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
		if (this instanceof Entity && ((Entity) this).getBasicAttack() != null) {
			Attack a = ((Entity) this).getBasicAttack();
			drawCircle(a.getRange());
			drawCircle((int) (a.getRange() * a.getCooldownPercent()));
		}
		if (this instanceof Unit) {
			player.app.line(getX(), getY() / 2, ((Unit) this).xTarget, ((Unit) this).yTarget / 2);
		}
		if (this instanceof Entity) {
			/*
			 * Buff buff = ((Entity)this).getBuff(Ticul.ArmorShred.class); if
			 * (buff != null) player.app.rect(getX(), getY()/2, 20,
			 * 20*buff.getCooldownPercent());
			 */
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

	/** empty */
	public void onSpawn(boolean isServer) {
	}

	protected void drawShadow() {
		player.app.getDrawer().getImageHandler().drawImage(player.app, shadowImg, xToGrid(getX()), yToGrid(getY()),
				getStats().getRadius().getTotalAmount() * 2, getStats().getRadius().getTotalAmount());
	}

	protected void drawTaged() {
		/** just for debug */
		if (isTaged) {
			player.app.fill(0, 0);
			player.app.stroke(player.color);
			player.app.rect(xToGrid(getX()), yToGrid(getY()) - getStats().getRadius().getTotalAmount() * 0.3f,
					getStats().getRadius().getTotalAmount() * 2, getStats().getRadius().getTotalAmount() * 1.5f);
			player.app.stroke(0);
		}
		isTaged = false;
	}

	public void drawBar(float f) {
		int h = 1;
		if (true) {//
			player.app.fill(0, 150);
			player.app.rect(xToGrid(getX()), yToGrid(getY() - h * 3) - getStats().getRadius().getTotalAmount() * 1.5f,
					getStats().getRadius().getTotalAmount() * 2, h);
			player.app.tint(200);
			player.app.getDrawer().getImageHandler().drawImage(player.app, hpImg, xToGrid(getX()),
					yToGrid(getY() - h * 3) - getStats().getRadius().getTotalAmount() * 1.5f,
					getStats().getRadius().getTotalAmount() * 2 * f, h);
			player.app.tint(255);
		}
	}

	public void drawBar(float f, int c) {
		int h = 1;
		if (true) {//
			player.app.fill(0, 150);
			player.app.rect(xToGrid(getX()), yToGrid(getY() - h * 3) - getStats().getRadius().getTotalAmount() * 1.5f,
					getStats().getRadius().getTotalAmount() * 2, h);
			player.app.tint(c);
			player.app.getDrawer().getImageHandler().drawImage(player.app, hpImg, xToGrid(getX()),
					yToGrid(getY() - h * 3) - getStats().getRadius().getTotalAmount() * 1.5f,
					getStats().getRadius().getTotalAmount() * 2 * f, h);
			player.app.tint(255);
		}
	}

	protected void drawCircle(int r) {
		player.app.getDrawer().getImageHandler().drawImage(player.app, selectedImg, xToGrid(getX()), yToGrid(getY()),
				r * 2, r);
	}

	protected void drawCircle(float x, float y, byte range) {
		player.app.getDrawer().getImageHandler().drawImage(player.app, selectedImg, xToGrid(x), yToGrid(y), range * 2,
				range);
	}

	void drawLine(float tx, float ty) {
		player.app.line(xToGrid(getX()), yToGrid(getY()), tx, ty / 2);
	}

	public void drawOnMinimap(PGraphics graphics) {
	}

	public void drawOnMinimapUnder(PGraphics graphics) {
	}

	public void drawSight(Updater updater) {
		int scale = updater.map.fogScale;
		updater.map.fogOfWar.ellipse(getX() / scale, getY() / scale / 2,
				getStats().getSight().getTotalAmount() * 2 / scale, getStats().getSight().getTotalAmount() / scale);
	}

	public void sendDefaultAnimation(Animation oldAnimation) {
		sendAnimation("stand", this);
	}

	public Animation getDefaultAnimation() {
		return null;
	}

	public void sendAnimation(String s, Object o) {
		if (s != "") {
			if (o instanceof gameStructure.champs.Ticul) {
				System.out.println("GameObject.sendAnimation()ticul " + ((gameStructure.champs.Ticul) o).getNumber());
			}
			player.app.getUpdater().send(Coms.EXECUTE + " " + getNumber() + " " + s);
		}
	}

	public void setAnimation(Animation a) {
		if ((a != null && animation == null) || (a != null && animation.isInterruptable() && animation != a)) {
			if (Animation.observe.isAssignableFrom(this.getClass())) {
				System.out.println("Entity.setAnimation()" + a.getName(this));
			}

			if (animation == null)
				animation = a;
			animation.onEnd();
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

	public static float gridToX(int x) {
		return ((x - GameDrawer.xMapOffset) / GameDrawer.zoom);
	}

	public static float gridToY(int y) {
		return ((y - GameDrawer.yMapOffset) / GameDrawer.zoom * 2);
	}

	public boolean isCollision(GameObject e) {
		if (getStats().getRadius().getTotalAmount() == 0 || e.getStats().getRadius().getTotalAmount() == 0)
			return false;
		float f = PApplet.dist(getX(), getY(), e.getX(), e.getY());
		boolean b = f < getStats().getRadius().getTotalAmount() + e.getStats().getRadius().getTotalAmount()
				&& e.isCollidable(this);
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

	public boolean isInRange(GameObject target, int range) {
		return isInRange(target.getX(), target.getY(), range);
	}

	public boolean isEnemyTo(GameObject o) {
		return (o != null) && (this.player != null) && (o.player != null)
				&& this.player.getUser().team != o.player.getUser().team && isAlive() && o.isAlive();
	}

	public boolean isAlive() {
		return true;
	}

	public boolean isEnemyTo(Player p) {
		return isEnemyTo(p.getChampion());
	}

	public boolean isAllyTo(GameObject e) {
		return (this.player == e.player);
	}

	public boolean isAllyTo(Player p) {
		return (this.player == p);
	}

	public boolean isVisibleTo(Player p) {
		boolean isVisible = false;
		for (GameObject spotter : player.app.getUpdater().getGameObjects()) {
			if (spotter.player == p && spotter.getStats().getSight().getTotalAmount() > 0 && spotter.isInRange(getX(),
					getY(), spotter.getStats().getSight().getTotalAmount() + getStats().getRadius().getTotalAmount()))
				isVisible = true;
			if (player == player.app.getUpdater().neutral)
				isVisible = true;
		}
		return isVisible;
	}

	protected static String path(GameObject object) {
		return object.getClass().getEnclosingClass().getName().replace('.', '/').replace('$', '/') + "/";
	}

	@Deprecated
	protected static String path1(GameObject object) {
		String path = object.getClass().getEnclosingClass().getSimpleName() + "/";
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

	public float calcImportanceOf(Entity e) {
		float importance = PApplet
				.abs(10000 / (e.getStats().getHp().getCurrentAmount() * PApplet.dist(getX(), getY(), e.getX(), e.getY())
						- getStats().getRadius().getTotalAmount() - e.getStats().getRadius().getTotalAmount()));
		// TODO speziefische Thread werte

		return importance;
	}

	public String getInternName() {
		return this.getClass().getSimpleName();
	}

	public void info() {

	}

	public String getIngameName() {
		return getInternName();
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Stats getStats() {
		return stats;
	}

	public void initStats(GameBaseApp app) {
		stats = new Stats(app);
	}

	/** empty */
	public void onRegistration(ContentListManager contentListManager) {

	}

	public boolean isTargetable() {
		return false;
	}

	public void onEvent(Event event, boolean isServer) {

	}

	public void drawTargetInfo() {
		// TODO Auto-generated method stub
		
	}



}
