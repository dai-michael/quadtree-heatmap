package DaiToku;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * Injests CSV of coordinates into the quadtree
 * Coordinates are converted into six digit integers as
 * the quadtree only takes integers
 * Adapter is hardcoded to only accept coordinates in the
 * Northwestern hemisphere, as that is our area of interest
 * Stores adapted quadtree
 * Allows conversion for adapted quadtree entries back to coordinates if 
 * necessary, although we did not have time to impelment or fully
 * utilize this feature
 */
public class QuadtreeAdapter{

	public Quadtree quadtree;
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	private int dimX1;
	private int dimY1;
	private int dimX2;
	private int dimY2;

	public QuadtreeAdapter(File csv, int depth) {
		try{
			Scanner scanner = new Scanner(csv);
			// Skip first line
			scanner.nextLine();

			// Get min and max values to define how big the quadtree should be
			if (scanner.hasNextLine()) {
				RidePt firstPt = processNextLine(scanner.nextLine());
				minX = (int) firstPt.getX();
				minY = (int) firstPt.getY();
				maxX = minX;
				maxY = minY;
			}

			while (scanner.hasNextLine()) {
				RidePt currPt = processNextLine(scanner.nextLine());
				int x = (int) currPt.getX();
				int y = (int) currPt.getY();

				// Set max and min values
				if (x > maxX) maxX = x;
				if (y > maxY) maxY = y;
				if (x < minX) minX = x;
				if (y < minY) minY = y;
			}

		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}		

		// Calculate new quadtree
		int width = maxX - minX;
		int height = maxY - minY; 
		quadtree = new Quadtree(width, height, depth);

		// Add rides to quadtree
		try{
			Scanner scanner = new Scanner(csv);
			// Skip first line
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				// Process line into point
				RidePt readPt = processNextLine(scanner.nextLine());
				// Convert that point into one quadtree can take
				quadtree.insert(convertToQuad(readPt));
			}
		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}		
	}

	/** 
	 * Converts floats with 4 decimal places to integer
	 * representation for use in quadtree
	 */
	private RidePt processNextLine(String line) {
		String[] lineArray = line.trim().split(",");
		double y = Double.parseDouble(lineArray[0].substring(0, 7));
		double x = Double.parseDouble(lineArray[1].substring(0, 7));
		int convertedX = (int) (x * 10000);
		int convertedY = (int) (y * 10000);
		int numRides = Integer.parseInt(lineArray[2]);
		return new RidePt(convertedX, convertedY, numRides);
	}

	// Convert processed point into one the quadtree can accept
	public RidePt convertToQuad(RidePt originalPt) {
		// Flip x coordinates horizontally and y coordinates vertically
		int newX = (maxX - minX) - (int) (originalPt.getX() - minX);
		int newY = (maxY - minY) - (int) (originalPt.getY() - minY);
		return new RidePt(newX, newY, originalPt.numRides());
	}

	// Insert converted point into quadtree
	public void insert(RidePt insertPoint) {
		quadtree.insert(convertToQuad(insertPoint));
	}

	public static void main(String[] args) {
		File csv = new File("rides.csv");
		QuadtreeAdapter test = new QuadtreeAdapter(csv, 4);
		System.out.println(test.quadtree.size());
		// Compare total X and Total Y to range of coordinates in dataset
		// to see if we converted successfuly
		System.out.println(test.quadtree.TOT_X);
		System.out.println(test.quadtree.TOT_Y);
	}


}