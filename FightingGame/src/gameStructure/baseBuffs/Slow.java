package gameStructure.baseBuffs;

import java.util.ArrayList;

import gameStructure.Unit;

public class Slow extends Buff {
	private int slow;
	private Num totalSlow;
	private ArrayList<Buff> buffs;

	public Slow(String[] c) {
		super(c);
		if (c != null && c.length > 5)
			slow = Integer.parseInt(c[5]);
	}

	@Override
	public void onStart() {
		super.onStart();
		calculateSlow();
	}

	public void calculateSlow() {
		if (owner instanceof Unit) {
			int maxSlow = 0;
			for (Buff buff : buffs) {
				if (buff instanceof Slow) {
					if (totalSlow == null)
						totalSlow = ((Slow) buff).totalSlow;
					if (((Slow) buff).slow > maxSlow && !((Slow) buff).timer.isNotOnCooldown())
						maxSlow = ((Slow) buff).slow;
					System.out.println(
							"Slow.calculateSlow()1 " + ((Slow) buff).slow + " " + ((Slow) buff).timer.getTimeLeft());
				}
			}
			System.out.println(
					"Slow.calculateSlow()2 " + maxSlow + " " + (((Unit) owner).getSpeedMult() + totalSlow.get()) + " "
							+ (((Unit) owner).getSpeedMult() + totalSlow.get() - maxSlow));
			((Unit) owner).setSpeedMult(((Unit) owner).getSpeedMult() + totalSlow.get() - maxSlow);
			System.out.println("Slow.calculateSlow()3 " + maxSlow + " " + ((Unit) owner).getSpeed());

			totalSlow.set(maxSlow);

		}
	}

	@Override
	public void onEnd() {
		super.onEnd();
		calculateSlow();
	}

	@Override
	public void onFirstApply(ArrayList<Buff> buffs) {
		this.buffs = buffs;
		totalSlow = new Num();
		super.onFirstApply(buffs);
	}

	@Override
	public void onReapply(ArrayList<Buff> buffs) {
		this.buffs = buffs;

		buffs.add(this);
		onStart();
	}

	class Num {
		private int i = 0;

		public int get() {
			return i;
		}

		public void set(int i) {
			this.i = i;
		}
	}
}
