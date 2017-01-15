package gameStructure.items;

import gameStructure.Stats;
import gameStructure.items.ItemBuff.StackingStatsBuff;
import shared.GameBaseApp;

public class ArmorOfWar extends InventoryItem {

	public ArmorOfWar(GameBaseApp app, String[] c) {
		super(app, c);
		stackingStatsBuff = new StackingStatsBuff(app, getBuffInitInfo());
		stackingStatsBuff.itemStats.add(new ItemNormalStat(Stats.HEALTH, 500));
		stackingStatsBuff.itemStats.add(new ItemNormalStat(Stats.ARMOR, 50));
		descrText = "+500 §img hp§ \n +50 §img armor§";
	}

}
