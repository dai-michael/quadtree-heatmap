public interface Graph{
	/* Inserts a new cooridnate pair */
	public abstract void insert(int x, int y);

	/* Gets number of points in a certain branch */
	public abstract int getPointNumber(int x, int y);

	// /* Divide until each region only contains one point */
	// public abstract void subDivide(Region region);

}