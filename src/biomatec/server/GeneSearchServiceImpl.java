package biomatec.server;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
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

	private boolean initialized = false;
	private Statement statement = null;
	private Connection connection = null;

	public void init() {
//		if (!this.initialized) {
			try {
				Class.forName(DRIVER).newInstance();
				connection = DriverManager.getConnection( URL+DB, DB_USERNAME, DB_PASSWORD);
				statement = connection.createStatement();
				this.initialized = true;
			}
			catch ( Exception e ) { 
				e.printStackTrace(); 
				connection = null;
			}
//		}
	}   

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
	public ArrayList<Dataset> geneDetailSearch(String gene)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}