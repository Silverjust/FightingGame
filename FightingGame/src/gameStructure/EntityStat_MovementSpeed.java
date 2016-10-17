package gameStructure;

public class EntityStat_MovementSpeed extends EntityStat_BonusAmount {

	public EntityStat_MovementSpeed(Stats stats, String name) {
		super(stats, name);
	}

	@Override
	public void setTotalAmountMult(int totalAmountMult) {
		if (totalAmountMult > 0)
			super.setTotalAmountMult(totalAmountMult);
	}

	public float getTotalAmountF() {
		return super.getTotalAmount() / 100.f;
	}

	public float getBaseAmountPerLvlF() {
		return super.getBaseAmountPerLvl() / 100.f;
	}

	public float getBrutoBaseAmountF() {
		return super.getBrutoBaseAmount() / 100.f;
	}

	public float getBrutoBonusAmountF() {
		return super.getBrutoBonusAmount() / 100.f;
	}

	public float getTotalBaseAmountF() {
		return super.getTotalBaseAmount() / 100.f;
	}

	public float getTotalBonusAmountF() {
		return super.getTotalBonusAmount() / 100.f;
	}

	@Deprecated
	@Override
	public int getBaseAmountPerLvl() {
		return super.getBaseAmountPerLvl();
	}

	@Deprecated
	@Override
	public int getBrutoBaseAmount() {
		return super.getBrutoBaseAmount();
	}

	@Deprecated
	@Override
	public int getBrutoBonusAmount() {
		return super.getBrutoBonusAmount();
	}

	@Deprecated
	@Override
	public int getTotalAmount() {
		return super.getTotalAmount();
	}

	@Deprecated
	@Override
	public int getTotalBaseAmount() {
		return super.getTotalBaseAmount();
	}

	@Deprecated
	@Override
	public int getTotalBonusAmount() {
		return super.getTotalBonusAmount();
	}
}
