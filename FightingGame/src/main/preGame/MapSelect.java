package main.preGame;

import processing.data.JSONObject;
import shared.ContentListManager;
import shared.ref;
import g4p_controls.GDropList;

public class MapSelect {
	public int previousMap;
	public GDropList mapSelector;
	public String[] intNames;
	private PreGameDisplay display;

	public MapSelect(PreGameDisplay display, int startMap) {
		this.previousMap = startMap;
		this.display = display;
		mapSelector = new GDropList(ref.app, ref.app.width - 320, ref.app.height - 450, 300, 200, 5);
		setupMapSelection();
	}

	protected void setupMapSelection() {
		int size = ContentListManager.getModeMaps().size();
		System.out.println("MapSelect.setupMapSelection()" + size);
		@SuppressWarnings("unchecked")
		String[] intNames = (String[]) ContentListManager.getModeMaps().keys().toArray(new String[size]);
		this.intNames = intNames;
		String[] names = new String[size];
		for (int j = 0; j < names.length; j++) {
			try {
				JSONObject mapData = ref.app
						.loadJSONObject("data/" + ContentListManager.getModeMaps().getString(intNames[j]) + ".json");
				names[j] = mapData.getString("name");
			} catch (Exception e) {
				names[j] = "(X) " + intNames[j];
				e.printStackTrace();
			}
		}
		if (names.length == 0) {
			mapSelector.setItems(new String[] { " " }, 0);
			System.out.println("MapSelect.setupMapSelection()" + ref.preGame.getUser("").nation + names.length);
		} else {
			mapSelector.setItems(names, previousMap);
			display.preGame.map = ContentListManager.getModeMaps().getString(intNames[previousMap]);
		}
		mapSelector.addEventHandler(display, "handleSelectMap");

	}

	public void setMap(String string) {
		int size = ContentListManager.getModeMaps().keys().size();
		@SuppressWarnings("unchecked")
		String[] mapArray = (String[]) ContentListManager.getModeMaps().keys().toArray(new String[size]);
		int index = -1;
		for (int i = 0; i < mapArray.length; i++) {
			try {
				if (mapArray[i].equals(string)) {
					System.out.println("MainPreGame.setMap()");
					@SuppressWarnings("unused")
					JSONObject mapData = ref.app.loadJSONObject(
							"data/" + ContentListManager.getModeMaps().getString(mapArray[i]) + ".json");
					index = i;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (index < 0) {
			System.err.println(string + " not found");
			return;
		}
		display.preGame.map = ContentListManager.getModeMaps().getString(mapArray[index]);
		mapSelector.setSelected(index);
	}

	public void dispose(PreGameDisplay preGameDisplay) {
		mapSelector.dispose();
	}

	public void setActive(PreGameDisplay preGameDisplay, boolean b) {
		mapSelector.setEnabled(b);
	}
}