package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.client.function.FunctionDictionary;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class GeneView extends Composite {
	
	protected static final String SERVER_ERROR = null;

	private GeneSearchServiceAsync geneSearchSvc = GWT.create(GeneSearchService.class);
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private HorizontalPanel panelHeader = new HorizontalPanel();
	private ScrollPanel viewsPanel = new ScrollPanel();

	private Label genesLabel = new Label();
	private Label databaseLabel = new Label();
	private Label errorLabel = new Label();
	private ListBox viewsListBox = new ListBox();
	private Button addViewButton = new Button("Add");
	
	private FunctionDictionary fd = new FunctionDictionary();
	
	private Image heatmap = new Image();

	public GeneView(ArrayList<Gene> selectedGenes, Dataset dataset) {

		// Assemble main panel
		mainPanel.add(panelHeader);
		mainPanel.add(viewsPanel);

		// Assemble the main panel header
		panelHeader.add(infoPanel);
		panelHeader.add(viewsListBox);
		panelHeader.add(addViewButton);

		// Assemble the info panel of the header
		infoPanel.add(genesLabel);
		infoPanel.add(databaseLabel);
		genesLabel.setText("genes: "+selectedGenes.get(0).getSymbol());
		for (int i=1; i<selectedGenes.size(); i++) {
			genesLabel.setText(genesLabel.getText()+", "+selectedGenes.get(i).getSymbol());
		}
		databaseLabel.setText("database: "+dataset.getName());
		
		// Assemble the functions list box
		fd.generateList(viewsListBox);
		
		// Set up the callback object.
		AsyncCallback<ArrayList<ArrayList<Double>>> callback = new AsyncCallback<ArrayList<ArrayList<Double>>>() {
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(SERVER_ERROR);
			}

			@Override
			public void onSuccess(ArrayList<ArrayList<Double>> results) {
				errorLabel.setText("");
				//heatmap.setUrl("biomatec.itesm.mx/web.svg");
				heatmap.setUrl("gene0heatmap.svg");
			}
		};
		
		try {
			geneSearchSvc.generateHeatMap(selectedGenes, dataset, callback);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				viewsPanel.add(fd.getView(0));
				
				addView(viewsListBox.getItemText(viewsListBox.getSelectedIndex()));
			}
		});

		this.initWidget(mainPanel);
	}

	public void addView(String s) {
		Widget w = new Widget();
		viewsPanel.add(w);
	}
}
