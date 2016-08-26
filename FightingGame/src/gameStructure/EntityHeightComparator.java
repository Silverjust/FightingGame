package gameStructure;

import java.util.Comparator;

public class EntityHeightComparator implements Comparator<GameObject> {

	@Override
	public int compare(GameObject e1, GameObject e2) {
		if (e1.getY() > e2.getY()) {
			return 1;
		} else if (e1.getY() < e2.getY()) {
			return -1;
		} else {
			return 0;
		}
	}
}
