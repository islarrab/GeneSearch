package biomatec.server;


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
					"SELECT UNIFEATURE_KEY, SYMBOL, ALL_KNOWN_IDS, ANNOTATION " +
					"FROM UNIFEATURE " +
					"WHERE ALL_KNOWN_IDS LIKE '%|" + input + "%'";

				ResultSet rs = statement.executeQuery(query);	

				while(rs.next()){
					Gene gene = new Gene();
					gene.setUnifeatureKey(rs.getInt(1));
					gene.setSymbol(rs.getString(2));
					gene.setAllKnownIds(rs.getString(3));
					gene.setAnnotation(rs.getString(4));
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
	public ArrayList<Dataset> geneDetailSearch(ArrayList<Int> input)
	throws IllegalArgumentException {
		init();
		ArrayList<Dataset> results = new ArrayList<Dataset>();
		try{
			String query = 
				"SELECT DATA_SET_KEY, NAME, DESCRIPTION " +
				"FROM DATA_SET " +
				"WHERE DATA_SET_KEY IN " +
				"(SELECT DISTINCT(DATA_SET_KEY) " +
				"FROM FEATURE " +
				"WHERE UNIFEATURE_KEY IN (" + input.get(0).getInt();
			for(int i = 1; i < input.size(); i++){
				query += ", " + input.get(i).getInt();
			}
			query += "));";
			
			ResultSet rs = statement.executeQuery(query);	
			while(rs.next()){
				Dataset dataset = new Dataset();
				dataset.setDatasetkey(rs.getInt(1));
				dataset.setName(rs.getString(2));
				dataset.setDescription(rs.getString(3));
				results.add(dataset);
				System.out.println(rs.getInt(1) + " " + rs.getString(2));
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("ERROR: Problems with the database" );
			e.printStackTrace();	
		}
		return results;
	}

}