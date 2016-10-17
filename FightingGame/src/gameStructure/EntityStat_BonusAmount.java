package gameStructure;

public class EntityStat_BonusAmount extends EntityStat_LevelScaling {
	int brutoBonusAmount, totalAmountMult = 100;

	public EntityStat_BonusAmount(Stats stats, String name) {
		super(stats, name);
	}

	@Override
	public int getBrutoBonusAmount() {
		return brutoBonusAmount;
	}

	@Override
	public void setBrutoBonusAmount(int brutoBonusAmount) {
		this.brutoBonusAmount = brutoBonusAmount;
	}

	@Override
	public void addBrutoBonusAmount(int brutoBonusAmount) {
		this.brutoBonusAmount += brutoBonusAmount;
	}

	@Override
	public int getTotalAmountMult() {
		return totalAmountMult;
	}

	@Override
	public void setTotalAmountMult(int totalAmountMult) {
		this.totalAmountMult = totalAmountMult;
	}

	@Override
	public void addTotalAmountMult(int totalAmountMult) {
		this.totalAmountMult += totalAmountMult;
	}

	public int getTotalBonusAmount() {
		return getBrutoBonusAmount();
	}

	@Override
	public int getTotalAmount() {
		return (int) ((getTotalBaseAmount() + getTotalBonusAmount()) * getTotalAmountMult() / 100.f);
	}
}
