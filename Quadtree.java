package DaiToku;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Quadtree made for storing RidePt
 * Allows for insertion, initialization, and high level manipulation of region class
 */
public class Quadtree implements QuadtreeInterface{
	public final int TOT_X;
	public final int TOT_Y;
	private final int DEFAULT_DEPTH;
	public Region root;

	public Quadtree(int x, int y, int depth) {
		this.TOT_X = x;
		this.TOT_Y = y;
		this.DEFAULT_DEPTH = depth;
		root = new Region(0, 0, TOT_X, TOT_Y);
		init(root, 0);
	}

	/** 
	 * Inserts a new cooridnate pair 
	 */
	public void insert(RidePt insertRide) {
		insertHelper(insertRide, root);
	}

	private void insertHelper(RidePt insertRide, Region currRegion) {
		// Base case: has space to insert
		if (currRegion.isEmpty()) {
			currRegion.setRidePt(insertRide);
		}
		// Base case 2: Duplicate ride
		else if (!currRegion.isDivided() && insertRide.equals(currRegion.storedRidePt)) {
			// Add to number of rides instead of replacing
			currRegion.storedRidePt.addRides(insertRide.numRides());
		}
		// Recursive case 1: no space in region and no divisions
		else if (!currRegion.isDivided()) {
			resolveCollision(currRegion, insertRide, currRegion.storedRidePt);
		}
		// Recursive case 2: region is divided
		else {
			insertHelper(insertRide, currRegion.findSubregion(insertRide));
		}
	}

	/**
	 * Resolve collision where two points occupy same leaf
	 * Assumes both rides are different
	 * Assumption is met in insertHelper as it already checks for duplicates
	 */
	private void resolveCollision(Region currRegion, RidePt Ride1, RidePt Ride2) {
		currRegion.subDivide();
		Region subregion1 = currRegion.findSubregion(Ride1);
		Region subregion2 = currRegion.findSubregion(Ride2);

		// Recursive step: Both regions which the Ride lies in is the same
		if (subregion1 == subregion2) {
			resolveCollision(subregion1, Ride1, Ride2);
		}
		// Base case: If regions are different, set Rides to those regions
		else {
			subregion1.setRidePt(Ride1);
			subregion2.setRidePt(Ride2);
		}
	}

	/** 
	 * Get root node
	 */
	public Region getRoot() {
		return root;
	}

	public int getDefaultDepth() {
		return DEFAULT_DEPTH;
	}

	/**
	 *  Gets number of Rides in the quadtree
	 */
	public int size() {
		return countRides(root);
	}

	/**
	 * Find and return region which ridepoint is located in
	 * Finds region at desired depth
	 * If point not in quadtree, return null
	 */
	public Region findRegion(RidePt point) {
		if (!this.root.containsLocation(point)) {
			return null;
		}
		return findRegionHelper(point, this.root, 0);
	}

	private Region findRegionHelper(RidePt point, Region currRegion, int currDepth) {
		// Base case: desired depth reached
		if (currDepth >= DEFAULT_DEPTH) {
			return currRegion;
		}

		if (currRegion.isDivided()) {
			for (Region region : currRegion.subregionList) {
				if (region.containsLocation(point)) {
					// Recursive step: keep searching all subregions
					return findRegionHelper(point, region, currDepth + 1);
				}
			}
		}
		// Return current region if there are no regions in subregion list
		// This case should never be reached because the quadtree is 
		// initialized at default depth
		return currRegion;
	}

	/**
	 * Initialize quadtree with regions going down to specified depth
	 */
	public void init(Region region, int currDepth) {
		if (currDepth < DEFAULT_DEPTH) {
			region.subDivide();

			// Recursively call function again on regions
			init(region.NE, currDepth + 1);
			init(region.SE, currDepth + 1);
			init(region.SW, currDepth + 1);
			init(region.NW, currDepth + 1);
		}
	}

	/**
	 *  Gets number of Rides in a certain branch 
	 */
	public static int countRides(Region currRegion) {
		// Base case 1: region is empty
		if (currRegion.isEmpty()) {
			return 0;
		}
		// Base case 2: region contains a Ride
		if (currRegion.storesRidePt()) {
			return currRegion.storedRidePt.numRides();
		}
		// Recursive step: for every subregion count Rides
		int numRides = 0;
		for (Region region : currRegion.subregionList) {
			numRides += countRides(region);
		}
		return numRides;
	}

	/**
	 *  Gets number of ridePoints in a certain branch 
	 */
	public static int countRidePoints(Region currRegion) {
		// Base case 1: region is empty
		if (currRegion.isEmpty()) {
			return 0;
		}
		// Base case 2: region contains a Ride
		if (currRegion.storesRidePt()) {
			return 1;
		}
		// Recursive step: for every subregion count Rides
		int numRides = 0;
		for (Region region : currRegion.subregionList) {
			numRides += countRidePoints(region);
		}
		return numRides;
	}

	public static void main(String[] args) {
		Quadtree testQuad = new Quadtree(100, 100, 3);
		testQuad.insert(new RidePt(10,10));
		testQuad.insert(new RidePt(20,10));
		testQuad.insert(new RidePt(20,10));
		System.out.println(testQuad.root.SW.SW.storesRidePt());
		System.out.println(testQuad.root.SW.SW.SW.storesRidePt());
		System.out.println(countRides(testQuad.root));
		System.out.println(testQuad.getRoot());
		System.out.println(testQuad.findRegion(new RidePt(10,10)));
		System.out.println(testQuad.findRegion(new RidePt(0,0)));
		System.out.println(countRidePoints(testQuad.root));
	}
}