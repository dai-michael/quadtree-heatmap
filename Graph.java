public interface Graph{
	/* Inserts a new cooridnate pair */
	public void insert(int x, int y);

	/* Gets number of points in a certain branch */
	public int getPointNumber(int x, int y);

	/* Divide until each region only contains one point */
	public void subDivide();

}