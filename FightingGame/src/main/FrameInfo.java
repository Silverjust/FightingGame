package main;

import shared.ref;

public class FrameInfo {
	public static float width;
	public static float height;
	public static float xCenter;
	public static float yCenter;
	public static float scale;

	public static void setup() {
		width = ref.app.width;
		height = ref.app.height;
		
		xCenter = (float) (ref.app.width / 2.0);
		yCenter = (float) (ref.app.height / 2.0);

		scale = ((ref.app.width / 1600.0F) < (ref.app.height / 900.0F)) ? (ref.app.width / 1600.0F)
				: (ref.app.height / 900.0F);
	}
}
