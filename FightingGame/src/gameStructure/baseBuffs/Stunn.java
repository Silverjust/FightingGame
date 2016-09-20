package gameStructure.baseBuffs;

import shared.GameBaseApp;

public class Stunn extends Buff {
	public Stunn(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void onStart() {
		super.onStart();
		owner.getStats().setStunned(true);
		onHardCC();
	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.getStats().setStunned(false);
	}
}
