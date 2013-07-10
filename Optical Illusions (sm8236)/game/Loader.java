/*
 * Created on Apr 6, 2005
 */
package game;

import game.graphics.FlatRenderer;
import game.graphics.IllusionRenderer;
import game.graphics.IsometricRenderer;
import game.graphics.Renderer;
import game.maps.Map;
import game.maps.MapRandom;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Apr 6, 2005
 * 
 * @author Sean T. McBeth
 */
public class Loader extends JFrame implements ActionListener
{
    Map        map;

    Renderer   renderer;

    static int mapWidth  = 30;

    static int mapHeight = 30;

    JButton    butOrth, butIll, butIso;

    /**
     * 
     *  
     */
    public Loader()
    {
        super(Messages.getString("Loader.0")); //$NON-NLS-1$

        map = new MapRandom(mapWidth, mapHeight);
        LayoutManager l = new GridLayout(3, 1);
        butOrth = new JButton(Messages.getString("Loader.1")); //$NON-NLS-1$
        butIll = new JButton(Messages.getString("Loader.2")); //$NON-NLS-1$
        butIso = new JButton(Messages.getString("Loader.3")); //$NON-NLS-1$
        butOrth.addActionListener(this);
        butIll.addActionListener(this);
        butIso.addActionListener(this);
        this.getContentPane().setLayout(l);
        this.getContentPane().add(butOrth);
        this.getContentPane().add(butIll);
        this.getContentPane().add(butIso);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        JButton o = (JButton) e.getSource();
        if (o == butOrth)
        {
            renderer = new FlatRenderer(map);
        }
        else if (o == butIll)
        {
            renderer = new IllusionRenderer(map);
        }
        else if (o == butIso)
        {
            renderer = new IsometricRenderer(map);
        }
        new Viewer(renderer).setVisible(true);
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        new Loader().setVisible(true);
    }
}