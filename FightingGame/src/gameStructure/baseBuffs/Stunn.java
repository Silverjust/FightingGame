package gameStructure.baseBuffs;

import shared.GameBaseApp;

public class Stunn extends Buff {
	public Stunn(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void onStart() {
		super.onStart();
		owner.setStunned(true);
		owner.onHardCC(this);
	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.setStunned(false);
	}
}
