package entity.neutral;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import shared.GameBaseApp;
import game.AimHandler;
import game.AimHandler.Cursor;
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
		player = GameBaseApp.getPlayer();// neutral

		iconImg = standImg;
		stand = new Animation(standImg, 1000);
		build = null;
		death = null;

		setAnimation(stand);

		// ************************************
		setxSize(30);
		setySize(30);

		stats.setRadius(15);

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
		GameBaseApp.getUpdater().sendDirect("<say SERVER " + player.getUser().name + "cheats");
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
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 1;
		if (isAlive() && isMortal()) {//
			GameBaseApp.app.fill(0, 150);
			GameBaseApp.app.rect(xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f, stats.getRadius() * 2, h);
			GameBaseApp.app.tint(player.color);
			ImageHandler.drawImage(GameBaseApp.app, hpImg, xToGrid(getX()), yToGrid(getY()) - stats.getRadius() * 1.5f,
					stats.getRadius() * 2 * getHp() / hp_max, h);
			GameBaseApp.app.tint(255);
		}
	}

	public float calcImportanceOf(GameObject e) {
		float importance = PApplet.abs(
				10000 / (e.getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY()) - stats.getRadius() - e.getStats().getRadius()));
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
			for (GameObject e2 : GameApplet.GameBaseApp.gameObjects) {
				if (e2 != null && e2.isInRange(x, y, e2.getStats().getRadius() + 10)) {
					GameBaseApp.getUpdater().sendDirect("<remove " + e2.getNumber());
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
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e instanceof SandboxBuilding) {
					i = new ArrayList<String>(GameApplet.GameBaseApp.players.keySet())
							.indexOf(e.player.getUser().getIp()) + 1;
				}
			}
			if (i >= GameApplet.GameBaseApp.players.keySet().size())
				i = 0;
			for (GameObject e : GameApplet.GameBaseApp.gameObjects) {
				if (e instanceof SandboxBuilding) {
					e.player = GameApplet.GameBaseApp.players.get(new ArrayList<String>(
							GameApplet.GameBaseApp.players.keySet()).get(i));
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
			GameBaseApp.getPreGameInfo().addPlayer((GameApplet.GameBaseApp.players.size() + 1) + "",
					"player" + (GameApplet.GameBaseApp.players.size() + 1));
		}

		@Override
		public String getDescription() {
			return "add player";
		}

	}
}
