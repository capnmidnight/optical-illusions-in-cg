/*
 * Created on Apr 6, 2005
 */
package game.maps;

import java.util.Random;

/**
 * Apr 6, 2005
 * 
 * @author Sean T. McBeth
 */
public class MapRandom extends Map
{
	Random			r;

	static int		FLAT	= 0, UP = 1, DOWN = 2, LEFT = 3, NDIAG = 4,
			PDIAG = 5, RIGHT = 6;

	static int[]	DIRS	= {NDIAG, UP, PDIAG, LEFT, FLAT, RIGHT, PDIAG,
			DOWN, NDIAG		};

	/**
	 * @see game.maps.Map#Map(int, int)
	 * @param width
	 * @param height
	 */
	public MapRandom(int width, int height)
	{
		super(width, height);
	}

	/**
	 * creates a map of random tile values.
	 */
	public void buildMap()
	{
		r = new Random();
		// maximum and minimum number of flatland must represent a constant
		// density of flatlands, given an increase in size of the map
		int maxFlats = getWidth() * getHeight() / 15;
		int minFlats = getWidth() * getHeight() / 20;
		maxFlats -= minFlats;
		if (maxFlats <= 0)
			maxFlats = 1;
		int numFlats = minFlats + r.nextInt(maxFlats) / 2;
		// maximum area of the flatland must allow for a flatland to be placed
		// in the center of the map and be completely contained on the map.
		int maxArea = Math.min(getWidth(), getHeight()) / 10;
		if (maxArea <= 0)
			maxArea = 1;
		// int maxHeight = 7;
		for (int h = 0; h < numFlats; ++h)
		{
			int x = r.nextInt(getWidth());
			int y = r.nextInt(getHeight());
			createFlat(x, y, 3 + r.nextInt(maxArea));
		}

		int maxHills = 5;
		int minHills = 2;
		maxHills -= minHills;
		int numHills = minHills + r.nextInt(maxHills);
		int maxHeight = Math.min(getWidth(), getHeight()) / 8;
		if (maxHeight <= 0)
			maxHeight = 1;
		int minHeight = 1;
		for (int h = 0; h < numHills; ++h)
		{
			tallestX = r.nextInt(getWidth());
			tallestY = r.nextInt(getHeight());
			int height = minHeight + r.nextInt(maxHeight);
			minHeight = height + 1;
			if (!createHill(tallestX, tallestY, height))
				--h;
		}
		fillTileValues();
		int x, y;
		do
		{
			x = r.nextInt(getWidth());
			y = r.nextInt(getHeight());

		}
		while (tileAlts[y][x] == 0);

	}

	/**
	 * creates a flatland at a given location.
	 * 
	 * @param lx
	 * @param ly
	 * @param h
	 */
	public void createFlat(int lx, int ly, int h)
	{
		for (int y = 0; y < getHeight(); ++y)
		{
			for (int x = 0; x < getWidth(); ++x)
			{
				int dx = x - lx;
				int dy = y - ly;
				int distance = (int) Math.sqrt(dx * dx + dy * dy);
				if (distance < h)
					tileAlts[y][x] = 1;
			}
		}
	}

	/**
	 * creates a hill at a given location. If the hill is not tall enough for
	 * the location, it will not be placed.
	 * 
	 * @param lx
	 * @param ly
	 * @param h
	 * @return true if hill was created
	 */
	public boolean createHill(int lx, int ly, int h)
	{
		if (h <= tileAlts[ly][lx])
		{
			return false;
		}
		tileAlts[ly][lx] = h;

		for (int y = 0; y < getHeight(); ++y)
		{
			for (int x = 0; x < getWidth(); ++x)
			{
				if (x != lx || y != ly)
				{
					int dx = x - lx;
					int dy = y - ly;
					int distance = (int) Math.sqrt(dx * dx + dy * dy);
					int th = h - distance;
					if (th > tileAlts[y][x])
						tileAlts[y][x] = th;
				}
			}

		}
		return true;
	}

	/**
	 * @param x location
	 * @param y location
	 * @return the index into the tile list of a given point on the map
	 */
	protected int calculateTileIndex(int x, int y)
	{
		if (tileAlts[y][x] == 0)
		{
			return calculateWaterTileIndex(x, y);
		}
		// else
		int tileParity = (x + y + 1) % 2;
		return 8 + tileParity * 7 + calculateLandTileDirection(x, y);
	}

	/**
	 * there are 8 different tiles used in displaying water, and they are
	 * displayed sequentially
	 * 
	 * @param x location
	 * @param y location
	 * @return the index of the water tile
	 */
	protected static int calculateWaterTileIndex(int x, int y)
	{
		return (x + y) % 8;
	}

	/**
	 * determines the direction of the slope of the land. The values returned
	 * are arbitrary, but consistent.
	 * 
	 * @param x location of tile
	 * @param y location of tile
	 * @return an index value for given tile
	 */
	protected int calculateLandTileDirection(int x, int y)
	{
		if (getTileHeight(x, y) == 1)
			return FLAT;
		int vSlope = calculateHeightDifferential(x, y - 1, x, y + 1);
		if (vSlope != 0)
			vSlope /= Math.abs(vSlope);
		int hSlope = calculateHeightDifferential(x - 1, y, x + 1, y);
		if (hSlope != 0)
			hSlope /= Math.abs(hSlope);
		int dir = (vSlope + 1) * 3 + hSlope + 1;
		return DIRS[dir];
	}

	/**
	 * @param x0 initial point location
	 * @param y0 initial point location
	 * @param x1 final point location
	 * @param y1 final point location
	 * @return the difference in height between the final and initial points
	 */
	protected int calculateHeightDifferential(int x0, int y0, int x1, int y1)
	{
		try
		{
			return getTileHeight(x1, y1) - getTileHeight(x0, y0);
		}
		catch (IndexOutOfBoundsException e)
		{
			return 0;
		}
	}
}