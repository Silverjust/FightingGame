package preGame;

import java.util.ArrayList;
import java.util.HashMap;

import shared.Team;
import shared.User;

public class UserLabelManager {

	private ArrayList<UserLabel> leftUserLabels = new ArrayList<UserLabel>();
	private ArrayList<UserLabel> rightUserLabels = new ArrayList<UserLabel>();
	private ArrayList<UserLabel> leftUserLabelsRemove = new ArrayList<UserLabel>();
	private ArrayList<UserLabel> rightUserLabelsRemove = new ArrayList<UserLabel>();
	private PreGameApp app;
	float h = 50;
	float w = 100;

	public UserLabelManager(PreGameApp app) {
		this.app = app;
	}

	public void update() {
		HashMap<String, User> users = app.getPreGameInfo().getUsers();
		for (String ip : users.keySet()) {
			boolean userLabelExists = false;
			User u = users.get(ip);
			float x = u.team == Team.LEFTSIDE ? 20 : app.width - 20 - 100;

			for (UserLabel label : leftUserLabels)
				if (label.getUser() == u) {
					userLabelExists = true;
					if (u.team != Team.LEFTSIDE) {
						leftUserLabelsRemove.add(label);
						rightUserLabels.add(label);
					}
				}
			for (UserLabel label : rightUserLabels)
				if (label.getUser() == u) {
					userLabelExists = true;
					if (u.team != Team.RIGHTSIDE) {
						rightUserLabelsRemove.add(label);
						leftUserLabels.add(label);
					}
				}
			for (UserLabel label : leftUserLabelsRemove)
				leftUserLabels.remove(label);
			leftUserLabelsRemove.clear();
			for (UserLabel label : rightUserLabelsRemove)
				rightUserLabels.remove(label);
			rightUserLabelsRemove.clear();
			for (UserLabel label : leftUserLabels)
				if (label != null && label.getUser() == u) {
					label.updateText();
					float y = getY(leftUserLabels.indexOf(label), u);
					label.move(x, y);
				}
			for (UserLabel label : rightUserLabels)
				if (label != null && label.getUser() == u) {
					label.updateText();
					float y = getY(rightUserLabels.indexOf(label), u);
					label.move(x, y);
				}

			if (!userLabelExists) {
				if (u.team == Team.LEFTSIDE) {
					int y;
					if (rightUserLabels.size() > 0)
						y = getY(leftUserLabels.indexOf(leftUserLabels.get(leftUserLabels.size() - 1)) + 1, u);
					else
						y = getY(0, u);
					UserLabel label = new UserLabel(app, this, u, x, y);
					leftUserLabels.add(label);
				} else {
					int y;
					if (rightUserLabels.size() > 0)
						y = getY(rightUserLabels.indexOf(rightUserLabels.get(rightUserLabels.size() - 1)) + 1, u);
					else
						y = getY(0, u);
					UserLabel label = new UserLabel(app, this, u, x, y);
					rightUserLabels.add(label);
				}
			}

		}
	}

	public int getY(int index, User u) {
		return 100 + 60 * index;
	}

	void updateUserLabels(PreGameApp app, UserLabel[] leftLabels, UserLabel[] rightLabels) {

	}

}
