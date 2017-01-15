package gameStructure.items;

import java.util.ArrayList;

import gameStructure.EntityStats;
import gameStructure.baseBuffs.Buff;
import gameStructure.items.InventoryItem.ItemNormalStat;
import shared.GameBaseApp;

public class ItemBuff extends Buff {

	protected ArrayList<ItemNormalStat> itemStats = new ArrayList<ItemNormalStat>();

	public ItemBuff(GameBaseApp app, String[] c) {
		super(app, c);

	}

	@Override
	public void onStart() {
		super.onStart();
		EntityStats stats = owner.getStats();
		for (ItemNormalStat stat : itemStats) {
			stats.get(stat.statName).addBrutoBonusAmount(stat.amount);
			System.out.println("ItemBuff.onStart() add " + stat.statName + " " + stat.amount + " to "
					+ owner.getNumber() + " " + app.isServer());

		}
	}

	@Override
	public void onEnd() {
		System.out.println("ItemBuff.onEnd()");
		super.onEnd();
		EntityStats stats = owner.getStats();
		for (ItemNormalStat stat : itemStats) {
			stats.get(stat.statName).addBrutoBonusAmount(-stat.amount);
		}

	}

	static class StackingStatsBuff extends ItemBuff {

		public StackingStatsBuff(GameBaseApp app, String[] c) {
			super(app, c);
		}

		@Override
		public boolean doesStack() {
			return false;
		}

		@Override
		public void onReapply(ArrayList<Buff> buffs, Buff newBuff) {
			System.out.println("ItemBuff.StackingStatsBuff.onReapply()");
			buffs.add(this);
			newBuff.onStart();
		}
	}

	static class UniqueStatsBuff extends ItemBuff {

		public UniqueStatsBuff(GameBaseApp app, String[] c) {
			super(app, c);
		}

		@Override
		public boolean doesStack() {
			return false;
		}
	}
}
