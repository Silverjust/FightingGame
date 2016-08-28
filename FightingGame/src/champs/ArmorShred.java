package champs;

import game.GameBaseApp;
import gameStructure.baseBuffs.Buff;

public class ArmorShred extends Buff {
	private int amount = 30;
	private int totalAmount = 0;

	public ArmorShred(GameBaseApp app, String[] c) {
		super(app, c);
		maxStacks = 3;
	}

	@Override
	public void onStart() {
		super.onStart();
		owner.setArmor(owner.getArmor() - amount);
		totalAmount += amount;
	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.setArmor(owner.getArmor() - totalAmount);
	}

	@Override
	public boolean doesStack() {
		return stacks < maxStacks;
	}

	/** only apply 1 at a time */
	@Override
	protected void onStackApply(int i) {
		super.onStackApply(i);
		owner.setArmor(owner.getArmor() - amount);
		totalAmount += amount;
	}
}
