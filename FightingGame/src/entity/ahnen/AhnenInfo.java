package entity.ahnen;

import entity.neutral.ArcanumMine;
import entity.neutral.KeritMine;
import entity.neutral.PaxDrillTower;
import entity.neutral.PrunamHarvester;
import game.ActivesGrid;
import game.ActivesGridHandler;
import game.aim.MineAim.BuildMineActive;
import gameStructure.Building;
import gameStructure.Commander;
import gameStructure.MainBuilding;
import gameStructure.Trainer;
import gameStructure.Unit;
import shared.NationInfo;

public class AhnenInfo extends NationInfo {

	@Override
	public Class<? extends MainBuilding> getMainBuilding() {
		return AhnenMainBuilding.class;
	}

	@Override
	public Class<? extends KeritMine> getKeritMine() {
		return AhnenKeritMine.class;
	}

	@Override
	public Class<? extends PaxDrillTower> getPaxDrillTower() {
		return AhnenPaxDrillTower.class;
	}

	@Override
	public Class<? extends ArcanumMine> getArcanumMine() {
		return AhnenArcanumMine.class;
	}

	@Override
	public Class<? extends PrunamHarvester> getPrunamHarvester() {
		return AhnenPrunamHarvester.class;
	}

	public void setupActives(ActivesGrid grid, ActivesGridHandler handler) {
		ActivesGrid unitActives = grid.createTab(1, 1, Unit.class, handler, ActivesGrid.UNITS);
		unitActives.addActive(1, 1, Unit.AttackActive.class);
		unitActives.addActive(2, 1, Unit.WalkActive.class);
		unitActives.addActive(3, 1, Unit.StopActive.class);
		unitActives.addActive(1, 3, Berserker.LeuchteActive.class, Berserker.class, Leuchte.class);
		unitActives.addActive(2, 3, Witcher.UpgradeActive.class);
		unitActives.addActive(3, 3, Destructor.UpgradeActive.class);
		unitActives.addActive(1, 2, Angel.CloakActive.class);
		unitActives.addActive(2, 2, Witcher.BurstActive.class);

		ActivesGrid buildingActives = grid.createTab(2, 1, Commander.class, handler, ActivesGrid.BUILDINGS);
		//TODO first use doesnt work
		buildingActives.addBuildActive(1, 1, Commander.class, AhnenKaserne.class);
		buildingActives.addActive(2, 1, BuildMineActive.class, Commander.class, getKeritMine());
		buildingActives.addBuildActive(3, 1, Commander.class, AhnenTower.class);
		buildingActives.addActive(3, 2, AhnenTower.SelectActive.class);

		ActivesGrid trainActives = grid.createTab(3, 1, Trainer.class, handler, ActivesGrid.TRAINING);
		trainActives.addActive(4, 2, AhnenKaserne.LevelActive.class);
		trainActives.addActive(1, 1, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Berserker.class);
		trainActives.addActive(2, 1, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Witcher.class);
		trainActives.addActive(3, 1, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Warrior.class);
		trainActives.addActive(1, 2, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Angel.class);
		trainActives.addActive(2, 2, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Astrator.class);
		trainActives.addActive(3, 2, AhnenKaserne.AhnenTrainActive.class, AhnenKaserne.class, Destructor.class);
		trainActives.addActive(2, 3, Building.SetTargetActive.class);

	}
}
