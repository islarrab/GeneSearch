package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Function;
import biomatec.javaBeans.Gene;
import biomatec.javaBeans.SelectedGenesData;
import biomatec.shared.HTTPRequest;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GeneSearch implements EntryPoint{

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
	//private SuggestBox searchSuggestBox;
	private ListBox searchListBox = new ListBox();
	private Button searchButton = new Button("Search");
	private Button submitSelectionButton = new Button("Submit Selection");
	private Label errorLabel = new Label();

	private ArrayList<Gene> selectedGenes = new ArrayList<Gene>();
	private ArrayList<Function> functions = new ArrayList<Function>();
	private SelectedGenesData selectedGenesData = new SelectedGenesData();
	private Dataset dataset = new Dataset();
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// Initialize the service proxy.
		if (geneSearchSvc == null) {
			geneSearchSvc = GWT.create(GeneSearchService.class);
		}
		
		// Set up the callback object.
		AsyncCallback<ArrayList<Function>> callback = new AsyncCallback<ArrayList<Function>>() {
			@Override
			public void onFailure(Throwable caught) {
				System.err.print(caught);
			}

			@Override
			public void onSuccess(ArrayList<Function> results) {
				functions = results;
			}
		};
		
		// Make the call to the gene detail search service.
		try{
			geneSearchSvc.functions(callback);
		}catch (Exception e){
			System.err.print(e);
		}

		// Table headers for the genes table
		genesTable.setText(0, 0, "SYMBOL");
		genesTable.setText(0, 1, "ENTREZ ID");
		genesTable.setText(0, 2, "ALIASES");
		genesTable.setStyleName("resultsTable");
		genesTable.getRowFormatter().setStyleName(0, "resultsTable-headerRow");

		// Table headers for the datasets table
		datasetsTable.setText(0, 0, "DATA SET KEY");
		datasetsTable.setText(0, 1, "NAME");
		datasetsTable.setText(0, 2, "DESCRIPTION");
		datasetsTable.setText(0, 3, "GENES FOUND");
		datasetsTable.setStyleName("resultsTable");
		datasetsTable.getRowFormatter().setStyleName(0, "resultsTable-headerRow");

		// Assemble the search ListBox
		searchListBox.addItem("Gene");

		// Assemble the search suggest box
		//GeneSuggestOracle oracle = new GeneSuggestOracle();
		//searchSuggestBox = new SuggestBox(oracle);
		
		// Assemble the search panel
		searchPanel.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
		searchPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		//searchPanel.add(searchSuggestBox);
		searchPanel.add(searchTextBox);
		searchPanel.add(searchListBox);
		searchPanel.add(searchButton);
		searchPanel.setStyleName("searchPanel");
		searchTextBox.setWidth("300px");
		searchTextBox.setHeight("23px");
		searchListBox.setHeight("30px");
		searchButton.setHeight("30px");

		// Assemble the reultsTableContainer
		resultsTableContainer.add(submitSelectionButton);
		resultsTableContainer.add(genesTable);
		resultsTableContainer.add(datasetsTable);
		submitSelectionButton.setVisible(false);
		genesTable.setVisible(false);
		datasetsTable.setVisible(false);

		// Assemble the main panel
		mainPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
		mainPanel.add(searchPanel);
		mainPanel.add(resultsTableContainer);
		mainPanel.add(errorLabel);
		mainPanel.setWidth("1000px");

		// We can add style names to widgets
		searchButton.addStyleName("sendButton");
		
		// Attach RootPanel to div "yield"
		RootPanel.get("yield").add(mainPanel);
		
		// Focus the cursor on the search field when the app loads
		searchTextBox.setFocus(true);
		searchTextBox.selectAll();

		// Setup history
		History.newItem("Initial state");

		// Add a handler to search button
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				geneSearch(searchTextBox.getText());

				History.newItem("Gene search");
			}
		});

		// Add a handler to search TextBox
		searchTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					geneSearch(searchTextBox.getText());

					History.newItem("Gene search");
				}
			}
		});

		// Add a handler to the results table
		genesTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int row = genesTable.getCellForEvent(event).getRowIndex();
				if (row>0) {
					int i=0;
					int unifeatureKey = Integer.parseInt(genesTable.getText(row, 1));
					boolean found = false;


					// Searches array selectedGenes to find repeated values
					for (i=0; i<selectedGenes.size() && !found; i++) {
						if (selectedGenes.get(i).getUnifeatureKey() == unifeatureKey) {
							found = true;
						}
					}
					if (found) {
						selectedGenes.remove(i-1);
						genesTable.getRowFormatter().setStyleName(row, "resultsTable-dataRow");
					} else {
						Gene gene = new Gene();
						gene.setSymbol(genesTable.getText(row, 0));
						gene.setUnifeatureKey(unifeatureKey);
						
						selectedGenes.add(gene);
						genesTable.getRowFormatter().setStyleName(row, "resultsTable-dataRow-selected");
					}
				}
			}
		});

		// Add a handler to the gene selection submit button
		submitSelectionButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

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
						History.newItem("Details table");
					}
				};



				// Make the call to the gene detail search service.
				geneSearchSvc.geneDetailSearch(selectedGenes, callback);
			}
		});

		// Add a handler to the results table
		datasetsTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try{
					int row = datasetsTable.getCellForEvent(event).getRowIndex();
					if (row>0) {
						int datasetKey = Integer.parseInt(datasetsTable.getText(row, 0));
						dataset.setDatasetKey(datasetKey);
						dataset.setName(datasetsTable.getText(row, 1));
						dataset.setDescription(datasetsTable.getText(row, 2));
						dataset.setGenes(datasetsTable.getText(row, 3));
						
						
						getSelectedGenesData();
						
						RootPanel.get("yield").clear();
						RootPanel.get("yield").add(new GeneView(selectedGenes, dataset, functions, selectedGenesData));
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});


	}

	protected void getSelectedGenesData() {
		
		// Set up the data callback object.
		AsyncCallback<String> callbackD = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText("Conection error");
			}

			@Override
			public void onSuccess(String result) {
				errorLabel.setText("");
				result = result.replaceAll("\t", "+");
				result = result.replaceAll("\n", ";");
				selectedGenesData.setData(result);
			}
		};
		
		// Set up the columnsType callback object.
		AsyncCallback<String> callbackCT = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText("Conection error");
			}

			@Override
			public void onSuccess(String result) {
				errorLabel.setText("");
				selectedGenesData.setColumnsType(result);
			}
		};
		
		
		String dsk = dataset.getDatasetKey()+"";
		String ufk = selectedGenes.get(0).getUnifeatureKey()+"";
		for (int i=1; i<selectedGenes.size(); i++) {
			ufk += ","+selectedGenes.get(i).getUnifeatureKey();
		}
		String endpoint = "http://biomatec.mty.itesm.mx:8080/Biomatec1/getRowData.jsp";
		String requestParameters = "dsk="+dsk+"&unifeaturekey="+ufk+"&format=text";
		
		// Make the call to the gene detail search service.
		try {
			geneSearchSvc.getData(endpoint, requestParameters, callbackD);
			geneSearchSvc.columnsType(dataset.getDatasetKey(), callbackCT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
			
			// Searches array selectedGenes to find repeated values
			boolean found = false;
			for (int j=0; j<selectedGenes.size() && !found; j++)
				if (selectedGenes.get(j).getUnifeatureKey() == gene.getUnifeatureKey())
					found = true;
			if (found)
				genesTable.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow-selected");
			else
				genesTable.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");

		}

		// Remove extra rows from previous search, if any
		i++;
		while(i<genesTableRowCount) {
			genesTable.removeRow(resultsRowCount+1);
			i++;
		}

		// Swap current table if it is not genesTable
		submitSelectionButton.setVisible(true);
		genesTable.setVisible(true);
		datasetsTable.setVisible(false);
	}

	protected void updateTableWithDetails(ArrayList<Dataset> results) {
		int i;
		int resultsRowCount = results.size();
		int datasetsTableRowCount = datasetsTable.getRowCount();

		for (i=0; i<results.size(); i++) {
			Dataset dataset = (Dataset)results.get(i);

			datasetsTable.setText(i+1, 0, dataset.getDatasetKey()+"");
			datasetsTable.setText(i+1, 1, dataset.getName());
			datasetsTable.setText(i+1, 2, dataset.getDescription());
			datasetsTable.setText(i+1, 3, dataset.getGenes());

			datasetsTable.getRowFormatter().setStyleName(i+1, "resultsTable-dataRow");
		}

		// Remove extra rows from previous search, if any
		i++;
		while(i<datasetsTableRowCount) {
			datasetsTable.removeRow(resultsRowCount+1);
			i++;
		}

		// Swap current table if it is not datasetsTable
		submitSelectionButton.setVisible(false);
		genesTable.setVisible(false);
		datasetsTable.setVisible(true);
	}
}
