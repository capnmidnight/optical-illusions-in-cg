/*
 * Created on Apr 9, 2005
 */
package work;

import game.graphics.FlatRenderer;
import game.graphics.IllusionRenderer;
import game.graphics.IsometricRenderer;
import game.graphics.Renderer;
import game.maps.Map;
import game.maps.MapRandom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * @author Sean T. McBeth Apr 9, 2005
 * 
 */
public class RendererProfiler
{
    private static final int     NUM_RENDER = 1000;
    private static BufferedImage img        = new BufferedImage (800, 700,
                                                    BufferedImage.TYPE_INT_ARGB);
    static int                   lastX, lastY;
    /**
     * main program entry point
     * 
     * @param args
     */
    public static void main (String[] args)
    {
        Graphics2D g = img.createGraphics ();
        g.setFont (new Font ("serif", Font.PLAIN, 36));
        g.setColor (Color.WHITE);
        g.fillRect (0, 0, 800, 700);
        g.setColor (Color.BLACK);
        g.drawString ("Renderering Time for 1000 Runs", 200, 50);
        g.drawString ("Seconds", 10, 30);
        for (int i = 0; i <= 5; ++i)
        {
            g.drawString ("" + (i * 5), 10, 550 - i * 100);
        }
        g.drawLine (50, 20, 50, 580);
        g.drawLine (20, 550, 780, 550);
        g.drawString ("Map Size", 350, 575);
        g.setColor (Color.GRAY);
        for (int i = 1; i <= 10; ++i)
        {
            AffineTransform t = g.getTransform ();
            int x = i * 70 - 10;
            g.translate (x, 660);
            g.rotate (-Math.PI / 4);
            g.drawString ( (i * 3) + "x" + (i * 4), 0, 0);
            g.setTransform (t);
            g.drawLine (x, 65, x, 610);
        }
        g.setColor (Color.BLACK);
        g.drawString ("Test 1", 700, 50);
        g.drawString ("Test 2", 700, 80);
        g.drawString ("Test 3", 700, 110);
        g.setColor (Color.BLUE);
        g.fillRect (680, 30, 15, 20);
        g.setColor (Color.RED);
        g.fillRect (680, 60, 15, 20);
        g.setColor (Color.GREEN);
        g.fillRect (680, 90, 15, 20);
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN};
        g.setStroke (new BasicStroke (8.0f));
        g.translate (-10, 0);
        for (int i = 0; i < 3; ++i)
        {
            g.setColor (colors[i]);
            System.out.println ("For renderer type #" + i);
            long duration = testMap (i, g);
            System.out.println ("Total Time: " + duration + " milliseconds\n");
        }

        try
        {
            ImageIO.write (img, "png", new File ("rendererprofiles.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            System.err.println ("Could not write image file");
        }
    }
    private static long testMap (int r, Graphics2D g)
    {
        long duration = 0;
        lastX = -1;
        lastY = -1;
        for (int i = 0; i < 10; ++i)
        {
            int testWidth = 3 * (i + 1);
            int testHeight = 4 * (i + 1);
            System.out.print ("  For map size: " + testWidth + " x "
                    + testHeight);
            Map map = new MapRandom (testWidth, testHeight);
            Renderer render = makeRenderer (r, map);
            long temp = runOneThousandRenderings (render);
            System.out.println ("  Time: " + temp + " milliseconds");
            int x = (i + 1) * 70;
            int y = (int) (550 - temp * 18 / 1000);
            if (lastX != -1 && lastY != -1)
            {
                g.drawLine (lastX, lastY, x, y);
            }
            lastX = x;
            lastY = y;
            duration += temp;
        }
        return duration;
    }
    private static Renderer makeRenderer (int i, Map map)
    {
        switch (i)
        {
            case 0 :
                return new FlatRenderer (map);
            case 1 :
                return new IllusionRenderer (map);
            case 2 :
                return new IsometricRenderer (map, false);
            default :
                return null;
        }
    }
    private static long runOneThousandRenderings (Renderer r)
    {
        int width = r.getImageWidth ();
        int height = r.getImageHeight ();
        BufferedImage canvas = new BufferedImage (width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics ();
        long begin = System.currentTimeMillis ();
        System.out.print ("  For " + NUM_RENDER + " renderings");
        for (int i = 0; i < NUM_RENDER; ++i)
        {
            r.paint (g);
        }
        long end = System.currentTimeMillis ();
        return end - begin;
    }
}