package gameStructure.items;

import gameStructure.Stats;
import gameStructure.items.ItemBuff.StackingStatsBuff;
import shared.GameBaseApp;

public class SwordOfTesting extends InventoryItem {

	public SwordOfTesting(GameBaseApp app, String[] c) {
		super(app, c);
		stackingStatsBuff = new StackingStatsBuff(app, getBuffInitInfo());
		stackingStatsBuff.itemStats.add(new ItemNormalStat(Stats.ATTACK_DAMAGE, 100));
		
		descrText="+100 §img ad§";
	}

}
