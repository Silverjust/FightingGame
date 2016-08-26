package shared;

import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import game.ActivesGridHandler;
import gameStructure.MainBuilding;

public abstract class NationInfo {
	public abstract Class<? extends MainBuilding> getMainBuilding();

	public abstract Class<? extends KeritMine> getKeritMine();

	public abstract Class<? extends PaxDrillTower> getPaxDrillTower();

	public abstract Class<? extends ArcanumMine> getArcanumMine();

	public abstract Class<? extends PrunamHarvester> getPrunamHarvester();

	public abstract void setupActives(ActivesGrid grid, ActivesGridHandler handler);

}
