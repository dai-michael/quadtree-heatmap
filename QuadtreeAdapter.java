package DaiToku;

import java.util.Arrays;
import java.util.ArrayList;
import java.lang.Exception;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

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


	/**
	 * Injests CSV of coordinates into the quadtree
	 * Coordinates are represented as six digit numbers as 
	 * the quadtree only takes integers
	 */
	public QuadtreeAdapter(File csv, int depth) {

		try{
			Scanner scanner = new Scanner(csv);
			// Skip first line
			scanner.nextLine();

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
				if (x > maxX) maxX = x;
				if (y > maxY) maxY = y;
				if (x < minX) minX = x;
				if (y < minY) minY = y;
			}
		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}		

		int width = maxX - minX;
		int height = maxY - minY; 

		quadtree = new Quadtree(width, height, depth);

		try{
			Scanner scanner = new Scanner(csv);
			// Skip first line
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				RidePt readPt = processNextLine(scanner.nextLine());
				quadtree.insert(convertToQuad(readPt));
				System.out.println("inserted point");
			}
		}
		catch(FileNotFoundException e){
			 System.err.println("File not found: " + e.getMessage());
		}		

	}


	// Converts floats with 4 decimal places to integer representation for quadtree
	private RidePt processNextLine(String line) {
		String[] lineArray = line.trim().split(",");
		double x = Double.parseDouble(lineArray[0].substring(0, 7));
		double y = Double.parseDouble(lineArray[1].substring(0, 7));
		int convertedX = (int) (x * 10000);
		int convertedY = (int) (y * 10000);
		int numRides = Integer.parseInt(lineArray[2]);
		return new RidePt(convertedX, convertedY, numRides);
	}

	public void insert(RidePt insertPoint) {
		quadtree.insert(convertToQuad(insertPoint));
	}

	public RidePt convertToQuad(RidePt originalPt) {
		int newX = (int) originalPt.getX() - minX;
		int newY = (int) originalPt.getY() - minY;
		return new RidePt(newX, newY, originalPt.numRides());
	}

	public static void main(String[] args) {
		File csv = new File("rides.csv");
		QuadtreeAdapter test = new QuadtreeAdapter(csv, 4);
		System.out.println(test.quadtree.size());
		System.out.println(test.quadtree.TOT_X);
		System.out.println(test.quadtree.TOT_Y);
	}


}