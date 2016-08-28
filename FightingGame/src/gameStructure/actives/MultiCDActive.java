package gameStructure.actives;

import java.lang.reflect.Method;

import game.GameBaseApp;
import gameStructure.GameObject;
import gameStructure.Spell;
import gameStructure.animation.Ability;
import processing.core.PImage;

/**
 * Aktive Fähigkeit
 * */
public abstract class MultiCDActive extends Spell {

	Method abilityGetter;
	GameObject lowestCDEntity;

	public MultiCDActive(int x, int y, char n, PImage symbol) {
		super(x, y, n, symbol);
	}

	protected void setAbilityGetter(String name) {
		try {
			abilityGetter = getClazz().getMethod(name, new Class[] {});
			GameObject e = (GameObject) getClazz().getConstructor(String[].class)
					.newInstance(new GameObject[] { null });
			setCooldown(((Ability) abilityGetter.invoke(e)).cooldown);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startCooldown() {
		if (!isNotOnCooldown()) {
			searchEntity();
			button.setEnabled(false);
		}
	}

	private void searchEntity() {
		if (lowestCDEntity == null) {
			for (GameObject e : GameApplet.GameBaseApp.selected)
				if (getClazz().isAssignableFrom(e.getClass()))
					lowestCDEntity = e;
		}
		for (GameObject e : GameApplet.GameBaseApp.selected) {
			if (getClazz().isAssignableFrom(e.getClass())) {
				try {
					float f1 = ((Ability) abilityGetter.invoke(lowestCDEntity))
							.getCooldownPercent();
					float f2 = ((Ability) abilityGetter.invoke(e))
							.getCooldownPercent();
					if (f2 > f1) {
						lowestCDEntity = e;

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	@Override
	public boolean isNotOnCooldown() {
		boolean b = false;
		if (lowestCDEntity == null)
			searchEntity();
		if (abilityGetter != null && lowestCDEntity != null) {
			try {
				b = ((Ability) abilityGetter.invoke(lowestCDEntity))
						.isNotOnCooldown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	@Override
	public float getCooldownPercent() {
		float f = 0;
		if (lowestCDEntity != null && abilityGetter != null) {
			try {
				f = ((Ability) abilityGetter.invoke(lowestCDEntity))
						.getCooldownPercent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return f > 1 || f < 0 ? 1 : f;
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (!GameApplet.GameBaseApp.selected.contains(lowestCDEntity))
			lowestCDEntity = null;
		searchEntity();
	}
}
