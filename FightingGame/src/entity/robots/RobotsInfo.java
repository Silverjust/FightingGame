package entity.robots;

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
import shared.NationInfo;

public class RobotsInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return RobotsMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return RobotsKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return RobotsPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return RobotsArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return RobotsPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		ActivesGrid unitActives = grid.createTab(1, 1, Unit.class, handler, ActivesGrid.UNITS);
		unitActives.addActive(1, 1, Unit.AttackActive.class);
		unitActives.addActive(2, 1, Unit.WalkActive.class);
		unitActives.addActive(3, 1, Unit.StopActive.class);
		unitActives.addActive(4, 2, M1N1B0T.BuildDepotActive.class);
		unitActives.addActive(3, 2, SN41L10N.AnchorActive.class);
		unitActives.addActive(3, 3, W4SP.SpeedActive.class);

		ActivesGrid buildingActives = grid.createTab(2, 1, Commander.class, handler, ActivesGrid.BUILDINGS);
		buildingActives.addBuildActive(1, 1, Commander.class, RobotsKaserne.class);
		buildingActives.addActive(2, 1, BuildMineActive.class, Commander.class, getKeritMine());

		ActivesGrid trainActives = grid.createTab(3, 1, Trainer.class, handler, ActivesGrid.TRAINING);
		trainActives.addTrainActive(1, 3, RobotsKaserne.class, M1N1B0T.class);
		trainActives.addTrainActive(2, 3, RobotsKaserne.class, B0T.class);
		trainActives.addTrainActive(3, 3, RobotsKaserne.class, W4SP.class);
		trainActives.addTrainActive(2, 2, RobotsKaserne.class, PL0S10N.class);
		trainActives.addTrainActive(3, 2, RobotsKaserne.class, SN41L10N.class);
		trainActives.addTrainActive(2, 1, RobotsKaserne.class, KR4B1T.class);
		trainActives.addTrainActive(3, 1, RobotsKaserne.class, F4CT0RY.class);
		trainActives.addActive(1, 1, Building.SetTargetActive.class);

	}

}
