package entity;

import java.util.ArrayList;

import champs.Rock;
import entity.neutral.*;
import game.GameApplet;
import game.HUD;
import game.PreGameInfo;
import gameStructure.Spell;
import gameStructure.actives.BuildWallActive;
import gameStructure.GameObject;
import shared.Helper;
import shared.Nation;

public class ActivesGridHandler {
	@Deprecated
	public static boolean showUnitActives;
	public int x = GameApplet.app.width - 420;
	public int y = GameApplet.app.height - HUD.height + 10;
	int w = 60;
	static final int gridHeight = 3;
	static final int gridWidth = 7;

	public ActivesGrid baseGrid;
	public ArrayList<ActivesGrid> gridList = new ArrayList<ActivesGrid>();
	public Nation nation;
	public ActivesGrid displayGrid;

	public ActivesGridHandler() {
		if (GameApplet.player.getNation() != null) {
			this.nation = GameApplet.player.getNation();
		}
		Spell.x = x;
		Spell.y = y;
		displayGrid = baseGrid = new ActivesGrid(this, ActivesGrid.BASEGRID);
		gridList.add(displayGrid);
		setup(GameApplet.player.getNation());
		selectionChange();
	}

	void setup(Nation nation) {
		if (nation != null) {
			removeActives();
			this.nation = nation;
			nation.getNationInfo().setupActives(baseGrid, this);
			if (PreGameInfo.isSandbox()) {
				baseGrid.addActive(7, 1, SandboxBuilding.BuildSetup.class);
			}
		}
	}

	public void update() {
		for (int x = 0; x < gridWidth; x++)
			for (int y = 0; y < gridHeight; y++)
				if (displayGrid.get(x, y) != null && //
						displayGrid.get(x, y).isVisible())
					displayGrid.get(x, y).update();
	}

	public void fire(int x, int y) {
		try {
			displayGrid.get(x, y).pressManually();
		} catch (NullPointerException e) {
		}
	}

	public void setupSandbox() {
		System.out.println("setupSandbox");
		SandboxBuilding.commandRange = Integer.MAX_VALUE;
		ActivesGrid unitActives = baseGrid.createTab(7, 1, GameObject.class, this, "sandbox");
		unitActives.addActive(1, 1, SandboxBuilding.DeleteActive.class);
		unitActives.addBuildActive(2, 1, SandboxBuilding.class,
				GameApplet.player.getNation().getNationInfo().getMainBuilding());
		unitActives.addBuildActive(1, 2, SandboxBuilding.class, Kerit.class);
		unitActives.addBuildActive(2, 2, SandboxBuilding.class, Pax.class);
		unitActives.addBuildActive(1, 3, SandboxBuilding.class, Arcanum.class);
		unitActives.addBuildActive(2, 3, SandboxBuilding.class, Prunam.class);
		unitActives.addBuildActive(3, 2, SandboxBuilding.class, Rock.class);
		unitActives.addActive(3, 3, BuildWallActive.class, SandboxBuilding.class, Rock.class);
		unitActives.addActive(3, 1, SandboxBuilding.ChangeSide.class);
		unitActives.addActive(4, 1, SandboxBuilding.AddPlayer.class);
	}

	public void removeActives() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				baseGrid.disposeActive(x, y);
			}
		}
	}

	public void dispose() {
		removeActives();
	}

	public void selectionChange() {
		displayGrid = displayGrid.getObviousGrid();
		System.out.println("ActivesGridHandler.selectionChange() " + displayGrid.getType());
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (displayGrid.get(x, y) != null)
					if (Helper.listContainsInstanceOf(displayGrid.get(x, y).getClazz(), GameApplet.updater.selected)) {
						displayGrid.get(x, y).setVisible(true);
						displayGrid.get(x, y).selectionUpdate();

					} else
						displayGrid.get(x, y).setVisible(false);
				for (ActivesGrid activesGrid : gridList) {
					if (activesGrid != displayGrid && activesGrid.get(x, y) != null)
						activesGrid.get(x, y).setVisible(false);
				}

			}

		}

	}

	public void resetGrid() {
		displayGrid = baseGrid;
	}
}
