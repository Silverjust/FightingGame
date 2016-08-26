package entity.neutral;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import game.AimHandler;
import game.AimHandler.Cursor;
import game.GameApplet;
import game.HUD;
import game.ImageHandler;
import game.aim.CustomAim;
import gameStructure.Spell;
import gameStructure.AimingActive;
import gameStructure.Attacker;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.GameObject;
import gameStructure.animation.Animation;
import gameStructure.animation.Death;

public class SandboxBuilding extends Building implements Commander {

	private static PImage standImg;

	public static void loadImages() {
		String path = path(new GameObject() {
		});
		standImg = game.ImageHandler.load(path, "SandboxBuilding");
	}

	public static int commandRange = 0;
	private int hp;
	private int hp_max;
	private int armor;
	public PImage iconImg;
	protected String descr = " ";
	protected String stats = " ";
	public Death death;

	public SandboxBuilding(String[] c) {
		super(c);
		player = GameApplet.player;// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);

		// ************************************
		setxSize(30);
		setySize(30);

		setRadius(15);

		descr = "only for sandbox";
		// ************************************
	}

	@Override
	public void renderGround() {
		super.renderGround();
		getAnimation().draw(this, (byte) 0, getCurrentFrame());
	}

	@Override
	public PImage preview() {
		System.out.println("woat?");
		GameApplet.updater.send("<say SERVER " + player.getUser().name + "cheats");
		return standImg;
	}

	@Override
	public int commandRange() {
		return commandRange;
	}

	public void hit(int damage, byte pirce) {
	
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);
	
			setHp((int) (getHp() - damage * (1.0 - ((armor - pirce > 0) ? armor - pirce : 0) * 0.1)));
			/** check if it was lasthit */
			if (getHp() <= 0 && getHp() != Integer.MAX_VALUE) {// marker
				setHp(-32768);
				onDeath();
			}
	
		}
	}

	public void heal(int heal) {
		setHp(getHp() + heal);
		/** check if it was overheal */
		if (getHp() > hp_max) {
			setHp(hp_max);
		}
	}

	protected void onDeath() {
		sendAnimation("death");
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameApplet.app.fill(0, 150);
			GameApplet.app.rect(xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f, getRadius() * 2, h);
			GameApplet.app.tint(player.color);
			ImageHandler.drawImage(GameApplet.app, hpImg, xToGrid(getX()), yToGrid(getY()) - getRadius() * 1.5f,
					getRadius() * 2 * getHp() / hp_max, h);
			GameApplet.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - getRadius() - e.getRadius()));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public boolean isAlive() {
		if (isMortal())
			return (getAnimation().getClass() != death.getClass()) && getHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void initHp(int hp) {
		this.hp = hp;
		this.hp_max = hp;
	}

	public static class BuildSetup extends Spell {

		public BuildSetup(int x, int y, char n) {
			super(x, y, n, standImg);
			setClazz(SandboxBuilding.class);
		}

		@Override
		public void onActivation() {
			HUD.activesGrid.setupSandbox();
		}

		@Override
		public String getDescription() {
			return "start mapedit";
		}

	}

	public static class DeleteActive extends Spell implements AimingActive {

		public DeleteActive(int x, int y, char n) {
			super(x, y, n, standImg);
			setClazz(SandboxBuilding.class);
		}

		@Override
		public void onActivation() {
			AimHandler.setAim(new CustomAim(this, Cursor.SHOOT));
		}

		@Override
		public String getDescription() {
			return "delete";
		}

		@Override
		public void execute(float x, float y) {
			for (GameObject e2 : GameApplet.updater.gameObjects) {
				if (e2 != null && e2.isInRange(x, y, e2.getRadius() + 10)) {
					GameApplet.updater.send("<remove " + e2.number);
				}
			}
		}

	}

	public static class ChangeSide extends Spell {

		public ChangeSide(int x, int y, char n) {
			super(x, y, n, standImg);
			setClazz(SandboxBuilding.class);
		}

		@Override
		public void onActivation() {
			int i = 0;
			for (GameObject e : GameApplet.updater.gameObjects) {
				if (e instanceof SandboxBuilding) {
					i = new ArrayList<String>(GameApplet.updater.players.keySet())
							.indexOf(e.player.getUser().ip) + 1;
				}
			}
			if (i >= GameApplet.updater.players.keySet().size())
				i = 0;
			for (GameObject e : GameApplet.updater.gameObjects) {
				if (e instanceof SandboxBuilding) {
					e.player = GameApplet.updater.players.get(new ArrayList<String>(
							GameApplet.updater.players.keySet()).get(i));
				}
			}

		}

		@Override
		public String getDescription() {
			return "switch player";
		}

	}

	public static class AddPlayer extends Spell {

		public AddPlayer(int x, int y, char n) {
			super(x, y, n, standImg);
			setClazz(SandboxBuilding.class);
		}

		@Override
		public void onActivation() {
			GameApplet.preGame.addPlayer((GameApplet.updater.players.size() + 1) + "",
					"player" + (GameApplet.updater.players.size() + 1));
		}

		@Override
		public String getDescription() {
			return "add player";
		}

	}
}
