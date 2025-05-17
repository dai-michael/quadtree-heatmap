package DaiToku;
import java.awt.Point;

public interface QuadtreeInterface{
	/**
	 *  Inserts a new cooridnate pair 
	 */
	public abstract void insert(RidePt insertPoint);

	/** 
	 *  Gets number of points in a certain branch 
	 */
	public abstract int countRides(Region currRegion);

	/** 
	 *  Initializes tree to specified depth
	 */
	public abstract void init(Region region, int currDepth);

}