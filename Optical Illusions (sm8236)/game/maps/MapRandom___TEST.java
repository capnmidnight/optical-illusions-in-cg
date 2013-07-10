/*
 * Created on Mar 10, 2005
 *  
 */
package game.maps;

import junit.framework.TestCase;

/**
 * @author Sean T. McBeth
 *  
 */
public class MapRandom___TEST extends Map___TEST
{
	String	filename;

	int		testMapWidth;

	int		testMapHeight;

	Map		testMap;

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp()
	{
		filename = "islandtiles.png";
		testMapWidth = 23;
		testMapHeight = 27;
		testMap = new MapRandom(testMapWidth, testMapHeight);
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * @param y
	 * @param x
	 * @param deltaHeight
	 */
	protected void fillHeightDifferential(int y, int x, int[] deltaHeight)
	{
		deltaHeight[0] = ((MapRandom) testMap).calculateHeightDifferential(x,
				y, x, y - 1);
		deltaHeight[1] = ((MapRandom) testMap).calculateHeightDifferential(x,
				y, x - 1, y);
		deltaHeight[2] = ((MapRandom) testMap).calculateHeightDifferential(x,
				y, x + 1, y);
		deltaHeight[3] = ((MapRandom) testMap).calculateHeightDifferential(x,
				y, x, y + 1);
	}

	/**
	 * tests the integrity of the randomly generated content. testing the height
	 * of the map. The height of the map should never vary by more than one unit
	 * from tile to tile.
	 *  
	 */
	public void testRandomizerHeightIntegrity()
	{
		// If this test does not fail in 100 tries, it probably won't ever fail.
		for (int j = 0; j < 100; ++j)
		{
			testMap.buildMap();

			int totalNonZero = 0;
			for (int y = 0; y < testMapHeight; ++y)
			{
				for (int x = 0; x < testMapWidth; ++x)
				{
					totalNonZero += checkTileHeights(y, x);
				}
			}
			assertTrue("some tiles should be higher than 0", totalNonZero != 0);
		}
	}

	/**
	 * @param y
	 * @param x
	 * @return 1 if this section is higher than 0
	 */
	private int checkTileHeights(int y, int x)
	{
		int[] deltaHeight = new int[4];
		fillHeightDifferential(y, x, deltaHeight);
		int totalNonZero = 0;
		for (int i = 0; i < 4; ++i)
		{
			if (deltaHeight[i] > 0)
			{
				++totalNonZero;
				if (deltaHeight[i] > 1)
				{
					fail("change in map height was too great");
				}
			}
		}
		return totalNonZero;
	}

	/**
	 * fills an array with the given value, used for initialization
	 * 
	 * @param arr the array to be filled
	 * @param val the value with which to fill the array
	 */
	private void fillBoolArray(boolean[] arr, boolean val)
	{
		for (int i = 0; i < arr.length; ++i)
		{
			arr[i] = val;
		}
	}

	/**
	 * the map should alternate color schemes in a checkerboard fashion, and
	 * should only use certain colors in certain situations. These colors are
	 * defined as indexes into a tile palette. The only way the test knows if
	 * these are right is if they utilize values independent to each possible
	 * situation. Related tiles should also be in sequence.
	 */

	/**
	 * a water tile must be one of the 8 available tiles for water. What the map
	 * uses is arbitrary, all that is important is that it uses 8 different
	 * values for those tiles.
	 */
	public void testWaterTiles()
	{
		testMap.buildMap();

		// the water requires 8 different tiles
		int[] waterTiles;
		boolean[] waterTileIsSet;
		waterTiles = new int[8];
		waterTileIsSet = new boolean[8];
		fillBoolArray(waterTileIsSet, false);
		for (int y = 0; y < testMap.getHeight(); ++y)
		{
			for (int x = 0; x < testMap.getWidth(); ++x)
			{
				if (testMap.getTileHeight(x, y) == 0)
				{
					int expectedTileIndex = testMap.calculateTileIndex(x, y);
					int realTileValue = testMap.getTileValue(x, y);
					if (!waterTileIsSet[expectedTileIndex])
					{
						waterTiles[expectedTileIndex] = realTileValue;
						waterTileIsSet[expectedTileIndex] = true;
					}
					assertEquals(
							"a tile value does not match the expectation.",
							waterTiles[expectedTileIndex], realTileValue);
				}
			}
		}
	}

	/**
	 * a land tile must be one of 14 tiles available for land. The values the
	 * map uses is arbitrary, all that matters is that those values are unique
	 * unto each other.
	 */
	public void testLandTiles()
	{
		testMap.buildMap();

		// the hills require 14 different tiles.
		int[] landTiles;
		boolean[] landTileIsSet;
		landTiles = new int[14];
		landTileIsSet = new boolean[14];
		fillBoolArray(landTileIsSet, false);
		for (int y = 0; y < testMap.getHeight(); ++y)
		{
			for (int x = 0; x < testMap.getWidth(); ++x)
			{
				if (testMap.getTileHeight(x, y) > 0)
				{
					int realTileValue = testMap.getTileValue(x, y);
					/*
					 * subtract 8 to fit the tile index into the temporary array
					 * index bounds, as the land tiles are offset by the water
					 * tiles.
					 */
					int expectedTileIndex = testMap.calculateTileIndex(x, y) - 8;

					if (!landTileIsSet[expectedTileIndex])
					{
						landTiles[expectedTileIndex] = realTileValue;
						landTileIsSet[expectedTileIndex] = true;
					}
					assertEquals(
							"a tile value does not match the expectation.",
							landTiles[expectedTileIndex], realTileValue);
				}
			}
		}
	}
}