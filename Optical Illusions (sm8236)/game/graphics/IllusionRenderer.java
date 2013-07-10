/*
 * Created on Apr 5, 2005
 *
 */
package game.graphics;

import game.maps.Map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author Sean T. McBeth
 *  
 */
public class IllusionRenderer extends FlatRenderer
{
    /**
     * reuse a lot of code
     * 
     * @param m
     */
    public IllusionRenderer(Map m)
    {
        super(m, "islandtiles.png");
    }

    /**
     * @see game.graphics.Renderer#paintTile(Graphics2D, int, int)
     */
    public void paintTile(Graphics2D g, int x, int y)
    {
        int t = map.getTileValue(x, y);
        BufferedImage img = (BufferedImage) tiles.get(t);
        g.drawImage(img, x * DIM, y * DIM, null);
    }
}