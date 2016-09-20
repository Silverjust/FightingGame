package gameStructure.baseBuffs;

import shared.GameBaseApp;

public class Silence extends Buff {
	public Silence(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void onStart() {
		super.onStart();
		owner.getStats().setSilenced(true);
		onHardCC();

	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.getStats().setRooted(false);
	}
}
