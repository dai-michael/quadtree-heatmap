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
		Region root = new Region(0, 0, TOT_X, TOT_Y);
		init(root, 0);
		System.out.println(root.NE);
	}

	/* Inserts a new cooridnate pair */
	public void insert(Point insertPoint) {
		insertHelper(insertPoint, root);
	}

	public void insertHelper(Point insertPoint, Region currRegion) {

		// Base case: has space to insert
		if (!currRegion.isEmpty()) {
			currRegion.setPoint(insertPoint);
		}
		// Case 1: no space in region and no divisions
		// else if (!currRegion.isDivided()) {
		// 	resolveCollision(currRegion, insertPoint, currRegion.getPoint());
		// }
		// Case 2: region is divided
		else {
			insertHelper(insertPoint, findPointRegion(currRegion, insertPoint));
		}
	}

	// public void resolveCollision(Region currRegion, Point point1, Point point2) {
	// 	// Divide region into four quadrants
	// 	subDivide(currRegion);
	// 	if (findPointRegion(currRegion.NW, point1) == findPointRegion(currRegion.))
	// }

	/**
	 * Find region subdivision that the point lies in
	 */
	public Region findPointRegion(Region currRegion, Point currPoint) {
		if (!currRegion.isDivided()) {
			// Throw an exception because there are no regions to search through 
		}

		for (Region region : currRegion.getSubRegionList()) {
			if (region.containsLocation(currPoint)) {
				return region;
			}
		}

		// Throw an exception because point out of acceptable area
		return null;
	}

	/* Gets number of points in a certain branch */
	public int getPointNumber(int x, int y) {
		// Use assert statement here
		return 0;
	}

	/** Divide region into four quadrants
	 *  Returns true if successful division
	 *  Returns false if region is already partitioned
	 **/
	public boolean subDivide(Region region) {
		if (!region.isDivided()) {
			int x1 = region.X1;
			int y1 = region.Y1;
			int x2 = region.X2;
			int y2 = region.Y2;
			int midX = (x1 + x2) / 2;
			int midY = (y1+ y2) / 2;

			// Add new quadrants to the current region
			region.setQuadrants(new Region(midX, midY, x2, y2), 
								new Region(midX, y1, x2, midY), 
								new Region(x1, y1, midX, midY), 
								new Region(x1, midY, midX, y2));
			return true;
		}
		return false;
	}

	public void init(Region region, int currDepth) {
		if (currDepth < defaultDepth) {
			subDivide(region);

			// Recursively call function again on regions
			init(region.NE, currDepth + 1);
			init(region.SE, currDepth + 1);
			init(region.SW, currDepth + 1);
			init(region.NW, currDepth + 1);
		}
	}


	private class Region{
		public final int X1;
		public final int Y1;
		public final int X2;
		public final int Y2;
		private Region[] subRegionList;
		public Region NE;
		public Region SE;
		public Region SW;
		public Region NW;
		private Point coord;
		// Whether or not region has subregions
		// NOT whether or not there is a point assigned
		private boolean isDivided; 

		public Region(int x1, int y1, int x2, int y2) {
			this.X1 = x1;
			this.Y1 = y1;
			this.X2 = x2;
			this.Y2 = y2;
			this.isDivided = true;
		}

		public Region(int x1, int y1, int x2, int y2, Point point) {
			this.X1 = x1;
			this.Y1 = y1;
			this.X2 = x2;
			this.Y2 = y2;
			this.coord = coord;
			this.isDivided = true;
		}

		/* 
		 * Used for debugging, gets coordinates of region area
		 */
		public Region[] getSubRegionList() {
			Region[] subRegionList = {NE, SE, SW, NW};
			return subRegionList;
		}

		/** 
		 *  Set cooridnate of point in terminal node
		 * 	Return true if successfuly completed
		 * 	Return false is unsuccessful
		 * **/
		public void setPoint(Point coord) {
			if (isDivided == false) {
				this.coord = coord;
			}
			else {
				// throw an exception
			}
		}

		public Point getPoint() {
			return coord;
		}

		/**
		 * Sets quadrants
		 */
		public void setQuadrants(Region NE, Region SE, Region SW, Region NW) {
			this.NE = NE;
			this.SE = SE;
			this.SW = SW;
			this.NW = NW;
			this.coord = null;
			this.isDivided = false;
		}

		/** 
		 * Return if the region has subdivisions
		 */
		public boolean isDivided() {
			return isDivided;
		}

		/* 
		 * Return if a point location lies in that region
		 */
		public boolean containsLocation(Point currPoint) {
			int x = (int) currPoint.getX();
			int y = (int) currPoint.getY();
			if (x >= X1 && x <= X2 && y >= Y1 && y <= Y2) {
				return true;
			}
			return false;
		}

		/* 
		 * Return if the region stores a point
		 */
		public boolean storesPoint() {
			return coord != null;
		}

		/* 
		 * Return if the region stores no points and has no subregions
		 */
		public boolean isEmpty() {
			return !isDivided() && !storesPoint();
		}

	}

	public static void main(String[] args) {
		Point test = new Point(1,2);
		System.out.println(test);
		Quadtree testQuad = new Quadtree(100, 100, 2);
	}
}