/*
 * Created on Apr 6, 2005
 */
package game.maps;

/**
 * Apr 6, 2005
 * 
 * @author Sean T. McBeth
 */
public abstract class Map
{
    protected int[][] tileVals, tileAlts;

    /**
     * location of tallest hill
     */
    public int        tallestX;

    /**
     * location of tallest hill
     */
    public int        tallestY;

    /**
     * main constructor, build a map of specified dimensions
     * 
     * @param width
     * @param height
     */
    public Map(int width, int height)
    {
        tileVals = new int[height][width];
        tileAlts = new int[height][width];
        buildMap();
    }

    /**
     * @param x
     *            coordinate component value
     * @param y
     *            coordinate component value
     * @return the value of the tile at the given coordinate
     */
    public int getTileValue(int x, int y)
    {
        return tileVals[y][x];
    }

    /**
     * @return the height of the map.
     */
    public int getHeight()
    {
        return tileVals.length;
    }

    /**
     * @return the width of the map.
     */
    public int getWidth()
    {
        return tileVals[0].length;
    }

    /**
     * @param x
     *            coordinate component value
     * @param y
     *            coordinate component value
     * @return the height of the tile at the given coordinate.
     */
    public int getTileHeight(int x, int y)
    {
        return tileAlts[y][x];
    }

    /**
     * fills the tileVals array with the correct values given the associated
     * tileAlt value
     */
    public void fillTileValues()
    {
        for (int y = 0; y < getHeight(); ++y)
        {
            for (int x = 0; x < getWidth(); ++x)
            {
                tileVals[y][x] = calculateTileIndex(x, y);
            }
        }
    }

    /**
     * @param x
     *            location
     * @param y
     *            location
     * @return the index into the tile list of a given point on the map
     */
    protected abstract int calculateTileIndex(int x, int y);

    /**
     * creates a map of random tile values.
     */
    public abstract void buildMap();

}