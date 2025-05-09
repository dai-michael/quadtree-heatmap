public abstract Graph{
	/* Inserts a new cooridnate pair */
	public void insert(int x, int y);

	/* Gets number of points in a certain branch */
	public int getPointNumber();

	/* Divide until each region only contains one point */
	public void subDivide();

}