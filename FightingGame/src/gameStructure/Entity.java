package gameStructure;

import java.util.ArrayList;

import champs.TestProjectile;
import gameStructure.animation.Death;
import gameStructure.baseBuffs.Buff;
import processing.core.PApplet;
import processing.core.PImage;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;
import shared.Player;

public class Entity extends GameObject {

	public PImage iconImg;
	public Death death;
	public int hpBarLength;
	private ArrayList<Buff> buffs = new ArrayList<Buff>();
	private ArrayList<Buff> buffsToRemove = new ArrayList<Buff>();

	public Entity(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void initStats(GameBaseApp app) {
		stats = new EntityStats(app);
	}

	@Override
	public EntityStats getStats() {
		return (gameStructure.EntityStats) stats;
	}

	@Override
	public void display() {
		drawHpBar();
	}

	public void sendDamage(TestProjectile testProjectile, Damage damage, Player player, String origin) {
		player.app.getUpdater().send(
				Coms.DAMAGE + " " + getNumber() + " " + damage.get() + " " + player.getUser().getIp() + " " + origin);
	}

	@Override
	public void updateDecisions(boolean isServer) {
		for (int i = buffsToRemove.size() - 1; i >= 0; i--) {
			Buff buff = buffsToRemove.get(i);
			if (buff != null) {
				buffs.remove(buff);
				buffsToRemove.remove(i);
			}
		}
		for (Buff buff : buffs) {
			buff.updateDecisions(isServer);
		}
	}

	@Override
	public void exec(String[] c) {
		super.exec(c);
		try {
			String string = c[2];
			if ("death".equals(string)) {
				if (this instanceof Unit)
					((Unit) this).setMoving(false);
				setAnimation(death);
			}
		} catch (Exception e) {
			System.err.println(getAnimation() + " " + getNextAnimation());
			PApplet.printArray(c);
			e.printStackTrace();
		}

	}

	public void info() {
		player.app.getDrawer().getHud().chat.println(this.getClass().getSimpleName() + "_" + getNumber(),
				"(" + getX() + "|" + getY() + ")" + "\nhp:" + getStats().getCurrentHp());
	}

	public void hit(int damage, Player attacker, String origin) {
		System.out.println("Entity.hit()" + getInternName() + " got hit with " + damage + " from "
				+ attacker.getUser().name + " with " + origin);
		if (isMortal()) {// only for nonimmortal objects
			// SoundHandler.startIngameSound(hit, x, y);

			if (damage > 0) {
				getStats().setHp((int) (getStats().getCurrentHp() - damage));
				/** check if it was lasthit */
				if (getStats().getCurrentHp() <= 0 && getStats().getCurrentHp() != Integer.MAX_VALUE) {// marker
					getStats().setHp(-32768);
					onDeath();
				}
			}
		}
	}

	public void heal(int heal) {
		getStats().setHp(getStats().getCurrentHp() + heal);
		/** check if it was overheal */
		if (getStats().getCurrentHp() > getStats().getTotalHp()) {
			getStats().setHp(getStats().getTotalHp());
		}
	}

	protected void onDeath() {
		sendAnimation("death", this);
	}

	void drawHpBar() {
		int h = 5;
		if (isAlive() && isMortal()) {//
			player.app.fill(0, 150);
			player.app.rect(xToGrid(getX()), yToGrid(getY()) - getStats().getRadius() * 1.5f, hpBarLength, h);
			player.app.tint(player.color);
			player.app.getDrawer().imageHandler.drawImage(player.app, hpImg, xToGrid(getX()),
					yToGrid(getY()) - getStats().getRadius() * 1.5f,
					hpBarLength * getStats().getCurrentHp() / getStats().getTotalHp(), h);
			player.app.tint(255);
		}
	}

	public float calcImportanceOf(Entity e) {
		float importance = PApplet
				.abs(10000 / (e.getStats().getCurrentHp() * PApplet.dist(getX(), getY(), e.getX(), e.getY())
						- getStats().getRadius() - e.getStats().getRadius()));
		// TODO speziefische Thread werte
		if (e instanceof Attacker) {
			importance *= 20;
		}
		return importance;
	}

	public boolean isAlive() {
		if (isMortal())
			return (getAnimation().getClass() != death.getClass()) && getStats().getCurrentHp() > 0;
		return true;
	}

	public boolean isMortal() {
		return death != null;
	}

	public void addBuff(Buff buff) {
		System.out.println("Entity.addBuff()");
		if (!Helper.listContainsInstanceOf(buff.getClass(), buffs)) {
			buff.onFirstApply(buffs);
		} else {
			buff.onReapply(buffs);
		}
	}

	public void removeBuff(Buff buff) {
		buff.onEnd();
		buffsToRemove.add(buff);

	}

}
