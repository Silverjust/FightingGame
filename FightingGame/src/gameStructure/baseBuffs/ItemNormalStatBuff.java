package gameStructure.baseBuffs;

import java.util.ArrayList;

import gameStructure.EntityStats;
import gameStructure.Stats;
import gameStructure.items.InventoryItem;
import gameStructure.items.InventoryItem.ItemNormalStat;
import shared.GameBaseApp;

public class ItemNormalStatBuff extends Buff {
	private ArrayList<ItemNormalStat> stats;

	public ItemNormalStatBuff(GameBaseApp app, String[] c) {
		super(app, c);
		if (c != null) {
			InventoryItem i = app.getContentListManager().createItem(app.getContentListManager().getItemClass(c[5]));
			stats = i.getNurmalStats();
		}
	}

	

}
