package DaiToku;
import java.awt.Point;

public interface Graph{
	/**
	 *  Inserts a new cooridnate pair 
	 */
	public abstract void insert(Point insertPoint);

	/** 
	 *  Gets number of points in a certain branch 
	 */
	public abstract int countPoints(Region currRegion);

	/** 
	 *  Gets number of points in a certain branch 
	 */
	public abstract void init(Region region, int currDepth);

}