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
		if (!currRegion.isEmpty()) {
			currRegion.setPoint(insertPoint);
		}
		// Case 1: no space in region and no divisions
		else if (!currRegion.isDivided()) {
			resolveCollision(currRegion, insertPoint, currRegion.getPoint());
		}
		// Case 2: region is divided
		else {
			insertHelper(insertPoint, currRegion.findSubregion(insertPoint));
		}
	}

	public void resolveCollision(Region currRegion, Point point1, Point point2) {
		currRegion.subDivide();
		Region subregion1 = currRegion.findSubregion(point1);
		Region subregion2 = currRegion.findSubregion(point2);


		// Case 1: Both regions which the point lies in is the same
		if (subregion1 == subregion2) {
			resolveCollision(subregion1, point1, point2);
		}
		// If regions are different, set points to those regions
		else {
			subregion1.setPoint(point1);
			subregion2.setPoint(point2);
		}
	}

	/**
	 *  Gets number of points in a certain branch 
	 */
	public int getPointNumber(int x, int y) {
		// Use assert statement here
		return 0;
	}

	/**
	 * Initialize quadtree with regions going down to specified depth
	 * 
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
	}
}