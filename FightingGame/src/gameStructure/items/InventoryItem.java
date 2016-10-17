package gameStructure.items;

import gameStructure.Entity;
import gameStructure.items.ItemBuff.StackingStatsBuff;
import shared.ContentListManager;
import shared.GameBaseApp;

public class InventoryItem {

	protected GameBaseApp app;
	protected Entity owner;
	protected StackingStatsBuff stackingStatsBuff;
	protected StackingStatsBuff uniqueStatsBuff;

	public InventoryItem(GameBaseApp app, String[] c) {
		this.app = app;
		if (c != null) {
			int n_o = Integer.parseInt(c[2]);
			owner = (Entity) app.getUpdater().getGameObject(n_o);
		}
	}

	public void giveStats() {
		if (stackingStatsBuff != null)
			owner.addBuff(stackingStatsBuff);
		if (uniqueStatsBuff != null)
			owner.addBuff(uniqueStatsBuff);
	}

	public void onRegistration(ContentListManager contentListManager) {
	}

	protected String[] getBuffInitInfo() {
		if (owner == null)
			return null;
		return new String[] { "", "", owner.getNumber() + "", owner.getNumber() + "", "-" };
	}

	public class ItemNormalStat {
		public String statName;
		public int amount;

		public ItemNormalStat(String statName, int amount) {
			this.statName = statName;
			this.amount = amount;
		}

	}

	public String getInternName() {
		return getClass().getSimpleName();
	}
}
