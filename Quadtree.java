import java.awt.Point;
import java.util.Arrays;
import java.util.ArrayList;

public class Quadtree implements Graph{
	private final int TOT_X;
	private final int TOT_Y;
	private int defaultDepth;
	private Region root;

	public Quadtree(int x, int y, int depth) {
		this.TOT_X = x;
		this.TOT_Y = y;
		this.defaultDepth = depth;
		Region root = new Region(0, 0, TOT_X, TOT_Y);
		System.out.println(Arrays.toString(root.getCoordinates()));
		init(root, 0);
		System.out.println(root.NE);
		// System.out.println(Arrays.toString(root.NE.getCoordinates()));
	}

	/* Inserts a new cooridnate pair */
	public void insert(int x, int y) {
		insertHelper(x, y, root);
		return;
	}

	public void insertHelper(int x, int y, Region currRegion) {
		if (!currRegion.isEmpty()) {
			System.out.println("Hello");
		}
		else {
			currRegion.coord = new Point(x, y);
		}
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
			// Calculate new coordinates for all regions and create new region
			region.NE = new Region(midX, midY, x2, y2);
			region.SE = new Region(midX, y1, x2, midY);
			region.SW = new Region(x1, y1, midX, midY);
			region.NW = new Region(x1, midY, midX, y2);
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
		public Region NE;
		public Region SE;
		public Region SW;
		public Region NW;
		private Point coord; 

		public Region(int x1, int y1, int x2, int y2) {
			this.X1 = x1;
			this.Y1 = y1;
			this.X2 = x2;
			this.Y2 = y2;
		}

		public Region(int x1, int y1, int x2, int y2, Point point) {
			this.X1 = x1;
			this.Y1 = y1;
			this.X2 = x2;
			this.Y2 = y2;
			this.coord = coord;
		}

		public Region(int x1, int y1, int x2, int y2, Region NE, Region SE, Region SW, Region NW) {
			this.X1 = x1;
			this.Y1 = y1;
			this.X2 = x2;
			this.Y2 = y2;
			this.NE = NE;
			this.SE = SE;
			this.SW = SW;
			this.NW = NW;
		}

		public Integer[] getCoordinates() {
			Integer[] coordinates = {X1, Y1, X2, Y2};
			return coordinates;
		}

		/** Set cooridnate of point in terminal node
		 * 	Return true if successfuly completed
		 * 	Return false is unsuccessful
		 * **/
		public boolean setPoint(Point coord) {
			if (NE == null) {
				this.coord = coord;
				return true;
			}
			else {
				return false;
			}
		}

		// Return if region has a point thus making it terminal
		public boolean isTerminal() {
			return coord != null;
		}

		// Return if there region has subdivisions
		public boolean isDivided() {
			return NE != null;
		}

		public boolean isEmpty() {
			return !isDivided() && !isTerminal();
		}

	}

	public static void main(String[] args) {
		Point test = new Point(1,2);
		System.out.println(test);
		Quadtree testQuad = new Quadtree(100, 100, 2);
	}
}