import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.text.AbstractDocument.BranchElement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ContrastGini extends JFrame {
	static JFreeChart chart;
	static double gini;
	
	 
	public ContrastGini(String path) {
		 BufferedImage in = null;
		
		try {
			in = ImageIO.read(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        int[] brightnessValues = new int[in.getWidth() * in.getHeight()];
        double totalBrightness = 0;
        int totalValues = brightnessValues.length;
        int counter = 0;
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries("Lorenz");
        for (int x = 0; x < in.getWidth(); x++)
        {
       	 for (int y = 0; y < in.getHeight(); y++)
       	 {
       		 int argb = in.getRGB(x, y);
       		 int rgb[] = new int[] {
       				    (argb >> 16) & 0xff, //red
       				    (argb >>  8) & 0xff, //green
       				    (argb      ) & 0xff  //blue
       				};
       		 int brightness = rgb[0] + rgb[1] + rgb[2];
       		 totalBrightness += brightness;
       		
       		 brightnessValues[counter] = brightness;
       		 counter++;
       	 }
        }
        Arrays.sort(brightnessValues);
        double cumulativeBrightnessPercentage = 0;
        double totalArea = 0;
        boolean first = true;
        int counter2 = 1;
        for (int brightness : brightnessValues)
        {
       	 
       	 if (first)
       	 {
       		 series.add((double)counter/totalValues, cumulativeBrightnessPercentage);
           	 counter2++;
       		 first = false;
       		 continue;
       	 }
       	 double percentBrightness = (double)brightness/totalBrightness;
       	 double lastCumulativePercentage = cumulativeBrightnessPercentage;
       	 cumulativeBrightnessPercentage += percentBrightness;
       	 series.add((double)counter2/totalValues, cumulativeBrightnessPercentage);
       	 counter2++;
       	 double trapAprox = 1.0/(double)totalValues * (cumulativeBrightnessPercentage + lastCumulativePercentage)/2;
       	 totalArea += trapAprox;
       			 
        }
        result.addSeries(series);
		chart = ChartFactory.createScatterPlot("Lorenz", "Cumalative Percentage of Pixels", "Cumalative Percentage of Brightness", result);
		
	    gini = (.5 - totalArea) * 2;
         System.out.println("Gini: " + gini);
         ChartPanel chartPanel = new ChartPanel(chart);
         // default size
         chartPanel.setPreferredSize(new java.awt.Dimension(1000, 500));
         setContentPane(chartPanel);
	}
	
}
