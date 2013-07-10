/*
 * Created on Mar 10, 2005
 *
 */
package game.graphics;

import game.maps.Map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * A map is a 2D array of tiles. It must store both the tile used and the height
 * of the tile. It supports editting of the map and random map generation.
 * 
 * @author Sean T. McBeth
 *  
 */
public abstract class Renderer
{
    // Leave this as is, I'm serious.
    private static class Loader
    {
        // empty, used to get a reference to the resources for this object.
    }

    Map                        map;

    /**
     * the dimensions of a tile.
     */
    public static int          DIM;

    protected static final int FORMAT = BufferedImage.TYPE_INT_RGB;

    protected ArrayList        tiles;

    protected Sheep            sheep;

    /**
     * constructor defining dimensions for a map
     * 
     * @param data
     *            the map to render
     * @param filename
     *            the location of the TileSet image in relation to the working
     *            directory
     */
    public Renderer(Map data, String filename)
    {
        map = data;
        tiles = new ArrayList();
        createTilePalette(filename);
        sheep = new Sheep(0, 0);
    }

    /**
     * @param img
     *            the master image
     * @param x
     *            the location of image
     * @param y
     *            the location of image
     * @return a Tile that is a sub image of BufferedImage img, defined by
     */
    private BufferedImage getSubTile(BufferedImage img, int x, int y)
    {
        BufferedImage temp = new BufferedImage(DIM, DIM, FORMAT);
        temp.getGraphics().drawImage(img, -x, -y, null);
        return temp;
    }

    /**
     * creates the TilePalette for this map. Since the map is abstract, each
     * subclass of map has intimate knowledge of exactly what it's tileset
     * contains. The map subclass may generate the tileset or load it from the
     * file system.
     * 
     * @param filename
     *            the location of the TileSet image in relation to the working
     *            directory
     */
    protected void createTilePalette(String filename)
    {
        try
        {
            BufferedImage tileset = Renderer.getImage(filename);
            int width = tileset.getWidth();
            DIM = tileset.getHeight();
            int n = width / DIM;
            width /= n;
            for (int i = 0; i < n; ++i)
            {
                this.tiles.add(this.getSubTile(tileset, i * width, 0));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    protected abstract void paintTile(Graphics2D g, int x, int y);

    /**
     * render the map to the given context context
     * 
     * @param g
     *            the rendering context
     */
    public void paint(Graphics2D g)
    {
        for (int y = 0; y < map.getHeight(); ++y)
        {
            for (int x = 0; x < map.getWidth(); ++x)
            {
                paintTile(g, x, y);
            }
        }
    }

    /**
     * loads an image from either the filesystem or the jar file that the
     * program is running from.
     * 
     * @param filename
     *            the location of the image, relative to the current working
     *            directory
     * @return an java.awt.Image object
     * @throws IOException
     *             on file not found
     */
    public static BufferedImage getImage(String filename) throws IOException
    {
        return ImageIO.read(new Renderer.Loader().getClass().getResource(
                filename));
    }

    /**
     * @param src
     *            location of the mouse click
     * @return the x, y coordinate of the tile that was clicked
     */
    public abstract Point getTileClicked(Point src);

    /**
     * 
     * @return the width of the image created by this map.
     */
    public abstract int getImageWidth();

    /**
     * 
     * @return the height of the image created by this map.
     */
    public abstract int getImageHeight();

    /**
     * necessary for any animation
     *  
     */
    public abstract void update();

    /**
     * checks the specified tile and does things to it.
     * 
     * @param x
     *            the x location of the tile
     * @param y
     *            the y location of the tile
     */
    public abstract void checkTile(int x, int y);

    /**
     * @return true if the sheep is on the tallest hill.
     */
    public boolean isGoalMet()
    {
        return sheep.x == map.tallestX && sheep.y == map.tallestY;
    }
}