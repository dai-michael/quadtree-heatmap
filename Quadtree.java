package DaiToku;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;

public class Quadtree implements Graph{
	public final int TOT_X;
	public final int TOT_Y;
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
	public void insert(Ride insertRide) {
		insertHelper(insertRide, root);
	}

	public void insertHelper(Ride insertRide, Region currRegion) {
		// Base case: has space to insert
		if (currRegion.isEmpty()) {
			currRegion.setRide(insertRide);
		}
		// Recursive case 1: no space in region and no divisions
		else if (!currRegion.isDivided()) {
			resolveCollision(currRegion, insertRide, currRegion.getRide());
		}
		// Recursive case 2: region is divided
		else {
			insertHelper(insertRide, currRegion.findSubregion(insertRide));
		}
	}

	public void resolveCollision(Region currRegion, Ride Ride1, Ride Ride2) {
		currRegion.subDivide();
		Region subregion1 = currRegion.findSubregion(Ride1);
		Region subregion2 = currRegion.findSubregion(Ride2);

		// Recursive step: Both regions which the Ride lies in is the same
		if (subregion1 == subregion2) {
			resolveCollision(subregion1, Ride1, Ride2);
		}
		// Base case: If regions are different, set Rides to those regions
		else {
			subregion1.setRide(Ride1);
			subregion2.setRide(Ride2);
		}
	}

	/** 
	 * Get root node
	 */
	public Region getRoot() {
		return root;
	}


	/**
	 *  Gets number of Rides in a certain branch 
	 */
	public int countRides(Region currRegion) {
		// Base case 1: region is empty
		if (currRegion.isEmpty()) {
			return 0;
		}
		// Base case 2: region contains a Ride
		if (currRegion.storesRide()) {
			return 1;
		}
		// Recursive step: for every subregion count Rides
		int numRides = 0;
		for (Region region : currRegion.subregionList) {
			numRides += countRides(region);
		}
		return numRides;
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
		testQuad.insert(new Ride(10,10));
		testQuad.insert(new Ride(20,20));
		System.out.println(testQuad.root.SW.SW.storesRide());
		System.out.println(testQuad.root.SW.SW.SW.storesRide());
		System.out.println(testQuad.countRides(testQuad.root));
		System.out.println(testQuad.getRoot());
	}
}