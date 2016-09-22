package shared;

import java.util.ArrayList;

import gameStructure.Champion;
import gameStructure.Spell;

public abstract class SpellHandler {

	protected ArrayList<Spell> spells = new ArrayList<Spell>();
	protected GameBaseApp app;
	protected Player player;

	public abstract Spell addSpell(Spell spell) ;

	public void registerChampion(Champion champion) {
		for (Spell spell : spells) {
			spell.registerChampion(champion);
		}
	}

}
