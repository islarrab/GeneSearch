package biomatec.client;

import java.util.ArrayList;

import biomatec.client.function.FunctionDictionary;
import biomatec.javaBeans.Dataset;
import biomatec.javaBeans.Gene;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.CheckBox;

@SuppressWarnings("deprecation")
public class GeneView extends Composite {
	
	protected static final String SERVER_ERROR = null;
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel infoPanel = new VerticalPanel();
	private DockPanel panelHeader = new DockPanel();
	private VerticalPanel viewsPanel = new VerticalPanel();
	private HorizontalPanel addViewPanel = new HorizontalPanel();
	private HorizontalPanel genesPanel = new HorizontalPanel();

	private HTML genesLabel = new HTML();
	private HTML databaseLabel = new HTML();
	private Label errorLabel = new Label();
	private ListBox viewsListBox = new ListBox();
	private Button addViewButton = new Button("+ Add View");
	
	private FunctionDictionary fd = new FunctionDictionary();

    private CheckBox genesCheck[];

	public GeneView(final ArrayList<Gene> selectedGenes, final Dataset dataset) {

        genesCheck = new CheckBox[selectedGenes.size()];

		// Assemble main panel
		mainPanel.add(panelHeader);
		mainPanel.add(viewsPanel);
		mainPanel.setWidth("1000px");

		// Assemble the main panel header
		panelHeader.add(infoPanel, DockPanel.WEST);
		panelHeader.add(addViewPanel, DockPanel.EAST);
		panelHeader.setCellHorizontalAlignment(addViewPanel, HorizontalPanel.ALIGN_RIGHT);
		panelHeader.setWidth("1000px");
		panelHeader.setStyleName("panelHeader");
		
		//Assemble the add view panel
		addViewPanel.setSpacing(5);
		addViewPanel.add(viewsListBox);
		addViewPanel.add(addViewButton);
		addViewPanel.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);

		// Assemble the info panel of the header
        infoPanel.add(genesPanel);
        genesLabel.setHTML("<b>Genes: </b>");
		genesPanel.add(genesLabel);
		String [] genesAvailable = dataset.getGenes().split(" ");
		boolean found = false;
		for (int i = 0; i < genesAvailable.length && !found; i++)
			if (selectedGenes.get(0).getSymbol().equals(genesAvailable[i]))
				found = true;
		if (found) {
			//genesLabel.setHTML("<b>Genes:</b> "+selectedGenes.get(0).getSymbol());
            genesCheck[0] = new CheckBox(selectedGenes.get(0).getSymbol());
            genesCheck[0].setValue(true);
            genesPanel.add(genesCheck[0]);
        }
            
		else {
			//genesLabel.setHTML("<b>Genes:</b> <font color=\"red\">"+selectedGenes.get(0).getSymbol()+"</font>");
            genesCheck[0] = new CheckBox(selectedGenes.get(0).getSymbol());
            genesCheck[0].setValue(false);
            genesPanel.add(genesCheck[0]);
        }
		for (int i=1; i<selectedGenes.size(); i++) {
			found = false;
			for (int j = 0; j < genesAvailable.length && !found; j++)
				if (selectedGenes.get(i).getSymbol().equals(genesAvailable[j]))
					found = true;
			if (found) {
                //genesLabel.setHTML(genesLabel.getHTML()+" "+selectedGenes.get(i).getSymbol());
                genesCheck[i] = new CheckBox(selectedGenes.get(i).getSymbol());
                genesCheck[i].setValue(true);
                genesPanel.add(genesCheck[i]);
            }
            else {
                //genesLabel.setHTML(genesLabel.getHTML()+"<font color=\"red\">"+" "+selectedGenes.get(i).getSymbol()+"</font>");
                genesCheck[i] = new CheckBox(selectedGenes.get(i).getSymbol());
                genesCheck[i].setEnabled(false);
                genesCheck[i].setStyleName("disabled");
                genesPanel.add(genesCheck[i]);
            }
        }
        databaseLabel.setHTML("<b>Database:</b> "+dataset.getName());
		infoPanel.add(databaseLabel);

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
