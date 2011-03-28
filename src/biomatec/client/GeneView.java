package biomatec.client;

import java.util.ArrayList;

import biomatec.client.function.FunctionDictionary;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GeneView extends Composite {
	
	protected static final String SERVER_ERROR = null;
	
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

	public GeneView(final ArrayList<Gene> selectedGenes, final Dataset dataset) {

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
		
		addViewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int x = Integer.parseInt(viewsListBox.getValue(viewsListBox.getSelectedIndex()));
				viewsPanel.add(fd.getView(x, selectedGenes, dataset));
				
			}
		});

		this.initWidget(mainPanel);
	}

}
