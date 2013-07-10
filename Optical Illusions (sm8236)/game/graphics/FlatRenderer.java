/*
 * Created on Mar 10, 2005
 *
 */
package game.graphics;

import game.maps.Map;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A map is a 2D array of tiles. It must store both the tile used and the height
 * of the tile. It supports editting of the map and random map generation.
 * 
 * @author Sean T. McBeth
 *  
 */
public class FlatRenderer extends Renderer
{
    /**
     * @see game.graphics.Renderer#Renderer(Map, String)
     * @param m
     *            the map data
     */
    public FlatRenderer(Map m)
    {
        this(m, "flattiles.png");
    }

    protected FlatRenderer(Map m, String file)
    {
        super(m, file);
    }


    /**
     * 
     * @see game.graphics.Renderer#paint(java.awt.Graphics2D)
     */
    public void paint(Graphics2D g)
    {
        super.paint(g);
        paintSheep(g);
    }

    protected void paintSheep(Graphics2D g)
    {
        if (sheep != null)
            sheep.paint(g, sheep.getX() * DIM, sheep.getY() * DIM);
    }

    /**
     * @see game.graphics.Renderer#getTileClicked(Point)
     */
    public Point getTileClicked(Point p)
    {
        return new Point(p.x / DIM, p.y / DIM);
    }

    /**
     * @see game.graphics.Renderer#getImageWidth()
     */
    public int getImageWidth()
    {
        return map.getWidth() * DIM;
    }

    /**
     * @see game.graphics.Renderer#getImageHeight()
     */
    public int getImageHeight()
    {
        return map.getHeight() * DIM;
    }

    /**
     * @see game.graphics.Renderer#update()
     */
    public void update()
    {
        sheep.update();
    }

    /**
     * @see game.graphics.Renderer#checkTile(int, int)
     */
    public void checkTile(int x, int y)
    {
        sheep.moveTo(x, y);
    }

    /**
     * @see game.graphics.Renderer#paintTile(java.awt.Graphics2D, int, int)
     */
    protected void paintTile(Graphics2D g, int x, int y)
    {
        int t = map.getTileValue(x, y);
        if (t < 8)
            t = 0;
        else
            t = 1;
        BufferedImage img = (BufferedImage) tiles.get(t);
        g.drawImage(img, x * DIM, y * DIM, null);
    }
}