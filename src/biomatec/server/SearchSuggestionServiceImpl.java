package biomatec.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import biomatec.client.SearchSuggestionService;
import biomatec.javaBeans.SearchSuggestion;

import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SearchSuggestionServiceImpl extends RemoteServiceServlet implements SearchSuggestionService {

	private static final long serialVersionUID = 1L;

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


	public SuggestOracle.Response getSuggestions(SuggestOracle.Request request) {

		ArrayList<SearchSuggestion> suggestions = new ArrayList<SearchSuggestion>();
		SuggestOracle.Response response = new SuggestOracle.Response();
		String query = "SELECT SYMBOL FROM UNIFEATURE WHERE SYMBOL = "+request.getQuery()+" LIMIT 10";
		//request.getLimit();
		//request.getQuery();

		try {
			ResultSet rs = statement.executeQuery(query);	

			while(rs.next()){
				suggestions.add(new SearchSuggestion(rs.getString("SYMBOL")));
			}
			connection.close();
		}
		catch(Exception e){
			System.err.println("ERROR: Problems with the database" );
			e.printStackTrace();
		}
		response.setSuggestions(suggestions);
		return response;
	}
}