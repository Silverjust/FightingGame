package gameStructure.baseBuffs;

import game.GameApplet;

public class Root extends Buff {
	public Root(GameApplet app, String[] c) {
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
