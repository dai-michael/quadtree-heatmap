package DaiToku;
import java.awt.Point;

public interface Graph{
	/**
	 *  Inserts a new cooridnate pair 
	 */
	public abstract void insert(RidePt insertPoint);

	/** 
	 *  Gets number of points in a certain branch 
	 */
	public abstract int countRides(Region currRegion);

	/** 
	 *  Gets number of points in a certain branch 
	 */
	public abstract void init(Region region, int currDepth);

}