package DaiToku;
import java.awt.Point;

public interface QuadtreeInterface{
	/**
	 *  Inserts a new cooridnate pair 
	 */
	public abstract void insert(RidePt insertPoint);

	/**
	 * Find and return region which ridepoint is located in
	 * Finds region at desired depth
	 * If point not in quadtree, return null
	 */
	public abstract Region findRegion(RidePt point);

	/** 
	 *  Initializes tree to specified depth
	 */
	public abstract void init(Region region, int currDepth);
	
	/**
	 *  Gets number of Rides in the quadtree
	 */
	public int size();
}