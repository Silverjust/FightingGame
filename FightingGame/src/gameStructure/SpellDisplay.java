package gameStructure;

import game.PlayerInterface;
import processing.core.PImage;
import shared.GameBaseApp;

public class SpellDisplay {
	static final int buttonWidth = 50;
	static final int b = 3;
	private GameBaseApp app;
	private Spell spell;
	private int x;
	private int y;
	private int w;
	private int h;
	private String buttonChar;
	private PlayerInterface inter;
	private PImage symbol;

	public SpellDisplay(GameBaseApp app, Spell spell, PlayerInterface inter, int pos, PImage symbol) {
		this.app = app;
		this.spell = spell;
		this.inter = inter;
		this.symbol = symbol;
		this.x = getXbyPos(inter, pos);
		this.y = getYbyPos(inter, pos);
		this.w = buttonWidth;
		this.h = buttonWidth;
		buttonChar = app.getDrawer().getHud().playerInterface.getKeyFromPos(pos);
		app.image(symbol, x, y, w, h);
	}

	public void update() {
		app.fill(30);
		app.rect(x, y, w, h);
		app.fill(230);
		app.image(symbol, x + b, y + b, w - b * 2, h - b * 2);
		if (!spell.isNotOnCooldown()) {
			// app.blendMode(MULTIPLY);
			app.tint(100);
			app.image(symbol.get(0, 0, (int) (symbol.width * (1 - spell.getCooldownPercent())), symbol.height), x + b,
					y + b, (w - b * 2) * (1 - spell.getCooldownPercent()), h - b * 2);
			// winApp.blendMode(BLEND);
			app.tint(255);
		}
		app.textSize(h / 2 < 15 ? 15 : h / 2);
		app.text(buttonChar, x + w - app.textWidth(buttonChar) + b, y + app.textAscent() * 0.5f + h * 0.1f + b);
	}

	public int getXbyPos(PlayerInterface inter, int pos) {
		return inter.x + (pos - 1) * (buttonWidth + 10);
	}

	public int getYbyPos(PlayerInterface inter, int pos) {
		return inter.y + 0 * (buttonWidth + 10);
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub

	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setEnabled(boolean b) {
		// TODO Auto-generated method stub

	}
}
