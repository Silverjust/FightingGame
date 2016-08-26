package game.aim;

import game.AimHandler.Cursor;
import gameStructure.AimingActive;

public class CustomAim extends Aim {

	private   Cursor cursor;
	private AimingActive aiming;

	public CustomAim(AimingActive a,Cursor c) {
		aiming=a;
		cursor=c;
	}
	@Override
	public Cursor getCursor() {
		return cursor;
	}

	@Override
	public void execute(float x, float y) {
		aiming.execute( x,  y);
	}

}
