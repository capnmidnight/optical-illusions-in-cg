/*
 * Created on Mar 23, 2005
 */
package game.graphics;

import game.maps.Map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

/**
 * Mar 23, 2005
 * 
 * @author Sean T. McBeth
 */
public class Renderer___TEST extends TestCase
{
    String filename;

    protected class DummyMap extends Map
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
         * (non-Javadoc)
         * 
         * @see game.maps.Map#buildMap()
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

        /*
         * (non-Javadoc)
         * 
         * @see game.maps.Map#calculateTileIndex(int, int)
         */
        protected int calculateTileIndex(int x, int y)
        {
            // TODO Auto-generated method stub
            return 0;
        }
    }

    private class DummyRenderer extends Renderer
    {
        /**
         * call the super constructor
         * 
         * @param m
         *            the map data
         */
        public DummyRenderer(Map m)
        {
            super(m, filename);
        }

        /**
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#checkTile(int, int)
         */
        public void checkTile(int x, int y)
        {
        // TODO Auto-generated method stub

        }

        /**
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#getImageHeight()
         */
        public int getImageHeight()
        {
            // TODO Auto-generated method stub
            return map.getHeight() * DIM;
        }

        /**
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#getImageWidth()
         */
        public int getImageWidth()
        {
            // TODO Auto-generated method stub
            return map.getWidth() * DIM;
        }

        /**
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#getTileClicked(java.awt.Point)
         */
        public Point getTileClicked(Point src)
        {
            // TODO Auto-generated method stub
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#paintTile(java.awt.Graphics2D, int, int)
         */
        protected void paintTile(Graphics2D g, int x, int y)
        {
            g.drawImage(((BufferedImage) tiles.get(map.getTileValue(x, y))), x
                    * DIM, y * DIM, null);

        }

        /**
         * (non-Javadoc)
         * 
         * @see game.graphics.Renderer#update()
         */
        public void update()
        {
        // TODO Auto-generated method stub

        }
    }

    int      testMapWidth;

    int      testMapHeight;

    Map      testMap;

    Renderer testRenderer;

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        filename = "testtiles.png";

        testMapWidth = 7;
        testMapHeight = 11;
        testMap = new DummyMap(testMapWidth, testMapHeight);
        testRenderer = new DummyRenderer(testMap);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }


    /**
     * this map should generate tile data specific for the map
     * 
     * @throws IOException
     *             on file not found for islandtiles.png in the game.graphics
     *             package.
     */
    public void testGetPalette() throws IOException
    {
        // if all the of the tiles are rendered in order, they should look
        // exactly the same as the original tileset
        BufferedImage tileset = Renderer.getImage(filename);
        BufferedImage temp = new BufferedImage(tileset.getWidth(), tileset
                .getHeight(), Renderer.FORMAT);
        ArrayList tiles = testRenderer.tiles;
        int n = tiles.size();
        int w = Renderer.DIM;
        int tsw = n * w;
        assertEquals("The tile set should be as wide as all of the tiles", tsw,
                tileset.getWidth());
        Graphics2D g = temp.createGraphics();
        for (int i = 0; i < tiles.size(); ++i)
        {
            g
                    .drawImage((BufferedImage) tiles.get(i), i * Renderer.DIM,
                            0, null);
        }
        for (int y = 0; y < temp.getHeight(); ++y)
        {
            for (int x = 0; x < temp.getWidth(); ++x)
            {
                assertEquals("Image Test failed at (" + x + "," + y + ")",
                        tileset.getRGB(x, y), temp.getRGB(x, y));
            }
        }
    }

    /**
     * checks a portion of a larger image for consistency with the tileset.
     * 
     * @param img
     *            the map
     * @param ix
     *            the index location into the map
     * @param iy
     *            the index location into the map
     * @param t
     *            the tile to test against
     */
    private void tileTester(BufferedImage img, int ix, int iy, BufferedImage t)
    {
        for (int y = 0; y < Renderer.DIM; ++y)
        {
            for (int x = 0; x < Renderer.DIM; ++x)
            {
                int px = ix * Renderer.DIM + x;
                int py = iy * Renderer.DIM + y;
                assertEquals("Pixel of map at (" + px + "," + py
                        + ") failed to match a tile at (" + x + "," + y
                        + "). TileDIM:" + Renderer.DIM, t.getRGB(x, y), img
                        .getRGB(px, py));
            }
        }
    }

    /**
     * testing if the map paints as expected
     *  
     */
    public void testPaint()
    {
        BufferedImage canvas = new BufferedImage(testRenderer.getImageWidth(),
                testRenderer.getImageHeight(), Renderer.FORMAT);
        testRenderer.paint(canvas.createGraphics());
        for (int y = 0; y < testMap.getHeight(); ++y)
        {
            for (int x = 0; x < testMap.getWidth(); ++x)
            {
                tileTester(canvas, x, y, (BufferedImage) testRenderer.tiles
                        .get(testMap.getTileHeight(x, y)));
            }
        }
    }
}