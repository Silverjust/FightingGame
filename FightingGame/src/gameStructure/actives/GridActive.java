package gameStructure.actives;

import entity.ActivesGrid;
import entity.ActivesGridHandler;
import gameStructure.Building;
import gameStructure.Spell;

public class GridActive extends Spell {
	/** aktive fähigkeit die grid aufruft */
	Class<? extends Building> building;
	String descr = " ", stats = " ";
	private ActivesGrid grid;
	private ActivesGridHandler handler;

	public GridActive(int x, int y, char n, Class<?> displayer, ActivesGrid grid, ActivesGridHandler handler) {
		super(x, y, n, null);// TODO bilder
		this.grid = grid;
		this.handler = handler;
		setClazz(displayer);
	}

	@Override
	protected void onActivation() {
		handler.displayGrid = getGrid();
		handler.selectionChange();
	}

	@Override
	public String getDescription() {
		return getGrid().getDesription();
	}


	public ActivesGrid getGrid() {
		return grid;
	}

}
