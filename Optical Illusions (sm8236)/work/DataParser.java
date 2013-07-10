/*
 * Created on Apr 7, 2005
 *  
 */
package work;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author sean
 */
public class DataParser
{
    private static BufferedReader bread;

    private static String readLine ()
    {
        try
        {
            return bread.readLine ();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private static class DataPoint
    {
        private int       test;

        private boolean[] runs;

        /**
         * creates a new data point from the string representation of the form
         * "testnumber:#####" where # is the true/false code for the specific
         * run.
         * 
         * @param p
         *            the string represntation
         */
        public DataPoint (String p)
        {
            String[] parts = p.split (":");
            test = Integer.parseInt (parts[0]);
            runs = new boolean[parts[1].length ()];
            for (int i = 0; i < parts[1].length (); ++i)
            {
                runs[i] = parts[1].charAt (i) == '1';
            }
        }
        /**
         * create a new data point from the integer representation that is a
         * bitwise packing of the test information. The least significant 5 bits
         * are the test results and the 6th and 7th bits are the test number.
         * 
         * @param p
         */
        public DataPoint (int p)
        {
            this.test = p & 0x30 >> 4;
            p &= 0xf;
            this.runs = new boolean[5];
            for (int i = 0; i < 5; ++i)
            {
                this.runs[i] = (p & 1) == 1;
                p >>= 1;
            }
        }

        /**
         * @return the test number for this data point
         */
        public int getTest ()
        {
            return test;
        }

        /**
         * 
         * @return the number of successes for this data point
         */
        public int getSuccessCount ()
        {
            int i = 0;
            for (int n = 0; n < runs.length; ++n)
            {
                if (runs[n])
                    ++i;
            }
            return i;
        }
        /**
         * @return the number of failures for this data point
         */
        public int getFailureCount ()
        {
            return runs.length - getSuccessCount ();
        }

        /**
         * @return the total number of runs for this data point
         */
        public int getTotalRuns ()
        {
            return runs.length;
        }
        /**
         * @return a string representation of the data.
         */
        public String toString ()
        {
            return getTest () + ": S" + getSuccessCount () + " + F"
                    + getFailureCount () + " = " + getTotalRuns ();

        }
    }

    /**
     * program entry point
     * 
     * @param args
     *            command line arguments
     */
    public static void main (String[] args)
    {
        try
        {
            bread = new BufferedReader (new FileReader ("data.txt"));
        }
        catch (Exception e)
        {
            System.err.println ("Oh, no!");
            e.printStackTrace ();
            System.exit (1);
        }
        int[] totalSuccesses = {0, 0, 0};
        int[] totalFailures = {0, 0, 0};
        String input = readLine ();
        while (input != null)
        {
            DataPoint p;
            try
            {
                if (input.charAt (1) == ':')
                {
                    p = new DataPoint (input);
                }
                else
                {
                    p = new DataPoint (Integer.parseInt (input));
                }
            }
            catch (NumberFormatException e)
            {
                // Skip this line of input, it must be bad. Can't take the time
                // to figure out how to fix it.
                break;
            }
            if (! ( (p.getTest () == 1 || p.getTest () == 2) && p
                    .getSuccessCount () <= 1))
            {
                totalSuccesses[p.getTest ()] += p.getSuccessCount ();
                totalFailures[p.getTest ()] += p.getFailureCount ();
            }

            input = readLine ();
        }
        BufferedImage img = new BufferedImage (800, 600,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = img.createGraphics ();
        g.setFont (new Font ("serif", Font.PLAIN, 36));
        g.setColor (Color.WHITE);
        g.fillRect (0, 0, 800, 600);
        g.setColor (Color.BLACK);
        g.drawString ("Percentage Success/Failure Rate", 175, 50);
        g.drawString ("Test #", 10, 50);

        g.drawString ("Percentage of Total Tests", 200, 595);

        for (int i = 0; i <= 5; ++i)
        {
            int x = 55 + i * 130;
            g.setColor (Color.GRAY);
            g.drawLine (x, 60, x, 540);
            g.setColor (Color.BLACK);
            g.drawString ( (i * 20) + "%", x, 562);
        }
        g.drawLine (55, 20, 55, 580);
        g.drawLine (20, 520, 780, 520);
        g.drawString ("Success", 680, 50);
        g.drawString ("Failure", 680, 75);
        g.setColor (Color.BLUE);
        g.fillRect (665, 25, 15, 20);
        g.setColor (Color.RED);
        g.fillRect (665, 50, 15, 20);
        for (int i = 0; i < 3; ++i)
        {
            int y = 150 * (i + 1);
            g.setColor (Color.BLACK);
            g.drawString ("# " + (i + 1), 10, y);
            double percentageSuccess = (double) totalSuccesses[i]
                    / (totalSuccesses[i] + totalFailures[i]);
            double percentageFailure = (double) totalFailures[i]
                    / (totalSuccesses[i] + totalFailures[i]);
            int sWidth = (int) (percentageSuccess * 650);
            int fWidth = (int) (percentageFailure * 650);
            g.setColor (Color.BLUE);
            g.fillRect (56, y - 40, sWidth, 40);
            g.setColor (Color.RED);
            g.fillRect (56, y, fWidth, 40);
            System.out.println ("\nTest #" + (i + 1) + "  ======");
            System.out.println ("Percentages:");
            System.out.println ("|   success = " + percentageSuccess * 100);
            System.out.println ("|   failure = " + percentageFailure * 100);
            System.out.println ("Totals:");
            System.out.println ("|   success = " + totalSuccesses[i]);
            System.out.println ("|   failure = " + totalFailures[i]);
            System.out.println ("----total   = "
                    + (totalSuccesses[i] + totalFailures[i]) + "\n");
        }

        try
        {
            ImageIO.write (img, "png", new File ("surveyresults.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            System.err.println ("Could not write image file");
        }
    }
}