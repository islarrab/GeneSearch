package biomatec.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Int;
//import biomatec.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GeneSearchServiceImpl extends RemoteServiceServlet implements
GeneSearchService {

	private final String DRIVER = "com.mysql.jdbc.Driver"; 
	private final String URL = "jdbc:mysql://10.17.210.8:3306/";
	private final String DB = "biomatec";
	private final String DB_USERNAME = "numberone";
	private final String DB_PASSWORD = "theone";

	private Statement statement = null;
	private Connection connection = null;

	public void init() {
		try {
			Class.forName(DRIVER).newInstance();
			connection = DriverManager.getConnection( URL+DB, DB_USERNAME, DB_PASSWORD);
			statement = connection.createStatement();
		}
		catch ( Exception e ) { 
			e.printStackTrace(); 
			connection = null;
		}
	}   

	@Override
	public ArrayList<Gene> geneSearch(String input) throws IllegalArgumentException {
		if (!input.equals("")){
			init();
			ArrayList<Gene> results = new ArrayList<Gene>();
			try{
				String query = 
					"SELECT * FROM UNIFEATURE " +
					"WHERE ALL_KNOWN_IDS LIKE '%|" + input + "%'";

				ResultSet rs = statement.executeQuery(query);	

				while(rs.next()){
					Gene gene = new Gene();
					gene.setUnifeatureKey(rs.getInt(1));
					gene.setFeatureTypeKey(rs.getInt(2));
					gene.setSymbol(rs.getString(3));
					gene.setAllKnownIds(rs.getString(4));
					gene.setOrganism(rs.getString(5));
					gene.setAnnotation(rs.getString(6));
					results.add(gene);
				}
				connection.close();
			}
			catch(Exception e){
				System.err.println("ERROR: Problems with the database" );
				e.printStackTrace();
			}
			return results;
		} else {
			return null;
		}
	}

	@Override
	public ArrayList<Dataset> geneDetailSearch(ArrayList<Gene> selectedGenes)
	throws IllegalArgumentException {
		init();
		ArrayList<Dataset> results = new ArrayList<Dataset>();
		try{
			for (int i = 0; i < selectedGenes.size(); i++){
				String query = 
					"SELECT DATA_SET_KEY, NAME, DESCRIPTION " +
					"FROM DATA_SET " +
					"WHERE DATA_SET_KEY IN " +
					"(SELECT DISTINCT(DATA_SET_KEY) " +
					"FROM FEATURE " +
					"WHERE UNIFEATURE_KEY = " + selectedGenes.get(i).getUnifeatureKey() + ")";
				ResultSet rs = statement.executeQuery(query);
				while(rs.next()){
					boolean b = false;
					int j = 0;
					for (; j < results.size(); j++)
						if (rs.getInt(1) == results.get(j).getDatasetKey()){
							b = true;
							break;
						}
					if (!b){
						Dataset dataset = new Dataset();
						dataset.setDatasetKey(rs.getInt(1));
						dataset.setName(rs.getString(2));
						if (rs.getString(3) != null)
							dataset.setDescription(rs.getString(3));
						dataset.addGene(selectedGenes.get(i).getSymbol());
						results.add(dataset);
					}
					else
						results.get(j).addGene(selectedGenes.get(i).getSymbol());
				}
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("ERROR: Problems with the database" );
			e.printStackTrace();	
		}
		return results;
	}

	@Override
	public Int generateHeatMap(ArrayList<Gene> selectedGenes, Dataset dataset) throws IOException{
		// Get the SVG file ready for the drawing of the performance graph 
		File SVGOutputFile = null;
		FileWriter SVGout = null;
		try{
			SVGOutputFile = new File("SVGHeatMap.svg");
			SVGout = new FileWriter(SVGOutputFile);
		} catch (IOException e) {
			System.err.println("Error al escribir archivo .svg");
		}

		// Get the SVG graph ready
		SVGout.write("<?xml version=\"1.0\"?>");
		SVGout.write("\n<svg width=\"1300\" height=\"1300\">");
		SVGout.write("\n<desc>Biomatec gene HeatMaps.</desc>");

		// Move graph to wherever you want on the screen
		// Scale the graph to fit wherever
		SVGout.write("\n<g transform=\"translate(50,50) scale(1.0)\">");
		try{
			for(int i = 0; i < selectedGenes.size(); i++){   
				// Do the query for the values to be displayed on the Heat Map
				String query = 
					"SELECT uv.PVALUE " +
					"FROM UNI_VALUE uv " +
					"WHERE uv.DATA_SET_KEY = " + dataset.getDatasetKey() +
					"AND uv.FEATURE_KEY IN " +
					"(SELECT f.FEATURE_KEY " +
					"FROM FEATURE f " +
					"WHERE f.UNIFEATURE_KEY = " + selectedGenes.get(i).getUnifeatureKey() + ")";

				ResultSet rs = statement.executeQuery(query);	

				for(int j = 0; rs.next(); j++){
					double x = rs.getDouble(0);
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
			connection.close();
		} catch (Exception e){
			System.err.println("ERROR: Problems with the database");
			e.printStackTrace();
		}
		
		// All done. Close off the groups and the SVG file		  
		SVGout.write("\n</svg>");
		SVGout.close();
		
		return null;

	}

	
}