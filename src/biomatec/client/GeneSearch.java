package biomatec.client;

import java.util.ArrayList;

import biomatec.javaBeans.Gene;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GeneSearch implements EntryPoint {

	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
		+ "attempting to contact the server. Please check your network "
		+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side gene search service.
	 */
	private GeneSearchServiceAsync geneSearchSvc = GWT.create(GeneSearchService.class);

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	private FlexTable resultsTable = new FlexTable();
	private TextBox searchTextBox = new TextBox();
	private ListBox searchListBox = new ListBox();
	private Button searchButton = new Button("Search");
	private Label errorLabel = new Label();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// Table headers for the results table
		resultsTable.setText(0, 0, "SYMBOL");
		resultsTable.setText(0, 1, "ENTREZ ID");
		resultsTable.setText(0, 2, "ALIASES");
		resultsTable.setText(0, 3, "ORGANISM");
		resultsTable.setText(0, 4, "ANNOTATION");

		// Assemble the search ListBox
		searchListBox.addItem("Gene");

		// Assemble the search panel
		searchPanel.add(searchTextBox);
		searchPanel.add(searchListBox);
		searchPanel.add(searchButton);

		// Assemble the main panel
		mainPanel.add(searchPanel);
		mainPanel.add(resultsTable);
		mainPanel.add(errorLabel);

		// We can add style names to widgets
		//sendButton.addStyleName("sendButton");

		// Attach RootPanel to div "yield"
		RootPanel.get("yield").add(mainPanel);

		// Focus the cursor on the search field when the app loads
		searchTextBox.setFocus(true);
		searchTextBox.selectAll();

		// Add a handler to search button
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				geneSearch(searchTextBox.getText());
			}
		});

		// Add a handler to search TextBox
		searchTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					geneSearch(searchTextBox.getText());
				}
			}
		});

	}


	protected void geneSearch(String text) {

		if (!text.equals("")) {
			// Initialize the service proxy.
			if (geneSearchSvc == null) {
				geneSearchSvc = GWT.create(GeneSearchService.class);
			}

			// Set up the callback object.
			AsyncCallback<ArrayList<Gene>> callback = new AsyncCallback<ArrayList<Gene>>() {
				@Override
				public void onFailure(Throwable caught) {
					errorLabel.setText(SERVER_ERROR);
				}

				@Override
				public void onSuccess(ArrayList<Gene> results) {
					errorLabel.setText("");
					updateTable(results);
				}
			};

			// Make the call to the gene search service.
			geneSearchSvc.geneSearch(text, callback);
		}
	}


	protected void updateTable(ArrayList<Gene> results) {
		resultsTable.clear();

		// Table headers for the results table
		resultsTable.setText(0, 0, "SYMBOL");
		resultsTable.setText(0, 1, "ENTREZ ID");
		resultsTable.setText(0, 2, "ALIASES");
		resultsTable.setText(0, 3, "ORGANISM");
		resultsTable.setText(0, 4, "ANNOTATION");

		for (int i=0; i<results.size(); i++) {
			Gene gene = (Gene)results.get(i);
			resultsTable.setText(i+1, 0, gene.getSymbol());
			resultsTable.setText(i+1, 1, gene.getUnifeatureKey()+"");
			resultsTable.setText(i+1, 2, gene.getAllKnownIds());
			resultsTable.setText(i+1, 3, gene.getOrganism());
			resultsTable.setText(i+1, 4, gene.getAnnotation());
		}

	}
}
