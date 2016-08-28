package gameStructure.baseBuffs;

import shared.Helper.Timer;

import java.util.ArrayList;

import game.GameBaseApp;
import gameStructure.Entity;
import gameStructure.GameObject;

public class Buff {

	protected int maxStacks;
	protected int stacks = 1;
	protected Entity owner;
	protected Timer timer;
	private GameObject source;

	public Buff(GameBaseApp app, String[] c) {
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

	public void onReapply(ArrayList<Buff> buffs) {
		if (doesStack()) {
			addStacks(1);
		}
		if (doesRefresh()) {
			refreshTime();
		}
	}

	public GameObject getOrigin() {
		return source;
	}

	public boolean doesRefresh() {
		return true;
	}

	public boolean doesStack() {
		return false;
	}

	public void addStacks(int i) {
		stacks += i;
		onStackApply(i);
	}

	public void refreshTime() {
		if (timer != null) {
			timer.startCooldown();
		}
	}

	public String getInternName() {
		return this.getClass().getSimpleName();
	}
}
