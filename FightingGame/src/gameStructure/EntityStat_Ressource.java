package gameStructure;

public class EntityStat_Ressource extends EntityStat_BonusAmount {
	private int currentAmount;

	public EntityStat_Ressource(Stats stats, String name) {
		super(stats, name);
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public void addCurrentAmount(int currentAmount) {
		this.currentAmount += currentAmount;
	}

	public void initAmount(int amount) {
		setCurrentAmount(amount);
		setBrutoBaseAmount(amount);
	}

}
