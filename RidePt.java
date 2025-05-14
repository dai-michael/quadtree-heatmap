package DaiToku;


import java.awt.Point;

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

	public void addRide() {
		numRides++;
	}

	public void addRides(int rides) {
		numRides += rides;
	}

	public boolean equals(Point point) {
		if (this.getX() == point.getX() && this.getY() == point.getY()) {
			return true;
		}
		return false;
	}

	public int numRides() {
		return numRides;
	}

	public static void main(String[] args) {
		RidePt test = new RidePt(10,2);
		System.out.println(test.numRides);
		System.out.println(test.getX());
		test.addRide();
		System.out.println(test.numRides);
	}
}