/*
 * Created on Apr 5, 2005
 *  
 */
package game.graphics;

import game.maps.Map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author Sean T. McBeth
 *  
 */
public class IsometricRenderer extends FlatRenderer
{
	private static int		HEIGHT	= 10;

	private AffineTransform	isometricView;

	private int				miny	= 0;

	private BufferedImage	hitBuffer, imageBuffer;
	private boolean			cheat;
	private Polygon			left, right;

	/**
	 * create an isometric map, based on the same random map generator as the
	 * illusionary map.
	 * 
	 * @param map
	 */
	public IsometricRenderer(Map map)
	{
		super(map, "flattiles.png");

		HEIGHT = DIM / 2;
		//calculate the dimensions of the buffers
		Point curLoc = new Point();
		Point transLoc = new Point();
		isometricView = new AffineTransform();
		isometricView.scale(1.0, 0.5);
		isometricView.rotate(Math.PI * 45 / 180);

		for (int y = 0; y < map.getHeight(); ++y)
		{
			for (int x = 0; x < map.getWidth(); ++x)
			{
				curLoc.y = y * DIM - map.getTileHeight(x, y) * HEIGHT;
				curLoc.x = x * DIM - map.getTileHeight(x, y) * HEIGHT;
				isometricView.transform(curLoc, transLoc);
				if (transLoc.y < miny)
				{
					miny = transLoc.y;
				}
			}
		}
		double sqrt2 = Math.sqrt(2.0);
		int w = (int) ((map.getWidth() + map.getHeight()) * DIM / sqrt2);
		int h = w / 2 - miny;
		hitBuffer = new BufferedImage(w, h, FORMAT);

		left = new Polygon();
		left.addPoint(0, DIM);
		left.addPoint(DIM, DIM);
		left.addPoint(DIM + HEIGHT, DIM + HEIGHT);
		left.addPoint(HEIGHT, DIM + HEIGHT);
		left.addPoint(0, DIM);

		right = new Polygon();
		right.addPoint(DIM, 0);
		right.addPoint(DIM + HEIGHT, HEIGHT);
		right.addPoint(DIM + HEIGHT, DIM + HEIGHT);
		right.addPoint(DIM, DIM);
		right.addPoint(DIM, 0);

		cheat = true;
		//don't create the imageBuffer, because it's status as null lets us
		// know when to render
	}
	/**
	 * creates a renderer for a given map, with or without background image
	 * caching.
	 * @param map
	 * @param cheat true=use background image caching
	 */
	public IsometricRenderer(Map map, boolean cheat)
	{
		this(map);
		this.cheat = cheat;
	}
	protected int calculateHeightDifferential(int x0, int y0, int x1, int y1)
	{
		try
		{
			return map.getTileHeight(x1, y1) - map.getTileHeight(x0, y0);
		}
		catch (IndexOutOfBoundsException e)
		{
			if (x0 < 0)
				x0 = 0;
			if (y0 < 0)
				y0 = 0;
			if (x0 >= map.getWidth())
				x0 = map.getWidth() - 1;
			if (y0 >= map.getHeight())
				y0 = map.getHeight() - 1;
			return -1 * map.getTileHeight(x0, y0);
		}
	}

	protected void paintTile(Graphics2D g, int x, int y)
	{
		int t = map.getTileValue(x, y);
		if (t < 8)
			t = 0;
		else
			t = 1;
		BufferedImage tile = (BufferedImage) tiles.get(t);
		int th = map.getTileHeight(x, y);
		int realX = x * DIM - th * HEIGHT;
		int realY = y * DIM - th * HEIGHT;
		AffineTransform t1 = g.getTransform();
		g.translate(realX, realY);
		g.drawImage(tile, 0, 0, null);
		paintHitBufferTile(g, x, y);

		int lth = this.calculateHeightDifferential(x, y + 1, x, y);
		int rth = this.calculateHeightDifferential(x + 1, y, x, y);

		if (lth > 0)
		{
			g.setColor(Color.YELLOW.darker());
			g.fillPolygon(left);
			g.setColor(Color.YELLOW);
			g.drawPolygon(left);
		}
		if (rth > 0)
		{
			g.setColor(Color.YELLOW.darker());
			g.fillPolygon(right);
			g.setColor(Color.YELLOW);
			g.drawPolygon(right);
		}
		g.setTransform(t1);
	}

	/**
	 * @param g
	 * @param x
	 * @param y
	 */
	private void paintHitBufferTile(Graphics2D g, int x, int y)
	{
		Graphics2D g2 = hitBuffer.createGraphics();
		g2.setTransform(g.getTransform());
		Color c = new Color(x + 1, y + 1, map.getTileHeight(x, y));
		g2.setColor(c);
		g2.fillRect(0, 0, DIM, DIM);
	}
	/**
	 * paint the map in a different way from original map.
	 * 
	 * @param graph the graphics context
	 */
	public void paint(Graphics2D graph)
	{
		if (cheat && imageBuffer == null)
		{
			imageBuffer = new BufferedImage(getImageWidth(), getImageHeight(),
					FORMAT);
			Graphics2D g = imageBuffer.createGraphics();
			paintMap(g);
		}
		else if (!cheat)
		{
			paintMap(graph);
		}
		graph.drawImage(imageBuffer, 0, 0, null);
		paintSheep(graph);
	}

	/**
	 * @param g
	 */
	private void paintMap(Graphics2D g)
	{
		AffineTransform original = g.getTransform();
		AffineTransform temp = new AffineTransform();
		temp.translate(6 * getImageWidth() / 14, -miny);
		temp.concatenate(isometricView);
		isometricView = temp;
		g.setTransform(isometricView);

		for (int y = 0; y < map.getHeight(); ++y)
		{
			for (int x = 0; x < map.getWidth(); ++x)
			{
				paintTile(g, x, y);
			}
		}
		g.setTransform(original);
	}
	/**
	 * (non-Javadoc)
	 * 
	 * @see game.graphics.IllusionRenderer#paintSheep(java.awt.Graphics2D)
	 * @param g
	 */
	protected void paintSheep(Graphics2D g)
	{
		int x = sheep.getX();
		int y = sheep.getY();
		int h = map.getTileHeight(x, y) * HEIGHT;
		int tx = x * DIM - h;
		int ty = y * DIM - h;
		Point src = new Point(tx, ty);
		Point dest = new Point();
		isometricView.transform(src, dest);
		int dx = -sheep.getImageWidth() / 2;
		int dy = (int) (Renderer.DIM * Math.sqrt(2.0) / 2.0)
				- sheep.getImageHeight();
		sheep.paint(g, dest.x + dx, dest.y + dy);
	}

	/**
	 * @param p the location on the screen that the user clicked
	 * @return Point the location on the map that the user clicked.
	 */
	public Point getTileClicked(Point p)
	{
		int c = hitBuffer.getRGB(p.x, p.y);
		Color c2 = new Color(c);
		return new Point(c2.getRed() - 1, c2.getGreen() - 1);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see game.graphics.Renderer#getImageHeight()
	 */
	public int getImageHeight()
	{
		return hitBuffer.getHeight();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see game.graphics.Renderer#getImageWidth()
	 */
	public int getImageWidth()
	{
		return hitBuffer.getWidth();
	}
}