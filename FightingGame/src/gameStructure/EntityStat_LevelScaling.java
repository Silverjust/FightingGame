package gameStructure;

public class EntityStat_LevelScaling extends EntityStat {
	int baseAmountPerLvl;

	public EntityStat_LevelScaling(Stats stats, String name) {
		super(stats, name);
	}

	public int getBaseAmountPerLvl() {
		return baseAmountPerLvl;
	}

	public void setBaseAmountPerLvl(int baseAmountPerLvl) {
		this.baseAmountPerLvl = baseAmountPerLvl;
	}

	public void addBaseAmountPerLvl(int baseAmountPerLvl) {
		this.baseAmountPerLvl += baseAmountPerLvl;
	}

	/** use getTotalAmount */
	@Override
	public int getTotalBaseAmount() {
		return (int) ((getBrutoBaseAmount() + getBaseAmountPerLvl() * stats.getLevel()) * getBaseAmountMult() / 100.f);
	}
}
