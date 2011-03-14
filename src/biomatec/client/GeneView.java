package biomatec.client;

import java.util.ArrayList;

import biomatec.javaBeans.Int;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GeneView extends Composite {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private HorizontalPanel panelHeader = new HorizontalPanel();
	private ScrollPanel viewsPanel = new ScrollPanel();
	
	private Label genesLabel = new Label();
	private Label databaseLabel = new Label();
	private ListBox viewsListBox = new ListBox();
	private Button addViewButton = new Button("Add");
	
	
	public GeneView(ArrayList<Int> selectedGenes) {
		
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
		
		//viewsListBox.addItem(item, value);
		
		this.initWidget(mainPanel);
	}
}
