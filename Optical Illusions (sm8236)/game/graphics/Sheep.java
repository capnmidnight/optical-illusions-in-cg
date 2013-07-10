/*
 * Created on Apr 5, 2005
 *
 */
package game.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Sean T. McBeth
 *  
 */
public class Sheep
{
    int           x, y, tx, ty;

    BufferedImage avatar;

    /**
     * create a new sheep object
     * 
     * @param x
     *            the location of the sheep
     * @param y
     *            the location of the sheep
     */
    public Sheep(int x, int y)
    {
        this.x = x;
        this.y = y;
        try
        {
            avatar = Renderer.getImage("sheep.png");
        }
        catch (IOException e)
        {
            // file not found
        }
    }

    /**
     * paint the damn sheep.
     * 
     * @param g
     * @param transx
     * @param transy
     */
    public void paint(Graphics2D g, int transx, int transy)
    {

        g.drawImage(avatar, transx, transy, null);

    }

    /**
     * @return the width of the sheep in pixels
     */
    public int getImageWidth()
    {
        return avatar.getWidth();
    }

    /**
     * @return the height of the sheep in pixels
     */
    public int getImageHeight()
    {
        return avatar.getHeight();
    }

    /**
     * utilizes a feed back system to move the sheep to the target location
     * 
     * @param tx
     *            target x
     * @param ty
     *            target y
     */
    public void moveTo(int tx, int ty)
    {
        this.tx = tx;
        this.ty = ty;
    }

    /**
     * move the sheep
     *  
     */
    public void update()
    {
        if (tx != x || ty != y)
        {
            int dx = tx - x;
            int dy = ty - y;
            if (Math.abs(dx) > Math.abs(dy))
            {
                x += dx / Math.abs(dx);
            }
            else
            {
                y += dy / Math.abs(dy);
            }
        }
    }

    /**
     * @return Returns the x.
     */
    public int getX()
    {
        return x;
    }

    /**
     * @return Returns the y.
     */
    public int getY()
    {
        return y;
    }
}