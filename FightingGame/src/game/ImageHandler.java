package game;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import gameStructure.Building;
import gameStructure.GameObject;
import gameStructure.Unit;
import main.MainApp;
import processing.core.PApplet;
import processing.core.PImage;
import shared.ContentListManager;
import shared.ref;

public class ImageHandler {
	// TODO dispose with load null
	static String dataPath;
	public static ArrayList<PImage> imagesToLoad = new ArrayList<PImage>();

	public static int nImagesToLoad;

	public static ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
	private static boolean dispose = false;
	private static ContentListManager contentListHandler;

	public static void setup() {
		contentListHandler = ((MainApp) ref.app).contentListHandler;
	}

	public static boolean requestAllImages() {
		try {
			dataPath = "data/";

			/*
			 * Reflections reflections = new Reflections("entity"); Set<Class<?
			 * extends ImgLoading>> classes = reflections
			 * .getSubTypesOf(ImgLoading.class);
			 */

			classes.add(GameDrawer.class);
			classes.add(HUD.class);

			if (stateOfLoading() != 1) {
				classes.add(GameObject.class);
				classes.add(Unit.class);
				classes.add(Building.class);
				classes.add(AimHandler.class);
				for (Class<? extends GameObject> c : contentListHandler.getGameObjectArray()) {
					classes.add(c);
				}
			}

			// PApplet.printArray(classes);

			for (Class<?> c : classes) {
				try {
					Method m = c.getDeclaredMethod("loadImages");
					m.invoke(null);
				} catch (NoSuchMethodException e) {
					System.out.println("no loadImage method, add:\npublic static void loadImages(){ /*code*/ }");
					e.printStackTrace();
				}
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static float stateOfLoading() {
		int loadedImages = 0;
		boolean error = false;
		for (PImage i : imagesToLoad) {
			if (i.width == -1) {
				System.err.println("Image error");
				error = true;
			}
			if (i.width > 0) {
				loadedImages++;
			}

		}
		if (error)
			loadedImages = -1;
		return (float) (loadedImages) / nImagesToLoad;
	}

	/** loads images: path, name, animation-type, #directions, #frames */
	public static PImage[][] load(String path, String name, char animation, byte directions, byte iterations) {
		if (dispose)
			return null;
		PImage[][] imageArray = new PImage[directions][iterations];
		for (int d = 0; d < directions || directions == 0 && d == 0; d++) {
			for (int i = 0; i < iterations || iterations == 0 && i == 0; i++) {
				nImagesToLoad++;
				String s = dataPath + path + name + (animation != 0 ? "_" + animation : "")
						+ (directions != 0 ? "_" + d : "") + (iterations != 0 ? "_" + PApplet.nf(i, 4) : "") + ".png";
				System.out.println(s);
				s = getPath(s);
				imageArray[d][i] = ref.app.requestImage(s);
				imagesToLoad.add(imageArray[d][i]);
			}
		}
		return imageArray;
	}

	/** loads images: path, name, animation-type, #frames */
	public static PImage[] load(String path, String name, char animation, int j) {
		if (dispose)
			return null;
		PImage[] imageArray = new PImage[j];
		for (int i = 0; i < j || j == 0 && i == 0; i++) {
			nImagesToLoad++;
			String s = dataPath + path + name + (animation != 0 ? "_" + animation : "")
					+ (j != 0 ? "_" + PApplet.nf(i, 4) : "") + ".png";
			System.out.println(s);
			s = getPath(s);
			imageArray[i] = ref.app.requestImage(s);
			imagesToLoad.add(imageArray[i]);
		}

		return imageArray;
	}

	/** loads images: path, name, animation-type */
	public static PImage load(String path, String name, char animation) {
		if (dispose)
			return null;
		PImage image;
		nImagesToLoad++;
		String s = dataPath + path + name + (animation != 0 ? "_" + animation : "") + ".png";
		System.out.println(s);
		s = getPath(s);
		image = ref.app.requestImage(s);
		imagesToLoad.add(image);
		return image;
	}

	/** loads images: path, name */
	public static PImage load(String path, String name) {
		if (dispose)
			return null;
		PImage image;
		nImagesToLoad++;
		String s = dataPath + path + name + ".png";
		System.out.println(s);
		s = getPath(s);
		image = ref.app.requestImage(s);
		imagesToLoad.add(image);
		return image;
	}

	private static String getPath(String path) {
		try {
			path = ref.app.getClass().getClassLoader().getResource(path).getFile();
			return path;
		} catch (Exception e) {
			System.err.println("File is missing: "+path);
			e.printStackTrace();
		}
		return null;
	}

	static String getClassPath(GameObject o) {
		String pkg = o.getClass().getEnclosingClass().getCanonicalName();

		int pos = pkg.lastIndexOf("."); // Slash before the class name
		if (pos == -1)
			return ""; // No package
		pkg = pkg.substring(0, pos + 1); // Keep the ending dot
		String cp = pkg.replaceAll("\\.", "/");
		return cp;

	}

	public static String getBinaryPath() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File classpathRoot = new File(classLoader.getResource("").getPath());
		return classpathRoot.getPath() + "\\";
	}

	public static void dispose() {
		/*
		 * for (PImage img : imagesToLoad) { Object cache =
		 * ref.app.getCache(img); if (cache instanceof Texture) ((Texture)
		 * cache).disposeSourceBuffer(); ref.app.removeCache(img); } dispose =
		 * true; requestAllImages(); dispose = false;
		 * 
		 * // for (int i = 0; i < imagesToLoad.size(); i++) { imagesToLoad }
		 * 
		 * imagesToLoad.clear();
		 */
	}

	public static void drawImage(PApplet pApplet, PImage img, float a, float b, float c, float d) {
		try {
			pApplet.image(img, a, b, c, d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
