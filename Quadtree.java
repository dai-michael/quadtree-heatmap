package DaiToku;

import java.awt.Point;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;

public class Quadtree implements Graph{
	private final int TOT_X;
	private final int TOT_Y;
	public int defaultDepth;
	private Region root;

	public Quadtree(int x, int y, int depth) {
		this.TOT_X = x;
		this.TOT_Y = y;
		this.defaultDepth = depth;
		root = new Region(0, 0, TOT_X, TOT_Y);
		init(root, 0);
	}

	/** 
	 * Inserts a new cooridnate pair 
	 */
	public void insert(Point insertPoint) {
		insertHelper(insertPoint, root);
	}

	public void insertHelper(Point insertPoint, Region currRegion) {
		// Base case: has space to insert
		if (currRegion.isEmpty()) {
			currRegion.setPoint(insertPoint);
		}
		// Recursive case 1: no space in region and no divisions
		else if (!currRegion.isLeaf()) {
			resolveCollision(currRegion, insertPoint, currRegion.getPoint());
		}
		// Recursive case 2: region is divided
		else {
			insertHelper(insertPoint, currRegion.findSubregion(insertPoint));
		}
	}

	public void resolveCollision(Region currRegion, Point point1, Point point2) {
		currRegion.subDivide();
		Region subregion1 = currRegion.findSubregion(point1);
		Region subregion2 = currRegion.findSubregion(point2);

		// Recursive step: Both regions which the point lies in is the same
		if (subregion1 == subregion2) {
			resolveCollision(subregion1, point1, point2);
		}
		// Base case: If regions are different, set points to those regions
		else {
			subregion1.setPoint(point1);
			subregion2.setPoint(point2);
		}
	}

	/** 
	 * Get root node
	 */
	public Region getRoot() {
		return root;
	}


	/**
	 *  Gets number of points in a certain branch 
	 */
	public int countPoints(Region currRegion) {
		// Base case 1: region is empty
		if (currRegion.isEmpty()) {
			return 0;
		}
		// Base case 2: region contains a point
		if (currRegion.storesPoint()) {
			return 1;
		}
		// Recursive step: for every subregion count points
		int numPoints = 0;
		for (Region region : currRegion.subregionList) {
			numPoints += countPoints(region);
		}
		return numPoints;
	}

	/**
	 * Initialize quadtree with regions going down to specified depth
	 */
	public void init(Region region, int currDepth) {
		if (currDepth < defaultDepth) {
			region.subDivide();

			// Recursively call function again on regions
			init(region.NE, currDepth + 1);
			init(region.SE, currDepth + 1);
			init(region.SW, currDepth + 1);
			init(region.NW, currDepth + 1);
		}
	}

	public static void main(String[] args) {
		Quadtree testQuad = new Quadtree(100, 100, 2);
		testQuad.insert(new Point(10,10));
		testQuad.insert(new Point(20,20));
		System.out.println(testQuad.root.SW.SW.storesPoint());
		System.out.println(testQuad.root.SW.SW.SW.storesPoint());
		System.out.println(testQuad.countPoints(testQuad.root));
		System.out.println(testQuad.getRoot());
	}
}