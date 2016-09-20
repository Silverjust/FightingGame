package champs;

import gameStructure.baseBuffs.Buff;
import shared.GameBaseApp;

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
		owner.getStats().setArmor(owner.getStats().getArmor() - amount);
		totalAmount += amount;
	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.getStats().setArmor(owner.getStats().getArmor() - totalAmount);
	}

	@Override
	public boolean doesStack() {
		return stacks < maxStacks;
	}

	/** only apply 1 at a time */
	@Override
	protected void onStackApply(int i) {
		super.onStackApply(i);
		owner.getStats().setArmor(owner.getStats().getArmor() - amount);
		totalAmount += amount;
	}
}
