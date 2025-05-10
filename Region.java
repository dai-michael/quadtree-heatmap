package DaiToku;
import java.awt.Point;

class Region{
	public final int X1;
	public final int Y1;
	public final int X2;
	public final int Y2;
	public Region NE;
	public Region SE;
	public Region SW;
	public Region NW;
	public Region[] subRegionList = {NE, SE, SW, NW};
	private Point coord;
	// Whether or not region has subregions
	// NOT whether or not there is a point assigned
	private boolean isDivided; 

	public Region(int x1, int y1, int x2, int y2) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.isDivided = false;
	}

	public Region(int x1, int y1, int x2, int y2, Point point) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.coord = coord;
		this.isDivided = false;
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

	/* 
	 * Get list of subregions 
	 * Used for iterating through regions easily
	 */
	public Region[] getSubRegionList() {
		return subRegionList;
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
		this.isDivided = true;
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

	public static void main(String[] args) {
		Region test = new Region(0, 0, 100, 100, new Point(10,10));
		System.out.println("isDivided test: " + test.isDivided() + " | Expected output: false");
		System.out.println("isEmpty test: " + test.isEmpty() + " | Exepcted output: true");
	}

}