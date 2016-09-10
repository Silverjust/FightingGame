package gameStructure.baseBuffs;

import shared.GameBaseApp;

public class Root extends Buff {
	public Root(GameBaseApp app, String[] c) {
		super(app, c);
	}

	@Override
	public void onStart() {
		super.onStart();
		owner.setRooted(true);
	}

	@Override
	public void onEnd() {
		super.onEnd();
		owner.setRooted(false);
	}
}
