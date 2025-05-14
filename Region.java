package DaiToku;

class Region{
	public final int X1;
	public final int Y1;
	public final int X2;
	public final int Y2;
	public Region NE;
	public Region SE;
	public Region SW;
	public Region NW;
	public Region[] subregionList;
	public RidePt storedRidePt;
	// Whether or not region has subregions
	// NOT whether or not there is a RidePt assigned
	private boolean isDivided; 

	public Region(int x1, int y1, int x2, int y2) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.isDivided = false;
	}

	public Region(int x1, int y1, int x2, int y2, RidePt storedRidePt) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.storedRidePt = storedRidePt;
		this.isDivided = false;
	}

	/** 
	 *  Set cooridnate of RidePt in terminal node
	 *  Not to be confused with insert in quadtree
	 */
	public void setRidePt(RidePt storedRidePt) {
		if (isDivided == false) {
			this.storedRidePt = storedRidePt;
		}
		else {
			// throw an exception
		}
	}

	/**
	 * Sets all quadrants and marks as divided
	 */
	public void setQuadrants(Region NE, Region SE, Region SW, Region NW) {
		this.NE = NE;
		this.SE = SE;
		this.SW = SW;
		this.NW = NW;
		subregionList = new Region[]{NE, SE, SW, NW};
		this.storedRidePt = null;
		this.isDivided = true;
	}

	/** 
	 * Return if the region is a leaf node
	 */
	public boolean isDivided() {
		return isDivided;
	}

	/* 
	 * Return if a RidePt location lies in that region
	 */
	public boolean containsLocation(RidePt currRidePt) {
		int x = (int) currRidePt.getX();
		int y = (int) currRidePt.getY();
		if (x >= X1 && x <= X2 && y >= Y1 && y <= Y2) {
			return true;
		}
		return false;
	}

	/* 
	 * Return if the region stores a RidePt
	 */
	public boolean storesRidePt() {
		return storedRidePt != null;
	}

	/* 
	 * Return if the region is a leaf and has no RidePts
	 */
	public boolean isEmpty() {
		return !isDivided() && !storesRidePt();
	}

	/** 
	 * Divide region into four quadrants
	 *  Returns true if successful division
	 *  Returns false if region is already partitioned
	 */
	public boolean subDivide() {
		if (!isDivided()) {
			int midX = (X1 + X2) / 2;
			int midY = (Y1+ Y2) / 2;

			// Add new quadrants to the current region
			setQuadrants(new Region(midX, midY, X2, Y2),  // Northeast
						 new Region(midX, Y1, X2, midY),  // Southeast
						 new Region(X1, Y1, midX, midY),  // Southwest
						 new Region(X1, midY, midX, Y2)); // Northwest
			return true;
		}
		return false;
	}

	/**
	 * Find region subdivision that the RidePt lies in
	 */
	public Region findSubregion(RidePt currRidePt) {
		if (!isDivided()) {
			// Throw an exception because there are no regions to search through 
		}

		for (Region region : subregionList) {
			if (region.containsLocation(currRidePt)) {
				return region;
			}
		}

		// Throw an exception because RidePt out of acceptable area
		return null;
	}

	public String toString() {
		String output = "Region storedRidePtinates: (%d, %d), (%d, %d)";
		return String.format(output, X1, Y1, X2, Y2);
	}

	public static void main(String[] args) {
		Region test = new Region(0, 0, 100, 100, new RidePt(10,10));
		System.out.println("isDivided test: " + test.isDivided() + " | Expected output: false");
		System.out.println("isEmpty test: " + test.isEmpty() + " | Exepcted output: false");
		System.out.println("storesRidePt test: " + test.storesRidePt() + " | Expected output: true");
		System.out.println("-----------------");
		System.out.println("SUBDIVIDED REGION");
		System.out.println("-----------------");
		test.subDivide();
		System.out.println("isDivided test: " + test.isDivided() + " | Expected output: true");
		System.out.println("isEmpty test: " + test.isEmpty() + " | Exepcted output: false");
		System.out.println("storesRidePt test: " + test.storesRidePt() + " | Expected output: false");
		System.out.println("containsLocation(10,10) test: " + test.containsLocation(new RidePt(10,10)) + " | expected output: true");
		System.out.println("containsLocation(10,100) test: " + test.containsLocation(new RidePt(10,100)) + " | expected output: true");
		System.out.println("containsLocation(10,101) test: " + test.containsLocation(new RidePt(10,101)) + " | expected output: false");
		System.out.println(test.subregionList);

		// for (Region region : test.subregionList) {
		// 	System.out.println(region.X1);
		// 	System.out.println(region.Y1);
		// }

		System.out.println(test.isDivided());

	}

}