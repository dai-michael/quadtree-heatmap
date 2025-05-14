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
	public Ride coord;
	// Whether or not region has subregions
	// NOT whether or not there is a Ride assigned
	private boolean isDivided; 

	public Region(int x1, int y1, int x2, int y2) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.isDivided = false;
	}

	public Region(int x1, int y1, int x2, int y2, Ride coord) {
		this.X1 = x1;
		this.Y1 = y1;
		this.X2 = x2;
		this.Y2 = y2;
		this.coord = coord;
		this.isDivided = false;
	}

	/** 
	 *  Set cooridnate of Ride in terminal node
	 * 	Return true if successfuly completed
	 * 	Return false is unsuccessful
	 */
	public void setRide(Ride coord) {
		if (isDivided == false) {
			this.coord = coord;
		}
		else {
			// throw an exception
		}
	}

	/**
	 * Get current stored Ride
	 */
	public Ride getRide() {
		return coord;
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
		this.coord = null;
		this.isDivided = true;
	}

	/** 
	 * Return if the region is a leaf node
	 */
	public boolean isDivided() {
		return isDivided;
	}

	/* 
	 * Return if a Ride location lies in that region
	 */
	public boolean containsLocation(Ride currRide) {
		int x = (int) currRide.getX();
		int y = (int) currRide.getY();
		if (x >= X1 && x <= X2 && y >= Y1 && y <= Y2) {
			return true;
		}
		return false;
	}

	/* 
	 * Return if the region stores a Ride
	 */
	public boolean storesRide() {
		return coord != null;
	}

	/* 
	 * Return if the region is a leaf and has no Rides
	 */
	public boolean isEmpty() {
		return !isDivided() && !storesRide();
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
	 * Find region subdivision that the Ride lies in
	 */
	public Region findSubregion(Ride currRide) {
		if (!isDivided()) {
			// Throw an exception because there are no regions to search through 
		}

		for (Region region : subregionList) {
			if (region.containsLocation(currRide)) {
				return region;
			}
		}

		// Throw an exception because Ride out of acceptable area
		return null;
	}

	public String toString() {
		String output = "Region coordinates: (%d, %d), (%d, %d)";
		return String.format(output, X1, Y1, X2, Y2);
	}

	public static void main(String[] args) {
		Region test = new Region(0, 0, 100, 100, new Ride(10,10));
		System.out.println("isDivided test: " + test.isDivided() + " | Expected output: false");
		System.out.println("isEmpty test: " + test.isEmpty() + " | Exepcted output: false");
		System.out.println("storesRide test: " + test.storesRide() + " | Expected output: true");
		System.out.println("getRide test: " + test.getRide() + " | Expected output: 10,10");
		System.out.println("-----------------");
		System.out.println("SUBDIVIDED REGION");
		System.out.println("-----------------");
		test.subDivide();
		System.out.println("isDivided test: " + test.isDivided() + " | Expected output: true");
		System.out.println("isEmpty test: " + test.isEmpty() + " | Exepcted output: false");
		System.out.println("storesRide test: " + test.storesRide() + " | Expected output: false");
		System.out.println("getRide test: " + test.getRide() + " | Expected output: 10,10");
		System.out.println("containsLocation(10,10) test: " + test.containsLocation(new Ride(10,10)) + " | expected output: true");
		System.out.println("containsLocation(10,100) test: " + test.containsLocation(new Ride(10,100)) + " | expected output: true");
		System.out.println("containsLocation(10,101) test: " + test.containsLocation(new Ride(10,101)) + " | expected output: false");
		System.out.println(test.subregionList);

		// for (Region region : test.subregionList) {
		// 	System.out.println(region.X1);
		// 	System.out.println(region.Y1);
		// }

		System.out.println(test.isDivided());

	}

}