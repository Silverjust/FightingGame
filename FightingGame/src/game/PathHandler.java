package game;

import pathfinder.*;
import processing.core.PImage;
import shared.ref;

public class PathHandler {
	//FIXME ADD Laufweg algorithmus
	public static void makeGraph(Graph g, PImage backImg,
			int tilesX, int tilesY) {// des hab ich aus dem pathfinder beispiel
		/*
		 * boolean allowDiagonals=true;
		 * 
		 * int dx = backImg.width / tilesX; int dy = backImg.height / tilesY;
		 * int sx = dx / 2, sy = dy / 2; // use deltaX to avoid horizontal wrap
		 * around edges int deltaX = tilesX + 1; // must be > tilesX
		 * 
		 * float hCost = dx, vCost = dy, dCost = PApplet.sqrt(dx * dx + dy *
		 * dy); float cost = 0; int px, py, nodeID, col; GraphNode aNode;
		 * 
		 * py = sy; for (int y = 0; y < tilesY; y++) { nodeID = deltaX * y +
		 * deltaX; px = sx; for (int x = 0; x < tilesX; x++) { // Calculate the
		 * cost col = backImg.get(px, py) & 0xFF; cost = 1;
		 * 
		 * // If col is not black then create the node and edges if (col != 0) {
		 * aNode = new GraphNode(nodeID, px, py); g.addNode(aNode); if (x > 0) {
		 * g.addEdge(nodeID, nodeID - 1, hCost * cost); if (allowDiagonals) {
		 * g.addEdge(nodeID, nodeID - deltaX - 1, dCost * cost);
		 * g.addEdge(nodeID, nodeID + deltaX - 1, dCost * cost); } } if (x <
		 * tilesX - 1) { g.addEdge(nodeID, nodeID + 1, hCost * cost); if
		 * (allowDiagonals) { g.addEdge(nodeID, nodeID - deltaX + 1, dCost *
		 * cost); g.addEdge(nodeID, nodeID + deltaX + 1, dCost * cost); } } if
		 * (y > 0) g.addEdge(nodeID, nodeID - deltaX, vCost * cost); if (y <
		 * tilesY - 1) g.addEdge(nodeID, nodeID + deltaX, vCost * cost); } px +=
		 * dx; nodeID++; } py += dy; }
		 */
	}

	public static boolean isObstacle(float x, float y) {
		boolean b = ref.updater.map.collision.pixels[(int) (x + y
				* ref.updater.map.collision.width)] == 0x00;
		return b;
	}
}
