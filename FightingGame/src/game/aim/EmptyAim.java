package game.aim;

import shared.ref;
import game.AimHandler.Cursor;
import gameStructure.GameObject;
import gameStructure.Unit;

public class EmptyAim extends Aim {

	private boolean aggresive;

	public EmptyAim() {
	}

	@Override
	public Cursor getCursor() {
		if (!aggresive)
			return Cursor.MOVE;
		return Cursor.ARROW;
	}

	@Override
	public void execute(float x, float y) {

	}
}
