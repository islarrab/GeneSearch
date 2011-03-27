package biomatec.client.function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Heatmap {

	public static void heatmap(ArrayList<ArrayList<Double>> pvalues) {

		// Get the SVG file ready for the drawing of the performance graph 
		File SVGOutputFile = null;
		FileWriter SVGout = null;
		try{
			SVGOutputFile = new File("SVGHeatMap.svg");
			SVGout = new FileWriter(SVGOutputFile);
			
			SVGout.write("<?xml version=\"1.0\"?>");
			SVGout.write("\n<svg width=\"1300\" height=\"1300\">");
			SVGout.write("\n<desc>Biomatec gene HeatMaps.</desc>");

			// Move graph to wherever you want on the screen
			// Scale the graph to fit wherever
			SVGout.write("\n<g transform=\"translate(50,50) scale(1.0)\">");
			for(int i = 0; i < pvalues.size(); i++){
				for(int j = 0; j < pvalues.get(i).size(); j++){

					Double x = 0.0;
					if(x >= 0 && x < .1)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#0000FF\" />");
					else if(x >= .1 && x < .2)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#3333FF\" />");
					else if(x >= .2 && x < .3)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#6666FF\" />");
					else if(x >= .3 && x < .4)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#9999FF\" />");
					else if(x >= .4 && x < .5)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#CCCCFF\" />");
					else if(x >= .5 && x < .6)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FFFFFF\" />");
					else if(x >= .6 && x < .7)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FFCCCC\" />");
					else if(x >= .7 && x < .8)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FF9999\" />");
					else if(x >= .8 && x < .9)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FF6666\" />");
					else if(x >= .9 && x < 1)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FF3333\" />");
					else if(x == 1)
						SVGout.write("\n<rect x=\"" + (j*3) + "\" y=\"" + (i*10) + "\" width=\"3\" height=\"8\" style=\"fill:#FF0000\" />");
				}
			}
			// All done. Close off the groups and the SVG file		  
			SVGout.write("\n</svg>");
			SVGout.close();
		} catch (IOException e) {
			System.err.println("Error al escribir archivo .svg");
			e.printStackTrace();
		}
	}
}
