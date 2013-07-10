/*
 * Created on Apr 6, 2005
 */
package game.maps;

import junit.framework.TestCase;

/**
 * Apr 6, 2005
 * 
 * @author Sean T. McBeth
 */
public class Map___TEST extends TestCase
{
    private int testMapHeight = 7;

    private int testMapWidth  = 11;

    private Map testMap;

    private class DummyMap extends Map
    {
        /**
         * 
         * @param w
         * @param h
         */
        public DummyMap(int w, int h)
        {
            super(w, h);
        }

        /**
         * @param x
         * @param y
         * @return crap
         */
        protected int calculateTileIndex(int x, int y)
        {
            // empty
            return 0;
        }

        /**
         * 
         *  
         */
        public void buildMap()
        {
            for (int y = 0; y < getHeight(); ++y)
            {
                for (int x = 0; x < getWidth(); ++x)
                {
                    tileVals[y][x] = (y + x) % 2;
                }
            }
        }

    }

    protected void setUp()
    {
        testMap = new DummyMap(testMapWidth, testMapHeight);
    }

    /**
     * tests the getHeight method of the class, to see if it returns the correct
     * results. Remember, we are paranoid.
     *  
     */
    public void testGetHeight()
    {
        assertEquals(testMapHeight, testMap.getHeight());
    }

    /**
     * tests the getWidth method of the class, to see if it returns the correct
     * results. Remember, we are paranoid.
     *  
     */
    public void testGetWidth()
    {
        assertEquals(testMapWidth, testMap.getWidth());
    }

    /**
     * calculate the location of a border test case for an array
     * 
     * @param dim
     *            the length of the array
     * @param coord
     *            the relative location
     * @return the location in the array.
     */
    private int calcCoord(int dim, int coord)
    {
        return (int) (((dim - 1.0) / 2.0) * (coord - 1.0));
    }

    /**
     * determines if an array access caused an IndexOutOfBoundsException or not.
     * 
     * @param x
     *            the second index into the 2D array
     * @param y
     *            the first index into the 2D array
     * @return true when the exception is caught
     */
    private boolean catchOutOfBounds(int x, int y)
    {
        try
        {
            testMap.getTileHeight(x, y);
        }
        catch (IndexOutOfBoundsException e)
        {
            return true;
        }
        return false;
    }

    /**
     * test the getTileHeight method. It should throw an exception on an out of
     * bounds error. It should return the value of the height of the tile at the
     * given coordinate otherwise.
     */
    public void testGetTileHeightExceptions()
    {
        for (int dy = 0; dy < 5; ++dy)
        {
            for (int dx = 0; dx < 5; ++dx)
            {
                int x = calcCoord(testMapWidth, dx);
                int y = calcCoord(testMapHeight, dy);
                if (dx > 0 && dx < 4 && dy > 0 && dy < 4)
                {
                    assertFalse("No exception should be thrown (" + dx + ","
                            + dy + ")", catchOutOfBounds(x, y));
                }
                else
                {
                    assertTrue("An exception should be thrown (" + dx + ","
                            + dy + ")", catchOutOfBounds(x, y));
                }
            }
        }
    }

}