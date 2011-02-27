package biomatec.client;

import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
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
import com.google.gwt.user.client.ui.SuggestBox;
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
	private VerticalPanel resultsTableContainer = new VerticalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel();
	private FlexTable genesTable = new FlexTable();
	private FlexTable datasetsTable = new FlexTable();
	private TextBox searchTextBox = new TextBox();
	private SuggestBox searchSuggestBox;
	private ListBox searchListBox = new ListBox();
	private Button searchButton = new Button("Search");
	private Label errorLabel = new Label();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Table headers for the genes table
		genesTable.setText(0, 0, "SYMBOL");
		genesTable.setText(0, 1, "ENTREZ ID");
		genesTable.setText(0, 2, "ALIASES");
		genesTable.setText(0, 3, "ORGANISM");
		genesTable.setText(0, 4, "ANNOTATION");
		genesTable.setStyleName("resultsTable");
		genesTable.getRowFormatter().setStyleName(0, "resultsTable-headerRow");
		
		// Table headers for the datasets table
		datasetsTable.setText(0, 0, "DATA SET KEY");
		datasetsTable.setText(0, 1, "NAME");
		datasetsTable.setText(0, 2, "DESCRIPTION");
		datasetsTable.setStyleName("resultsTable");
		datasetsTable.getRowFormatter().setStyleName(0, "resultsTable-headerRow");
		
		// Assemble the search ListBox
		searchListBox.addItem("Gene");
		
		// Assemble the search suggest box
		GeneSuggestOracle oracle = new GeneSuggestOracle();
		searchSuggestBox = new SuggestBox(oracle);

		// Assemble the search panel
		searchPanel.add(searchSuggestBox);
		searchPanel.add(searchListBox);
		searchPanel.add(searchButton);
		
		// Assemble the reultsTableContainer
		resultsTableContainer.add(genesTable);
		
		// Assemble the main panel
		mainPanel.add(searchPanel);
		mainPanel.add(resultsTableContainer);
		mainPanel.add(errorLabel);

		// We can add style names to widgets
		searchButton.addStyleName("sendButton");

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

		// Add a handler to the results table
		genesTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int row = genesTable.getCellForEvent(event).getRowIndex();
				if (row>0) {

					int unifeatureKey = Integer.parseInt(genesTable.getText(row, 1));

					// Initialize the service proxy.
					if (geneSearchSvc == null) {
						geneSearchSvc = GWT.create(GeneSearchService.class);
					}

					// Set up the callback object.
					AsyncCallback<ArrayList<Dataset>> callback = new AsyncCallback<ArrayList<Dataset>>() {
						@Override
						public void onFailure(Throwable caught) {
							errorLabel.setText(SERVER_ERROR);
						}

						@Override
						public void onSuccess(ArrayList<Dataset> results) {
							errorLabel.setText("");
							updateTableWithDetails(results);
						}
					};

					// Make the call to the gene detail search service.
					geneSearchSvc.geneDetailSearch(unifeatureKey, callback);
				}
			}
		});
	}
/*
	private void initializaOracle() {
		if (geneSearchSvc == null) {
			geneSearchSvc = GWT.create(GeneSearchService.class);
		}

		// Set up the callback object.
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(SERVER_ERROR);
			}

			@Override
			public void onSuccess(ArrayList<String> results) {
				errorLabel.setText("");
				for (int i=0; i<results.size(); i++) {
					oracle.add(results.get(i));
				}
			}
		};

		// Make the call to the gene search service.
		geneSearchSvc.getSuggestions(callback);
	}
*/
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
					updateTableWithGenes(results);
				}
			};

			// Make the call to the gene search service.
			geneSearchSvc.geneSearch(text, callback);
		}
	}

	protected void updateTableWithGenes(ArrayList<Gene> results) {
		int i;
		int resultsRowCount = results.size();
		int genesTableRowCount = genesTable.getRowCount();
		for (i=0; i<resultsRowCount; i++) {
			Gene gene = (Gene)results.get(i);

			genesTable.setText(i+1, 0, gene.getSymbol());
			genesTable.setText(i+1, 1, gene.getUnifeatureKey()+"");
			genesTable.setText(i+1, 2, gene.getAllKnownIds());
			genesTable.setText(i+1, 3, gene.getOrganism());
			genesTable.setText(i+1, 4, gene.getAnnotation());
			
			genesTable.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
		}
		
		// Remove extra rows from previous search, if any
		i++;
		while(i<genesTableRowCount) {
			genesTable.removeRow(resultsRowCount+1);
			i++;
		}
		
		// Swap current table if it is not genesTable
		if (!resultsTableContainer.getWidget(0).equals(genesTable)) {
			resultsTableContainer.remove(0);
			resultsTableContainer.add(genesTable);
		}
	}

	protected void updateTableWithDetails(ArrayList<Dataset> results) {
		int i;
		int resultsRowCount = results.size();
		int datasetsTableRowCount = datasetsTable.getRowCount();

		for (i=0; i<results.size(); i++) {
			Dataset dataset = (Dataset)results.get(i);

			datasetsTable.setText(i+1, 0, dataset.getDatasetkey()+"");
			datasetsTable.setText(i+1, 1, dataset.getName());
			datasetsTable.setText(i+1, 2, dataset.getDescription());
			
			datasetsTable.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
		}
		
		// Remove extra rows from previous search, if any
		i++;
		while(i<datasetsTableRowCount) {
			datasetsTable.removeRow(resultsRowCount+1);
			i++;
		}
		
		// Swap current table if it is not datasetsTable
		if (!resultsTableContainer.getWidget(0).equals(datasetsTable)) {
			resultsTableContainer.remove(0);
			resultsTableContainer.add(datasetsTable);
		}
	}
}
