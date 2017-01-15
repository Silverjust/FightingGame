package game.shop;

import java.util.HashMap;

import g4p_controls.GAlign;
import g4p_controls.GButton;
import g4p_controls.GEvent;
import g4p_controls.GLabel;
import g4p_controls.GOption;
import g4p_controls.GPanel;
import g4p_controls.GSketchPad;
import g4p_controls.GToggleGroup;
import gameStructure.items.InventoryItem;
import gameStructure.items.SwordOfTesting;
import processing.core.PGraphics;
import shared.Coms;
import shared.GameBaseApp;
import shared.Helper;

public class ShopManager {
	private GPanel shopWindow;
	private GameBaseApp app;
	private GButton closeButton;
	private GButton buyButton;
	private GToggleGroup itemGroup;
	@Deprecated
	private HashMap<String, GOption> itemsOptionsMap = new HashMap<String, GOption>();
	private HashMap<GOption, Class<? extends InventoryItem>> itemsNameMap = new HashMap<GOption, Class<? extends InventoryItem>>();
	private InventoryItem displayItem;
	private GLabel displayItemName;
	private GSketchPad itemDescrPad;
	private String itemDescr;
	private PGraphics pGraphics;

	public ShopManager(GameBaseApp app) {
		this.app = app;
		shopWindow = new GPanel(app, 100, 100, 500, 500, "shop");
		shopWindow.setCollapsible(false);
		shopWindow.setLocalColorScheme(1, true);

		closeButton = new GButton(app, shopWindow.getWidth() - 40, 0, 40, 40, "X");
		closeButton.addEventHandler(this, "handleButtonEvents");
		shopWindow.addControl(closeButton);

		buyButton = new GButton(app, 410, 50, 60, 40, "buy");
		buyButton.addEventHandler(this, "handleButtonEvents");
		buyButton.setEnabled(false);
		shopWindow.addControl(buyButton);

		displayItemName = new GLabel(app, 300, 50, 100, 40);
		displayItemName.setOpaque(true);
		shopWindow.addControl(displayItemName);

		itemDescrPad = new GSketchPad(app, 300, 100, 180, 380);
		pGraphics = app.createGraphics(180, 380);
		itemDescrPad.setGraphic(pGraphics);

		// displayItemDescr.setOpaque(true);
		// displayItemDescr.setTextAlign(GAlign.LEFT, GAlign.TOP);
		shopWindow.addControl(itemDescrPad);

		itemGroup = new GToggleGroup();

		int i = 0;
		for (Class<? extends InventoryItem> itemClass : app.getContentListManager().getItemArray()) {
			InventoryItem item = app.getContentListManager().createItem(itemClass);
			String ingameName = item.getIngameName();

			GOption option = new GOption(app, 10, 50 + 40 * i, 120, 30);
			option.setTextAlign(GAlign.LEFT, GAlign.MIDDLE);
			option.setText(ingameName);
			option.setOpaque(true);
			option.addEventHandler(this, "itemOptionHandler");
			// itemsOptionsMap.put(ingameName, option);
			itemsNameMap.put(option, itemClass);
			itemGroup.addControl(option);
			shopWindow.addControl(option);

			i++;
		}
	}

	public void draw() {
		Helper.text(app, itemDescr, 300 + shopWindow.getX(), 100 + shopWindow.getY());
	}

	public void itemOptionHandler(GOption option, GEvent event) {
		System.out.println("ShopManager.itemOptionHandler()" + event);
		if (event == GEvent.SELECTED) {
			displayItem = app.getContentListManager().createItem(itemsNameMap.get(option));
			displayItemName.setText(displayItem.getIngameName());
			itemDescr = displayItem.getDescription();
			pGraphics.beginDraw();
			// pGraphics.rect(10, 10, 100, 100);
			pGraphics.clear();
			pGraphics.fill(0);
			Helper.text(app, pGraphics, itemDescr, 50, 50);
			pGraphics.endDraw();
			buyButton.setEnabled(true);
		}
	}

	private void clearSelectedItem() {
		displayItemName.setText("");
		pGraphics.beginDraw();
		pGraphics.clear();
		pGraphics.endDraw();
		for (GOption gOption : itemsNameMap.keySet()) {
			gOption.setSelected(false);
		}
		buyButton.setEnabled(false);
	}

	public void handleButtonEvents(GButton button, GEvent event) {
		if (button == closeButton)
			app.getDrawer().getHud().disposeShop();
		else if (button == buyButton) {
			app.getUpdater().send(Coms.BUY + " " + displayItem.getInternName() + " "
					+ app.getPlayer().getChampion().getNumber());
			clearSelectedItem();
		}
	}

	public void dispose() {
		closeButton.dispose();
		shopWindow.dispose();
	}

}
