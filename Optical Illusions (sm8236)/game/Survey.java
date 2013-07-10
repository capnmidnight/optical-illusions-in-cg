/*
 * Created on Apr 7, 2005
 */
package game;

import game.graphics.FlatRenderer;
import game.graphics.IllusionRenderer;
import game.graphics.IsometricRenderer;
import game.graphics.Renderer;
import game.maps.Map;
import game.maps.MapRandom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Apr 7, 2005
 * 
 * @author Sean T. McBeth
 */
public class Survey extends JFrame
        implements
            ActionListener,
            MouseListener,
            KeyListener
{
    private static final int FLAT_TEST      = 0;
    private static final int ILLUSION_TEST  = 1;
    private static final int ISOMETRIC_TEST = 2;
    private Random           r;
    private int              testType, testCount;
    private boolean          refresh;
    private int              results;
    private Renderer         renderer;
    private Map              map;
    private BufferStrategy   bs;
    private Timer            t;
    private static final int MAX_TEST       = 5;

    private Survey ()
    {
        super ("Graphics Testing");
        setupFrame ();
        setupVars ();
        setupMap ();
        setupDoubleBuffering ();
        start ();
    }

    private void setupFrame ()
    {
        this.setBounds (0, 0, 800, 600);
        this.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        this.setUndecorated (true);
        this.addMouseListener (this);
        this.addKeyListener (this);

    }

    /**
     * 
     */
    private void setupVars ()
    {
        r = new Random ();
        // testType = r.nextInt (3);
        testType = 0;
        testCount = 0;
        refresh = true;
        results = testType;
        t = new Timer (100, this);
    }

    private void setupMap ()
    {
        ++testCount;
        refresh = true;
        int width, height;
        if (testType == ISOMETRIC_TEST)
        {

            double dim = Math.min (this.getWidth (), this.getHeight () * 2);
            width = (int) (dim * Math.sqrt (2)) / 35;
            height = width * 3 / 4;
        }
        else
        {
            width = this.getWidth () / 20;
            height = this.getHeight () / 20;
        }
        map = new MapRandom (width, height);
        switch (testType)
        {
            case FLAT_TEST :
                renderer = new FlatRenderer (map);
                break;
            case ILLUSION_TEST :
                renderer = new IllusionRenderer (map);
                break;
            case ISOMETRIC_TEST :
                renderer = new IsometricRenderer (map);
                break;
            default : // none
        }
        this.setSize (renderer.getImageWidth (), renderer.getImageHeight ());
    }

    private void setupDoubleBuffering ()
    {
        this.setVisible (true);
        this.createBufferStrategy (2);
        bs = this.getBufferStrategy ();
    }

    private void start ()
    {
        JOptionPane
                .showMessageDialog (
                        this,
                        "You will be presented with "
                                + MAX_TEST
                                + " random maps.\n"
                                + "Evaluate the map and 'heard' the sheep to the tallest hill by clicking it.\n"
                                + "Once the sheep has moved to the spot, hit 'E' to advance to the next map.");
        t.start ();
    }

    /**
     * update the display, but only in the necessary places.
     * 
     * @param g
     */
    public void update (Graphics g)
    {
        paint (g);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed (ActionEvent e)
    {
        if (e.getSource () == t)
        {
            renderer.update ();
            repaint ();
        }

    }

    /**
     * render the scene
     * 
     * @param gOld
     *            an unused graphics context
     */
    public void paint (Graphics gOld)
    {
        if (bs != null)
        {
            Graphics2D g = (Graphics2D) bs.getDrawGraphics ();
            if (refresh)
            {
                g.setColor (Color.BLACK);
                g.fillRect (0, 0, this.getWidth (), this.getHeight ());
            }

            renderer.paint (g);
            bs.show ();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked (MouseEvent e)
    {
        if (e.getButton () == MouseEvent.BUTTON1)
        {
            Point p = renderer.getTileClicked (e.getPoint ());
            renderer.checkTile (p.x, p.y);
        }
        else if (e.getButton () == MouseEvent.BUTTON3)
        {

            BufferedImage img = new BufferedImage (renderer.getImageWidth (),
                    renderer.getImageHeight (), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics ();
            renderer.paint (g);
            try
            {
                ImageIO.write (img, "png", new File (renderer.getClass ()
                        .getName ()
                        + ".png"));
            }
            catch (Exception ex)
            {
                ex.printStackTrace ();
                System.exit (1);
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered (MouseEvent e)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited (MouseEvent e)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed (MouseEvent e)
    {
        // empty

    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased (MouseEvent e)
    {
        // empty

    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed (KeyEvent e)
    {
        int c = e.getKeyCode ();

        if (c == KeyEvent.VK_E)
        {
            results <<= 1;
            if (renderer.isGoalMet ())
            {
                results |= 1;
            }

            if (testCount < MAX_TEST)
            {
                setupMap ();
            }
            else
            {
                JOptionPane
                        .showInputDialog (
                                this,
                                "The test session is complete. Please transmit the result code to the test administrator.",
                                "" + results);
                System.exit (0);
            }
        }

    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased (KeyEvent e)
    {
        // empty

    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped (KeyEvent e)
    {
        // empty

    }

    /**
     * main program entry point
     * 
     * @param args
     */
    public static void main (String[] args)
    {
        new Survey ();
    }
}