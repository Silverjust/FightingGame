package entity.humans;

import entity.ActivesGrid;
import entity.ActivesGridHandler;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.aim.MineAim.BuildMineActive;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.Trainer;
import gameStructure.Unit;
import gameStructure.actives.BuildWallActive;
import shared.NationInfo;

public class HumanInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return HumanMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return HumanKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return HumanPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return HumanArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return HumanPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		ActivesGrid unitActives = grid.createTab(1, 1, Unit.class, handler, ActivesGrid.UNITS);
		unitActives.addActive(1, 1, Unit.AttackActive.class);
		unitActives.addActive(2, 1, Unit.WalkActive.class);
		unitActives.addActive(3, 1, Unit.StopActive.class);

		ActivesGrid buildingActives = grid.createTab(2, 1, Commander.class, handler, ActivesGrid.BUILDINGS);
		buildingActives.addBuildActive(1, 2, Commander.class, HumanKaserne.class);
		buildingActives.addBuildActive(2, 2, Commander.class, HumanMechKaserne.class);
		buildingActives.addBuildActive(1, 1, Commander.class, HumanDepot.class);
		buildingActives.addActive(3, 1, BuildWallActive.class, Commander.class, HumanWall.class);
		buildingActives.addActive(2, 1, BuildMineActive.class, Commander.class, getKeritMine());

		ActivesGrid trainActives = grid.createTab(3, 1, Trainer.class, handler, ActivesGrid.TRAINING);
		trainActives.addActive(1, 1, Building.SetTargetActive.class);
		trainActives.addTrainActive(1, 3, HumanKaserne.class, Scout.class);
		trainActives.addTrainActive(2, 3, HumanKaserne.class, HeavyAssault.class);
		trainActives.addTrainActive(1, 2, HumanKaserne.class, Medic.class);
		trainActives.addTrainActive(3, 3, HumanKaserne.class, Exo.class);
		trainActives.addTrainActive(2, 2, HumanMechKaserne.class, SmallTank.class);
		trainActives.addTrainActive(3, 2, HumanMechKaserne.class, Tank.class);
		trainActives.addTrainActive(2, 1, HumanMechKaserne.class, Drone.class);
		trainActives.addTrainActive(3, 1, HumanMechKaserne.class, Helicopter.class);
	}

}
