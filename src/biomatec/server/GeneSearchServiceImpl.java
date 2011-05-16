package biomatec.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import biomatec.client.GeneSearchService;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;

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
	public ArrayList<ArrayList<Double>> generateHeatMap(ArrayList<Gene> selectedGenes, Dataset dataset) throws IOException{
		ArrayList<ArrayList<Double>> results = new ArrayList<ArrayList<Double>>();
		init();
		try{
			for(int i = 0; i < selectedGenes.size(); i++){   
				results.add(new ArrayList<Double>());

				// Do the query for the values to be displayed on the Heat Map
				String query = 
					"SELECT uv.PVALUE " +
					"FROM UNI_VALUE uv " +
					"WHERE uv.DATA_SET_KEY = 90"+ //+ dataset.getDatasetKey() +
					" AND uv.FEATURE_KEY IN " +
					"(SELECT f.FEATURE_KEY " +
					"FROM FEATURE f " +
					"WHERE f.UNIFEATURE_KEY = 19 )";// + selectedGenes.get(i).getUnifeatureKey() + ")";

				ResultSet rs = statement.executeQuery(query);	

				while(rs.next()){
					Double d = new Double((double)rs.getFloat("PVALUE"));
					results.get(i).add(d);
				}
			}
			connection.close();
		} catch (Exception e){
			System.err.println("ERROR: Problems with the database");
			e.printStackTrace();
		}

		return results;

	}

	@Override
	public String columnsType(int datasetKey) throws IOException{
		String s = "";
		init();
		try{
			String query = 
				"SELECT COLUMNS_TYPE " +
				"FROM DATA_MATRIX " +
				"WHERE DATA_SET_KEY = " + datasetKey;
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			s = rs.getString("COLUMNS_TYPE");
			connection.close();
		} catch(Exception e){
			System.err.println("ERROR: Problems with the database");
			e.printStackTrace();
		}
		return s;
	}

	@Override
	public ArrayList<Function> functions() throws IOException{
		ArrayList<Function> functionArray = new ArrayList<Function>();
		init();
		try{
			String query = 
				"SELECT NAME, URL, RETURN_TYPE, MULTI_GENE, GET_POST " +
				"FROM VIEWER_SERVICES ";
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()){
				String name = rs.getString("NAME");
				String url = rs.getString("URL");
				char return_type = rs.getString("RETURN_TYPE").charAt(0);
				char function_type = rs.getString("MULTI_GENE").charAt(0);
				char method = rs.getString("GET_POST").charAt(0);
				Function f = new Function(name, url, return_type, function_type, method);
				functionArray.add(f);
			}
			connection.close();
		} catch (Exception e){
			System.err.println("ERROR: Problems with the database");
			e.printStackTrace();
		}
		return functionArray;
	}

	@Override
	public String getData(String endpoint, String requestParameters) throws IOException{
		String text;
		
		text = HTTPRequest.sendGetRequest(endpoint, requestParameters);
		
		return text;
	}
	
	@Override
	public String doPost(String endpoint, String data) {
		String result = "";
		
		result = HTTPRequest.postData(endpoint, data);
		
		return result;
	}
}