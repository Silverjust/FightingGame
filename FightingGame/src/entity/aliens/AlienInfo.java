package entity.aliens;

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

public class AlienInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return AlienMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return AlienKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return AlienPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return AlienArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return AlienPrunamHarvester.class;
	}

	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		ActivesGrid unitActives = grid.createTab(1, 1, Unit.class, handler, ActivesGrid.UNITS);
		unitActives.addActive(1, 1, Unit.AttackActive.class);
		unitActives.addActive(2, 1, Unit.WalkActive.class);
		unitActives.addActive(3, 1, Unit.StopActive.class);
		// grid.addActive(3, 2, Ticul.Flash.class);

		ActivesGrid buildingActives = grid.createTab(2, 1, Commander.class, handler, ActivesGrid.BUILDINGS);
		buildingActives.addActive(2, 1, BuildMineActive.class, Commander.class, getKeritMine());
		buildingActives.addBuildActive(3, 1, Commander.class, SpawnTower.class);
		buildingActives.addBuildActive(1, 1, Commander.class, AlienKaserne.class);
		buildingActives.addBuildActive(1, 2, AlienKaserne.class, AlienKaserneArcanum.class);
		buildingActives.addBuildActive(1, 3, AlienKaserne.class, AlienKasernePrunam.class);

		ActivesGrid trainActives = grid.createTab(3, 1, Trainer.class, handler, ActivesGrid.TRAINING);
		trainActives.addTrainActive(1, 3, AlienKaserne.class, Ticul.class);
		trainActives.addTrainActive(2, 3, AlienKaserne.class, Brux.class);
		trainActives.addTrainActive(3, 3, AlienKaserne.class, Valcyrix.class);
		trainActives.addTrainActive(2, 2, AlienKaserneArcanum.class, Colum.class);
		trainActives.addTrainActive(3, 2, AlienKaserneArcanum.class, Arol.class);
		trainActives.addTrainActive(2, 1, AlienKasernePrunam.class, Rug.class);
		trainActives.addTrainActive(3, 1, AlienKasernePrunam.class, Ker.class);
		trainActives.addActive(1, 1, Building.SetTargetActive.class);

	}
}
