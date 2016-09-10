package preGame;

import g4p_controls.GLabel;
import processing.core.PApplet;
import shared.User;

public class UserLabel {
	private GLabel label;
	private User u;

	public UserLabel(PApplet app, UserLabelManager manager, User u, float x, float y) {
		this.u = u;
		label = new GLabel(app, x, y, manager.w, manager.h);
		label.setOpaque(true);
		updateText();
	}

	public void move(float x, float y) {
		label.moveTo(x, y);
	}

	public void dispose() {
		label.dispose();
	}

	public User getUser() {
		return u;
	}

	public void updateText() {
		String champion = u.championName;
		if (champion == null || champion.equals("") || champion.equals("null")) {
			champion = "-not selected-";
		}
		label.setText(u.name + "\n" + champion);
	}

}
