package entity.scientists;

import entity.ActivesGrid;
import entity.ActivesGridHandler;
import entity.MainBuilding;
import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import entity.scientists.ScientistKaserne.EquipActive;
import game.aim.MineAim.BuildMineActive;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.Trainer;
import gameStructure.Unit;
import shared.NationInfo;

public class ScientistInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return ScientistMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return ScientistKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return ScientistPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return ScientistArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return ScientistPrunamHarvester.class;
	}

	@Override
	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		ActivesGrid unitActives = grid.createTab(1, 1, Unit.class, handler, ActivesGrid.UNITS);
		unitActives.addActive(1, 1, Unit.AttackActive.class);
		unitActives.addActive(2, 1, Unit.WalkActive.class);
		unitActives.addActive(3, 1, Unit.StopActive.class);
		unitActives.addActive(1, 2, AirshipGuineaPig.AnchorActive.class);
		unitActives.addActive(2, 2, PhysicsLab.TeleportActive.class);
		unitActives.addActive(3, 2, BioLab.CreateSwampActive.class);
		unitActives.addActive(4, 2, ChemLab.ScientistWallActive.class);

		ActivesGrid buildingActives = grid.createTab(2, 1, Commander.class, handler, ActivesGrid.BUILDINGS);
		buildingActives.addActive(1, 2, BuildMineActive.class, Commander.class, getKeritMine());
		buildingActives.addBuildActive(1, 1, Commander.class, ScientistKaserne.class);

		ActivesGrid trainActives = grid.createTab(3, 1, Trainer.class, handler, ActivesGrid.TRAINING);
		trainActives.addTrainActive(1, 2, ScientistKaserne.class, GuineaPig.class);
		trainActives.addTrainActive(4, 1, ScientistKaserne.class, ChemLab.class);
		trainActives.addTrainActive(3, 1, ScientistKaserne.class, BioLab.class);
		trainActives.addTrainActive(2, 1, ScientistKaserne.class, PhysicsLab.class);
		trainActives.addActive(2, 2, EquipActive.class, PhysicsLab.class, ShieldGuineaPig.class);
		trainActives.addActive(2, 3, EquipActive.class, PhysicsLab.class, RailgunGuineaPig.class);
		trainActives.addActive(3, 2, EquipActive.class, BioLab.class, Cell.class);
		trainActives.addActive(3, 3, EquipActive.class, BioLab.class, SpawnerGuineaPig.class);
		trainActives.addActive(4, 2, EquipActive.class, ChemLab.class, AirshipGuineaPig.class);
		trainActives.addActive(4, 3, EquipActive.class, ChemLab.class, RocketGuineaPig.class);
		trainActives.addActive(1, 1, Building.SetTargetActive.class);

	}
}
