package entity.neutral;

import entity.ActivesGrid;
import entity.ActivesGridHandler;
import entity.MainBuilding;
import shared.NationInfo;

public class NeutralInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return null;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return null;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return null;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return null;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return null;
	}

	@Override
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
	}

}
