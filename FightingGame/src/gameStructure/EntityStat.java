package gameStructure;

public class EntityStat {

	int brutoBaseAmount;
	int baseAmountMult = 100;
	protected Stats stats;

	public EntityStat(Stats stats, String name) {
		this.stats = stats;
		stats.getStats().put(name, this);
	}

	public int getBrutoBaseAmount() {
		return brutoBaseAmount;
	}

	public void setBrutoBaseAmount(int brutoBaseAmount) {
		this.brutoBaseAmount = brutoBaseAmount;
	}

	public void addBrutoBaseAmount(int brutoBaseAmount) {
		this.brutoBaseAmount += brutoBaseAmount;
	}

	public int getBaseAmountMult() {
		return baseAmountMult;
	}

	public void setBaseAmountMult(int baseAmountMult) {
		this.baseAmountMult = baseAmountMult;
	}

	public void addBaseAmountMult(int baseAmountMult) {
		this.baseAmountMult += baseAmountMult;
	}

	/** use getTotalAmount */
	public int getTotalBaseAmount() {
		return (int) (getBrutoBaseAmount() * getBaseAmountMult() / 100.f);
	}

	public int getTotalAmount() {
		return getTotalBaseAmount();
	}

	/** use if unclear this is base or bonus */
	public int getBrutoBonusAmount() {
		return getBrutoBaseAmount();
	}

	/** use if unclear this is base or bonus */
	public void setBrutoBonusAmount(int brutoBonusAmount) {
		setBrutoBaseAmount(brutoBonusAmount);
	}

	/** use if unclear this is base or bonus */
	public void addBrutoBonusAmount(int brutoBonusAmount) {
		addBrutoBaseAmount(brutoBonusAmount);
	}

	/** use if unclear this is base or bonus */
	public int getTotalAmountMult() {
		return getBaseAmountMult();
	}

	/** use if unclear this is base or bonus */
	public void setTotalAmountMult(int totalAmountMult) {
		setBaseAmountMult(totalAmountMult);
	}

	/** use if unclear this is base or bonus */
	public void addTotalAmountMult(int totalAmountMult) {
		addBaseAmountMult(totalAmountMult);
	}

}