package shared;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.util.LinkedList;

import g4p_controls.GLabel;
import g4p_controls.StyledString.TextLayoutInfo;
import processing.core.PApplet;

public class GGLabel extends GLabel {

	public GGLabel(PApplet theApplet, float p0, float p1, float p2, float p3) {
		super(theApplet, p0, p1, p2, p3);
	}

	@Override
	protected void updateBuffer() {
		if (bufferInvalid) {
			Graphics2D g2d = buffer.g2;
			// Get the latest lines of text
			LinkedList<TextLayoutInfo> lines = stext.getLines(g2d);
			bufferInvalid = false;
			buffer.beginDraw();
			// Back ground colour
			buffer.background(opaque ? palette[6] : palette[2] & 0xFFFFFF);
			// Calculate text and icon placement
			calcAlignment();
			// If there is an icon draw it
			if (iconW != 0)
				buffer.image(bicon[0], siX, siY);
			float wrapWidth = stext.getWrapWidth();
			float sx = 0, tw = 0;
			buffer.translate(stX, stY);
			for (TextLayoutInfo lineInfo : lines) {
				TextLayout layout = lineInfo.layout;
				buffer.translate(0, layout.getAscent());
				switch (textAlignH) {
				case CENTER:
					tw = layout.getVisibleAdvance();
					tw = (tw > wrapWidth) ? tw - wrapWidth : tw;
					sx = (wrapWidth - tw) / 2;
					break;
				case RIGHT:
					tw = layout.getVisibleAdvance();
					tw = (tw > wrapWidth) ? tw - wrapWidth : tw;
					sx = wrapWidth - tw;
					break;
				case LEFT:
				case JUSTIFY:
				default:
					sx = 0;
				}
				// display text
				g2d.setColor(jpalette[2]);
				lineInfo.layout.draw(g2d, sx, 0);
				buffer.translate(0, layout.getDescent() + layout.getLeading());
			}
			buffer.endDraw();
		}
	}
}
