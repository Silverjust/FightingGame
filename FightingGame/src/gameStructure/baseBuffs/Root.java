package gameStructure.baseBuffs;

public class Root extends Buff {
	public Root(String[] c) {
		super(c);
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
