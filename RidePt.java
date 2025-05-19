package DaiToku;


import java.awt.Point;

/**
 * RidePt stores a single rideshare origin location
 */
class RidePt extends Point{
	private int numRides;

	public RidePt(int x, int y) {
		super(x, y);
		numRides = 1;
	}

	public RidePt(int x, int y, int numRides) {
		super(x, y);
		this.numRides = numRides;
	}

	// Add an additional ride at same location
	public void addRide() {
		numRides++;
	}

	// Add multiple rides at same location
	public void addRides(int rides) {
		numRides += rides;
	}

	// Compare if two ridepts have the same location
	public boolean equals(Point point) {
		if (this.getX() == point.getX() && this.getY() == point.getY()) {
			return true;
		}
		return false;
	}

	public int numRides() {
		return numRides;
	}

	public String toString() {
		return x + "," + y;
	}

	public static void main(String[] args) {
		RidePt test = new RidePt(10,2);
		System.out.println(test.numRides);
		System.out.println(test.getX());
		test.addRide();
		System.out.println(test.numRides);
	}
}