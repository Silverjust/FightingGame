package gameStructure.baseBuffs;

import java.util.ArrayList;

import gameStructure.Entity;
import gameStructure.GameObject;
import gameStructure.baseBuffs.events.Event;
import shared.GameBaseApp;
import shared.Helper.Timer;

public class Buff {

	protected int maxStacks;
	private int stacks = 1;
	protected Entity owner;
	protected Timer timer;
	private GameObject source;
	protected GameBaseApp app;

	public Buff(GameBaseApp app, String[] c) {
		this.app = app;
		if (c != null) {
			int n_o = Integer.parseInt(c[2]);
			owner = (Entity) app.getUpdater().getGameObject(n_o);
			int n_s = Integer.parseInt(c[3]);
			source = (Entity) app.getUpdater().getGameObject(n_s);
			if (c.length > 4 && !c[4].equals("-"))
				timer = new Timer(Integer.parseInt(c[4]));
		}
	}

	public void updateDecisions(boolean isServer) {
		// System.out.println("Buff.updateDecisions()" + timer.getTimeLeft());
		//System.out.println("Buff.updateDecisions() " + getStacks());
		if (timer != null && timer.isNotOnCooldown()) {
			owner.removeBuff(this);
		}
	}

	/** does the Buff-stuff */
	public void onStart() {
		System.out.println("Buff.onStart()");
		if (timer != null) {
			timer.startCooldown();
		}
	}

	/** undoes the Buff-stuff */
	public void onEnd() {
	}

	protected void onStackApply(int i) {
	}

	public void onFirstApply(ArrayList<Buff> buffs) {
		buffs.add(this);
		onStart();
	}

	public void onReapply(ArrayList<Buff> buffs, Buff newBuff) {
		//System.out.println("Buff.onReapply()1 " + getStacks());
		if (doesStack()) {
			addStacks(1);
		}
		//System.out.println("Buff.onReapply()2 " + getStacks());
		if (doesRefresh()) {
			refreshTime();
		}
	}

	public GameObject getSource() {
		return source;
	}

	public boolean doesRefresh() {
		return true;
	}

	public boolean doesStack() {
		return false;
	}

	public void addStacks(int i) {
		System.out.println("Buff.addStacks()1 " + app.isServer() + getStacks());
		stacks += i;
		System.out.println("Buff.addStacks()2 " + app.isServer() + getStacks());
		onStackApply(i);
		System.out.println("Buff.addStacks()3 " + app.isServer() + getStacks());

	}

	public int getStacks() {
		//System.out.println("Buff.getStacks()" + app.isServer() + getStacks());
		return stacks;

	}

	public void setStacks(int stacks) {
		System.out.println("Buff.setStacks()" + app.isServer() + stacks);
		this.stacks = stacks;
	}

	public void refreshTime() {
		if (timer != null) {
			timer.startCooldown();
		}
	}

	public String getInternName() {
		return this.getClass().getSimpleName();
	}

	/** interrupt channel abilities */
	public void onHardCC() {
	}

	/** interrupts dashes */
	public void onDisplace() {
	}

	public boolean isNotOnCooldown() {
		return timer.isNotOnCooldown();
	}

	public float getCooldownPercent() {
		if (timer == null)
			return 0;
		return timer.getCooldownPercent();
	}

	public float getTimeLeft() {
		return timer.getTimeLeft();
	}

	public void onEvent(Event event) {

	}

	public void draw(int x, int y, int s) {
		app.rect(x, y, s, s);
		app.fill(100);
		app.rect(x, y, (1 - getCooldownPercent()) * s, s);
		app.fill(0);
		app.textSize(20);
		app.text(getStacks(), x, y + app.textAscent());
		// System.out.println("Buff.draw()" + stacks);

	}
}
