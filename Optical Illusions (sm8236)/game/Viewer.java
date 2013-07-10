package game;

import game.graphics.Renderer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * Created on Mar 10, 2005
 *
 */

/**
 * @author Sean T. McBeth
 * 
 */
public class Viewer extends JFrame implements MouseListener, Runnable
{
    Renderer       render;

    int            w = 40;

    int            h = 30;

    Thread         t;

    BufferStrategy bs;

    /**
     * 
     * @param render
     */
    public Viewer (Renderer render)
    {
        super ("title");
        this.setUndecorated (true);
        this.render = render;
        this.setBounds (100, 50, render.getImageWidth (), render
                .getImageHeight ());
        this.addMouseListener (this);
        this.setVisible (true);
        this.createBufferStrategy (2);
        bs = this.getBufferStrategy ();

        t = new Thread (this);
        t.start ();
    }

    /**
     * 
     */
    public void run ()
    {
        long now = System.currentTimeMillis ();
        long later = 0;
        while (true)
        {
            later = System.currentTimeMillis ();
            if (later - now > 250)
            {
                now = later;
                render.update ();
                repaint ();
            }
        }
    }

    /**
     * @param gOld
     *            unused
     */
    public void paint (Graphics gOld)
    {
        if (bs != null)
        {
            Graphics g = bs.getDrawGraphics ();
            render.paint ((Graphics2D) g);
            bs.show ();
        }
    }

    /**
     * @param arg0
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed (MouseEvent arg0)
    {
        if (arg0.getButton () == MouseEvent.BUTTON1)
        {
            Point p = render.getTileClicked (arg0.getPoint ());
            render.checkTile (p.x, p.y);
        }
        else if (arg0.getButton () == MouseEvent.BUTTON3)
        {

            BufferedImage img = new BufferedImage (this.getWidth () * 2, this
                    .getHeight () * 2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics ();
            render.paint (g);
            try
            {
                ImageIO.write (img, "png", new File (render.getClass ()
                        .getName ()
                        + ".png"));
            }
            catch (Exception e)
            {
                e.printStackTrace ();
                System.exit (1);
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered (MouseEvent arg0)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited (MouseEvent arg0)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked (MouseEvent arg0)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased (MouseEvent arg0)
    {
        // empty

    }
}