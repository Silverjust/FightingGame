package gameStructure.baseBuffs;

import gameStructure.Unit;
import shared.GameBaseApp;

public class SpeedBuff extends Buff {
	private int amount = 0;

	public SpeedBuff(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null && c.length > 5)
			amount = Integer.parseInt(c[5]);
	}

	@Override
	public void onStart() {
		super.onStart();
		((Unit) owner).getStats().getMovementSpeed()
				.setTotalAmountMult(((Unit) owner).getStats().getMovementSpeed().getTotalAmountMult() + amount);
	}

	@Override
	public void onEnd() {
		super.onEnd();
		((Unit) owner).getStats().getMovementSpeed()
				.setTotalAmountMult(((Unit) owner).getStats().getMovementSpeed().getTotalAmountMult() - amount);
	}

}
