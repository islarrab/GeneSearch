package biomatec.client;

import java.io.IOException;
import java.util.ArrayList;

import biomatec.javaBeans.Gene;
import biomatec.javaBeans.Int;
import biomatec.javaBeans.Dataset;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
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
	
	private Image heatmap = new Image();

	public GeneView(ArrayList<Int> selectedGenes, Dataset dataset) throws IOException {

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
		genesLabel.setText("genes: ");
		databaseLabel.setText("database: ");

		viewsListBox.addItem("Heatmap");
		
		

		// Set up the callback object.
		AsyncCallback<Int> callback = new AsyncCallback<Int>() {
			@Override
			public void onFailure(Throwable caught) {
				errorLabel.setText(SERVER_ERROR);
			}

			@Override
			public void onSuccess(Int result) {
				errorLabel.setText("");
				heatmap.setUrl("web.svg");
			}
		};
		
		geneSearchSvc.generateHeatMap(selectedGenes, dataset, callback);
		
		addViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
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
