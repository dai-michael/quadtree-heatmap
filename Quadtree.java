package DaiToku;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Quadtree made for storing RidePt
 */
public class Quadtree implements Graph{
	public final int TOT_X;
	public final int TOT_Y;
	private int defaultDepth;
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
		return defaultDepth;
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
	 *  Gets number of Rides in the quadtree
	 */
	public int size() {
		return countRides(root);
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
		Quadtree testQuad = new Quadtree(1000000, 1000000, 2);
		testQuad.insert(new RidePt(10,10));
		testQuad.insert(new RidePt(20,10));
		testQuad.insert(new RidePt(20,10));
		System.out.println(testQuad.root.SW.SW.storesRidePt());
		System.out.println(testQuad.root.SW.SW.SW.storesRidePt());
		System.out.println(testQuad.countRides(testQuad.root));
		System.out.println(testQuad.getRoot());

	}
}